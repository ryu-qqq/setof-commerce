---
description: 모든 레이어 구현 전문가. Knowledge Base 참조하여 Zero-Tolerance 규칙 준수하며 코드 생성.
tags: [implementation, coding]
activationCommands: ["/work", "/impl"]
---

# Implementer Skill

모든 레이어(Domain, Application, Persistence, REST API)의 구현을 담당하는 전문가 스킬입니다.

## 역할

1. **코드 구현**: Knowledge Base 규칙에 따른 코드 생성
2. **테스트 작성**: 구현과 동시에 테스트 코드 작성
3. **Zero-Tolerance 준수**: 필수 규칙 위반 방지

## 활성화 시점

- `/work` 커맨드 실행 시
- 코드 구현 요청 시

## Knowledge Base 참조

### 레이어별 규칙 파일

```bash
# Domain Layer
@knowledge/rules/domain-rules.md        # 214개 규칙
@knowledge/templates/domain-templates.md # 16개 템플릿
@knowledge/examples/domain-examples.md   # 73개 예제

# Application Layer
@knowledge/rules/application-rules.md    # 103개 규칙
@knowledge/templates/application-templates.md
@knowledge/examples/application-examples.md

# Persistence Layer
@knowledge/rules/persistence-rules.md    # 18개 규칙
@knowledge/templates/persistence-templates.md
@knowledge/examples/persistence-examples.md

# REST API Layer
@knowledge/rules/rest-api-rules.md       # 58개 규칙
@knowledge/templates/rest-api-templates.md
@knowledge/examples/rest-api-examples.md
```

## Zero-Tolerance 규칙 (필수 준수)

### Domain Layer

```java
// ❌ 금지: Lombok
@Data  // NEVER
@Getter @Setter  // NEVER

// ❌ 금지: Getter 체이닝 (Law of Demeter)
order.getCustomer().getAddress().getCity()  // NEVER

// ✅ 허용: 도메인 메서드로 캡슐화
order.getShippingCity()  // OK
```

### Application Layer

```java
// ❌ 금지: @Transactional 내 외부 API 호출
@Transactional
public void process() {
    externalApi.call();  // NEVER - 트랜잭션 경계 위반
}

// ✅ 허용: 트랜잭션 외부에서 호출
public void process() {
    externalApi.call();
    transactionalService.save();  // OK
}
```

### Persistence Layer

```java
// ❌ 금지: JPA 관계 어노테이션
@ManyToOne  // NEVER
@OneToMany  // NEVER

// ✅ 허용: Long FK 전략
private Long customerId;  // OK
```

### REST API Layer

```java
// ❌ 금지: MockMvc 테스트
@WebMvcTest  // NEVER

// ✅ 허용: TestRestTemplate
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
TestRestTemplate restTemplate;  // OK
```

## 구현 패턴

### Domain Aggregate

```java
public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private OrderStatus status;
    private final List<OrderLine> lines;

    // 생성자 (protected/private)
    protected Order(OrderId id, CustomerId customerId, List<OrderLine> lines) {
        this.id = id;
        this.customerId = customerId;
        this.status = OrderStatus.PLACED;
        this.lines = new ArrayList<>(lines);
    }

    // 정적 팩토리 메서드 (public)
    public static Order create(CustomerId customerId, List<OrderLine> lines) {
        validateLines(lines);
        return new Order(OrderId.generate(), customerId, lines);
    }

    // 비즈니스 메서드 (Tell, Don't Ask)
    public void cancel(String reason) {
        validateCancellable();
        this.status = OrderStatus.CANCELLED;
        registerEvent(new OrderCancelledEvent(this.id, reason));
    }

    private void validateCancellable() {
        if (!status.isCancellable()) {
            throw new OrderNotCancellableException(this.id, this.status);
        }
    }
}
```

### Application UseCase

```java
public interface CancelOrderUseCase {
    void execute(CancelOrderCommand command);
}

public record CancelOrderCommand(
    Long orderId,
    String reason
) {}

@Component
public class CancelOrderService implements CancelOrderUseCase {
    private final OrderQueryOutPort orderQuery;
    private final OrderCommandOutPort orderCommand;

    @Override
    @Transactional
    public void execute(CancelOrderCommand command) {
        Order order = orderQuery.getById(command.orderId());
        order.cancel(command.reason());
        orderCommand.save(order);
    }
}
```

### JPA Entity

```java
@Entity
@Table(name = "orders")
public class OrderJpaEntity extends BaseAuditEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private Long customerId;  // Long FK, NOT @ManyToOne

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    protected OrderJpaEntity() {} // JPA용

    public static OrderJpaEntity of(Order domain) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.id = domain.getId().value();
        entity.customerId = domain.getCustomerId().value();
        entity.status = domain.getStatus();
        return entity;
    }
}
```

### REST Controller

```java
@RestController
@RequestMapping(ApiPaths.Orders.BASE)
public class OrderCommandController {
    private final CancelOrderUseCase cancelOrderUseCase;
    private final OrderCommandApiMapper mapper;

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderApiRequest request) {

        CancelOrderCommand command = mapper.toCommand(orderId, request);
        cancelOrderUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.success());
    }
}
```

## 테스트 작성

### 단위 테스트 (Domain)

```java
class OrderTest {
    @Test
    void cancel_WithPlacedStatus_ShouldSucceed() {
        // given
        Order order = OrderFixture.placedOrder();

        // when
        order.cancel("고객 요청");

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void cancel_WithShippedStatus_ShouldThrow() {
        // given
        Order order = OrderFixture.shippedOrder();

        // when & then
        assertThatThrownBy(() -> order.cancel("고객 요청"))
            .isInstanceOf(OrderNotCancellableException.class);
    }
}
```

### 통합 테스트 (REST API)

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void cancelOrder_ShouldReturn200() {
        // given
        Long orderId = createTestOrder();
        CancelOrderApiRequest request = new CancelOrderApiRequest("고객 요청");

        // when
        ResponseEntity<ApiResponse<Void>> response = restTemplate.postForEntity(
            "/api/v1/orders/{orderId}/cancel",
            request,
            new ParameterizedTypeReference<>() {},
            orderId
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

## 관련 스킬

- **planner**: 구현할 Task 정의
- **tester**: 추가 테스트 작성
- **reviewer**: 구현 검토

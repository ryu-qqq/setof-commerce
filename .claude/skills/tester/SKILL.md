---
description: 테스트 전문가. ArchUnit 테스트, 단위 테스트, 통합 테스트 작성 및 실행.
tags: [testing, quality]
activationCommands: ["/check"]
---

# Tester Skill

테스트 작성과 실행을 담당하는 전문가 스킬입니다.

## 역할

1. **단위 테스트**: Domain, Application 레이어 테스트
2. **통합 테스트**: REST API E2E 테스트
3. **ArchUnit 테스트**: 아키텍처 규칙 검증
4. **커버리지 분석**: JaCoCo 기반 커버리지 측정

## 활성화 시점

- `/check` 커맨드 실행 시
- 테스트 작성 요청 시

## 테스트 전략

### 테스트 피라미드

```
        ┌─────────┐
        │  E2E    │  ← 통합 테스트 (TestRestTemplate)
       ┌┴─────────┴┐
       │Integration│  ← 서비스 통합
      ┌┴───────────┴┐
      │    Unit     │  ← Domain, Application 단위
     ┌┴─────────────┴┐
     │   ArchUnit    │  ← 아키텍처 규칙 검증
    └────────────────┘
```

## Zero-Tolerance 테스트 규칙

### 1. MockMvc 금지 → TestRestTemplate 사용

```java
// ❌ 금지
@WebMvcTest
MockMvc mockMvc;

// ✅ 허용
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
TestRestTemplate restTemplate;
```

### 2. @Sql 어노테이션으로 테스트 데이터

```java
@SpringBootTest
@Sql("/sql/order-test-data.sql")
class OrderIntegrationTest {
    // ...
}
```

### 3. TestFixtures 활용

```java
// testFixtures 모듈 사용
public class OrderFixture {
    public static Order placedOrder() {
        return Order.create(
            CustomerId.of(1L),
            List.of(OrderLineFixture.defaultLine())
        );
    }
}
```

## 테스트 패턴

### Domain 단위 테스트

```java
class OrderTest {

    @Nested
    @DisplayName("주문 생성")
    class CreateOrder {

        @Test
        @DisplayName("유효한 주문 라인으로 생성 성공")
        void create_WithValidLines_ShouldSucceed() {
            // given
            CustomerId customerId = CustomerId.of(1L);
            List<OrderLine> lines = List.of(
                OrderLine.of(ProductId.of(1L), Money.won(10000), 2)
            );

            // when
            Order order = Order.create(customerId, lines);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
            assertThat(order.getTotalAmount()).isEqualTo(Money.won(20000));
        }

        @Test
        @DisplayName("빈 주문 라인으로 생성 시 예외")
        void create_WithEmptyLines_ShouldThrow() {
            // given
            CustomerId customerId = CustomerId.of(1L);
            List<OrderLine> emptyLines = List.of();

            // when & then
            assertThatThrownBy(() -> Order.create(customerId, emptyLines))
                .isInstanceOf(EmptyOrderLinesException.class);
        }
    }

    @Nested
    @DisplayName("주문 취소")
    class CancelOrder {

        @Test
        @DisplayName("PLACED 상태에서 취소 성공")
        void cancel_WhenPlaced_ShouldSucceed() {
            // given
            Order order = OrderFixture.placedOrder();

            // when
            order.cancel("고객 변심");

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"SHIPPED", "DELIVERED"})
        @DisplayName("취소 불가 상태에서 예외")
        void cancel_WhenNotCancellable_ShouldThrow(OrderStatus status) {
            // given
            Order order = OrderFixture.orderWithStatus(status);

            // when & then
            assertThatThrownBy(() -> order.cancel("사유"))
                .isInstanceOf(OrderNotCancellableException.class);
        }
    }
}
```

### Application 서비스 테스트

```java
@ExtendWith(MockitoExtension.class)
class CancelOrderServiceTest {

    @Mock
    OrderQueryOutPort orderQuery;

    @Mock
    OrderCommandOutPort orderCommand;

    @InjectMocks
    CancelOrderService sut;

    @Test
    @DisplayName("주문 취소 성공")
    void execute_ShouldCancelOrder() {
        // given
        Order order = OrderFixture.placedOrder();
        given(orderQuery.getById(1L)).willReturn(order);

        CancelOrderCommand command = new CancelOrderCommand(1L, "고객 요청");

        // when
        sut.execute(command);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        then(orderCommand).should().save(order);
    }
}
```

### REST API 통합 테스트

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD)
class OrderControllerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    OrderJpaRepository orderRepository;

    @Test
    @DisplayName("주문 취소 API 성공")
    @Sql("/sql/order-test-data.sql")
    void cancelOrder_ShouldReturn200() {
        // given
        Long orderId = 1L;
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

        OrderJpaEntity savedOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("존재하지 않는 주문 취소 시 404")
    void cancelOrder_WhenNotFound_ShouldReturn404() {
        // given
        Long nonExistentOrderId = 999L;
        CancelOrderApiRequest request = new CancelOrderApiRequest("고객 요청");

        // when
        ResponseEntity<ApiResponse<Void>> response = restTemplate.postForEntity(
            "/api/v1/orders/{orderId}/cancel",
            request,
            new ParameterizedTypeReference<>() {},
            nonExistentOrderId
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
```

## ArchUnit 테스트

### Zero-Tolerance 규칙

```java
@AnalyzeClasses(packages = "com.ryuqq")
class ZeroToleranceArchTest {

    @ArchTest
    static final ArchRule lombok_should_not_be_used_in_domain =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("lombok..")
            .because("Domain layer must not use Lombok");

    @ArchTest
    static final ArchRule jpa_relations_should_not_be_used =
        noFields()
            .should().beAnnotatedWith(ManyToOne.class)
            .orShould().beAnnotatedWith(OneToMany.class)
            .orShould().beAnnotatedWith(OneToOne.class)
            .orShould().beAnnotatedWith(ManyToMany.class)
            .because("Use Long FK strategy instead of JPA relations");
}
```

## Gradle 테스트 명령어

```bash
# 전체 테스트
./gradlew test

# ArchUnit만
./gradlew test --tests "*ArchTest"

# 특정 클래스
./gradlew test --tests "OrderTest"

# 커버리지 리포트
./gradlew test jacocoTestReport
# → build/reports/jacoco/test/html/index.html
```

## 관련 스킬

- **implementer**: 테스트 대상 코드 구현
- **reviewer**: 테스트 커버리지 검토

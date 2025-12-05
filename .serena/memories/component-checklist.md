# 컴포넌트별 생성 체크리스트

## Domain Layer

### Aggregate Root
```java
public class Order {
    private Long id;                    // null = 미저장
    private Long customerId;            // Long FK (관계 X)
    private OrderStatus status;
    private List<OrderLine> lines;      // 내부 Entity
    private Instant createdAt;          // Instant (LocalDateTime X)
    
    // Private 생성자
    private Order(...) { }
    
    // Static Factory
    public static Order forNew(...) { }        // 신규 생성
    public static Order reconstitute(...) { }  // DB 복원
    
    // 비즈니스 메서드 (Tell, Don't Ask)
    public void place() { validateAndChangeStatus(); }
    public Money totalAmount() { return calculate(); }
}
```
- [ ] Lombok 금지
- [ ] Long FK (관계 어노테이션 X)
- [ ] private 생성자 + static factory
- [ ] Instant (LocalDateTime X)
- [ ] 비즈니스 메서드로 상태 변경

### Value Object
```java
public final class Email {
    private final String value;
    
    private Email(String value) {
        validate(value);
        this.value = value;
    }
    
    public static Email of(String value) { return new Email(value); }
    
    // equals, hashCode 필수
}
```
- [ ] final class
- [ ] private final 필드
- [ ] private 생성자 + of/from factory
- [ ] 생성 시 validation
- [ ] equals/hashCode 구현

### Domain Exception
```java
public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(Long orderId) {
        super(OrderErrorCode.NOT_FOUND, "Order not found: " + orderId);
    }
}
```
- [ ] DomainException 상속
- [ ] BC별 ErrorCode enum 사용

## Application Layer

### UseCase (Port-In)
```java
// Command
public interface CreateOrderUseCase {
    Long execute(CreateOrderCommand command);
}

// Query
public interface FindOrderQueryUseCase {
    OrderResponse execute(Long orderId);
}
```
- [ ] 단일 메서드 (execute 또는 명확한 동사)
- [ ] Command/Query 분리

### Service (UseCase 구현)
```java
@Service
public class OrderCommandService implements CreateOrderUseCase {
    
    @Override
    @Transactional
    public Long execute(CreateOrderCommand command) {
        Order order = factory.create(command);
        return manager.persist(order).id();
    }
}
```
- [ ] @Service
- [ ] @Transactional (Command만)
- [ ] Port-Out 통해서만 외부 접근
- [ ] 외부 API 호출 금지

### DTO (Record 필수)
```java
public record CreateOrderCommand(
    Long customerId,
    List<OrderItemCommand> items
) { }

public record OrderResponse(
    Long id,
    String status,
    Instant createdAt
) { }
```
- [ ] Java Record 타입
- [ ] 불변
- [ ] Lombok 금지

## Persistence Layer

### JPA Entity
```java
@Entity
@Table(name = "orders")
public class OrderJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;  // Long FK!
    
    protected OrderJpaEntity() { }  // JPA용
    
    // Setter 금지, 생성자/빌더로만 값 설정
}
```
- [ ] Suffix: JpaEntity
- [ ] Long FK (관계 어노테이션 금지)
- [ ] protected 기본 생성자
- [ ] Setter 금지
- [ ] Lombok 금지

### Repository
```java
// Command: JpaRepository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> { }

// Query: QueryDSL (DTO Projection)
@Repository
public class OrderQueryDslRepository {
    public Optional<OrderResponse> findById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .select(new QOrderResponse(...))  // DTO Projection
                .from(order)
                .where(order.id.eq(id))
                .fetchOne()
        );
    }
}
```
- [ ] Command: JpaRepository 상속
- [ ] Query: QueryDSL + DTO Projection
- [ ] Entity 직접 반환 금지 (Query)

## REST API Layer

### Controller
```java
@RestController
@RequestMapping("/api/v1/orders")
public class OrderCommandController {
    
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateOrderRequest request) {
        Long id = useCase.execute(mapper.toCommand(request));
        return ResponseEntity.ok(ApiResponse.success(id));
    }
}
```
- [ ] @RestController
- [ ] @Valid 필수
- [ ] UseCase 위임만 (비즈니스 로직 X)
- [ ] Mapper로 DTO 변환

### Request DTO
```java
public record CreateOrderRequest(
    @NotNull Long customerId,
    @NotEmpty List<OrderItemRequest> items
) { }
```
- [ ] Java Record
- [ ] Bean Validation 어노테이션
- [ ] Suffix: Request

## Application Layer 추가 컴포넌트

### Event Listener
```java
@Component
public class OrderEventListener {
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderPlacedEvent event) {
        // 외부 API 호출 가능 (트랜잭션 밖)
        notificationService.send(event);
    }
}
```
- [ ] @Component
- [ ] @TransactionalEventListener (AFTER_COMMIT)
- [ ] Suffix: EventListener

### Scheduler
```java
@Component
public class OrderSyncScheduler {
    
    @Scheduled(cron = "0 0 * * * *")
    public void syncOrders() {
        List<Order> orders = queryPort.findPendingOrders();
        orders.forEach(manager::process);
    }
}
```
- [ ] @Component
- [ ] @Scheduled
- [ ] Suffix: Scheduler
- [ ] 외부 API 직접 호출 금지 (Manager 위임)

### Factory (Command/Query)
```java
@Component
public class OrderCommandFactory {
    public Order create(CreateOrderCommand command) {
        return Order.forNew(
            command.customerId(),
            command.items().stream()
                .map(this::toOrderLine)
                .toList()
        );
    }
}
```
- [ ] @Component
- [ ] Suffix: CommandFactory / QueryFactory
- [ ] Domain 객체 생성 책임

### Bundle DTO
```java
// Persist Bundle (저장용 묶음)
public record OrderPersistBundle(
    Order order,
    List<OrderLine> lines
) { }

// Query Bundle (조회 결과 묶음)
public record OrderQueryBundle(
    OrderResponse order,
    List<OrderLineResponse> lines
) { }
```
- [ ] Java Record
- [ ] Suffix: PersistBundle / QueryBundle
- [ ] 여러 객체를 묶어서 전달

## Terraform (Infrastructure)

### Wrapper Module 패턴
```hcl
module "ecr_web_api" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecr"
  
  # 네이밍 컨벤션 강제
  repository_name = local.naming.web_api
  
  # 보안 설정 하드코딩
  image_tag_mutability = "IMMUTABLE"
  scan_on_push         = true
  
  # 필수 태그 주입
  environment  = var.environment
  team         = var.team
  owner        = var.owner
}
```
- [ ] Infrastructure 레포 모듈 래핑
- [ ] 네이밍: `{project}-{resource}-{env}`
- [ ] 보안 설정 하드코딩 (변경 불가)
- [ ] 8개 필수 태그 주입
- [ ] SSM Parameter로 Cross-Stack 참조

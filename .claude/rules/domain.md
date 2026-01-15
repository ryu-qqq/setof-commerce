---
paths:
  - "domain/**"
  - "domain-*/**"
---

# Domain Layer 규칙 (자동 주입)

이 규칙은 `domain/` 또는 `domain-*/` 경로의 파일을 작업할 때 **자동으로 적용**됩니다.

## Zero-Tolerance 규칙 (절대 위반 금지)

### 어노테이션 금지
- **AGG-001**: Lombok 어노테이션 절대 금지 (`@Data`, `@Getter`, `@Setter`, `@Builder` 등)
- **AGG-002**: JPA 어노테이션 금지 (`@Entity`, `@Table`, `@Column` 등)
- **AGG-003**: Spring 어노테이션 금지 (`@Component`, `@Service` 등)

### 구조 필수사항
- **AGG-004**: `forNew()` 정적 팩토리 메서드 필수
- **AGG-005**: `reconstitute()` 정적 팩토리 메서드 필수
- **AGG-007**: Aggregate ID는 ID VO 사용 (예: `OrderId`, `CustomerId`)
- **AGG-008**: `isNew()` 메서드 필수
- **AGG-012**: Setter 메서드 절대 금지

### 행위 규칙
- **AGG-010**: `Instant.now()` 직접 호출 금지 - 파라미터로 주입
- **AGG-014**: Law of Demeter - Getter 체이닝 금지 (`order.getCustomer().getAddress()` 금지)

### 의존성 규칙
- **AGG-023**: 외부 레이어(Application, Persistence, REST API) 의존 금지
- **AGG-024**: Repository 직접 참조 금지

## Aggregate 패턴

```java
public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private OrderStatus status;

    // protected 생성자
    protected Order(OrderId id, CustomerId customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    // 신규 생성 팩토리
    public static Order forNew(CustomerId customerId, Instant now) {
        Order order = new Order(OrderId.forNew(), customerId);
        order.status = OrderStatus.CREATED;
        return order;
    }

    // 재구성 팩토리 (DB 로드용)
    public static Order reconstitute(OrderId id, CustomerId customerId, OrderStatus status) {
        Order order = new Order(id, customerId);
        order.status = status;
        return order;
    }

    public boolean isNew() {
        return id.isNew();
    }

    // 비즈니스 메서드 (Tell, Don't Ask)
    public void cancel(String reason, Instant now) {
        if (!status.isCancellable()) {
            throw new OrderNotCancellableException(this.id);
        }
        this.status = OrderStatus.CANCELLED;
    }
}
```

## ID Value Object 패턴

```java
public record OrderId(Long value) {
    public static OrderId of(Long value) {
        return new OrderId(value);
    }

    public static OrderId forNew() {
        return new OrderId(null);  // 신규 생성 시 null
    }

    public boolean isNew() {
        return value == null;
    }
}
```

## DomainException 패턴

```java
// DomainException만 사용 (공통 예외 클래스 분리 금지)
public class OrderNotCancellableException extends DomainException {
    public OrderNotCancellableException(OrderId orderId) {
        super(OrderErrorCode.NOT_CANCELLABLE, orderId);
    }
}
```

## 상세 규칙 참조

더 자세한 규칙은 다음 파일을 참조하세요:
- `.claude/knowledge/rules/domain-rules.md` (214개 규칙)
- `.claude/knowledge/templates/domain-templates.md` (16개 템플릿)
- `.claude/knowledge/examples/domain-examples.md` (73개 예제)

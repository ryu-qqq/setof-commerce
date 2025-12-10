# Domain Layer - Domain Event Rules

## DOMAIN_EVENT_STRUCTURE (4 rules)

Domain Event 구조 규칙

```json
{
  "category": "DOMAIN_EVENT_STRUCTURE",
  "rules": [
    {
      "ruleId": "EVT-001",
      "name": "DomainEvent 인터페이스 구현 필수",
      "severity": "ERROR",
      "description": "Domain Event는 DomainEvent 인터페이스를 구현해야 함",
      "pattern": "record\\s+\\w+Event\\(.*\\)\\s+implements\\s+DomainEvent",
      "archUnitTest": "DomainEventArchTest.event_MustImplementDomainEvent"
    },
    {
      "ruleId": "EVT-002",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "Domain Event는 Record 타입 필수 (불변성 보장)",
      "pattern": "public\\s+record\\s+\\w+Event\\(",
      "antiPattern": "public\\s+class\\s+\\w+Event",
      "archUnitTest": "DomainEventArchTest.event_MustBeRecord"
    },
    {
      "ruleId": "EVT-003",
      "name": "occurredAt 필드 필수",
      "severity": "ERROR",
      "description": "Domain Event는 occurredAt (Instant) 필드 필수",
      "pattern": "Instant\\s+occurredAt",
      "archUnitTest": "DomainEventArchTest.event_MustHaveOccurredAtField"
    },
    {
      "ruleId": "EVT-004",
      "name": "from() 정적 팩토리 메서드 필수",
      "severity": "ERROR",
      "description": "Domain Event는 from() 정적 팩토리 메서드 필수",
      "pattern": "public\\s+static\\s+\\w+Event\\s+from\\(",
      "archUnitTest": "DomainEventArchTest.event_MustHaveFromMethod"
    }
  ]
}
```

---

## DOMAIN_EVENT_NAMING (2 rules)

Domain Event 네이밍/패키지 규칙

```json
{
  "category": "DOMAIN_EVENT_NAMING",
  "rules": [
    {
      "ruleId": "EVT-005",
      "name": "과거형 네이밍 필수 (*Event)",
      "severity": "ERROR",
      "description": "Domain Event는 과거형 네이밍 필수 (OrderPlacedEvent, UserRegisteredEvent 등)",
      "pattern": "\\w+(Placed|Cancelled|Completed|Approved|Rejected|Shipped|Delivered|Paid|Sent|Sold|Created|Updated|Deleted|Registered|Activated|Deactivated|Expired|Renewed|Confirmed|Verified|Published|Unpublished|Archived|Restored)Event",
      "antiPattern": "\\w+(Place|Cancel|Create|Update|Delete)Event"
    },
    {
      "ruleId": "EVT-006",
      "name": "domain.[bc].event 패키지 위치",
      "severity": "ERROR",
      "description": "Domain Event는 domain.[bc].event 패키지에 위치",
      "pattern": "domain\\.\\w+\\.event\\.\\w+Event",
      "archUnitTest": "DomainEventArchTest.event_MustBeInCorrectPackage"
    }
  ]
}
```

---

## DOMAIN_EVENT_PROHIBITION (5 rules)

Domain Event 금지 규칙

```json
{
  "category": "DOMAIN_EVENT_PROHIBITION",
  "rules": [
    {
      "ruleId": "EVT-007",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Domain Event에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor|AllArgsConstructor|Value)",
      "archUnitTest": "DomainEventArchTest.event_MustNotUseLombok"
    },
    {
      "ruleId": "EVT-008",
      "name": "JPA 금지",
      "severity": "ERROR",
      "description": "Domain Event에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column|Embeddable|Embedded)",
      "archUnitTest": "DomainEventArchTest.event_MustNotUseJpa"
    },
    {
      "ruleId": "EVT-009",
      "name": "Spring 금지",
      "severity": "ERROR",
      "description": "Domain Event에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository|Transactional|Autowired|EventListener)",
      "archUnitTest": "DomainEventArchTest.event_MustNotUseSpring"
    },
    {
      "ruleId": "EVT-010",
      "name": "Spring Framework 의존 금지",
      "severity": "ERROR",
      "description": "Domain Event에서 Spring Framework 의존 금지",
      "antiPattern": "import\\s+org\\.springframework\\.",
      "archUnitTest": "DomainEventArchTest.event_MustNotDependOnSpring"
    },
    {
      "ruleId": "EVT-011",
      "name": "외부 레이어 의존 금지",
      "severity": "ERROR",
      "description": "Domain Event에서 Application/Adapter/Bootstrap 레이어 의존 금지",
      "antiPattern": "import\\s+.*\\.(application|adapter|bootstrap)\\.",
      "archUnitTest": "DomainEventArchTest.event_MustNotDependOnOuterLayer"
    }
  ]
}
```

---

## 코드 예시

### DomainEvent 인터페이스 (common 패키지)

```java
// domain/common/event/DomainEvent.java
public interface DomainEvent {
    Instant occurredAt();
}
```

### Domain Event 구현

```java
// domain/order/event/OrderPlacedEvent.java
public record OrderPlacedEvent(
    OrderId orderId,
    UserId userId,
    Money totalAmount,
    Instant occurredAt      // ✅ 필수 필드
) implements DomainEvent {

    // ✅ from() 팩토리 메서드 필수
    public static OrderPlacedEvent from(Order order, Instant occurredAt) {
        return new OrderPlacedEvent(
            order.getId(),
            order.getUserId(),
            order.getTotalAmount(),
            occurredAt
        );
    }
}
```

### 다양한 Event 예시

```java
// 취소 이벤트 - 취소 사유 포함
public record OrderCancelledEvent(
    OrderId orderId,
    UserId userId,
    String reason,
    Instant occurredAt
) implements DomainEvent {

    public static OrderCancelledEvent from(Order order, String reason, Instant occurredAt) {
        return new OrderCancelledEvent(
            order.getId(),
            order.getUserId(),
            reason,
            occurredAt
        );
    }
}

// 결제 완료 이벤트
public record PaymentCompletedEvent(
    PaymentId paymentId,
    OrderId orderId,
    Money amount,
    Instant occurredAt
) implements DomainEvent {

    public static PaymentCompletedEvent from(Payment payment, Instant occurredAt) {
        return new PaymentCompletedEvent(
            payment.getId(),
            payment.getOrderId(),
            payment.getAmount(),
            occurredAt
        );
    }
}

// 배송 이벤트
public record OrderShippedEvent(
    OrderId orderId,
    TrackingNumber trackingNumber,
    Instant occurredAt
) implements DomainEvent {

    public static OrderShippedEvent from(Order order, TrackingNumber tracking, Instant occurredAt) {
        return new OrderShippedEvent(
            order.getId(),
            tracking,
            occurredAt
        );
    }
}
```

---

## Event 네이밍 가이드

### 규칙적 과거형

| 동사 | 과거형 | Event 이름 |
|------|--------|------------|
| place | placed | `OrderPlacedEvent` |
| cancel | cancelled | `OrderCancelledEvent` |
| complete | completed | `PaymentCompletedEvent` |
| register | registered | `UserRegisteredEvent` |
| ship | shipped | `ItemShippedEvent` |
| activate | activated | `AccountActivatedEvent` |
| expire | expired | `CouponExpiredEvent` |
| approve | approved | `RequestApprovedEvent` |
| reject | rejected | `RequestRejectedEvent` |
| confirm | confirmed | `OrderConfirmedEvent` |
| publish | published | `ArticlePublishedEvent` |
| archive | archived | `DocumentArchivedEvent` |
| restore | restored | `AccountRestoredEvent` |

### 불규칙 과거형

| 동사 | 과거형 | Event 이름 |
|------|--------|------------|
| pay | paid | `OrderPaidEvent` |
| send | sent | `EmailSentEvent` |
| sell | sold | `ProductSoldEvent` |

---

## Aggregate에서 Event 발행

```java
public class Order {
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    // 생성 시 이벤트
    public static Order forNew(OrderId id, UserId userId, ..., Instant now) {
        Order order = new Order(id, userId, ...);
        order.domainEvents.add(OrderPlacedEvent.from(order, now));
        return order;
    }
    
    // 취소 시 이벤트
    public void cancel(String reason, Instant now) {
        if (this.status == OrderStatus.SHIPPED) {
            throw new OrderCannotCancelException(this.id, "이미 배송됨");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = now;
        this.domainEvents.add(OrderCancelledEvent.from(this, reason, now));
    }
    
    // 이벤트 조회 및 클리어
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }
}
```

---

## 관련 문서

- [Event Guide](docs/coding_convention/02-domain-layer/event/event-guide.md)
- [Event ArchUnit](docs/coding_convention/02-domain-layer/event/event-archunit.md)
- [Transaction Event Registry Guide](docs/coding_convention/03-application-layer/event/transaction-event-registry-guide.md)

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09

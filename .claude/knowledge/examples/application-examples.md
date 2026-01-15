# APPLICATION Layer 예제 코드 (21개)

## 개요

이 문서는 APPLICATION Layer의 코딩 규칙에 대한 GOOD/BAD 예제를 제공합니다.

## 예제 목록

| Rule Code | Rule Name | Type |
|-----------|-----------|------|
| BDL-001 | PersistBundle은 Class로 정의 (mutable) | ✅ GOOD |
| BDL-002 | QueryBundle은 Record로 정의 (immutable) | ✅ GOOD |
| CDTO-001 | Command DTO는 Record로 정의 | ✅ GOOD |
| CDTO-001 | Command DTO는 Record로 정의 | ❌ BAD |
| EVT-001 | EventListener는 외부 시스템 호출 전용 | ✅ GOOD |
| EVT-001 | EventListener는 외부 시스템 호출 전용 | ❌ BAD |
| EVT-005 | EventListener는 @EventListener 사용 (@Trans... | ✅ GOOD |
| EVT-005 | EventListener는 @EventListener 사용 (@Trans... | ❌ BAD |
| FAC-001 | Facade는 @Component 어노테이션 사용 | ✅ GOOD |
| FAC-001 | Facade는 @Component 어노테이션 사용 | ❌ BAD |
| FAC-002 | CommandFacade와 QueryFacade 분리 | ✅ GOOD |
| QDTO-001 | Query DTO는 Record로 정의 | ✅ GOOD |
| RDTO-001 | Response DTO는 Record로 정의 | ✅ GOOD |
| SCHS-003 | Application Layer에 @Scheduled 어노테이션 금지 | ✅ GOOD |
| SCHS-003 | Application Layer에 @Scheduled 어노테이션 금지 | ❌ BAD |
| SCHS-005 | Scheduler Service는 Manager를 통해 Port 사용 (... | ✅ GOOD |
| SCHS-005 | Scheduler Service는 Manager를 통해 Port 사용 (... | ❌ BAD |
| SVC-001 | @Service 어노테이션 필수 | ✅ GOOD |
| SVC-001 | @Service 어노테이션 필수 | ❌ BAD |
| SVC-002 | UseCase(Port-In) 인터페이스 구현 필수 | ✅ GOOD |
| SVC-002 | UseCase(Port-In) 인터페이스 구현 필수 | ❌ BAD |

---

## 상세 예제

### BDL-001: PersistBundle은 Class로 정의 (mutable)

#### ✅ GOOD Example

```java
// ✅ PersistBundle은 class (ID Enrichment 패턴)
public class OrderPersistBundle {
    private final Order order;
    private Long id;

    public OrderPersistBundle(Order order) {
        this.order = order;
    }

    // ✅ ID Enrichment 메서드
    public OrderPersistBundle withId(Long id) {
        this.id = id;
        return this;
    }

    public Order order() {
        return order;
    }

    public Long id() {
        if (id == null) {
            throw new IllegalStateException("ID not enriched yet");
        }
        return id;
    }
}
```

**설명**: PersistBundle은 class로 정의하여 with{Id}() 메서드로 ID를 주입합니다.

---

### BDL-002: QueryBundle은 Record로 정의 (immutable)

#### ✅ GOOD Example

```java
// ✅ QueryBundle은 record (조회 결과 묶음)
public record OrderDetailBundle(
    Order order,
    Customer customer,
    List<Product> products
) {
    public OrderDetailBundle {
        Objects.requireNonNull(order, "order must not be null");
        Objects.requireNonNull(customer, "customer must not be null");
        if (products == null) products = List.of();
    }

    // ✅ 편의 메서드
    public Money totalAmount() {
        return order.totalAmount();
    }

    public String customerName() {
        return customer.name();
    }
}
```

**설명**: QueryBundle은 불변이므로 record로 정의합니다.

---

### CDTO-001: Command DTO는 Record로 정의

#### ✅ GOOD Example

```java
// ✅ record 사용
public record CreateOrderCommand(
    Long customerId,
    List<OrderItemCommand> items,
    String shippingAddress,
    PaymentMethod paymentMethod
) {
    // ✅ 유효성 검증 (Compact Constructor)
    public CreateOrderCommand {
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(items, "items must not be null");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("items must not be empty");
        }
    }
}

// ✅ 중첩 Command도 record
public record OrderItemCommand(
    Long productId,
    int quantity
) {
    public OrderItemCommand {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
```

**설명**: Java record를 사용하여 불변성과 간결함을 보장합니다.

---

#### ❌ BAD Example

```java
// ❌ class 사용 금지
public class CreateOrderCommand {
    private Long customerId;
    private List<OrderItemCommand> items;

    // ❌ Setter - 불변성 위반
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // ❌ 보일러플레이트 코드
    public Long getCustomerId() {
        return customerId;
    }
}
```

**설명**: class는 불변성 보장이 어렵고 보일러플레이트 코드가 많습니다.

---

### EVT-001: EventListener는 외부 시스템 호출 전용

#### ✅ GOOD Example

```java
@Component
public class OrderEventListener {
    private final NotificationClientManager notificationClientManager;
    private final AnalyticsClientManager analyticsClientManager;

    // ✅ @TransactionalEventListener + AFTER_COMMIT
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        // ✅ 외부 호출만 수행
        notificationClientManager.sendOrderConfirmation(event.orderId());
        analyticsClientManager.trackOrderCreation(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCancelled(OrderCancelledEvent event) {
        notificationClientManager.sendCancellationNotice(event.orderId());
    }
}
```

**설명**: EventListener는 트랜잭션 커밋 후 외부 시스템 호출만 처리합니다.

---

#### ❌ BAD Example

```java
@Component
public class OrderEventListener {
    private final OrderPersistenceManager orderPersistenceManager;  // ❌ PersistenceManager 의존 금지

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        // ❌ DB 조작 금지 - AFTER_COMMIT 이후이므로 별도 트랜잭션 필요
        orderPersistenceManager.updateNotificationSent(event.orderId());
    }
}
```

**설명**: EventListener에서 DB를 조작하면 트랜잭션 문제가 발생합니다.

---

### EVT-005: EventListener는 @EventListener 사용 (@TransactionalEventListener 금지)

#### ✅ GOOD Example

```java
@Component
public class PaymentEventListener {
    private final SlackClientManager slackClientManager;
    private final EmailClientManager emailClientManager;

    // ✅ AFTER_COMMIT - 결제 성공 확정 후 알림
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        emailClientManager.sendReceipt(event.customerId(), event.paymentId());
    }

    // ✅ AFTER_ROLLBACK - 결제 실패 시 알림
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        slackClientManager.alertPaymentFailure(event);
    }
}
```

**설명**: 트랜잭션 커밋 후에만 이벤트를 처리하여 데이터 일관성을 보장합니다.

---

#### ❌ BAD Example

```java
@Component
public class PaymentEventListener {
    private final EmailClientManager emailClientManager;

    // ❌ @EventListener - 커밋 전 실행되어 롤백 시 문제
    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        // 이메일 발송 후 트랜잭션 롤백되면?
        emailClientManager.sendReceipt(event.customerId(), event.paymentId());
    }
}
```

**설명**: @EventListener는 트랜잭션 커밋 전에 실행되어 롤백 시 문제가 됩니다.

---

### FAC-001: Facade는 @Component 어노테이션 사용

#### ✅ GOOD Example

```java
@Component
public class OrderCommandFacade {
    // ✅ PersistenceManager와 ReadManager만 의존
    private final OrderPersistenceManager orderPersistenceManager;
    private final InventoryPersistenceManager inventoryPersistenceManager;
    private final OrderReadManager orderReadManager;

    // ✅ 내부 RDB만 다루므로 @Transactional 허용
    @Transactional
    public OrderWithInventoryBundle persistOrderWithInventory(
            Order order,
            InventoryDeduction deduction) {

        // 다중 PersistenceManager 조합
        OrderPersistBundle orderBundle = orderPersistenceManager.persist(order);
        InventoryPersistBundle inventoryBundle = inventoryPersistenceManager.deduct(deduction);

        return new OrderWithInventoryBundle(orderBundle, inventoryBundle);
    }
}
```

**설명**: Facade는 내부 RDB 트랜잭션 조합만 담당합니다. 외부 호출은 Service에서 처리합니다.

---

#### ❌ BAD Example

```java
@Component
public class OrderCommandFacade {
    private final OrderPersistenceManager orderPersistenceManager;
    private final PaymentClientManager paymentClientManager;  // ❌ ClientManager 의존 금지

    @Transactional
    public OrderBundle persistWithPayment(Order order, PaymentRequest payment) {
        OrderPersistBundle bundle = orderPersistenceManager.persist(order);

        // ❌ @Transactional 내에서 외부 호출 위험!
        paymentClientManager.requestPayment(payment);

        return bundle;
    }
}
```

**설명**: ClientManager를 의존하면 @Transactional 내에서 외부 호출 위험이 발생합니다.

---

### FAC-002: CommandFacade와 QueryFacade 분리

#### ✅ GOOD Example

```java
@Component
public class OrderQueryFacade {
    private final OrderReadManager orderReadManager;
    private final CustomerReadManager customerReadManager;
    private final ProductReadManager productReadManager;

    // ✅ readOnly 트랜잭션
    @Transactional(readOnly = true)
    public OrderDetailBundle fetchOrderDetail(Long orderId) {
        Order order = orderReadManager.getById(orderId);
        Customer customer = customerReadManager.getById(order.customerId());
        List<Product> products = productReadManager.findByIds(order.productIds());

        return new OrderDetailBundle(order, customer, products);
    }
}
```

**설명**: QueryFacade는 읽기 전용이므로 readOnly 트랜잭션을 사용합니다.

---

### QDTO-001: Query DTO는 Record로 정의

#### ✅ GOOD Example

```java
// ✅ Query DTO는 record
public record SearchOrdersQuery(
    Long customerId,
    OrderStatus status,
    LocalDate fromDate,
    LocalDate toDate,
    int page,
    int size
) {
    // ✅ 기본값 제공
    public SearchOrdersQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 20;
        if (size > 100) size = 100;  // 최대 제한
    }

    // ✅ 정적 팩토리 메서드
    public static SearchOrdersQuery ofCustomer(Long customerId) {
        return new SearchOrdersQuery(customerId, null, null, null, 0, 20);
    }
}
```

**설명**: 검색 조건을 record로 캡슐화하여 타입 안전성을 보장합니다.

---

### RDTO-001: Response DTO는 Record로 정의

#### ✅ GOOD Example

```java
// ✅ Response DTO는 record
public record OrderResponse(
    Long id,
    Long customerId,
    List<OrderItemResponse> items,
    OrderStatus status,
    Money totalAmount,
    LocalDateTime createdAt
) {
    // ✅ 중첩 Response도 record
    public record OrderItemResponse(
        Long productId,
        String productName,
        int quantity,
        Money unitPrice,
        Money subtotal
    ) {}
}
```

**설명**: record를 사용하여 응답 구조를 명확히 정의합니다.

---

### SCHS-003: Application Layer에 @Scheduled 어노테이션 금지

#### ✅ GOOD Example

```java
@Component
public class ProcessOutboxService implements ProcessOutboxUseCase {
    private static final int BATCH_SIZE = 100;
    private static final int MAX_RETRY_COUNT = 3;

    private final OutboxQueryFactory outboxQueryFactory;  // ✅ Criteria 생성용 Factory
    private final OutboxReadManager outboxReadManager;
    private final OutboxPersistenceManager outboxPersistenceManager;
    private final EventPublisher eventPublisher;
    private final DistributedLockManager lockManager;

    // ✅ @Scheduled 없음! UseCase 인터페이스 구현
    @Override
    public void execute() {
        // 분산락 획득
        if (!lockManager.tryLock("outbox-processor", Duration.ofMinutes(5))) {
            return;
        }

        try {
            // ✅ Factory를 통해 Criteria 생성 (직접 생성 금지)
            OutboxSearchCriteria criteria = outboxQueryFactory.createPendingCriteria(BATCH_SIZE, MAX_RETRY_COUNT);
            List<OutboxEvent> events = outboxReadManager.findByCriteria(criteria);

            for (OutboxEvent event : events) {
                processEvent(event);
            }
        } finally {
            lockManager.unlock("outbox-processor");
        }
    }

    private void processEvent(OutboxEvent event) {
        eventPublisher.publish(event);
        outboxPersistenceManager.markAsPublished(event.id());
    }
}
```

**설명**: @Scheduled는 adapter-in/scheduler의 Thin Scheduler에서만 사용합니다. Criteria는 QueryFactory를 통해 생성합니다.

---

#### ❌ BAD Example

```java
@Component
public class ProcessOutboxService {
    private final OutboxRepository outboxRepository;

    // ❌ Application Layer에서 @Scheduled 금지
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<OutboxEvent> events = outboxRepository.findPending();
        // 로직 처리...
    }
}
```

**설명**: @Scheduled는 인프라 관심사이므로 Application Layer에 위치하면 안 됩니다.

---

### SCHS-005: Scheduler Service는 Manager를 통해 Port 사용 (Port 직접 주입 금지)

#### ✅ GOOD Example

```java
@Component
public class RetryFailedPaymentService implements RetryFailedPaymentUseCase {
    private static final int BATCH_SIZE = 50;

    // ✅ Factory + Manager 의존 (Port 직접 주입 금지)
    private final PaymentOutboxQueryFactory paymentOutboxQueryFactory;
    private final PaymentOutboxReadManager paymentOutboxReadManager;
    private final PaymentPersistenceManager paymentPersistenceManager;
    private final PaymentClientManager paymentClientManager;
    private final DistributedLockManager lockManager;

    @Override
    public void execute() {
        if (!lockManager.tryLock("payment-retry", Duration.ofMinutes(10))) {
            return;
        }

        try {
            // ✅ Factory를 통해 Criteria 생성
            PaymentOutboxSearchCriteria criteria = paymentOutboxQueryFactory.createFailedCriteria(BATCH_SIZE);
            List<PaymentOutbox> failedPayments = paymentOutboxReadManager.findByCriteria(criteria);

            for (PaymentOutbox outbox : failedPayments) {
                retryPayment(outbox);
            }
        } finally {
            lockManager.unlock("payment-retry");
        }
    }

    private void retryPayment(PaymentOutbox outbox) {
        PaymentResult result = paymentClientManager.retryWithIdempotencyKey(outbox.paymentId(), outbox.id());
        paymentPersistenceManager.updateOutboxStatus(outbox.id(), result.status());
    }
}
```

**설명**: Port 직접 주입 대신 Manager를 통해 간접 사용합니다. Criteria는 QueryFactory를 통해 생성합니다.

---

#### ❌ BAD Example

```java
@Component
public class RetryFailedPaymentService implements RetryFailedPaymentUseCase {
    // ❌ Port 직접 주입 금지
    private final PaymentQueryPort paymentQueryPort;
    private final PaymentCommandPort paymentCommandPort;
    private final PaymentClientPort paymentClientPort;

    @Override
    public void execute() {
        List<Payment> payments = paymentQueryPort.findFailed();
        for (Payment payment : payments) {
            PaymentResult result = paymentClientPort.retry(payment);
            paymentCommandPort.updateStatus(payment.id(), result);  // ❌ 트랜잭션 경계 불명확
        }
    }
}
```

**설명**: Port를 직접 주입하면 트랜잭션 경계가 불명확해집니다.

---

### SVC-001: @Service 어노테이션 필수

#### ✅ GOOD Example

```java
@Component
public class CreateOrderService implements CreateOrderUseCase {
    private final OrderPersistenceManager orderPersistenceManager;
    private final ProductReadManager productReadManager;
    private final OrderCommandFactory orderCommandFactory;
    private final OrderAssembler orderAssembler;

    // @Transactional 없음!
    @Override
    public OrderResponse execute(CreateOrderCommand command) {
        // 1. 조회 (ReadManager)
        Product product = productReadManager.getById(command.productId());

        // 2. 도메인 생성
        Order order = orderCommandFactory.create(command, product);

        // 3. 저장 (PersistenceManager - 여기서 트랜잭션)
        OrderPersistBundle bundle = orderPersistenceManager.persist(order);

        // 4. 응답 변환
        return orderAssembler.toResponse(bundle);
    }
}
```

**설명**: Service는 비즈니스 로직 조합만 담당하고, 트랜잭션은 Manager/Facade에 위임합니다.

---

#### ❌ BAD Example

```java
@Component
@Transactional  // ❌ Service에 트랜잭션 금지
public class CreateOrderService implements CreateOrderUseCase {
    private final OrderCommandPort orderCommandPort;  // ❌ Port 직접 주입도 금지

    @Override
    public OrderResponse execute(CreateOrderCommand command) {
        Order order = Order.create(command);
        orderCommandPort.save(order);  // ❌ 트랜잭션 경계가 Service에 있음
        return OrderResponse.from(order);
    }
}
```

**설명**: Service에 트랜잭션을 걸면 Manager의 역할이 모호해지고, 트랜잭션 경계가 불명확해집니다.

---

### SVC-002: UseCase(Port-In) 인터페이스 구현 필수

#### ✅ GOOD Example

```java
@Component
public class CancelOrderService implements CancelOrderUseCase {
    // ✅ Manager 의존
    private final OrderPersistenceManager orderPersistenceManager;
    private final OrderReadManager orderReadManager;
    private final PaymentClientManager paymentClientManager;

    @Override
    public OrderResponse execute(CancelOrderCommand command) {
        // ReadManager로 조회
        Order order = orderReadManager.getById(command.orderId());

        // 도메인 로직
        order.cancel(command.reason());

        // PersistenceManager로 저장
        OrderPersistBundle bundle = orderPersistenceManager.persist(order);

        // ClientManager로 외부 호출 (트랜잭션 밖)
        paymentClientManager.requestRefund(bundle.id());

        return OrderAssembler.toResponse(bundle);
    }
}
```

**설명**: Port 직접 주입 대신 Manager를 통해 간접 사용합니다. 트랜잭션 경계가 명확해집니다.

---

#### ❌ BAD Example

```java
@Component
public class CancelOrderService implements CancelOrderUseCase {
    // ❌ Port 직접 주입 금지
    private final OrderCommandPort orderCommandPort;
    private final OrderQueryPort orderQueryPort;
    private final PaymentClientPort paymentClientPort;

    @Override
    public OrderResponse execute(CancelOrderCommand command) {
        Order order = orderQueryPort.findById(command.orderId());
        order.cancel(command.reason());
        orderCommandPort.save(order);  // ❌ 트랜잭션 경계 불명확
        paymentClientPort.refund(order.getId());  // ❌ 외부 호출 위치 부적절
        return OrderResponse.from(order);
    }
}
```

**설명**: Port를 직접 주입하면 트랜잭션 경계 관리가 어렵고, 일관성 없는 코드가 됩니다.

---


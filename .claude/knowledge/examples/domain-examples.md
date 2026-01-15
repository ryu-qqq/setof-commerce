# DOMAIN Layer 예제 코드 (73개)

## 개요

이 문서는 DOMAIN Layer의 코딩 규칙에 대한 GOOD/BAD 예제를 제공합니다.

## 예제 목록

| Rule Code | Rule Name | Type |
|-----------|-----------|------|
| AGG-001 | Aggregate Lombok 어노테이션 금지 | ✅ GOOD |
| AGG-002 | Aggregate JPA 어노테이션 금지 | ✅ GOOD |
| AGG-003 | Aggregate Spring 어노테이션 금지 | ✅ GOOD |
| AGG-004 | forNew() 팩토리 메서드 필수 | ✅ GOOD |
| AGG-005 | reconstitute() 팩토리 메서드 필수 | ✅ GOOD |
| AGG-006 | protected 기본 생성자 | ✅ GOOD |
| AGG-008 | isNew() 메서드 필수 | ✅ GOOD |
| AGG-009 | Aggregate 시간 필드는 Instant 타입 | ✅ GOOD |
| AGG-010 | Instant 파라미터 주입 (Instant.now() 금지) | ✅ GOOD |
| AGG-013 | Aggregate Getter 최소화 | ✅ GOOD |
| AGG-015 | Tell, Don't Ask 원칙 | ✅ GOOD |
| AGG-016 | 복잡한 비즈니스 규칙은 VO로 위임 | ✅ GOOD |
| BDL-001 | PersistBundle은 Class로 정의 (mutable) | ✅ GOOD |
| BDL-002 | QueryBundle은 Record로 정의 (immutable) | ✅ GOOD |
| CADP-001 | persist() 메서드만 제공 | ✅ GOOD |
| CADP-002 | CommandAdapter에서 @Transactional 금지 | ✅ GOOD |
| CADP-007 | CommandAdapter에 비즈니스 로직 금지 | ✅ GOOD |
| CDTO-001 | Command DTO는 Record로 정의 | ✅ GOOD |
| CDTO-001 | Command DTO는 Record로 정의 | ❌ BAD |
| CRI-001 | domain.[bc].query.criteria 패키지 | ✅ GOOD |
| CRI-004 | Record 타입 필수 | ✅ GOOD |
| CRI-005 | of() 정적 팩토리 메서드 필수 | ✅ GOOD |
| CRI-006 | Criteria Lombok 금지 | ✅ GOOD |
| ENT-001 | BaseAuditEntity 상속 필수 | ✅ GOOD |
| ENT-002 | Long FK 전략 (JPA 관계 어노테이션 금지) | ✅ GOOD |
| ENT-004 | of() 정적 팩토리 메서드 필수 | ✅ GOOD |
| ENT-006 | protected/private 생성자 사용 | ✅ GOOD |
| ENT-007 | SoftDeletableEntity 적용 (논리삭제) | ✅ GOOD |
| EVT-001 | DomainEvent 인터페이스 구현 필수 | ✅ GOOD |
| EVT-001 | DomainEvent 인터페이스 구현 필수 | ✅ GOOD |
| EVT-001 | DomainEvent 인터페이스 구현 필수 | ❌ BAD |
| EVT-002 | Record 타입 필수 | ✅ GOOD |
| EVT-003 | occurredAt (Instant) 필드 필수 | ✅ GOOD |
| EVT-004 | from(Aggregate, Instant) 정적 팩토리 메서드 필수 | ✅ GOOD |
| EVT-005 | 과거형 네이밍 필수 (*Event) | ✅ GOOD |
| EVT-005 | 과거형 네이밍 필수 (*Event) | ❌ BAD |
| EVT-006 | domain.[bc].event 패키지 위치 | ✅ GOOD |
| EXC-001 | ErrorCode 인터페이스 구현 필수 | ✅ GOOD |
| EXC-003 | ErrorCode Lombok 금지 | ✅ GOOD |
| EXC-005 | getCode() 메서드 필수 | ✅ GOOD |
| EXC-009 | DomainException 상속 필수 | ✅ GOOD |
| EXC-013 | Exception Spring 금지 | ✅ GOOD |
| FAC-001 | Facade는 @Component 어노테이션 사용 | ✅ GOOD |
| FAC-001 | Facade는 @Component 어노테이션 사용 | ❌ BAD |
| FAC-002 | CommandFacade와 QueryFacade 분리 | ✅ GOOD |
| ID-001 | *Id 네이밍 필수 | ✅ GOOD |
| ID-002 | Record 타입 필수 | ✅ GOOD |
| ID-004 | Long ID forNew() 필수 | ✅ GOOD |
| ID-008 | String ID는 외부에서 주입 | ✅ GOOD |
| QDR-001 | Query 전용 (조회만) | ✅ GOOD |
| QDR-002 | Join 절대 금지 | ✅ GOOD |
| QDR-003 | findAll 금지 (OOM 방지) | ✅ GOOD |
| QDR-005 | existsById 필수 메서드 | ✅ GOOD |
| QDR-008 | QueryDslRepository에서 @Transactional 금지 | ✅ GOOD |
| QDTO-001 | Query DTO는 Record로 정의 | ✅ GOOD |
| RDTO-001 | Response DTO는 Record로 정의 | ✅ GOOD |
| REPO-001 | Command 전용 (save/delete만) | ✅ GOOD |
| REPO-002 | Query Method 금지 | ✅ GOOD |
| REPO-003 | @Query 어노테이션 금지 | ✅ GOOD |
| REPO-004 | QuerydslPredicateExecutor 금지 | ✅ GOOD |
| SCHS-003 | Application Layer에 @Scheduled 어노테이션 금지 | ✅ GOOD |
| SCHS-003 | Application Layer에 @Scheduled 어노테이션 금지 | ❌ BAD |
| SCHS-005 | Scheduler Service는 Manager를 통해 Port 사용 (... | ✅ GOOD |
| SCHS-005 | Scheduler Service는 Manager를 통해 Port 사용 (... | ❌ BAD |
| SVC-001 | @Service 어노테이션 필수 | ✅ GOOD |
| SVC-001 | @Service 어노테이션 필수 | ❌ BAD |
| SVC-002 | UseCase(Port-In) 인터페이스 구현 필수 | ✅ GOOD |
| SVC-002 | UseCase(Port-In) 인터페이스 구현 필수 | ❌ BAD |
| VO-001 | Record 타입 필수 | ✅ GOOD |
| VO-002 | of() 정적 팩토리 메서드 필수 | ✅ GOOD |
| VO-003 | Compact Constructor 검증 필수 | ✅ GOOD |
| VO-004 | Enum VO displayName() 필수 | ✅ GOOD |
| VO-007 | VO Lombok 금지 | ✅ GOOD |

---

## 상세 예제

### AGG-001: Aggregate Lombok 어노테이션 금지

#### ✅ GOOD Example

```java
package com.ryuqq.domain.order.aggregate;

import com.ryuqq.domain.order.id.OrderId;
import com.ryuqq.domain.order.vo.OrderStatus;
import com.ryuqq.domain.order.event.OrderCreatedEvent;
import com.ryuqq.domain.common.event.DomainEvent;

import java.time.Instant;
import java.util.List;

/**
 * Order Aggregate Root.
 */
public class Order {
    // Aggregate 구현
}
```

**설명**: Aggregate Root는 domain.{bc}.aggregate 패키지에 위치합니다. 같은 Bounded Context 내의 id, vo, event 패키지를 import할 수 있습니다.

---

### AGG-002: Aggregate JPA 어노테이션 금지

#### ✅ GOOD Example

```java
public class Order {

    /**
     * 새로운 Order 생성을 위한 팩토리 메서드.
     *
     * @param customerId 고객 ID
     * @param items 주문 항목
     * @param now 생성 시점 (외부 주입)
     * @return 새로운 Order 인스턴스
     */
    public static Order forNew(
            CustomerId customerId,
            List<OrderItem> items,
            Instant now) {
        validateForNew(customerId, items);
        Order order = new Order(
            OrderId.forNew(),
            customerId,
            items,
            OrderStatus.PENDING,
            now,
            now
        );
        order.registerEvent(OrderCreatedEvent.from(order, now));
        return order;
    }

    private static void validateForNew(CustomerId customerId, List<OrderItem> items) {
        if (customerId == null) {
            throw new IllegalArgumentException("customerId must not be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items must not be empty");
        }
    }
}
```

**설명**: forNew()는 새로운 Aggregate 생성을 위한 정적 팩토리 메서드입니다. 검증 → 생성 → 이벤트 발행 순서로 처리합니다. Instant는 외부에서 주입받습니다.

---

### AGG-003: Aggregate Spring 어노테이션 금지

#### ✅ GOOD Example

```java
public class Order {

    /**
     * DB에서 복원하기 위한 팩토리 메서드.
     * 검증 없이 그대로 복원합니다.
     *
     * @param id 주문 ID
     * @param customerId 고객 ID
     * @param status 주문 상태
     * @param createdAt 생성 시점
     * @param updatedAt 수정 시점
     * @return 복원된 Order 인스턴스
     */
    public static Order reconstitute(
            OrderId id,
            CustomerId customerId,
            OrderStatus status,
            Instant createdAt,
            Instant updatedAt) {
        // DB 데이터는 이미 검증된 데이터이므로 검증 생략
        // 이벤트 발행하지 않음
        return new Order(id, customerId, status, createdAt, updatedAt);
    }
}
```

**설명**: reconstitute()는 DB에서 Aggregate를 복원하기 위한 팩토리 메서드입니다. DB 데이터는 이미 검증된 것이므로 검증을 생략하고, 이벤트도 발행하지 않습니다.

---

### AGG-004: forNew() 팩토리 메서드 필수

#### ✅ GOOD Example

```java
public class Order {

    private final OrderId id;
    private final CustomerId customerId;
    private OrderStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    // private 생성자 - 외부에서 직접 호출 불가
    private Order(
            OrderId id,
            CustomerId customerId,
            OrderStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // JPA용 protected 기본 생성자
    protected Order() {
        this.id = null;
        this.customerId = null;
        this.status = null;
        this.createdAt = null;
        this.updatedAt = null;
    }

    // 생성은 forNew(), 복원은 reconstitute() 사용
    public static Order forNew(...) { ... }
    public static Order reconstitute(...) { ... }
}
```

**설명**: 생성자는 private 또는 protected로 선언합니다. 객체 생성은 forNew()/reconstitute() 팩토리 메서드를 통해서만 가능합니다. JPA 프록시를 위해 protected 기본 생성자를 제공합니다.

---

### AGG-005: reconstitute() 팩토리 메서드 필수

#### ✅ GOOD Example

```java
public class Order {

    private OrderStatus status;
    private Instant updatedAt;

    // ❌ Setter 금지
    // public void setStatus(OrderStatus status) { ... }

    // ✅ 비즈니스 의도가 드러나는 명시적 메서드
    public void confirm(Instant now) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING order can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = now;
        registerEvent(OrderConfirmedEvent.from(this, now));
    }

    public void cancel(String reason, Instant now) {
        if (this.status.isTerminal()) {
            throw new IllegalStateException("Cannot cancel terminal order");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = now;
        registerEvent(OrderCancelledEvent.from(this, reason, now));
    }

    public void ship(TrackingNumber trackingNumber, Instant now) {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED order can be shipped");
        }
        this.status = OrderStatus.SHIPPED;
        this.trackingNumber = trackingNumber;
        this.updatedAt = now;
        registerEvent(OrderShippedEvent.from(this, now));
    }
}
```

**설명**: Setter 메서드를 사용하지 않습니다. 상태 변경은 비즈니스 의도가 명확한 메서드(confirm, cancel, ship)로 수행합니다. 각 메서드는 불변식 검증 후 상태를 변경하고 이벤트를 발행합니다.

---

### AGG-006: protected 기본 생성자

#### ✅ GOOD Example

```java
package com.ryuqq.domain.order.aggregate;

// ❌ Lombok 금지
// import lombok.Getter;
// import lombok.Builder;
// import lombok.RequiredArgsConstructor;

import java.time.Instant;

/**
 * Order Aggregate Root.
 * Lombok 없이 순수 Java로 구현합니다.
 */
public class Order {

    private final OrderId id;
    private String name;
    private final Instant createdAt;

    // 명시적 생성자
    private Order(OrderId id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    // 명시적 접근자 (Getter가 아닌 도메인 용어 사용)
    public OrderId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
```

**설명**: Domain Layer에서 Lombok(@Getter, @Setter, @Builder, @Data 등) 사용이 금지됩니다. 생성자와 접근자를 명시적으로 작성합니다. 접근자는 getXxx() 대신 xxx() 형태를 권장합니다.

---

### AGG-008: isNew() 메서드 필수

#### ✅ GOOD Example

```java
public class Order {

    private final ShippingAddress shippingAddress;
    private final Customer customer;

    // ❌ Bad: 외부에서 Getter 체이닝 유발
    // public ShippingAddress getShippingAddress() { return shippingAddress; }
    // 사용: order.getShippingAddress().getCity() - Law of Demeter 위반

    // ✅ Good: 위임 메서드로 필요한 정보만 노출
    public String deliveryCity() {
        return shippingAddress.city();
    }

    public String deliveryFullAddress() {
        return shippingAddress.fullAddress();
    }

    public boolean isVipCustomer() {
        return customer.isVip();
    }

    public String customerName() {
        return customer.name();
    }
}
```

**설명**: Law of Demeter: 객체 내부 구조를 노출하는 Getter 체이닝(order.getCustomer().getAddress().getCity())을 금지합니다. 위임 메서드(deliveryCity())로 필요한 정보만 노출합니다.

---

### AGG-009: Aggregate 시간 필드는 Instant 타입

#### ✅ GOOD Example

```java
public class Order {

    // ❌ Bad: 상태를 물어보고 외부에서 판단
    // if (order.getStatus() == OrderStatus.PENDING) {
    //     order.setStatus(OrderStatus.CONFIRMED);
    // }

    // ✅ Good: 객체에게 행동을 요청
    public void confirm(Instant now) {
        // 상태 판단은 객체 내부에서 수행
        if (!canConfirm()) {
            throw new OrderCannotBeConfirmedException(this.id);
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = now;
        registerEvent(OrderConfirmedEvent.from(this, now));
    }

    // 내부 상태 판단 메서드
    private boolean canConfirm() {
        return this.status == OrderStatus.PENDING;
    }

    // 상태 확인이 필요한 경우 의미 있는 메서드 제공
    public boolean isConfirmable() {
        return canConfirm();
    }

    public boolean isShippable() {
        return this.status == OrderStatus.CONFIRMED;
    }

    public boolean isTerminal() {
        return this.status == OrderStatus.COMPLETED
            || this.status == OrderStatus.CANCELLED;
    }
}
```

**설명**: Tell, Don't Ask: 객체의 상태를 물어보고 외부에서 판단하지 않습니다. 객체에게 행동(confirm, cancel)을 요청하고, 판단은 객체 내부에서 수행합니다.

---

### AGG-010: Instant 파라미터 주입 (Instant.now() 금지)

#### ✅ GOOD Example

```java
public class Order {

    // ❌ Bad: 내부에서 시간 생성
    // public static Order forNew(CustomerId customerId) {
    //     Instant now = Instant.now();  // 금지
    //     return new Order(OrderId.forNew(), customerId, now, now);
    // }

    // ❌ Bad: Clock 주입
    // public static Order forNew(CustomerId customerId, Clock clock) {
    //     Instant now = Instant.now(clock);
    //     return new Order(OrderId.forNew(), customerId, now, now);
    // }

    // ✅ Good: Instant 파라미터로 외부 주입
    public static Order forNew(CustomerId customerId, List<OrderItem> items, Instant now) {
        validateForNew(customerId, items);
        return new Order(OrderId.forNew(), customerId, items, now, now);
    }

    public void confirm(Instant now) {
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = now;
    }

    public void cancel(String reason, Instant now) {
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = now;
    }
}
```

**설명**: Domain Layer에서 Instant.now()나 Clock을 직접 사용하지 않습니다. 시간은 Instant 파라미터로 외부(Application Layer)에서 주입받습니다. 테스트 시 시간 제어가 용이합니다.

---

### AGG-013: Aggregate Getter 최소화

#### ✅ GOOD Example

```java
public class Order {

    private String name;
    private String description;
    private OrderStatus status;
    private Instant updatedAt;

    /**
     * UpdateData를 사용한 부분 업데이트.
     * null이 아닌 필드만 업데이트됩니다.
     */
    public void applyUpdate(OrderUpdateData updateData, Instant now) {
        if (updateData.hasName()) {
            this.name = updateData.name();
        }
        if (updateData.hasDescription()) {
            this.description = updateData.description();
        }
        if (updateData.hasStatus()) {
            validateStatusTransition(updateData.status());
            this.status = updateData.status();
        }
        this.updatedAt = now;
    }

    private void validateStatusTransition(OrderStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(this.status, newStatus);
        }
    }
}

// UpdateData Record
public record OrderUpdateData(
    String name,
    String description,
    OrderStatus status
) {
    public boolean hasName() { return name != null; }
    public boolean hasDescription() { return description != null; }
    public boolean hasStatus() { return status != null; }
}
```

**설명**: UpdateData Record를 사용하여 부분 업데이트를 처리합니다. null이면 변경하지 않음을 의미합니다. hasXxx() 메서드로 변경 여부를 확인하고, 상태 전이는 내부에서 검증합니다.

---

### AGG-015: Tell, Don't Ask 원칙

#### ✅ GOOD Example

```java
public class Order {

    private final OrderId id;
    private String name;
    private OrderStatus status;

    // ID 기반 동등성 비교
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        // ID가 null이면 동등하지 않음 (신규 객체)
        return id != null && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        // ID가 null이면 0 반환
        return id != null ? id.hashCode() : 0;
    }

    // name, status 등 다른 필드는 비교하지 않음
    // Entity의 동등성은 ID로만 판단
}
```

**설명**: Aggregate의 equals/hashCode는 ID 기반으로 구현합니다. 다른 필드(name, status 등)는 비교하지 않습니다. ID가 null인 신규 객체는 동등하지 않은 것으로 처리합니다.

---

### AGG-016: 복잡한 비즈니스 규칙은 VO로 위임

#### ✅ GOOD Example

```java
public class Order {

    private final Money totalAmount;
    private final Email customerEmail;
    private final PhoneNumber customerPhone;

    // ❌ Bad: Aggregate에서 복잡한 검증 로직 직접 구현
    // private void validateEmail(String email) {
    //     Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    //     if (!pattern.matcher(email).matches()) { ... }
    // }

    // ✅ Good: 복잡한 검증/계산은 VO에 위임
    public static Order forNew(
            CustomerId customerId,
            String email,
            String phone,
            List<OrderItem> items,
            Instant now) {
        // VO 생성 시 검증이 자동으로 수행됨
        Email customerEmail = Email.of(email);         // Email VO가 형식 검증
        PhoneNumber customerPhone = PhoneNumber.of(phone);  // PhoneNumber VO가 검증
        Money totalAmount = calculateTotal(items);     // Money VO가 금액 계산

        return new Order(
            OrderId.forNew(),
            customerId,
            customerEmail,
            customerPhone,
            totalAmount,
            now
        );
    }

    private static Money calculateTotal(List<OrderItem> items) {
        return items.stream()
            .map(OrderItem::subtotal)
            .reduce(Money.zero(), Money::add);
    }
}
```

**설명**: 복잡한 비즈니스 규칙(검증, 계산, 형식 확인)은 Value Object에 위임합니다. Aggregate는 VO를 조합하고 전체 불변식을 관리하는 역할에 집중합니다.

---

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

### CADP-001: persist() 메서드만 제공

#### ✅ GOOD Example

```java
@Component
public class OrderCommandAdapter implements OrderCommandPort {

    private final OrderJpaRepository repository;
    private final OrderJpaEntityMapper mapper;

    public OrderCommandAdapter(OrderJpaRepository repository, OrderJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // persist() 메서드만 제공 - save(), update(), delete() 별도 메서드 금지
    @Override
    public OrderId persist(Order order) {
        // Domain → Entity 변환
        OrderJpaEntity entity = mapper.toEntity(order);

        // JPA save() - INSERT/UPDATE 자동 판단 (Merge 방식)
        // Domain.isNew()로 신규 여부 판단 → Entity에 ID 유무로 JPA가 처리
        OrderJpaEntity saved = repository.save(entity);

        // 생성된 ID 반환
        return new OrderId(saved.getId());
    }

    // save(), update(), delete() 별도 메서드 정의 금지!
    // public void save(Order order) { ... }    // 금지!
    // public void update(Order order) { ... }  // 금지!
    // public void delete(OrderId id) { ... }   // 금지!
    // → SoftDelete는 Domain 상태 변경 후 persist() 호출
}
```

**설명**: CommandAdapter는 persist(Aggregate) 메서드만 제공합니다. save(), update(), delete() 별도 메서드를 정의하지 않습니다. JPA Merge 방식으로 INSERT/UPDATE를 자동 판단하고, SoftDelete는 Domain 상태 변경 후 persist()를 호출합니다.

---

### CADP-002: CommandAdapter에서 @Transactional 금지

#### ✅ GOOD Example

```java
@Component
// @Transactional 어노테이션 없음! 클래스 레벨에서도 금지
public class ProductCommandAdapter implements ProductCommandPort {

    private final ProductJpaRepository repository;
    private final ProductJpaEntityMapper mapper;

    public ProductCommandAdapter(ProductJpaRepository repository, ProductJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // @Transactional 없음 - 메서드 레벨에서도 금지
    @Override
    public ProductId persist(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        ProductJpaEntity saved = repository.save(entity);
        return new ProductId(saved.getId());
    }
}

// 트랜잭션은 Application Layer에서 관리
@Service
public class CreateProductService implements CreateProductUseCase {

    private final ProductCommandPort productCommandPort;

    public CreateProductService(ProductCommandPort productCommandPort) {
        this.productCommandPort = productCommandPort;
    }

    @Override
    @Transactional  // 트랜잭션 경계는 여기서 설정
    public ProductId execute(CreateProductCommand command) {
        Product product = Product.forNew(command.name(), command.price());
        return productCommandPort.persist(product);
    }
}
```

**설명**: @Transactional 어노테이션을 CommandAdapter 클래스나 메서드에 사용하지 않습니다. 트랜잭션 경계는 Application Layer(UseCase/Service)에서 관리합니다. 여러 Aggregate를 하나의 트랜잭션으로 묶는 것은 Application Layer 책임입니다.

---

### CADP-007: CommandAdapter에 비즈니스 로직 금지

#### ✅ GOOD Example

```java
@Component
public class OrderCommandAdapter implements OrderCommandPort {

    private final OrderJpaRepository repository;
    private final OrderJpaEntityMapper mapper;

    public OrderCommandAdapter(OrderJpaRepository repository, OrderJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public OrderId persist(Order order) {
        // 비즈니스 로직 없음!
        // if (order.getTotalAmount() > 1000000) { ... }  // 금지!
        // order.calculateDiscount();                     // 금지!
        // validateOrder(order);                          // 금지!

        // 단순히 Domain → Entity 변환 후 저장
        OrderJpaEntity entity = mapper.toEntity(order);
        OrderJpaEntity saved = repository.save(entity);
        return new OrderId(saved.getId());
    }

    // 검증, 계산, 상태 변경 로직은 Domain 또는 Application Layer에서 처리
    // Adapter는 기술적 변환(Domain ↔ Entity)만 담당
}
```

**설명**: CommandAdapter에 if/switch 조건 분기, 계산 로직, 상태 검증 등 비즈니스 로직을 포함하지 않습니다. 단순히 Domain → Entity 변환 후 저장만 수행합니다. 비즈니스 로직은 Domain과 Application Layer에서 처리합니다.

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

### CRI-001: domain.[bc].query.criteria 패키지

#### ✅ GOOD Example

```java
package com.ryuqq.domain.order.query.criteria;

import com.ryuqq.domain.common.vo.PageRequest;
import com.ryuqq.domain.common.vo.SortDirection;

/**
 * Order 검색 조건.
 */
public record OrderSearchCriteria(
    String status,
    String customerId,
    PageRequest page,
    SortDirection sortDirection
) {
    // 구현
}
```

**설명**: Criteria는 domain.{bc}.query.criteria 패키지에 위치합니다. Query 관련 객체를 query 하위 패키지로 분리하여 CQRS 패턴을 명확히 합니다.

---

### CRI-004: Record 타입 필수

#### ✅ GOOD Example

```java
// ✅ Good: Record 타입
public record OrderSearchCriteria(
    String status,
    String customerId,
    String fromDate,
    String toDate,
    PageRequest page,
    SortDirection sortDirection
) {

    // Compact Constructor로 기본값 및 검증
    public OrderSearchCriteria {
        if (page == null) {
            page = PageRequest.of(0, 20);
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.DESC;
        }
    }

    public static OrderSearchCriteria of(
            String status,
            String customerId,
            String fromDate,
            String toDate,
            int pageNo,
            int pageSize,
            SortDirection sortDirection) {
        return new OrderSearchCriteria(
            status,
            customerId,
            fromDate,
            toDate,
            PageRequest.of(pageNo, pageSize),
            sortDirection
        );
    }

    public boolean hasStatusFilter() {
        return status != null && !status.isBlank();
    }

    public boolean hasCustomerFilter() {
        return customerId != null && !customerId.isBlank();
    }

    public boolean hasDateRange() {
        return fromDate != null && toDate != null;
    }
}
```

**설명**: Criteria는 Record 타입으로 정의합니다. Compact Constructor에서 기본값을 적용하고, hasXxxFilter() 메서드로 필터 존재 여부를 확인합니다.

---

### CRI-005: of() 정적 팩토리 메서드 필수

#### ✅ GOOD Example

```java
public record OrderSearchCriteria(
    String status,
    String customerId,
    PageRequest page,
    SortDirection sortDirection
) {

    /**
     * 검색 조건 생성.
     */
    public static OrderSearchCriteria of(
            String status,
            String customerId,
            int pageNo,
            int pageSize,
            SortDirection sortDirection) {
        return new OrderSearchCriteria(
            status,
            customerId,
            PageRequest.of(pageNo, Math.min(pageSize, 100)),  // 최대 100개 제한
            sortDirection != null ? sortDirection : SortDirection.DESC
        );
    }

    /**
     * 기본 검색 조건 (전체 조회, 첫 페이지).
     */
    public static OrderSearchCriteria defaultCriteria() {
        return new OrderSearchCriteria(null, null, PageRequest.of(0, 20), SortDirection.DESC);
    }

    /**
     * 특정 고객의 주문 검색.
     */
    public static OrderSearchCriteria byCustomer(String customerId, int pageNo, int pageSize) {
        return of(null, customerId, pageNo, pageSize, SortDirection.DESC);
    }

    /**
     * 특정 상태의 주문 검색.
     */
    public static OrderSearchCriteria byStatus(String status, int pageNo, int pageSize) {
        return of(status, null, pageNo, pageSize, SortDirection.DESC);
    }
}
```

**설명**: Criteria는 of() 정적 팩토리 메서드를 제공합니다. 기본값 적용, 최대값 제한 등의 로직을 포함합니다. 자주 사용하는 조건은 별도 팩토리 메서드(byCustomer, byStatus)로 제공합니다.

---

### CRI-006: Criteria Lombok 금지

#### ✅ GOOD Example

```java
// ✅ Good: Record 사용 - Lombok 불필요
public record OrderSearchCriteria(
    String status,
    String customerId,
    PageRequest page,
    SortDirection sortDirection
) {

    public static OrderSearchCriteria of(
            String status,
            String customerId,
            int pageNo,
            int pageSize,
            SortDirection sortDirection) {
        return new OrderSearchCriteria(
            status,
            customerId,
            PageRequest.of(pageNo, pageSize),
            sortDirection != null ? sortDirection : SortDirection.DESC
        );
    }
}

// Record가 자동 제공:
// - 불변성 (final 필드)
// - 접근자 (status(), customerId(), page(), sortDirection())
// - equals(), hashCode(), toString()

// ❌ Bad: Lombok 사용
// @Getter
// @Builder
// public class OrderSearchCriteria {
//     private final String status;
//     private final String customerId;
//     ...
// }
```

**설명**: Criteria에서 Lombok을 사용하지 않습니다. Java Record가 불변성, 접근자, equals/hashCode/toString을 자동 제공합니다.

---

### ENT-001: BaseAuditEntity 상속 필수

#### ✅ GOOD Example

```java
@Entity
@Table(name = "orders")
public class OrderJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;  // Long FK 전략

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    protected OrderJpaEntity() {
    }

    // Entity.of() 정적 팩토리 메서드
    public static OrderJpaEntity of(String orderNumber, Long customerId, OrderStatus status) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.orderNumber = orderNumber;
        entity.customerId = customerId;
        entity.status = status;
        return entity;
    }

    // Getter만 제공 (Setter 금지)
    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public Long getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
}
```

**설명**: BaseAuditEntity를 상속하여 createdAt, updatedAt 필드를 자동 관리합니다. @CreatedDate, @LastModifiedDate는 부모 클래스에서 처리됩니다. 모든 Entity는 생성/수정 시간 추적을 위해 BaseAuditEntity를 상속해야 합니다.

---

### ENT-002: Long FK 전략 (JPA 관계 어노테이션 금지)

#### ✅ GOOD Example

```java
@Entity
@Table(name = "order_items")
public class OrderItemJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;  // Long FK (JPA 관계 어노테이션 금지)

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // 명시적 protected 생성자 (Lombok @NoArgsConstructor 미사용)
    protected OrderItemJpaEntity() {
    }

    // 명시적 정적 팩토리 메서드 (Lombok @Builder 미사용)
    public static OrderItemJpaEntity of(Long orderId, String productId, int quantity) {
        OrderItemJpaEntity entity = new OrderItemJpaEntity();
        entity.orderId = orderId;
        entity.productId = productId;
        entity.quantity = quantity;
        return entity;
    }

    // 명시적 Getter (Lombok @Getter 미사용)
    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}
```

**설명**: @Data, @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor 등 Lombok 사용이 금지됩니다. Pure Java로 생성자, Getter를 명시적으로 작성합니다. 컴파일 타임에 코드가 명확히 보이고 디버깅이 용이합니다.

---

### ENT-004: of() 정적 팩토리 메서드 필수

#### ✅ GOOD Example

```java
@Entity
@Table(name = "orders")
public class OrderJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Long FK 전략 - @ManyToOne, @OneToMany 금지
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "shipping_address_id")
    private Long shippingAddressId;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    // 연관 엔티티 필요 시 Repository에서 별도 조회
    // List<OrderItemJpaEntity> items; 금지!
    // CustomerJpaEntity customer;    금지!

    protected OrderJpaEntity() {
    }

    public static OrderJpaEntity of(Long customerId, Long shippingAddressId, Long paymentMethodId) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.customerId = customerId;
        entity.shippingAddressId = shippingAddressId;
        entity.paymentMethodId = paymentMethodId;
        return entity;
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public Long getShippingAddressId() { return shippingAddressId; }
    public Long getPaymentMethodId() { return paymentMethodId; }
}
```

**설명**: @ManyToOne, @OneToMany, @OneToOne, @ManyToMany JPA 관계 어노테이션 사용이 금지됩니다. 외래 키는 Long 타입 필드로 저장하고, 연관 엔티티가 필요하면 Repository에서 별도 조회합니다. N+1 문제와 LazyLoading 이슈를 원천 차단합니다.

---

### ENT-006: protected/private 생성자 사용

#### ✅ GOOD Example

```java
@Entity
@Table(name = "products")
public class ProductJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "category_id")
    private Long categoryId;

    // 생성자는 protected/private (외부 직접 호출 금지)
    protected ProductJpaEntity() {
    }

    // of() 정적 팩토리 메서드 - 유일한 public 생성 경로
    public static ProductJpaEntity of(String name, Long price, Long categoryId) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.name = name;
        entity.price = price;
        entity.categoryId = categoryId;
        return entity;
    }

    // ID 포함 복원용 (영속화된 데이터에서 Entity 재생성)
    public static ProductJpaEntity withId(Long id, String name, Long price, Long categoryId) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.id = id;
        entity.name = name;
        entity.price = price;
        entity.categoryId = categoryId;
        return entity;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getPrice() { return price; }
    public Long getCategoryId() { return categoryId; }
}
```

**설명**: Entity 생성은 of(...) 정적 팩토리 메서드만 외부에 공개합니다. 생성자는 protected/private으로 선언하여 외부에서 직접 new 호출을 금지합니다. 불변식 검증과 생성 로직을 of() 메서드에서 중앙 관리합니다.

---

### ENT-007: SoftDeletableEntity 적용 (논리삭제)

#### ✅ GOOD Example

```java
@Entity
@Table(name = "orders")
public class OrderJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    protected OrderJpaEntity() {
    }

    public static OrderJpaEntity of(String orderNumber, OrderStatus status) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.orderNumber = orderNumber;
        entity.status = status;
        return entity;
    }

    // Getter만 제공 (Setter 금지)
    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public OrderStatus getStatus() { return status; }

    // 상태 변경이 필요하면 의미 있는 이름의 메서드로 제공
    // setStatus(OrderStatus) 대신 의미 있는 비즈니스 메서드 사용
    public void changeStatus(OrderStatus newStatus) {
        // 상태 전이 검증 등 비즈니스 로직 포함 가능
        this.status = newStatus;
    }

    public void cancel() {
        if (this.status == OrderStatus.SHIPPED) {
            throw new IllegalStateException("배송 완료된 주문은 취소할 수 없습니다");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
```

**설명**: setXxx() 형태의 Setter 메서드를 제공하지 않습니다. 상태 변경이 필요하면 의미 있는 이름의 비즈니스 메서드(cancel(), changeStatus())로 제공합니다. 불변성과 데이터 무결성을 보호합니다.

---

### EVT-001: DomainEvent 인터페이스 구현 필수

#### ✅ GOOD Example

```java
package com.ryuqq.domain.order.event;

import com.ryuqq.domain.order.id.OrderId;
import com.ryuqq.domain.common.event.DomainEvent;

import java.time.Instant;

/**
 * Order 생성 이벤트.
 */
public record OrderCreatedEvent(
    OrderId orderId,
    String customerName,
    Instant occurredAt
) implements DomainEvent {

    public static OrderCreatedEvent from(Order order, Instant now) {
        return new OrderCreatedEvent(order.id(), order.customerName(), now);
    }
}
```

**설명**: Domain Event는 domain.{bc}.event 패키지에 위치합니다. Bounded Context별로 이벤트를 분리하여 관리합니다.

---

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

### EVT-002: Record 타입 필수

#### ✅ GOOD Example

```java
// ✅ Good: Record 타입 + DomainEvent 구현
public record OrderCreatedEvent(
    OrderId orderId,
    CustomerId customerId,
    Money totalAmount,
    Instant occurredAt
) implements DomainEvent {

    public static OrderCreatedEvent from(Order order, Instant now) {
        return new OrderCreatedEvent(
            order.id(),
            order.customerId(),
            order.totalAmount(),
            now
        );
    }
}

// 공통 인터페이스
package com.ryuqq.domain.common.event;

public interface DomainEvent {
    Instant occurredAt();
}
```

**설명**: Domain Event는 반드시 Record 타입으로 정의하고 DomainEvent 인터페이스를 구현합니다. Record는 이벤트의 불변성을 보장합니다.

---

### EVT-003: occurredAt (Instant) 필드 필수

#### ✅ GOOD Example

```java
// ✅ Good: 과거형 네이밍 (이미 발생한 사실)
public record OrderCreatedEvent(...) implements DomainEvent { }
public record OrderConfirmedEvent(...) implements DomainEvent { }
public record OrderCancelledEvent(...) implements DomainEvent { }
public record OrderShippedEvent(...) implements DomainEvent { }
public record OrderCompletedEvent(...) implements DomainEvent { }
public record PaymentPaidEvent(...) implements DomainEvent { }
public record PaymentRefundedEvent(...) implements DomainEvent { }

// ❌ Bad: 현재형/명령형 네이밍
// public record OrderCreateEvent(...) { }      // 현재형
// public record CreateOrderEvent(...) { }      // 명령형
// public record OrderConfirmEvent(...) { }     // 현재형

// 정규식 패턴:
// ^[A-Z][a-zA-Z]*(ed|ent|aid|ade|one|ept|ilt|elt|ought|aught|old|eld|own|ven|ken|ten|ung|ost|eft)Event$
// - Created, Confirmed, Cancelled, Completed, Paid, Sent, Made, Done, Bought, Sold, Shown 등
```

**설명**: Domain Event는 과거형으로 네이밍합니다. 이벤트는 "이미 발생한 사실"을 나타내므로 Created, Confirmed, Cancelled 등 과거분사를 사용합니다.

---

### EVT-004: from(Aggregate, Instant) 정적 팩토리 메서드 필수

#### ✅ GOOD Example

```java
public record OrderCreatedEvent(
    OrderId orderId,
    CustomerId customerId,
    Money totalAmount,
    List<OrderItemSnapshot> items,
    Instant occurredAt
) implements DomainEvent {

    /**
     * Aggregate로부터 이벤트 생성.
     * @param order 이벤트 발생 원인이 된 Aggregate
     * @param now 이벤트 발생 시점
     * @return 생성 이벤트
     */
    public static OrderCreatedEvent from(Order order, Instant now) {
        return new OrderCreatedEvent(
            order.id(),
            order.customerId(),
            order.totalAmount(),
            order.items().stream()
                .map(OrderItemSnapshot::from)
                .toList(),
            now
        );
    }

    // 이벤트 내 스냅샷 (이벤트 발생 시점의 상태 보존)
    public record OrderItemSnapshot(
        String productId,
        int quantity,
        Money unitPrice
    ) {
        public static OrderItemSnapshot from(OrderItem item) {
            return new OrderItemSnapshot(
                item.productId().value(),
                item.quantity(),
                item.unitPrice()
            );
        }
    }
}
```

**설명**: Domain Event는 from(Aggregate, Instant) 정적 팩토리 메서드를 제공합니다. Aggregate에서 필요한 정보를 추출하여 이벤트를 생성합니다. 스냅샷으로 이벤트 발생 시점의 상태를 보존합니다.

---

### EVT-005: 과거형 네이밍 필수 (*Event)

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

### EVT-006: domain.[bc].event 패키지 위치

#### ✅ GOOD Example

```java
// DomainEvent 인터페이스
package com.ryuqq.domain.common.event;

public interface DomainEvent {
    /**
     * 이벤트 발생 시점.
     * @return 발생 시점 (UTC)
     */
    Instant occurredAt();
}

// 구현 예시
public record OrderCreatedEvent(
    OrderId orderId,
    CustomerId customerId,
    Instant occurredAt        // 필수 필드
) implements DomainEvent {

    public static OrderCreatedEvent from(Order order, Instant now) {
        return new OrderCreatedEvent(
            order.id(),
            order.customerId(),
            now                // 외부에서 주입받은 시간
        );
    }
}

// Aggregate에서 이벤트 발행
public class Order {
    public static Order forNew(CustomerId customerId, Instant now) {
        Order order = new Order(...);
        order.registerEvent(OrderCreatedEvent.from(order, now));
        return order;
    }
}
```

**설명**: Domain Event는 occurredAt(Instant) 필드가 필수입니다. DomainEvent 인터페이스에서 occurredAt() 메서드를 정의하고, 모든 이벤트가 구현합니다.

---

### EXC-001: ErrorCode 인터페이스 구현 필수

#### ✅ GOOD Example

```java
package com.ryuqq.domain.order.exception;

import com.ryuqq.domain.common.exception.DomainException;
import com.ryuqq.domain.order.id.OrderId;

/**
 * Order를 찾을 수 없을 때 발생하는 예외.
 */
public class OrderNotFoundException extends DomainException {

    private final OrderId orderId;

    public OrderNotFoundException(OrderId orderId) {
        super(OrderErrorCode.ORDER_NOT_FOUND,
            String.format("Order not found: %s", orderId.value()));
        this.orderId = orderId;
    }

    public OrderId getOrderId() {
        return orderId;
    }
}
```

**설명**: Domain Exception은 domain.{bc}.exception 패키지에 위치합니다. Bounded Context별로 예외를 분리하여 관리합니다.

---

### EXC-003: ErrorCode Lombok 금지

#### ✅ GOOD Example

```java
// 공통 DomainException
package com.ryuqq.domain.common.exception;

public class DomainException extends RuntimeException {

    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}

// BC별 Exception
public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(OrderId orderId) {
        super(OrderErrorCode.ORDER_NOT_FOUND,
            String.format("Order not found: %s", orderId.value()));
    }
}

public class OrderCannotBeCancelledException extends DomainException {
    public OrderCannotBeCancelledException(OrderId orderId, OrderStatus currentStatus) {
        super(OrderErrorCode.ORDER_CANNOT_BE_CANCELLED,
            String.format("Order %s cannot be cancelled. Current status: %s",
                orderId.value(), currentStatus));
    }
}
```

**설명**: 모든 Domain Exception은 DomainException을 상속합니다. DomainException은 RuntimeException을 상속하고 ErrorCode를 포함합니다.

---

### EXC-005: getCode() 메서드 필수

#### ✅ GOOD Example

```java
// ErrorCode 인터페이스
package com.ryuqq.domain.common.exception;

public interface ErrorCode {
    String getCode();
    int getHttpStatus();
    String getMessage();
}

// BC별 ErrorCode Enum
package com.ryuqq.domain.order.exception;

public enum OrderErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_ORDER_ITEM("ORDER_INVALID_ITEM", 400, "주문 항목이 유효하지 않습니다"),
    INVALID_QUANTITY("ORDER_INVALID_QUANTITY", 400, "수량이 유효하지 않습니다"),

    // 404 Not Found
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", 404, "주문을 찾을 수 없습니다"),
    ORDER_ITEM_NOT_FOUND("ORDER_ITEM_NOT_FOUND", 404, "주문 항목을 찾을 수 없습니다"),

    // 409 Conflict
    ORDER_ALREADY_CONFIRMED("ORDER_ALREADY_CONFIRMED", 409, "이미 확정된 주문입니다"),
    ORDER_ALREADY_CANCELLED("ORDER_ALREADY_CANCELLED", 409, "이미 취소된 주문입니다"),

    // 422 Unprocessable Entity
    ORDER_CANNOT_BE_CANCELLED("ORDER_CANNOT_CANCEL", 422, "취소할 수 없는 주문 상태입니다"),
    ORDER_CANNOT_BE_SHIPPED("ORDER_CANNOT_SHIP", 422, "배송할 수 없는 주문 상태입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    OrderErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public int getHttpStatus() { return httpStatus; }

    @Override
    public String getMessage() { return message; }
}
```

**설명**: ErrorCode는 인터페이스로 정의하고, BC별 Enum이 구현합니다. HTTP 상태별로 그룹화하여 관리합니다.

---

### EXC-009: DomainException 상속 필수

#### ✅ GOOD Example

```java
// ✅ Good: int 타입 HTTP 상태 코드
public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND("ORDER_NOT_FOUND", 404, "주문을 찾을 수 없습니다"),
    ORDER_CANNOT_BE_CANCELLED("ORDER_CANNOT_CANCEL", 422, "취소할 수 없습니다");

    private final String code;
    private final int httpStatus;  // int 타입
    private final String message;

    OrderErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
}

// ❌ Bad: Spring HttpStatus 사용
// import org.springframework.http.HttpStatus;  // Domain Layer에서 금지!
//
// public enum OrderErrorCode implements ErrorCode {
//     ORDER_NOT_FOUND("ORDER_NOT_FOUND", HttpStatus.NOT_FOUND, "...");
//
//     private final HttpStatus httpStatus;  // Spring 의존성!
// }
```

**설명**: HTTP 상태 코드는 int 타입으로 정의합니다. Spring의 HttpStatus를 사용하면 Domain Layer가 Spring에 의존하게 되어 금지됩니다.

---

### EXC-013: Exception Spring 금지

#### ✅ GOOD Example

```java
public class OrderNotFoundException extends DomainException {

    private final OrderId orderId;

    public OrderNotFoundException(OrderId orderId) {
        super(OrderErrorCode.ORDER_NOT_FOUND,
            String.format("Order not found: %s", orderId.value()));
        this.orderId = orderId;
    }

    public OrderId getOrderId() {
        return orderId;
    }
}

public class OrderCannotBeCancelledException extends DomainException {

    private final OrderId orderId;
    private final OrderStatus currentStatus;

    public OrderCannotBeCancelledException(OrderId orderId, OrderStatus currentStatus) {
        super(OrderErrorCode.ORDER_CANNOT_BE_CANCELLED,
            String.format("Order %s cannot be cancelled. Current status: %s",
                orderId.value(), currentStatus.displayName()));
        this.orderId = orderId;
        this.currentStatus = currentStatus;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }
}
```

**설명**: Exception은 디버깅에 필요한 컨텍스트 정보(ID, 현재 상태 등)를 필드로 포함합니다. 메시지에 포함하고, getter로 접근 가능하게 합니다.

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

### ID-001: *Id 네이밍 필수

#### ✅ GOOD Example

```java
package com.ryuqq.domain.order.id;

/**
 * Order ID Value Object.
 * Long 기반 Auto Increment ID.
 */
public record OrderId(Long value) {

    public static OrderId forNew() {
        return new OrderId(null);
    }

    public static OrderId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("OrderId value must not be null");
        }
        return new OrderId(value);
    }

    public boolean isNew() {
        return value == null;
    }
}
```

**설명**: ID Value Object는 domain.{bc}.id 패키지에 위치합니다. Bounded Context별로 ID를 분리하여 관리합니다.

---

### ID-002: Record 타입 필수

#### ✅ GOOD Example

```java
// ✅ Good: Record 타입 사용
public record OrderId(Long value) {

    // Compact Constructor로 검증
    public OrderId {
        // Long ID는 null 허용 (신규 생성 시)
    }

    public static OrderId forNew() {
        return new OrderId(null);
    }

    public static OrderId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("OrderId value must not be null for existing entity");
        }
        return new OrderId(value);
    }

    public boolean isNew() {
        return value == null;
    }
}

// ❌ Bad: class 타입 사용
// public class OrderId {
//     private final Long value;
//     public OrderId(Long value) { this.value = value; }
//     public Long getValue() { return value; }
// }
```

**설명**: ID Value Object는 반드시 Java Record 타입으로 정의합니다. Record는 불변성, equals/hashCode, toString을 자동 제공합니다.

---

### ID-004: Long ID forNew() 필수

#### ✅ GOOD Example

```java
public record OrderId(Long value) {

    /**
     * 새로운 ID 생성 (Auto Increment용).
     * ID는 null로, persist 시점에 DB에서 할당됩니다.
     */
    public static OrderId forNew() {
        return new OrderId(null);
    }

    /**
     * 기존 ID 복원.
     */
    public static OrderId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("OrderId value must not be null for existing entity");
        }
        return new OrderId(value);
    }

    /**
     * 신규 생성 여부.
     * @return ID가 null이면 true (아직 persist 전)
     */
    public boolean isNew() {
        return value == null;
    }
}

// 사용 예시
// Order order = Order.forNew(...);  // order.id().isNew() == true
// orderRepository.save(order);       // DB에서 ID 할당
// order.id().isNew() == false        // persist 후
```

**설명**: Long 기반 ID는 forNew() → null 반환, isNew()로 신규 여부 판단 패턴을 사용합니다. Auto Increment 방식에서 ID는 persist 시점에 DB에서 할당됩니다.

---

### ID-008: String ID는 외부에서 주입

#### ✅ GOOD Example

```java
public record OrderId(String value) {

    // Compact Constructor에서 검증
    public OrderId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OrderId value must not be blank");
        }
    }

    /**
     * 새로운 ID 생성 (외부 주입 필수).
     * Application Layer의 ID Generator에서 생성된 값을 받습니다.
     *
     * @param value 외부에서 생성된 ID 값 (UUIDv7, Snowflake 등)
     */
    public static OrderId forNew(String value) {
        return new OrderId(value);
    }

    /**
     * 기존 ID 복원.
     */
    public static OrderId of(String value) {
        return new OrderId(value);
    }

    // ❌ String ID는 isNew() 없음
    // 생성 시점에 이미 값이 존재하므로 신규 여부를 ID로 판단 불가
}

// Application Layer에서 ID 생성
// public class CreateOrderService {
//     private final IdGenerator idGenerator;
//
//     public Long execute(CreateOrderCommand cmd, Instant now) {
//         String newId = idGenerator.generate();  // UUIDv7 등
//         Order order = Order.forNew(OrderId.forNew(newId), ...);
//     }
// }
```

**설명**: String 기반 ID는 forNew(String value)로 외부에서 생성된 값을 주입받습니다. Domain Layer에서 UUID를 직접 생성하지 않습니다. String ID는 isNew() 메서드가 없습니다.

---

### QDR-001: Query 전용 (조회만)

#### ✅ GOOD Example

```java
@Repository
public class OrderQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public OrderQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private static final QOrderJpaEntity order = QOrderJpaEntity.orderJpaEntity;

    public Optional<OrderJpaEntity> findById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(order)
                .where(order.id.eq(id))
                .fetchOne()
        );
    }

    public List<OrderJpaEntity> findByCriteria(OrderCriteria criteria) {
        return queryFactory
            .selectFrom(order)
            .where(buildWhereClause(criteria))
            .orderBy(order.createdAt.desc())
            .fetch();
    }

    private BooleanBuilder buildWhereClause(OrderCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();
        if (criteria.status() != null) {
            builder.and(order.status.eq(criteria.status()));
        }
        if (criteria.customerId() != null) {
            builder.and(order.customerId.eq(criteria.customerId()));
        }
        return builder;
    }
}
```

**설명**: QueryDslRepository 클래스는 @Repository 어노테이션으로 Bean 등록합니다. 인터페이스가 아닌 구체 클래스로 구현하며, JPAQueryFactory를 생성자 주입받습니다.

---

### QDR-002: Join 절대 금지

#### ✅ GOOD Example

```java
@Repository
// @Transactional 어노테이션 없음!
// 트랜잭션 관리는 Application Layer 책임
public class ProductQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ProductQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private static final QProductJpaEntity product = QProductJpaEntity.productJpaEntity;

    // @Transactional 없음 - 메서드 레벨에도 사용 금지
    public Optional<ProductJpaEntity> findById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(product)
                .where(product.id.eq(id))
                .fetchOne()
        );
    }

    // 읽기 전용도 Application Layer에서 @Transactional(readOnly = true) 설정
    public List<ProductJpaEntity> findByCategory(Long categoryId) {
        return queryFactory
            .selectFrom(product)
            .where(product.categoryId.eq(categoryId))
            .fetch();
    }
}
```

**설명**: @Transactional 어노테이션을 QueryDslRepository 클래스나 메서드에 사용하지 않습니다. 트랜잭션 경계는 Application Layer(UseCase/Service)에서 관리합니다. Repository는 쿼리 실행만 담당합니다.

---

### QDR-003: findAll 금지 (OOM 방지)

#### ✅ GOOD Example

```java
@Repository
public class CustomerQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    // JPAQueryFactory만 주입받음 - EntityManager 직접 주입 금지
    public CustomerQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private static final QCustomerJpaEntity customer = QCustomerJpaEntity.customerJpaEntity;

    public Optional<CustomerJpaEntity> findById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(customer)
                .where(customer.id.eq(id))
                .fetchOne()
        );
    }
}

// JPAQueryFactory는 Config에서 Bean 등록
@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
```

**설명**: QueryDslRepository는 JPAQueryFactory만 생성자 주입받습니다. EntityManager를 직접 주입받지 않습니다. JPAQueryFactory Bean은 Config 클래스에서 중앙 관리합니다.

---

### QDR-005: existsById 필수 메서드

#### ✅ GOOD Example

```java
@Repository
public class OrderQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public OrderQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private static final QOrderJpaEntity order = QOrderJpaEntity.orderJpaEntity;

    // Join 없이 단일 테이블 조회만 수행
    public List<OrderJpaEntity> findByCustomerId(Long customerId) {
        return queryFactory
            .selectFrom(order)
            .where(order.customerId.eq(customerId))
            .orderBy(order.createdAt.desc())
            .fetch();
    }

    // 연관 데이터가 필요하면 Application Layer에서 별도 조회 후 조합
    // .join(), .leftJoin(), .innerJoin() 사용 금지!
    // 예시 (금지):
    // queryFactory.selectFrom(order)
    //     .leftJoin(orderItem).on(orderItem.orderId.eq(order.id))
    //     .fetch();
}

// Application Layer에서 조합
@Component
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderQueryDslRepository orderRepository;
    private final OrderItemQueryDslRepository orderItemRepository;
    private final OrderJpaEntityMapper mapper;

    public OrderQueryAdapter(OrderQueryDslRepository orderRepository,
            OrderItemQueryDslRepository orderItemRepository,
            OrderJpaEntityMapper mapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return orderRepository.findById(id.value())
            .map(orderEntity -> {
                List<OrderItemJpaEntity> items = orderItemRepository.findByOrderId(id.value());
                return mapper.toDomain(orderEntity, items);
            });
    }
}
```

**설명**: .join(), .leftJoin(), .innerJoin() 사용이 금지됩니다. QueryDslRepository는 단일 테이블 조회만 수행합니다. 연관 데이터가 필요하면 Application Layer에서 별도 조회 후 조합합니다. N+1 제어와 쿼리 단순화를 위함입니다.

---

### QDR-008: QueryDslRepository에서 @Transactional 금지

#### ✅ GOOD Example

```java
@Repository
public class ProductQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ProductQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private static final QProductJpaEntity product = QProductJpaEntity.productJpaEntity;

    // findAll() 금지! 항상 조건 + 페이징 필수
    public List<ProductJpaEntity> findByCriteria(ProductCriteria criteria, int offset, int limit) {
        BooleanBuilder builder = buildWhereClause(criteria);

        return queryFactory
            .selectFrom(product)
            .where(builder)
            .orderBy(product.createdAt.desc())
            .offset(offset)
            .limit(limit)  // 필수: 페이징 제한
            .fetch();
    }

    public long countByCriteria(ProductCriteria criteria) {
        BooleanBuilder builder = buildWhereClause(criteria);

        return queryFactory
            .select(product.count())
            .from(product)
            .where(builder)
            .fetchOne();
    }

    private BooleanBuilder buildWhereClause(ProductCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();
        if (criteria.categoryId() != null) {
            builder.and(product.categoryId.eq(criteria.categoryId()));
        }
        if (criteria.minPrice() != null) {
            builder.and(product.price.goe(criteria.minPrice()));
        }
        return builder;
    }
}
```

**설명**: 조건 없이 전체 조회하는 findAll() 메서드를 제공하지 않습니다. 항상 조건(criteria)과 페이징(offset, limit)을 필수 파라미터로 요구합니다. 대용량 테이블 전체 조회로 인한 OOM을 방지합니다.

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

### REPO-001: Command 전용 (save/delete만)

#### ✅ GOOD Example

```java
package com.example.adapter.out.persistence.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adapter.out.persistence.order.entity.OrderJpaEntity;

/**
 * OrderJpaRepository - Command 작업 전용
 * save(), delete() 메서드만 사용
 * Query 작업은 OrderQueryDslRepository에서 처리
 */
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    // 추가 메서드 정의 금지
    // Query Method, @Query 어노테이션 금지
    // 상속받은 save(), delete(), findById()만 사용
}
```

**설명**: JpaRepository<Entity, ID>를 상속하여 기본 CRUD 기능을 제공받습니다. JpaRepository 상속만 허용하고 CrudRepository, PagingAndSortingRepository 직접 상속은 금지합니다.

---

### REPO-002: Query Method 금지

#### ✅ GOOD Example

```java
// JpaRepository는 Command 작업 전용
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    // 상속받은 메서드만 사용:
    // - save(S entity): 저장/수정
    // - delete(T entity): 삭제
    // - saveAll(Iterable<S> entities): 일괄 저장
    // - deleteAll(Iterable<? extends T> entities): 일괄 삭제

    // 조회 메서드 추가 금지:
    // List<OrderJpaEntity> findByStatus(String status);     // 금지!
    // Optional<OrderJpaEntity> findByOrderNumber(String n); // 금지!
    // → QueryDslRepository에서 처리
}

// CommandAdapter에서 사용 예시
@Component
public class OrderCommandAdapter implements OrderCommandPort {

    private final OrderJpaRepository repository;
    private final OrderJpaEntityMapper mapper;

    public OrderCommandAdapter(OrderJpaRepository repository, OrderJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public OrderId persist(Order order) {
        OrderJpaEntity entity = mapper.toEntity(order);
        OrderJpaEntity saved = repository.save(entity);  // save()만 사용
        return new OrderId(saved.getId());
    }
}
```

**설명**: JpaRepository는 save(), delete() 등 Command 작업만 수행합니다. 조회 메서드(findByXxx)를 추가하지 않습니다. CQRS 원칙에 따라 조회는 QueryDslRepository에서 처리합니다.

---

### REPO-003: @Query 어노테이션 금지

#### ✅ GOOD Example

```java
// JpaRepository - Query Method 없는 깨끗한 인터페이스
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    // Query Method 정의 금지!
    // List<ProductJpaEntity> findByCategory(String category);   // 금지!
    // Optional<ProductJpaEntity> findByName(String name);       // 금지!
    // List<ProductJpaEntity> findByPriceGreaterThan(Long p);    // 금지!
    // boolean existsByName(String name);                        // 금지!
    // long countByCategory(String category);                    // 금지!

    // 모든 조회는 QueryDslRepository에서 처리
}

// 조회는 QueryDslRepository에서 담당
@Repository
public class ProductQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ProductQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Optional<ProductJpaEntity> findById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(product)
                .where(product.id.eq(id))
                .fetchOne()
        );
    }

    public List<ProductJpaEntity> findByCategory(String category) {
        return queryFactory
            .selectFrom(product)
            .where(product.categoryId.eq(category))
            .fetch();
    }
}
```

**설명**: findByXxx, existsByXxx, countByXxx 등 Spring Data JPA Query Method를 정의하지 않습니다. 모든 조회는 QueryDslRepository에서 QueryDSL로 구현합니다. 쿼리 가독성과 타입 안전성을 보장합니다.

---

### REPO-004: QuerydslPredicateExecutor 금지

#### ✅ GOOD Example

```java
// JpaRepository - @Query 없는 깨끗한 인터페이스
public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {
    // @Query 어노테이션 사용 금지!
    // @Query("SELECT c FROM CustomerJpaEntity c WHERE c.email = :email")
    // Optional<CustomerJpaEntity> findByEmail(@Param("email") String email);

    // Native Query도 금지!
    // @Query(value = "SELECT * FROM customers WHERE email = ?1", nativeQuery = true)
    // CustomerJpaEntity findByEmailNative(String email);
}

// 복잡한 쿼리는 QueryDslRepository에서 처리
@Repository
public class CustomerQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public CustomerQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Optional<CustomerJpaEntity> findByEmail(String email) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(customer)
                .where(customer.email.eq(email))
                .fetchOne()
        );
    }

    public List<CustomerJpaEntity> findVipCustomers() {
        return queryFactory
            .selectFrom(customer)
            .where(customer.grade.eq(CustomerGrade.VIP))
            .orderBy(customer.createdAt.desc())
            .fetch();
    }
}
```

**설명**: @Query 어노테이션(JPQL, Native Query)을 JpaRepository에 사용하지 않습니다. 문자열 기반 쿼리는 컴파일 타임 검증이 불가능합니다. 모든 쿼리는 QueryDSL의 타입 안전 API로 작성합니다.

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

### VO-001: Record 타입 필수

#### ✅ GOOD Example

```java
package com.ryuqq.domain.order.vo;

/**
 * Order Status Value Object.
 */
public enum OrderStatus {
    PENDING("대기중"),
    CONFIRMED("확정됨"),
    SHIPPED("배송중"),
    COMPLETED("완료됨"),
    CANCELLED("취소됨");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
```

**설명**: Value Object는 domain.{bc}.vo 패키지에 위치합니다. 상태, 금액, 주소 등 도메인 개념을 표현하는 불변 객체입니다.

---

### VO-002: of() 정적 팩토리 메서드 필수

#### ✅ GOOD Example

```java
// ✅ Good: Record 타입 (단일/복합 값)
public record Email(String value) {

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        if (!value.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }

    public String domain() {
        return value.substring(value.indexOf("@") + 1);
    }
}

// ✅ Good: Enum 타입 (유한 집합)
public enum PaymentMethod {
    CREDIT_CARD("신용카드"),
    BANK_TRANSFER("계좌이체"),
    VIRTUAL_ACCOUNT("가상계좌");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
```

**설명**: Value Object는 Record(단일/복합 값) 또는 Enum(유한 집합) 타입으로 정의합니다. Compact Constructor에서 검증을 수행합니다.

---

### VO-003: Compact Constructor 검증 필수

#### ✅ GOOD Example

```java
public record Money(BigDecimal amount) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        // 소수점 2자리로 정규화
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    // of() 정적 팩토리 메서드
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money of(String amount) {
        return new Money(new BigDecimal(amount));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    // 연산 메서드
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }
}
```

**설명**: Value Object는 of() 정적 팩토리 메서드를 제공합니다. 다양한 입력 타입(BigDecimal, long, String)에 대한 오버로드와 zero() 같은 특수 팩토리를 제공할 수 있습니다.

---

### VO-004: Enum VO displayName() 필수

#### ✅ GOOD Example

```java
public enum OrderStatus {

    PENDING("대기중"),
    CONFIRMED("확정됨"),
    SHIPPED("배송중"),
    COMPLETED("완료됨"),
    CANCELLED("취소됨");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * UI 표시용 이름.
     * @return 한글 표시명
     */
    public String displayName() {
        return displayName;
    }

    // 도메인 로직 메서드
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }

    public boolean canTransitionTo(OrderStatus target) {
        return switch (this) {
            case PENDING -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == SHIPPED || target == CANCELLED;
            case SHIPPED -> target == COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}
```

**설명**: Enum Value Object는 displayName() 메서드를 필수로 제공합니다. UI 표시용 한글 이름을 반환합니다. 추가로 도메인 로직 메서드(isTerminal, canTransitionTo)를 포함할 수 있습니다.

---

### VO-007: VO Lombok 금지

#### ✅ GOOD Example

```java
// ✅ Good: Record 사용 - Lombok 불필요
public record Address(
    String zipCode,
    String city,
    String street,
    String detail
) {

    public Address {
        if (zipCode == null || zipCode.isBlank()) {
            throw new IllegalArgumentException("zipCode must not be blank");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("city must not be blank");
        }
    }

    public static Address of(String zipCode, String city, String street, String detail) {
        return new Address(zipCode, city, street, detail);
    }

    public String fullAddress() {
        return String.format("(%s) %s %s %s", zipCode, city, street, detail);
    }
}

// Record가 자동 제공하는 기능:
// - 불변성 (final 필드)
// - 접근자 (zipCode(), city(), street(), detail())
// - equals(), hashCode(), toString()
// - Canonical Constructor
```

**설명**: Value Object에서 Lombok을 사용하지 않습니다. Java Record가 불변성, 접근자, equals/hashCode/toString을 자동 제공하므로 Lombok이 불필요합니다.

---


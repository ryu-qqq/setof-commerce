# Order 모듈 분석

> 작성일: 2025-12-29
> 우선순위: P1 (핵심 비즈니스)
> 상태: **리팩토링 필요** - 다수 컨벤션 위반

---

## 1. 현재 상태 요약

| 항목 | 상태 | 비고 |
|------|------|------|
| QueryPort 메서드 네이밍 | ❌ 미준수 | `findByQuery`, `findByAdminQuery` 사용 |
| Criteria 패턴 | ❌ 미사용 | Query DTO 직접 전달 |
| QueryFactory | ❌ 미존재 | 생성 필요 |
| ReadManager @Transactional | ❌ 미준수 | 메서드 레벨 어노테이션 누락 |
| Query DTO 정렬 지원 | ❌ 미지원 | sortBy, sortDirection 누락 |
| Query DTO 기간 지원 | ⚠️ 부분 지원 | 확인 필요 |
| Domain Criteria | ❌ 미존재 | 생성 필요 |
| SortBy Enum | ❌ 미존재 | 생성 필요 |

---

## 2. 컴포넌트 분석

### 2.1 QueryPort (❌ 리팩토링 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/order/port/out/query/OrderQueryPort.java`

```java
public interface OrderQueryPort {

    Optional<Order> findById(OrderId orderId);                    // ✅ OK

    Optional<Order> findByOrderNumber(OrderNumber orderNumber);   // ✅ OK

    Optional<Order> findByLegacyId(Long legacyOrderId);          // ✅ OK (V1 호환)

    // ❌ 위반: findByCriteria 대신 findByQuery 사용
    List<Order> findByQuery(GetOrdersQuery query);

    // ❌ 위반: Criteria 패턴 미사용
    List<Order> findByAdminQuery(GetAdminOrdersQuery query);

    // ⚠️ 특수 메서드 - 유지
    Map<String, Long> getOrderStatusCounts(String memberId, List<String> statuses);
}
```

**위반 규칙**:
- APP-POQ-002: `findByQuery` → `findByCriteria`로 변경 필요
- APP-POQ-004: Query DTO 대신 Criteria 패턴 사용 필요

### 2.2 ReadManager (❌ 리팩토링 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/order/manager/query/OrderReadManager.java`

```java
@Component
public class OrderReadManager {

    // ❌ @Transactional(readOnly = true) 누락
    public Order findById(String orderId) { ... }

    // ❌ @Transactional(readOnly = true) 누락
    public Order findByOrderNumber(String orderNumber) { ... }

    // ❌ findByCriteria 메서드 없음
}
```

**위반 규칙**:
- APP-RM-002: `@Transactional(readOnly = true)` 누락
- APP-RM-001: `findByCriteria`, `countByCriteria` 메서드 미구현

### 2.3 Query DTO (❌ 리팩토링 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/order/dto/query/GetOrdersQuery.java`
**파일**: `application/src/main/java/com/ryuqq/setof/application/order/dto/query/GetAdminOrdersQuery.java`

```java
// 현재 상태 확인 필요 - 예상 구조
public record GetOrdersQuery(
    String memberId,
    String status,
    // ❌ sortBy 누락
    // ❌ sortDirection 누락
    int page,
    int size
) {}

public record GetAdminOrdersQuery(
    // Admin 필터 조건
    // ❌ sortBy 누락
    // ❌ sortDirection 누락
    int page,
    int size
) {}
```

**누락 필드**:
- `sortBy` (OrderSortBy Enum)
- `sortDirection` (SortDirection)
- 기간 조회 (startDate, endDate)

### 2.4 QueryFactory (❌ 미존재)

**생성 필요**: `application/src/main/java/com/ryuqq/setof/application/order/factory/query/OrderQueryFactory.java`

### 2.5 Domain Criteria (❌ 미존재)

**생성 필요**: `domain/src/main/java/com/ryuqq/setof/domain/order/query/criteria/OrderSearchCriteria.java`

---

## 3. 리팩토링 계획

### 3.1 Domain Layer 변경

#### 3.1.1 OrderSearchCriteria 생성

**파일**: `domain/src/main/java/com/ryuqq/setof/domain/order/query/criteria/OrderSearchCriteria.java`

```java
public record OrderSearchCriteria(
    // 필터 조건
    String memberId,
    Long sellerId,
    String status,
    String orderNumber,

    // 기간 조회
    LocalDateTime startDate,
    LocalDateTime endDate,

    // 정렬
    OrderSortBy sortBy,
    SortDirection sortDirection,

    // 페이지네이션
    int offset,
    int limit
) {
    public static OrderSearchCriteriaBuilder builder() {
        return new OrderSearchCriteriaBuilder();
    }
}
```

#### 3.1.2 OrderSortBy Enum 생성

**파일**: `domain/src/main/java/com/ryuqq/setof/domain/order/vo/OrderSortBy.java`

```java
public enum OrderSortBy {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    ORDER_DATE("orderDate"),
    TOTAL_AMOUNT("totalAmount");

    private final String field;

    OrderSortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static OrderSortBy defaultSortBy() {
        return CREATED_AT;
    }
}
```

### 3.2 Application Layer 변경

#### 3.2.1 QueryPort 수정

**변경 전**:
```java
List<Order> findByQuery(GetOrdersQuery query);
List<Order> findByAdminQuery(GetAdminOrdersQuery query);
```

**변경 후**:
```java
List<Order> findByCriteria(OrderSearchCriteria criteria);
long countByCriteria(OrderSearchCriteria criteria);
```

#### 3.2.2 Query DTO 수정

**변경 전**:
```java
public record GetOrdersQuery(
    String memberId,
    String status,
    int page,
    int size
) {}
```

**변경 후**:
```java
public record OrderSearchQuery(
    // 필터
    String memberId,
    Long sellerId,
    String status,
    String orderNumber,

    // 기간 조회
    LocalDateTime startDate,
    LocalDateTime endDate,

    // 정렬
    OrderSortBy sortBy,
    SortDirection sortDirection,

    // 페이지네이션
    int page,
    int size
) {
    public OrderSearchQuery {
        if (sortBy == null) sortBy = OrderSortBy.CREATED_AT;
        if (sortDirection == null) sortDirection = SortDirection.DESC;
    }

    public int offset() {
        return page * size;
    }
}
```

#### 3.2.3 QueryFactory 생성

**파일**: `application/src/main/java/com/ryuqq/setof/application/order/factory/query/OrderQueryFactory.java`

```java
@Component
public class OrderQueryFactory {

    public OrderSearchCriteria createCriteria(OrderSearchQuery query) {
        return OrderSearchCriteria.builder()
            .memberId(query.memberId())
            .sellerId(query.sellerId())
            .status(query.status())
            .orderNumber(query.orderNumber())
            .startDate(query.startDate())
            .endDate(query.endDate())
            .sortBy(query.sortBy())
            .sortDirection(query.sortDirection())
            .offset(query.offset())
            .limit(query.size())
            .build();
    }
}
```

#### 3.2.4 ReadManager 수정

**변경 전**:
```java
@Component
public class OrderReadManager {
    public Order findById(String orderId) { ... }
    public Order findByOrderNumber(String orderNumber) { ... }
}
```

**변경 후**:
```java
@Component
public class OrderReadManager {

    private final OrderQueryPort orderQueryPort;

    public OrderReadManager(OrderQueryPort orderQueryPort) {
        this.orderQueryPort = orderQueryPort;
    }

    @Transactional(readOnly = true)
    public Order findById(String orderId) {
        OrderId id = OrderId.fromString(orderId);
        return orderQueryPort.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Order findByOrderNumber(String orderNumber) {
        OrderNumber number = OrderNumber.of(orderNumber);
        return orderQueryPort.findByOrderNumber(number)
            .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }

    @Transactional(readOnly = true)
    public List<Order> findByCriteria(OrderSearchCriteria criteria) {
        return orderQueryPort.findByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(OrderSearchCriteria criteria) {
        return orderQueryPort.countByCriteria(criteria);
    }
}
```

### 3.3 Persistence Layer 변경

#### 3.3.1 QueryAdapter 수정

**변경 전**:
```java
@Override
public List<Order> findByQuery(GetOrdersQuery query) { ... }

@Override
public List<Order> findByAdminQuery(GetAdminOrdersQuery query) { ... }
```

**변경 후**:
```java
@Override
public List<Order> findByCriteria(OrderSearchCriteria criteria) { ... }

@Override
public long countByCriteria(OrderSearchCriteria criteria) { ... }
```

#### 3.3.2 QueryDslRepository 수정

Criteria 기반 조회 메서드 구현 필요.

---

## 4. 영향 파일 목록

### Domain Layer
| 파일 | 작업 |
|------|------|
| `domain/.../order/query/criteria/OrderSearchCriteria.java` | 신규 생성 |
| `domain/.../order/vo/OrderSortBy.java` | 신규 생성 |

### Application Layer
| 파일 | 작업 |
|------|------|
| `application/.../order/port/out/query/OrderQueryPort.java` | 수정 |
| `application/.../order/dto/query/GetOrdersQuery.java` | 수정 (또는 OrderSearchQuery로 교체) |
| `application/.../order/dto/query/GetAdminOrdersQuery.java` | 삭제 (통합) |
| `application/.../order/factory/query/OrderQueryFactory.java` | 신규 생성 |
| `application/.../order/manager/query/OrderReadManager.java` | 수정 |

### Persistence Layer
| 파일 | 작업 |
|------|------|
| `adapter-out/.../order/adapter/OrderQueryAdapter.java` | 수정 |
| `adapter-out/.../order/repository/OrderQueryDslRepository.java` | 수정 |

---

## 5. 체크리스트

- [ ] Domain: OrderSearchCriteria 생성
- [ ] Domain: OrderSortBy Enum 생성
- [ ] Application: QueryPort 메서드명 변경 (findByCriteria)
- [ ] Application: Query DTO 리팩토링 (정렬/기간 추가)
- [ ] Application: QueryFactory 생성
- [ ] Application: ReadManager @Transactional 추가
- [ ] Persistence: QueryAdapter 수정
- [ ] Persistence: QueryDslRepository 수정

---

## 6. 예상 작업량

| 작업 | 예상 복잡도 | 영향 파일 수 |
|------|------------|-------------|
| Domain Criteria/SortBy 생성 | 🟢 낮음 | 2 |
| QueryPort 리팩토링 | 🟡 중간 | 1 |
| Query DTO 리팩토링 | 🟡 중간 | 2 |
| QueryFactory 생성 | 🟢 낮음 | 1 |
| ReadManager 수정 | 🟢 낮음 | 1 |
| Persistence 수정 | 🟡 중간 | 2 |

**총 작업량**: 🟡 중간 (9개 파일 수정/생성)

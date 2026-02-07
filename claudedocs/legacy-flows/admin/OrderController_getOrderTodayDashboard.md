# API Flow Documentation: OrderController.getOrderTodayDashboard

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/order/today-dashboard` |
| Controller | `OrderController` |
| Service | `OrderFetchService` → `OrderFetchServiceImpl` |
| Repository | `OrderFetchRepository` → `OrderFetchRepositoryImpl`, `QnaFetchRepository` |
| 권한 | `@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| sellerId | Long | ❌ | 셀러 ID (자동 주입) | @AuthorityValidate |

### Request DTO 구조

```java
BaseRoleFilter implements RoleFilter {
    Long sellerId;  // @AuthorityValidate로 자동 주입
}
```

---

## 📤 Response

### Response DTO 구조

```java
OrderDashboardResponse {
    TodayDashboard todayDashboard;           // 오늘 현황
    List<OrderStatistics> monthOrderStatistics;  // 월별 통계
}

TodayDashboard {
    Long orderCount;          // 오늘 주문 건수
    Long claimCount;          // 오늘 클레임 건수
    Long orderInquiryCount;   // 주문 문의 건수
    Long productInquiryCount; // 상품 문의 건수
}

OrderStatistics {
    String flag;              // 월 (YYYY-MM)
    Long orderCount;          // 총 주문 건수
    Long pureOrderCount;      // 순수 주문 건수 (취소/반품 제외)
    BigDecimal orderAmount;   // 총 주문 금액
    BigDecimal pureOrderAmount;  // 순수 주문 금액
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "todayDashboard": {
      "orderCount": 25,
      "claimCount": 3,
      "orderInquiryCount": 8,
      "productInquiryCount": 12
    },
    "monthOrderStatistics": [
      {
        "flag": "2025-01",
        "orderCount": 1500,
        "pureOrderCount": 1350,
        "orderAmount": 75000000,
        "pureOrderAmount": 67500000
      },
      {
        "flag": "2025-02",
        "orderCount": 1200,
        "pureOrderCount": 1100,
        "orderAmount": 60000000,
        "pureOrderAmount": 55000000
      }
    ]
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                      │
│   OrderController.getOrderTodayDashboard(@ModelAttribute filter)  │
│   @GetMapping("/order/today-dashboard")                           │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                         │
│   OrderFetchServiceImpl.fetchOrderTodayDashboard(filter)          │
│   @Transactional(readOnly = true)                                 │
│                                                                   │
│   1. orderFetchRepository.fetchOrderMonthStatistics(filter)       │
│   2. qnaFetchRepository.fetchTodayQnaCountQuery(filter)           │
│   3. orderFetchRepository.fetchOrderTodayCountQuery(filter)       │
│   4. TodayDashboard 조합                                          │
│   5. OrderDashboardResponse 반환                                   │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                      │
│   OrderFetchRepositoryImpl                                        │
│   - fetchOrderMonthStatistics(filter)                             │
│   - fetchOrderTodayCountQuery(filter)                             │
│                                                                   │
│   QnaFetchRepository                                              │
│   - fetchTodayQnaCountQuery(filter)                               │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                        │
│   Tables: order, qna                                              │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 1. 월별 주문 통계 (fetchOrderMonthStatistics)

```java
LocalDate localDate = LocalDate.now();
LocalDate startOfYear = LocalDate.of(localDate.getYear(), 1, 1);
LocalDate endOfYear = LocalDate.of(localDate.getYear(), 12, 31);

StringTemplate monthGroup = Expressions.stringTemplate(
    "DATE_FORMAT({0}, {1})",
    order.insertDate,
    "%Y-%m"
);

getQueryFactory()
    .select(new QOrderStatistics(
        monthGroup,
        getTotalOrderCount(),      // order.id.count()
        getFilteredOrderCount(),   // 순수 주문 건수 (취소 제외)
        order.orderAmount.sum(),
        getFilteredOrderAmount()   // 순수 주문 금액
    ))
    .from(order)
    .where(
        order.insertDate.between(startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59)),
        order.orderStatus.notIn(OrderStatus.ORDER_FAILED),
        sellerIdEq(filter.getSellerId())
    )
    .groupBy(monthGroup)
    .fetch();
```

### 2. 오늘 주문/클레임 건수 (fetchOrderTodayCountQuery)

```java
getQueryFactory()
    .select(order.orderStatus, order.count())
    .from(order)
    .where(
        sellerIdEq(filter.getSellerId()),
        order.orderStatus.in(OrderStatus.ORDER_COMPLETED, OrderStatus.RETURN_REQUEST)
    )
    .groupBy(order.orderStatus)
    .fetch();

// 결과를 Map으로 변환 후:
new OrderTodayCountResponse(
    orderStatusCounts.getOrDefault(OrderStatus.ORDER_COMPLETED, 0L),  // orderCount
    orderStatusCounts.getOrDefault(OrderStatus.RETURN_REQUEST, 0L)    // claimCount
);
```

### 3. 오늘 QnA 건수 (fetchTodayQnaCountQuery)

- `QnaFetchRepository`에서 조회
- 주문 문의/상품 문의 분류

### 집계 함수

```java
// 순수 주문 건수 (유효 상태만 카운트)
private NumberExpression<Long> getFilteredOrderCount() {
    return Expressions.numberTemplate(
        Long.class,
        "sum(case when {0} in ({1}, {2}, {3}, {4}, {5}, {6}, {7}) then 1 else 0 end)",
        order.orderStatus,
        OrderStatus.ORDER_COMPLETED,
        OrderStatus.ORDER_PROCESSING,
        OrderStatus.DELIVERY_COMPLETED,
        OrderStatus.SETTLEMENT_COMPLETED,
        OrderStatus.SETTLEMENT_PROCESSING,
        OrderStatus.DELIVERY_PENDING,
        OrderStatus.DELIVERY_PROCESSING
    ).coalesce(Expressions.constant(0L));
}

// 순수 주문 금액
private NumberExpression<BigDecimal> getFilteredOrderAmount() {
    return new CaseBuilder()
        .when(order.orderStatus.in(
            OrderStatus.ORDER_COMPLETED,
            OrderStatus.ORDER_PROCESSING,
            OrderStatus.DELIVERY_COMPLETED,
            OrderStatus.SETTLEMENT_COMPLETED,
            OrderStatus.SETTLEMENT_PROCESSING,
            OrderStatus.DELIVERY_PENDING,
            OrderStatus.DELIVERY_PROCESSING
        ))
        .then(order.orderAmount)
        .otherwise(BigDecimal.ZERO)
        .sum().coalesce(Expressions.constant(BigDecimal.ZERO));
}
```

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert admin:OrderController.getOrderTodayDashboard

# Persistence Layer 생성
/legacy-query admin:OrderController.getOrderTodayDashboard
```

# API Flow Documentation: OrderController.getOrderDateDashboard

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/order/date-dashboard` |
| Controller | `OrderController` |
| Service | `OrderFetchService` → `OrderFetchServiceImpl` |
| Repository | `OrderFetchRepository` → `OrderFetchRepositoryImpl` |
| 권한 | `@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| startDate | LocalDateTime | ❌ | 조회 시작일 | @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") |
| endDate | LocalDateTime | ❌ | 조회 종료일 | @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") |
| sellerId | Long | ❌ | 셀러 ID (자동 주입) | @AuthorityValidate |

### Request DTO 구조

```java
BaseDateTimeRangeFilter extends BaseRoleFilter implements DateRangeFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endDate;

    Long sellerId;  // 상속 (BaseRoleFilter)
}
```

---

## 📤 Response

### Response DTO 구조

```java
OrderDateDashboardResponse {
    OrderStatistics orderStatistics;              // 기간 전체 통계
    List<OrderDashboardRanking> orderDashBoardRankings;  // 상품별 랭킹 (Top 10)
    List<OrderStatistics> externalMallOrderStatistics;   // 외부몰별 통계
}

OrderStatistics {
    String flag;              // 구분 ("Total" 또는 외부몰명)
    Long orderCount;          // 총 주문 건수
    Long pureOrderCount;      // 순수 주문 건수
    BigDecimal orderAmount;   // 총 주문 금액
    BigDecimal pureOrderAmount;  // 순수 주문 금액
}

OrderDashboardRanking {
    long productGroupId;      // 상품그룹 ID
    String productGroupName;  // 상품그룹명
    long totalOrderCount;     // 총 주문 건수
    long pureOrderCount;      // 순수 주문 건수
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "orderStatistics": {
      "flag": "Total",
      "orderCount": 5000,
      "pureOrderCount": 4500,
      "orderAmount": 250000000,
      "pureOrderAmount": 225000000
    },
    "orderDashBoardRankings": [
      {
        "productGroupId": 101,
        "productGroupName": "인기 상품 A",
        "totalOrderCount": 500,
        "pureOrderCount": 480
      },
      {
        "productGroupId": 102,
        "productGroupName": "인기 상품 B",
        "totalOrderCount": 350,
        "pureOrderCount": 340
      }
    ],
    "externalMallOrderStatistics": [
      {
        "flag": "OUR_MALL",
        "orderCount": 3000,
        "pureOrderCount": 2800,
        "orderAmount": 150000000,
        "pureOrderAmount": 140000000
      },
      {
        "flag": "NAVER",
        "orderCount": 1500,
        "pureOrderCount": 1400,
        "orderAmount": 75000000,
        "pureOrderAmount": 70000000
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
│   OrderController.getOrderDateDashboard(@ModelAttribute filter)   │
│   @GetMapping("/order/date-dashboard")                            │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                         │
│   OrderFetchServiceImpl.fetchOrderDateDashboard(filter)           │
│   @Transactional(readOnly = true)                                 │
│                                                                   │
│   1. orderFetchRepository.fetchOrderDateStatistics(filter)        │
│   2. orderFetchRepository.fetchOrderDashboardRanking(filter)      │
│   3. orderFetchRepository.fetchOrderExternalStatistics(filter)    │
│   4. OrderDateDashboardResponse 조합                               │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                      │
│   OrderFetchRepositoryImpl                                        │
│   - fetchOrderDateStatistics(filter)                              │
│   - fetchOrderDashboardRanking(filter)                            │
│   - fetchOrderExternalStatistics(filter)                          │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                        │
│   Tables: order, payment, order_snap_shot_product_group           │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 1. 기간 전체 통계 (fetchOrderDateStatistics)

```java
getQueryFactory()
    .select(new QOrderStatistics(
        Expressions.constant("Total"),  // 고정 라벨
        getTotalOrderCount(),           // order.id.count()
        getFilteredOrderCount(),        // 순수 주문 건수
        order.orderAmount.sum().coalesce(BigDecimal.ZERO),
        getFilteredOrderAmount()        // 순수 주문 금액
    ))
    .from(order)
    .where(
        order.insertDate.between(filter.getStartDate(), filter.getEndDate()),
        order.orderStatus.notIn(OrderStatus.ORDER_FAILED),
        sellerIdEq(filter.getSellerId())
    )
    .fetchOne();
```

### 2. 상품별 랭킹 (fetchOrderDashboardRanking)

```java
getQueryFactory()
    .select(new QOrderDashboardRanking(
        orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
        orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
        getTotalOrderCount(),
        getFilteredOrderCount()
    ))
    .from(order)
    .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
    .where(
        sellerIdEq(filter.getSellerId()),
        order.insertDate.between(filter.getStartDate(), filter.getEndDate()),
        order.orderStatus.notIn(OrderStatus.ORDER_FAILED)
    )
    .groupBy(
        orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
        orderSnapShotProductGroup.snapShotProductGroup.productGroupName
    )
    .orderBy(getTotalOrderCount().desc())
    .limit(10)  // Top 10
    .fetch();
```

### 3. 외부몰별 통계 (fetchOrderExternalStatistics)

```java
getQueryFactory()
    .select(new QOrderStatistics(
        payment.paymentDetails.siteName.stringValue(),  // 몰 이름
        getTotalOrderCount(),
        getFilteredOrderCount(),
        order.orderAmount.sum(),
        getFilteredOrderAmount()
    ))
    .from(order)
    .innerJoin(order.payment, payment)
    .where(
        sellerIdEq(filter.getSellerId()),
        order.insertDate.between(filter.getStartDate(), filter.getEndDate()),
        order.orderStatus.notIn(OrderStatus.ORDER_FAILED)
    )
    .groupBy(payment.paymentDetails.siteName.stringValue())
    .fetch();
```

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| order | order | FROM | - |
| payment | payment | INNER JOIN | order.payment (외부몰 통계용) |
| order_snap_shot_product_group | orderSnapShotProductGroup | INNER JOIN | order.orderSnapShotProductGroup (랭킹용) |

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| insertDate.between | order.insertDate | 기간 필터 |
| orderStatus.notIn | order.orderStatus | ORDER_FAILED 제외 |
| sellerIdEq | order.sellerId | 셀러 ID 필터 |

### 집계 함수

- 기간 통계: 단일 ROW 집계 (GROUP BY 없음)
- 랭킹: GROUP BY productGroupId, productGroupName + ORDER BY count DESC + LIMIT 10
- 외부몰: GROUP BY siteName

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert admin:OrderController.getOrderDateDashboard

# Persistence Layer 생성
/legacy-query admin:OrderController.getOrderDateDashboard
```

# API Flow Documentation: OrderController.getSettlements

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/settlements` |
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
| page | int | ❌ | 페이지 번호 | Pageable |
| size | int | ❌ | 페이지 크기 | Pageable |

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
CustomPageable<SettlementResponse> {
    List<SettlementResponse> content;
    long totalElements;
    int totalPages;
    int pageNumber;
    int pageSize;
    boolean hasNext;
    boolean hasPrevious;
}

SettlementResponse {
    long orderCount;              // 총 주문 건수
    long ourMallOrderCount;       // 자사몰 주문 건수
    long externalMallOrderCount;  // 외부몰 주문 건수
    String settlementDay;         // 정산 예정일
    String settlementCompleteDay; // 정산 완료일
    BigDecimal totalCurrentPrice;     // 총 정가 금액
    BigDecimal totalDiscountAmount;   // 총 할인 금액
    BigDecimal sellerDiscountAmount;  // 셀러 부담 할인 금액
    BigDecimal ourDiscountAmount;     // 자사 부담 할인 금액
    BigDecimal totalMileageAmount;    // 총 마일리지 사용 금액
    BigDecimal sellerMileageAmount;   // 셀러 부담 마일리지
    BigDecimal ourMileageAmount;      // 자사 부담 마일리지
    BigDecimal settlementAmount;      // 정산 금액
    BigDecimal expectationSettlementAmount;  // 예상 정산 금액
    BigDecimal totalFee;              // 총 수수료
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "orderCount": 150,
        "ourMallOrderCount": 100,
        "externalMallOrderCount": 50,
        "settlementDay": "2025-01-20",
        "settlementCompleteDay": "2025-01-25",
        "totalCurrentPrice": 15000000,
        "totalDiscountAmount": 500000,
        "sellerDiscountAmount": 250000,
        "ourDiscountAmount": 250000,
        "totalMileageAmount": 100000,
        "sellerMileageAmount": 50000,
        "ourMileageAmount": 50000,
        "settlementAmount": 14400000,
        "expectationSettlementAmount": 13680000,
        "totalFee": 720000
      }
    ],
    "totalElements": 30,
    "totalPages": 3,
    "pageNumber": 0,
    "pageSize": 10
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                      │
│   OrderController.getSettlements(@ModelAttribute filter, Pageable)│
│   @GetMapping("/settlements")                                     │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                         │
│   OrderFetchServiceImpl.fetchSettlements(filter, pageable)        │
│   @Transactional(readOnly = true)                                 │
│                                                                   │
│   1. orderFetchRepository.fetchSettlements(filter, pageable)      │
│   2. orderFetchRepository.fetchSettlementCount(filter)            │
│   3. orderPageableMapper.toSettlementResponse(...)                │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                      │
│   OrderFetchRepositoryImpl.fetchSettlements(filter, pageable)     │
│   QueryDSL Aggregation (GROUP BY settlement.expectedSettlementDate)│
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                        │
│   Tables: order, payment, settlement, shipment,                   │
│           order_snap_shot_product_group                           │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| order | order | FROM | - |
| payment | payment | INNER JOIN | order.payment |
| settlement | settlement | INNER JOIN | order.settlement |
| shipment | shipment | INNER JOIN | order.shipment |
| order_snap_shot_product_group | orderSnapShotProductGroup | INNER JOIN | order.orderSnapShotProductGroup |

### QueryDSL 코드

```java
StringTemplate groupDate = Expressions.stringTemplate(
    "DATE_FORMAT({0}, {1})",
    settlement.expectedSettlementDate,
    "%Y-%m-%d"
);

// 정산 금액 계산 로직
NumberExpression<BigDecimal> totalCurrentPrice =
    orderSnapShotProductGroup.snapShotProductGroup.price.currentPrice
        .multiply(order.quantity);

NumberExpression<BigDecimal> sellerDiscountAmount =
    getCalculateOurMallFlag(
        settlement.directDiscountPrice
            .multiply(order.quantity)
            .multiply(settlement.directDiscountSellerBurdenRatio.divide(100))
    );

NumberExpression<BigDecimal> totalFee =
    totalCurrentPrice
        .multiply(settlement.sellerCommissionRate.divide(100))
        .sum()
        .round();

getQueryFactory()
    .select(new QSettlementResponse(...))
    .from(order)
    .innerJoin(order.payment, payment)
    .innerJoin(order.settlement, settlement)
    .innerJoin(order.shipment, shipment)
    .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
    .where(
        settlement.expectedSettlementDate.between(startDate, endDate),
        sellerIdEq(filter.getSellerId()),
        order.orderStatus.in(SETTLEMENT_COMPLETED, SETTLEMENT_PROCESSING),
        shipment.deliveryStatus.eq(DELIVERY_COMPLETED)
    )
    .groupBy(groupDate)
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .fetch();
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| expectedSettlementDate.between | settlement.expectedSettlementDate | 정산 예정일 기간 필터 |
| sellerIdEq | order.sellerId | 셀러 ID 필터 |
| orderStatus.in | order.orderStatus | SETTLEMENT_COMPLETED, SETTLEMENT_PROCESSING |
| deliveryStatus.eq | shipment.deliveryStatus | DELIVERY_COMPLETED |

### GROUP BY

- `DATE_FORMAT(settlement.expectedSettlementDate, '%Y-%m-%d')` - 정산 예정일 기준 그룹화

### 집계 함수

- `order.id.count()` - 총 주문 건수
- `SUM(CASE WHEN siteName = 'OUR_MALL' THEN 1 ELSE 0 END)` - 자사몰 주문
- `SUM(CASE WHEN siteName <> 'OUR_MALL' THEN 1 ELSE 0 END)` - 외부몰 주문
- 각종 금액 SUM 연산

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert admin:OrderController.getSettlements

# Persistence Layer 생성
/legacy-query admin:OrderController.getSettlements
```

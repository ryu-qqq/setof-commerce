# API Flow Documentation: OrderController.getOrders

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/orders` |
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
| searchKeyword | SearchKeyword | ❌ | 검색 키워드 타입 | - |
| searchWord | String | ❌ | 검색어 | - |
| lastDomainId | Long | ❌ | 마지막 도메인 ID (커서) | - |
| orderStatusList | List<OrderStatus> | ❌ | 주문 상태 필터 | - |
| periodType | PeriodType | ✅ | 기간 타입 | @NotNull |
| sellerId | Long | ❌ | 셀러 ID (자동 주입) | @AuthorityValidate |
| page | int | ❌ | 페이지 번호 | Pageable |
| size | int | ❌ | 페이지 크기 | Pageable |
| sort | String | ❌ | 정렬 조건 | Pageable |

### Request DTO 구조

```java
OrderFilter extends SearchAndDateFilter implements RoleFilter {
    Long lastDomainId;
    List<OrderStatus> orderStatusList = new ArrayList<>();
    @NotNull(message = "periodType 은 빈 값일 수 없습니다.")
    PeriodType periodType;
    Long sellerId;  // @AuthorityValidate로 자동 주입
}

// 상속된 필드 (SearchAndDateFilter)
{
    LocalDateTime startDate;
    LocalDateTime endDate;
    SearchKeyword searchKeyword;
    String searchWord;
}
```

---

## 📤 Response

### Response DTO 구조

```java
CustomPageable<OrderListResponse> {
    List<OrderListResponse> content;
    long totalElements;
    int totalPages;
    int pageNumber;
    int pageSize;
    boolean hasNext;
    boolean hasPrevious;
}

OrderListResponse {
    long orderId;
    BuyerInfo buyerInfo;
    PaymentDetail payment;
    ReceiverInfo receiverInfo;
    PaymentShipmentInfo paymentShipmentInfo;
    SettlementInfo settlementInfo;
    OrderProduct orderProduct;
    List<OrderHistoryResponse> orderHistories;  // 주문 이력 포함
}

OrderHistoryResponse {
    long orderId;
    String changeReason;
    String changeDetailReason;
    OrderStatus orderStatus;
    String invoiceNo;
    ShipmentCompanyCode shipmentCompanyCode;
    LocalDateTime updateDate;
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "orderId": 12345,
        "buyerInfo": {...},
        "payment": {...},
        "receiverInfo": {...},
        "paymentShipmentInfo": {...},
        "settlementInfo": {...},
        "orderProduct": {...},
        "orderHistories": [
          {
            "orderId": 12345,
            "changeReason": "배송 시작",
            "orderStatus": "DELIVERY_PROCESSING",
            "invoiceNo": "1234567890",
            "updateDate": "2025-01-15 14:00:00"
          }
        ]
      }
    ],
    "totalElements": 100,
    "totalPages": 10,
    "pageNumber": 0,
    "pageSize": 10,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                      │
│   OrderController.getOrders(@ModelAttribute filter, Pageable)     │
│   @GetMapping("/orders")                                          │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                         │
│   OrderFetchServiceImpl.fetchOrders(filter, pageable)             │
│   @Transactional(readOnly = true)                                 │
│                                                                   │
│   1. orderFetchRepository.fetchOrders(filter, pageable)           │
│   2. orderSnapShotFetchService.fetchOrderSnapShotProducts(...)    │
│   3. orderHistoryFetchService.fetchOrderHistories(orderIds)       │
│   4. 결과 조합 및 매핑                                              │
│   5. orderFetchRepository.fetchOrderCountQuery(filter)            │
│   6. orderPageableMapper.toOrderResponse(...)                     │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                      │
│   OrderFetchRepositoryImpl                                        │
│                                                                   │
│   1. fetchOrderIds(filter, pageable) - ID 목록 조회               │
│   2. fetchOrders(...) - 상세 데이터 조회                          │
│   3. fetchOrderCountQuery(...) - 전체 개수 조회                   │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                        │
│   Tables: order, payment, payment_bill, payment_method,           │
│           shipment, settlement, brand, seller, users,             │
│           order_snap_shot_product_group,                          │
│           payment_snap_shot_shipping_address,                     │
│           payment_snap_shot_mileage, order_history                │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 1차 쿼리: Order ID 목록 조회

```java
getQueryFactory()
    .select(order.id)
    .from(order)
    .innerJoin(order.payment, payment)
    .innerJoin(order.settlement, settlement)
    .innerJoin(payment.paymentBill, paymentBill)
    .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
    .where(
        betweenTime(filter),
        orderStatusIn(filter),
        exclusiveOrderStatus(),
        sellerIdEq(filter.getSellerId()),
        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord())
    )
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .orderBy(ORDERS)
    .fetch();
```

### 2차 쿼리: 상세 데이터 조회

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| order | order | FROM | - |
| payment | payment | INNER JOIN | order.payment |
| settlement | settlement | INNER JOIN | order.settlement |
| payment_bill | paymentBill | INNER JOIN | payment.paymentBill |
| payment_method | paymentMethod | INNER JOIN | paymentBill.paymentMethod |
| shipment | shipment | INNER JOIN | order.shipment |
| order_snap_shot_product_group | orderSnapShotProductGroup | INNER JOIN | order.orderSnapShotProductGroup |
| brand | brand | INNER JOIN | brand.id = snapShotProductGroup.brandId |
| seller | seller | INNER JOIN | seller.id = order.sellerId |
| users | users | INNER JOIN | users.id = order.userId |
| payment_snap_shot_shipping_address | paymentSnapShotShippingAddress | INNER JOIN | payment.paymentSnapShotShippingAddress |
| payment_snap_shot_mileage | paymentSnapShotMileage | LEFT JOIN | payment.paymentSnapShotMileage |

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| betweenTime | order.insertDate / settlement.settlementDate | 기간 필터 (periodType에 따라 동적) |
| orderStatusIn | order.orderStatus | 주문 상태 필터 |
| exclusiveOrderStatus | order.orderStatus | 제외할 상태 (ORDER_FAILED 등) |
| sellerIdEq | order.sellerId | 셀러 ID 필터 |
| searchKeywordEq | 다양 | 검색 조건 (상품그룹ID, 주문번호 등) |

### 동적 기간 조건 (betweenTime)

```java
if(filter.isHistory()) {
    // 취소 이력 조회 시
    return payment.paymentDetails.canceledDate.between(startDate, endDate);
}

if(filter.isSettlement()) {
    // 정산 조회 시
    if(filter.getOrderStatusList().size() == 1 && isSettlementCompleted) {
        return settlement.settlementDate.between(startDate, endDate);
    } else {
        return settlement.expectedSettlementDate.between(startDate, endDate);
    }
}

// 기본: 주문 생성일
return order.insertDate.between(startDate, endDate);
```

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert admin:OrderController.getOrders

# Persistence Layer 생성
/legacy-query admin:OrderController.getOrders
```

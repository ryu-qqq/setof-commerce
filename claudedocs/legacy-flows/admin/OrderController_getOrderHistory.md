# API Flow Documentation: OrderController.getOrderHistory

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/order/history/{orderId}` |
| Controller | `OrderController` |
| Service | `OrderHistoryFetchService` → `OrderHistoryFetchServiceImpl` |
| Repository | `OrderFetchRepository` → `OrderFetchRepositoryImpl` |
| 권한 | `@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| orderId | long | ✅ | 주문 ID | @PathVariable |

---

## 📤 Response

### Response DTO 구조

```java
List<OrderHistoryResponse> {
    long orderId;
    String changeReason;           // 변경 사유 (기본값: "")
    String changeDetailReason;     // 상세 변경 사유 (기본값: "")
    OrderStatus orderStatus;       // 주문 상태
    String invoiceNo;              // 송장 번호 (기본값: "")
    ShipmentCompanyCode shipmentCompanyCode;  // 배송사 코드 (기본값: REFER_DETAIL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateDate;      // 변경 일시
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "orderId": 12345,
      "changeReason": "주문 접수",
      "changeDetailReason": "",
      "orderStatus": "ORDER_COMPLETED",
      "invoiceNo": "",
      "shipmentCompanyCode": "REFER_DETAIL",
      "updateDate": "2025-01-15 10:00:00"
    },
    {
      "orderId": 12345,
      "changeReason": "배송 시작",
      "changeDetailReason": "",
      "orderStatus": "DELIVERY_PROCESSING",
      "invoiceNo": "1234567890",
      "shipmentCompanyCode": "CJ",
      "updateDate": "2025-01-16 14:30:00"
    },
    {
      "orderId": 12345,
      "changeReason": "배송 완료",
      "changeDetailReason": "",
      "orderStatus": "DELIVERY_COMPLETED",
      "invoiceNo": "1234567890",
      "shipmentCompanyCode": "CJ",
      "updateDate": "2025-01-18 09:15:00"
    }
  ]
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                      │
│   OrderController.getOrderHistory(@PathVariable orderId)          │
│   @GetMapping("/order/history/{orderId}")                         │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                         │
│   OrderHistoryFetchServiceImpl.fetchOrderHistories(orderId)       │
│   @Transactional(readOnly = true)                                 │
│                                                                   │
│   1. orderFetchRepository.fetchOrderEntity(orderId, sellerIdOpt)  │
│   2. orderHistoryMapper.toOrderHistories(order)                   │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                      │
│   OrderFetchRepositoryImpl.fetchOrderEntity(orderId, sellerIdOpt) │
│   + OrderHistoryMapper 변환                                       │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                        │
│   Tables: order, shipment, payment, payment_bill, payment_method, │
│           order_snap_shot_*, settlement                           │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블 (Order Entity Fetch)

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| order | order | FROM | - |
| shipment | shipment | INNER JOIN (fetchJoin) | order.shipment |
| payment | payment | INNER JOIN (fetchJoin) | order.payment |
| payment_bill | paymentBill | INNER JOIN (fetchJoin) | payment.paymentBill |
| payment_method | paymentMethod | INNER JOIN (fetchJoin) | paymentBill.paymentMethod |
| payment_snap_shot_shipping_address | - | INNER JOIN (fetchJoin) | payment.paymentSnapShotShippingAddress |
| order_snap_shot_product | orderSnapShotProduct | INNER JOIN (fetchJoin) | order.orderSnapShotProduct |
| order_snap_shot_product_group | orderSnapShotProductGroup | INNER JOIN (fetchJoin) | order.orderSnapShotProductGroup |
| order_snap_shot_product_group_image | - | INNER JOIN (fetchJoin) | order.orderSnapShotProductGroupImages |
| order_snap_shot_product_group_detail_description | - | INNER JOIN (fetchJoin) | order.productGroupDetailDescription |
| order_snap_shot_product_notice | - | INNER JOIN (fetchJoin) | order.orderSnapShotProductNotice |
| order_snap_shot_product_delivery | - | INNER JOIN (fetchJoin) | order.orderSnapShotProductDelivery |
| order_snap_shot_product_stock | orderSnapShotProductStock | INNER JOIN (fetchJoin) | order.orderSnapShotProductStock |
| settlement | settlement | LEFT JOIN (fetchJoin) | order.settlement |
| payment_snap_shot_mileage | paymentSnapShotMileage | LEFT JOIN (fetchJoin) | payment.paymentSnapShotMileage |
| order_snap_shot_mileage | orderSnapShotMileage | LEFT JOIN (fetchJoin) | order.orderSnapShotMileage |

### QueryDSL 코드

```java
getQueryFactory()
    .selectFrom(order)
    .innerJoin(order.shipment, shipment).fetchJoin()
    .innerJoin(order.payment, payment).fetchJoin()
    .innerJoin(payment.paymentBill, paymentBill).fetchJoin()
    .innerJoin(paymentBill.paymentMethod, paymentMethod).fetchJoin()
    .innerJoin(payment.paymentSnapShotShippingAddress, paymentSnapShotShippingAddress).fetchJoin()
    .innerJoin(order.orderSnapShotProduct, orderSnapShotProduct).fetchJoin()
    .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup).fetchJoin()
    .innerJoin(order.orderSnapShotProductGroupImages, orderSnapShotProductGroupImage).fetchJoin()
    .innerJoin(order.productGroupDetailDescription, orderSnapShotProductGroupDetailDescription).fetchJoin()
    .innerJoin(order.orderSnapShotProductNotice, orderSnapShotProductNotice).fetchJoin()
    .innerJoin(order.orderSnapShotProductDelivery, orderSnapShotProductDelivery).fetchJoin()
    .innerJoin(order.orderSnapShotProductStock, orderSnapShotProductStock).fetchJoin()
    .leftJoin(order.settlement, settlement).fetchJoin()
    .leftJoin(payment.paymentSnapShotMileage, paymentSnapShotMileage).fetchJoin()
    .leftJoin(order.orderSnapShotMileage, orderSnapShotMileage).fetchJoin()
    .where(orderIdEq(orderId), sellerIdEq(sellerIdOpt))
    .fetchOne()
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| orderIdEq | order.id | 주문 ID 일치 |
| sellerIdEq | order.sellerId | 셀러 ID 일치 (권한 필터) |

### Mapper 변환

- `OrderHistoryMapper.toOrderHistories(order)` - Order Entity에서 이력 정보 추출
- Order Entity 내 `orderHistories` 컬렉션을 DTO로 변환

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert admin:OrderController.getOrderHistory

# Persistence Layer 생성
/legacy-query admin:OrderController.getOrderHistory
```

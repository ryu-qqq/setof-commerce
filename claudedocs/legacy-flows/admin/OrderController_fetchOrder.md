# API Flow Documentation: OrderController.fetchOrder

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/order/{orderId}` |
| Controller | `OrderController` |
| Service | `OrderFetchService` → `OrderFetchServiceImpl` |
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
OrderResponse {
    long orderId;
    BuyerInfo buyerInfo;              // 구매자 정보 (Embedded)
    PaymentDetail payment;            // 결제 상세
    ReceiverInfo receiverInfo;        // 수령자 정보
    PaymentShipmentInfo paymentShipmentInfo;  // 배송 정보
    SettlementInfo settlementInfo;    // 정산 정보
    OrderProduct orderProduct;        // 주문 상품 정보
}

PaymentDetail {
    long paymentId;
    String paymentAgencyId;
    PaymentStatus paymentStatus;
    PaymentMethodEnum paymentMethod;
    LocalDateTime paymentDate;
    LocalDateTime canceledDate;
    long userId;
    SiteName siteName;
    Money billAmount;
    Money paymentAmount;
    Money usedMileageAmount;
}

OrderProduct {
    long orderId;
    ProductGroupSnapShotDetails productGroupDetails;
    BaseBrandContext brand;
    long productGroupId;
    long productId;
    String sellerName;
    String productGroupMainImageUrl;
    String deliveryArea;
    int productQuantity;
    OrderStatus orderStatus;
    Money regularPrice;
    Money orderAmount;
    Money totalExpectedRefundMileageAmount;
    String option;
    String skuNumber;
    Set<OptionDto> options;
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "orderId": 12345,
    "buyerInfo": {
      "buyerName": "홍길동",
      "buyerEmail": "hong@example.com",
      "buyerPhoneNumber": "010-1234-5678"
    },
    "payment": {
      "paymentId": 1001,
      "paymentStatus": "PAYMENT_COMPLETED",
      "paymentMethod": "CARD",
      "paymentDate": "2025-01-15 10:30:00",
      "siteName": "OUR_MALL",
      "paymentAmount": { "amount": 50000 }
    },
    "receiverInfo": {
      "receiverName": "홍길동",
      "phoneNumber": "010-1234-5678",
      "addressLine1": "서울시 강남구",
      "addressLine2": "테헤란로 123"
    },
    "paymentShipmentInfo": {
      "deliveryStatus": "DELIVERY_COMPLETED",
      "companyCode": "CJ",
      "invoiceNo": "1234567890"
    },
    "settlementInfo": {...},
    "orderProduct": {
      "productGroupId": 100,
      "productId": 200,
      "productQuantity": 2,
      "orderStatus": "DELIVERY_COMPLETED",
      "orderAmount": { "amount": 50000 }
    }
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                      │
│   OrderController.fetchOrder(@PathVariable orderId)               │
│   @GetMapping("/order/{orderId}")                                 │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                         │
│   OrderFetchServiceImpl.fetchOrder(orderId)                       │
│   @Transactional(readOnly = true)                                 │
│                                                                   │
│   1. orderFetchRepository.fetchOrder(orderId, sellerIdOpt)        │
│   2. orderSnapShotFetchService.fetchOrderSnapShotProducts(...)    │
│   3. orderResponse.setOrderProduct(...)                           │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                      │
│   OrderFetchRepositoryImpl.fetchOrder(orderId, sellerIdOpt)       │
│   QueryDSL Projection                                             │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                        │
│   Tables: order, payment, payment_bill, payment_method,           │
│           shipment, settlement, brand, seller, users,             │
│           order_snap_shot_product_group,                          │
│           payment_snap_shot_shipping_address,                     │
│           payment_snap_shot_mileage                               │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| order | order | FROM | - |
| payment | payment | INNER JOIN | order.payment |
| payment_bill | paymentBill | INNER JOIN | payment.paymentBill |
| payment_method | paymentMethod | INNER JOIN | paymentBill.paymentMethod |
| shipment | shipment | INNER JOIN | order.shipment |
| order_snap_shot_product_group | orderSnapShotProductGroup | INNER JOIN | order.orderSnapShotProductGroup |
| settlement | settlement | INNER JOIN | order.settlement |
| brand | brand | INNER JOIN | brand.id = orderSnapShotProductGroup.snapShotProductGroup.brandId |
| seller | seller | INNER JOIN | seller.id = order.sellerId |
| users | users | INNER JOIN | users.id = order.userId |
| payment_snap_shot_shipping_address | paymentSnapShotShippingAddress | INNER JOIN | payment.paymentSnapShotShippingAddress |
| payment_snap_shot_mileage | paymentSnapShotMileage | LEFT JOIN | payment.paymentSnapShotMileage |

### QueryDSL 코드

```java
getQueryFactory().from(order)
    .innerJoin(order.payment, payment)
    .innerJoin(payment.paymentBill, paymentBill)
    .innerJoin(paymentBill.paymentMethod, paymentMethod)
    .innerJoin(order.shipment, shipment)
    .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
    .innerJoin(order.settlement, settlement)
    .innerJoin(brand).on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
    .innerJoin(seller).on(seller.id.eq(order.sellerId))
    .innerJoin(users).on(users.id.eq(order.userId))
    .innerJoin(payment.paymentSnapShotShippingAddress, paymentSnapShotShippingAddress)
    .leftJoin(payment.paymentSnapShotMileage, paymentSnapShotMileage)
    .where(orderIdEq(orderId), sellerIdEq(sellerIdOpt))
    .transform(GroupBy.groupBy(order.id).as(...))
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| orderIdEq | order.id | 주문 ID 일치 |
| sellerIdEq | order.sellerId | 셀러 ID 일치 (권한 필터) |

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert admin:OrderController.fetchOrder

# Persistence Layer 생성
/legacy-query admin:OrderController.fetchOrder
```

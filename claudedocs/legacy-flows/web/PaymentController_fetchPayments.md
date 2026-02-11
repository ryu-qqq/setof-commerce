# API Flow Documentation: PaymentController.fetchPayments

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/payments` |
| Controller | PaymentController |
| Service | PaymentFindService → PaymentFindServiceImpl |
| Repository | PaymentFindRepository → PaymentFindRepositoryImpl |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| lastDomainId | Long | ❌ | 커서 기반 페이징용 마지막 ID | - |
| orderStatusList | List\<OrderStatus\> | ❌ | 주문 상태 필터 | - |
| startDate | LocalDateTime | ❌ | 조회 시작일 | - |
| endDate | LocalDateTime | ❌ | 조회 종료일 | - |
| page | int | ❌ | 페이지 번호 (Pageable) | - |
| size | int | ❌ | 페이지 크기 (Pageable) | - |

### Request DTO 구조

```java
public class PaymentFilter extends SearchAndDateFilter {
    private Long lastDomainId;              // 커서 기반 페이징
    private List<OrderStatus> orderStatusList; // 주문 상태 필터
    // 상속: startDate, endDate
}
```

---

## 📤 Response

### Response DTO 구조

```java
public class PaymentResponse implements LastDomainIdProvider {
    private BuyerInfo buyerInfo;              // 구매자 정보 (목록에서는 null)
    private PaymentDetail payment;            // 결제 상세
    private ReceiverInfo receiverInfo;        // 수령인 정보 (목록에서는 null)
    private RefundAccountResponse refundAccount; // 환불 계좌 (목록에서는 null)
    private VBankAccountResponse vBankAccount;   // 가상계좌 정보
    private Set<OrderProductDto> orderProducts;  // 주문 상품 목록
}

public class PaymentDetail {
    private long paymentId;
    private String paymentAgencyId;
    private PaymentStatus paymentStatus;
    private PaymentMethodEnum paymentMethodEnum;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private LocalDateTime canceledDate;
    private long userId;
    private SiteName siteName;
    private long preDiscountAmount;
    private long paymentAmount;
    private double usedMileageAmount;
    private String cardName;
    private String cardNumber;
    private double totalExpectedMileageAmount;
    private Set<Long> orderIds;  // @JsonIgnore
}

public class VBankAccountResponse extends AccountResponse {
    private String bankName;      // 상속
    private String accountNumber; // 상속
    private long paymentAmount;
    private LocalDateTime vBankDueDate;
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "payment": {
          "paymentId": 12345,
          "paymentAgencyId": "imp_123456",
          "paymentStatus": "PAID",
          "paymentMethodEnum": "CARD",
          "paymentMethod": "신용카드",
          "paymentDate": "2024-01-15 10:30:00",
          "canceledDate": null,
          "paymentAmount": 50000,
          "usedMileageAmount": 1000.0,
          "cardName": "신한카드",
          "cardNumber": "1234-****-****-5678",
          "totalExpectedMileageAmount": 500.0
        },
        "vBankAccount": null,
        "orderProducts": [
          {
            "orderId": 1001,
            "productName": "상품명",
            "quantity": 2,
            "orderAmount": 50000
          }
        ]
      }
    ],
    "hasNext": true
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────┐
│   Controller                                          │
│   PaymentController.fetchPayments(filter, pageable)  │
│   @GetMapping("/payments")                           │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Service                                             │
│   PaymentFindServiceImpl.fetchPayments()             │
│   @Transactional(readOnly=true)                      │
│   1. SecurityUtils.currentUserId() 로 사용자 ID 획득  │
│   2. paymentFindRepository.fetchPayments() 호출      │
│   3. orderFindService.fetchOrderProducts() 호출      │
│   4. orderFindService.fetchRejectedOrder() 호출      │
│   5. paymentMapper.toPaymentResponses() 매핑         │
│   6. paymentSliceMapper.toSlice() 페이징 처리        │
└────────────────────┬─────────────────────────────────┘
                     │
                     ├─────────────────────────────────┐
                     ▼                                 ▼
┌──────────────────────────────┐  ┌───────────────────────────┐
│   PaymentFindRepository      │  │   OrderFindService        │
│   fetchPayments()            │  │   fetchOrderProducts()    │
│                              │  │   fetchRejectedOrder()    │
└──────────────────────────────┘  └───────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Database                                            │
│   Tables: payment, order, payment_bill,              │
│           payment_method, order_snap_shot_*,         │
│           brand, seller, seller_shipping_info,       │
│           payment_snap_shot_mileage, v_bank_account  │
└──────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 1단계: Payment ID 조회

```java
queryFactory
    .select(payment.id)
    .from(payment)
    .innerJoin(order).on(payment.id.eq(order.paymentId))
    .where(
        userIdEq(userId),
        betweenTime(filter),
        paymentIdLt(filter.getLastDomainId()),
        paymentFailNotIn(),
        orderStatusIn(filter.getOrderStatusList()))
    .limit(pageable.getPageSize() + 1)
    .fetch();
```

### 2단계: Payment 상세 조회

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| payment | payment | FROM | - |
| order | order | INNER JOIN | payment.id = order.paymentId |
| payment_bill | paymentBill | INNER JOIN | payment.id = paymentBill.paymentId |
| payment_method | paymentMethod | INNER JOIN | paymentMethod.id = paymentBill.paymentMethodId |
| order_snap_shot_product_group | orderSnapShotProductGroup | INNER JOIN | orderId = order.id |
| order_snap_shot_product_delivery | orderSnapShotProductDelivery | INNER JOIN | orderId = order.id |
| order_snap_shot_product | orderSnapShotProduct | INNER JOIN | orderId = order.id |
| brand | brand | INNER JOIN | brand.id = snapShotProductGroup.brandId |
| seller | seller | INNER JOIN | seller.id = order.sellerId |
| seller_shipping_info | sellerShippingInfo | INNER JOIN | sellerShippingInfo.id = seller.id |
| payment_snap_shot_mileage | paymentSnapShotMileage | LEFT JOIN | paymentId = payment.id |
| order_snap_shot_product_group_image | orderSnapShotProductGroupImage | INNER JOIN | orderId = order.id, imageType = MAIN |
| v_bank_account | vBankAccount | LEFT JOIN | paymentId = payment.id |

### QueryDSL 코드

```java
queryFactory
    .from(payment)
    .innerJoin(order).on(payment.id.eq(order.paymentId))
    .innerJoin(paymentBill).on(payment.id.eq(paymentBill.paymentId))
    .innerJoin(paymentMethod).on(paymentMethod.id.eq(paymentBill.paymentMethodId))
    .innerJoin(orderSnapShotProductGroup).on(orderSnapShotProductGroup.orderId.eq(order.id))
    .innerJoin(orderSnapShotProductDelivery).on(orderSnapShotProductDelivery.orderId.eq(order.id))
    .innerJoin(orderSnapShotProduct).on(orderSnapShotProduct.orderId.eq(order.id))
    .innerJoin(brand).on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
    .innerJoin(seller).on(seller.id.eq(order.sellerId))
    .innerJoin(sellerShippingInfo).on(sellerShippingInfo.id.eq(seller.id))
    .leftJoin(paymentSnapShotMileage).on(paymentSnapShotMileage.paymentId.eq(payment.id))
    .innerJoin(orderSnapShotProductGroupImage).on(orderSnapShotProductGroupImage.orderId.eq(order.id))
        .on(imageType.eq(ProductGroupImageType.MAIN))
    .leftJoin(vBankAccount).on(vBankAccount.paymentId.eq(payment.id))
    .where(orderStatusIn(...), paymentIdIn(paymentIds))
    .orderBy(payment.id.desc())
    .transform(GroupBy.groupBy(payment.id).list(...));
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| userIdEq | payment.paymentDetails.userId | 현재 사용자 ID |
| betweenTime | payment.insertDate | 조회 기간 필터 |
| paymentIdLt | payment.id | 커서 기반 페이징 (lastDomainId보다 작은 ID) |
| paymentFailNotIn | payment.paymentDetails.paymentStatus | PAYMENT_FAILED 제외 |
| orderStatusIn | order.orderStatus | 주문 상태 필터 |

---

## 📝 특이사항

1. **커서 기반 페이징**: `lastDomainId`를 사용한 무한 스크롤 지원
2. **복잡한 조인**: 12개 테이블 조인
3. **2단계 쿼리**: ID 조회 후 상세 조회하는 성능 최적화 패턴
4. **추가 서비스 호출**: OrderFindService를 통해 주문 상품 정보 추가 조회
5. **Mapper 사용**: PaymentMapper, PaymentSliceMapper로 응답 변환

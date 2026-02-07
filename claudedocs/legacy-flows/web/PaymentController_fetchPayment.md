# API Flow Documentation: PaymentController.fetchPayment

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/payment/{paymentId}` |
| Controller | PaymentController |
| Service | PaymentFindService → PaymentFindServiceImpl |
| Repository | PaymentFindRepository → PaymentFindRepositoryImpl |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| paymentId | long | ✅ | 결제 ID | @PathVariable |

---

## 📤 Response

### Response DTO 구조

```java
public class PaymentResponse implements LastDomainIdProvider {
    private BuyerInfo buyerInfo;              // 구매자 정보
    private PaymentDetail payment;            // 결제 상세
    private ReceiverInfo receiverInfo;        // 수령인 정보
    private RefundAccountResponse refundAccount; // 환불 계좌
    private VBankAccountResponse vBankAccount;   // 가상계좌 정보
    private Set<OrderProductDto> orderProducts;  // 주문 상품 목록
}

public class BuyerInfo {
    private String buyerName;    // 구매자 이름
    private String buyerTel;     // 구매자 전화번호
    private String buyerEmail;   // 구매자 이메일
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

public class ReceiverInfo {
    private String receiverName;
    private String receiverPhoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String zipCode;
    private Origin country;
    private String deliveryRequest;
    private String phoneNumber;
}

public class RefundAccountResponse extends AccountResponse {
    private String bankName;         // 상속
    private String accountNumber;    // 상속
    private long refundAccountId;
    private String accountHolderName;
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
    "buyerInfo": {
      "buyerName": "홍길동",
      "buyerTel": "010-1234-5678",
      "buyerEmail": "hong@example.com"
    },
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
    "receiverInfo": {
      "receiverName": "김수령",
      "receiverPhoneNumber": "010-9876-5432",
      "addressLine1": "서울시 강남구",
      "addressLine2": "테헤란로 123",
      "zipCode": "06234",
      "country": "KOREA",
      "deliveryRequest": "문 앞에 놓아주세요"
    },
    "refundAccount": {
      "bankName": "KB국민은행",
      "accountNumber": "123-456-789012",
      "refundAccountId": 100,
      "accountHolderName": "홍길동"
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
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────┐
│   Controller                                          │
│   PaymentController.fetchPayment(paymentId)          │
│   @GetMapping("/payment/{paymentId}")                │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Service                                             │
│   PaymentFindServiceImpl.fetchPayment()              │
│   @Transactional(readOnly=true)                      │
│   1. SecurityUtils.currentUserId() 로 사용자 ID 획득  │
│   2. paymentFindRepository.fetchPayment() 호출       │
│   3. orderFindService.fetchOrderProducts() 호출      │
│   4. orderFindService.fetchRejectedOrder() 호출      │
│   5. paymentMapper.toPaymentResponse() 매핑          │
└────────────────────┬─────────────────────────────────┘
                     │
                     ├─────────────────────────────────┐
                     ▼                                 ▼
┌──────────────────────────────┐  ┌───────────────────────────┐
│   PaymentFindRepository      │  │   OrderFindService        │
│   fetchPayment(id, userId)   │  │   fetchOrderProducts()    │
│                              │  │   fetchRejectedOrder()    │
└──────────────────────────────┘  └───────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Database                                            │
│   Tables: payment, order, payment_bill,              │
│           payment_method, shipment, users,           │
│           payment_snap_shot_shipping_address,        │
│           payment_snap_shot_mileage,                 │
│           payment_snap_shot_refund_account,          │
│           v_bank_account                             │
└──────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| payment | payment | FROM | - |
| order | order | INNER JOIN | payment.id = order.paymentId |
| payment_bill | paymentBill | INNER JOIN | payment.id = paymentBill.paymentId |
| payment_method | paymentMethod | INNER JOIN | paymentMethod.id = paymentBill.paymentMethodId |
| shipment | shipment | LEFT JOIN | shipment.orderId = order.id |
| users | users | INNER JOIN | users.id = order.userId |
| payment_snap_shot_shipping_address | paymentSnapShotShippingAddress | INNER JOIN | paymentId = order.paymentId |
| payment_snap_shot_mileage | paymentSnapShotMileage | LEFT JOIN | paymentId = payment.id |
| payment_snap_shot_refund_account | paymentSnapShotRefundAccount | LEFT JOIN | paymentId = payment.id |
| v_bank_account | vBankAccount | LEFT JOIN | paymentId = payment.id |

### QueryDSL 코드

```java
queryFactory
    .from(payment)
    .where(paymentIdEq(paymentId), userIdEq(userId))
    .innerJoin(order).on(payment.id.eq(order.paymentId))
    .innerJoin(paymentBill).on(payment.id.eq(paymentBill.paymentId))
    .innerJoin(paymentMethod).on(paymentMethod.id.eq(paymentBill.paymentMethodId))
    .leftJoin(shipment).on(shipment.orderId.eq(order.id))
    .innerJoin(users).on(users.id.eq(order.userId))
    .innerJoin(paymentSnapShotShippingAddress)
        .on(paymentSnapShotShippingAddress.paymentId.eq(order.paymentId))
    .leftJoin(paymentSnapShotMileage).on(paymentSnapShotMileage.paymentId.eq(payment.id))
    .leftJoin(paymentSnapShotRefundAccount).on(paymentSnapShotRefundAccount.paymentId.eq(payment.id))
    .leftJoin(vBankAccount).on(vBankAccount.paymentId.eq(payment.id))
    .transform(
        GroupBy.groupBy(payment.id)
            .as(
                new QPaymentResponse(
                    paymentBill.buyerInfo,
                    new QPaymentDetail(...),
                    new QReceiverInfo(...),
                    new QRefundAccountResponse(...),
                    new QVBankAccountResponse(...))));
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| paymentIdEq | payment.id | 결제 ID |
| userIdEq | payment.paymentDetails.userId | 현재 사용자 ID (보안) |

---

## 📝 특이사항

1. **사용자 ID 검증**: 본인 결제만 조회 가능 (SecurityUtils.currentUserId() 사용)
2. **10개 테이블 조인**: 결제 상세 정보를 위한 복합 조인
3. **스냅샷 테이블 사용**: 결제 시점의 배송지, 마일리지, 환불계좌 정보 보존
4. **추가 서비스 호출**: OrderFindService를 통해 주문 상품 및 거절 사유 조회
5. **예외 처리**: PaymentNotFoundException 발생 시 결제 정보 없음
6. **Optional 결과**: LEFT JOIN 필드들은 null일 수 있음 (vBank, refundAccount, mileage)

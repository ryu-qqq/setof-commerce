# API Flow Documentation: PaymentController.fetchPaymentMethods

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/payment/payment-method` |
| Controller | PaymentController |
| Service | PaymentMethodFindService → PaymentMethodFetchServiceImpl |
| Repository | PaymentMethodFindRepository → PaymentMethodFindRepositoryImpl |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |
| 캐싱 | `@Cacheable(cacheNames = "payMethods")` |

---

## 📥 Request

### Request Parameters

없음

---

## 📤 Response

### Response DTO 구조

```java
public class PayMethodResponse {
    private String displayName;           // 결제 수단 표시명
    private PaymentMethodEnum payMethod;  // 결제 방법 enum
    private String paymentMethodMerchantKey; // 가맹점 키
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "displayName": "신용카드",
      "payMethod": "CARD",
      "paymentMethodMerchantKey": "imp12345678"
    },
    {
      "displayName": "가상계좌",
      "payMethod": "VBANK",
      "paymentMethodMerchantKey": "imp12345678"
    }
  ]
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────┐
│   Controller                                          │
│   PaymentController.fetchPaymentMethods()            │
│   @GetMapping("/payment/payment-method")             │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Service                                             │
│   PaymentMethodFetchServiceImpl.fetchPayMethods()    │
│   @Transactional(readOnly=true)                      │
│   @Cacheable(cacheNames = "payMethods")              │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Repository                                          │
│   PaymentMethodFindRepositoryImpl.fetchPayMethods()  │
│   QueryDSL                                           │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Database                                            │
│   Tables: payment_method                             │
└──────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| payment_method | paymentMethod | FROM | - |

### QueryDSL 코드

```java
queryFactory
    .from(paymentMethod)
    .where(displayYnEq())  // paymentMethod.displayYn.eq(Yn.Y)
    .transform(
        GroupBy.groupBy(paymentMethod.id)
            .list(
                new QPayMethodResponse(
                    paymentMethod.paymentMethodEnum,
                    paymentMethod.paymentMethodMerchantKey)));
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| displayYnEq | paymentMethod.displayYn | 노출 여부 = 'Y' |

---

## 📝 특이사항

1. **캐싱 적용**: `payMethods` 캐시명으로 결과가 캐싱됨
2. **단순 조회**: 조인 없이 단일 테이블 조회
3. **GroupBy 변환**: QueryProjection을 사용하여 DTO로 직접 매핑

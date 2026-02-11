# API Flow Documentation: PaymentController.fetchPayResult

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/payment/{paymentId}/status` |
| Controller | PaymentController |
| Service | PaymentBillFindService → PaymentBillFindServiceImpl |
| Repository | PaymentBillFindRepository → PaymentBillFindRepositoryImpl |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |
| 외부 API | PortOne API (PG사 결제 상태 조회) |

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
public class PaymentResult {
    private boolean isSuccess;  // 결제 성공 여부
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "isSuccess": true
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────┐
│   Controller                                          │
│   PaymentController.fetchPayResult(paymentId)        │
│   @GetMapping("/payment/{paymentId}/status")         │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Service                                             │
│   PaymentBillFindServiceImpl.fetchPaymentResult()    │
│   @Transactional(readOnly=true)                      │
│   1. PortOneClient 활성화 확인                        │
│   2. paymentBillFindRepository.fetchPaymentAgencyId()│
│   3. portOneClient.fetchPaymentPortOne() (외부 API)  │
│   4. payment.status == "paid" 체크                   │
└────────────────────┬─────────────────────────────────┘
                     │
                     ├─────────────────────────────────┐
                     ▼                                 ▼
┌──────────────────────────────┐  ┌───────────────────────────┐
│   PaymentBillFindRepository  │  │   PortOneClient           │
│   fetchPaymentAgencyId()     │  │   fetchPaymentPortOne()   │
│                              │  │   (External API Call)     │
└──────────────────────────────┘  └───────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Database                                            │
│   Tables: payment_bill                               │
└──────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| payment_bill | paymentBill | FROM | - |

### QueryDSL 코드

```java
// paymentAgencyId 조회
queryFactory
    .select(paymentBill.paymentAgencyId)
    .from(paymentBill)
    .where(isPaymentIdEq(paymentId))
    .fetchOne();
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| isPaymentIdEq | paymentBill.paymentId | 결제 ID |

---

## 🔗 외부 API 연동

### PortOne API 호출

```java
// Service 로직
if (portOneClient.isEmpty()) {
    // PortOne 비활성화 시 DB 기반으로만 결과 반환
    return new PaymentResult(false);
}

Boolean aBoolean = paymentBillFindRepository
    .fetchPaymentAgencyId(paymentId)
    .filter(StringUtils::hasText)
    .flatMap(portOneClient.get()::fetchPaymentPortOne)
    .map(payment -> "paid".equals(payment.getStatus()))
    .orElse(false);

return new PaymentResult(aBoolean);
```

### 외부 API 흐름

```
1. DB에서 paymentAgencyId 조회 (PG사 거래 ID)
   └── paymentBill.paymentAgencyId

2. PortOne API 호출
   └── GET https://api.portone.io/payments/{paymentAgencyId}

3. 응답에서 status 확인
   └── status == "paid" → isSuccess = true
   └── 그 외 → isSuccess = false
```

---

## 📝 특이사항

1. **외부 API 의존**: PortOne(아임포트) API를 통해 실제 PG사 결제 상태 확인
2. **조건부 빈 주입**: `@Autowired(required = false)`로 PortOneClient 선택적 주입
3. **Fallback 처리**: PortOneClient가 없으면 `isSuccess = false` 반환
4. **단순 쿼리**: 단일 테이블에서 paymentAgencyId만 조회
5. **실시간 상태 조회**: DB 상태가 아닌 PG사 실제 결제 상태 기반 판단
6. **Optional 체인**: paymentAgencyId가 없거나 빈 문자열이면 false 반환

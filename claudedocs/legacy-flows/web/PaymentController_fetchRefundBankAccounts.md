# API Flow Documentation: PaymentController.fetchRefundBankAccounts

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/payment/refund-bank` |
| Controller | PaymentController |
| Service | VBankFindService → VBankFindServiceImpl |
| Repository | VBankFindRepository → VBankFindRepositoryImpl |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |
| 캐싱 | `@Cacheable(cacheNames = "refundBanks")` |

---

## 📥 Request

### Request Parameters

없음

---

## 📤 Response

### Response DTO 구조

```java
public class BankResponse {
    private String bankCode;   // 은행 코드
    private String bankName;   // 은행명
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "bankCode": "004",
      "bankName": "KB국민은행"
    },
    {
      "bankCode": "011",
      "bankName": "NH농협"
    }
  ]
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────┐
│   Controller                                          │
│   PaymentController.fetchRefundBankAccounts()        │
│   @GetMapping("/payment/refund-bank")                │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Service                                             │
│   VBankFindServiceImpl.fetchRefundBanks()            │
│   @Transactional(readOnly=true)                      │
│   @Cacheable(cacheNames = "refundBanks")             │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Repository                                          │
│   VBankFindRepositoryImpl.fetchRefundBanks()         │
│   QueryDSL                                           │
└────────────────────┬─────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────┐
│   Database                                            │
│   Tables: common_code                                │
└──────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| common_code | commonCode | FROM | - |

### QueryDSL 코드

```java
queryFactory
    .from(commonCode)
    .where(codeGroupIdRefundVBank())  // commonCode.codeGroupId.eq(12L)
    .transform(
        GroupBy.groupBy(commonCode.id)
            .list(
                new QBankResponse(
                    commonCode.codeDetail,
                    commonCode.codeDetailDisplayName)));
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| codeGroupIdRefundVBank | commonCode.codeGroupId | 공통코드 그룹 = 12 (환불 계좌 은행) |

---

## 📝 특이사항

1. **캐싱 적용**: `refundBanks` 캐시명으로 결과가 캐싱됨
2. **공통코드 사용**: common_code 테이블에서 환불 은행 목록 조회
3. **하드코딩된 그룹 ID**: codeGroupId = 12L로 하드코딩
4. **fetchVBankRefundAccounts와 유사**: 동일 서비스/리포지토리 사용, codeGroupId만 다름

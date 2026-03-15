# API Flow: DiscountController.fetchDiscountPolicy

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/discount/{discountPolicyId}` |
| Controller | `DiscountController` |
| Service | `DiscountFetchService` → `DiscountFetchServiceImpl` |
| Repository | `DiscountPolicyFetchRepository` → `DiscountPolicyFetchRepositoryImpl` |
| 보안 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

---

## 2. Request

### Parameters

| 이름 | 타입 | 필수 | 위치 | Validation |
|------|------|------|------|------------|
| discountPolicyId | long | O | @PathVariable | - |

### JSON Example

Path Variable만 사용하므로 Request Body 없음.

```
GET /api/v1/discount/1
```

---

## 3. Response

### DTO Structure

```
DiscountPolicyResponseDto
├── discountPolicyId          : long
├── discountDetails           : DiscountDetails (Embedded)
│   ├── discountPolicyName    : String           // 정책명
│   ├── discountType          : DiscountType     // RATE | PRICE
│   ├── publisherType         : PublisherType    // ADMIN | SELLER
│   ├── issueType             : IssueType        // PRODUCT | SELLER | BRAND
│   ├── discountLimitYn       : Yn               // Y | N
│   ├── maxDiscountPrice      : long             // 최대 할인 금액
│   ├── shareYn               : Yn               // Y | N
│   ├── shareRatio            : double           // 분담 비율
│   ├── discountRatio         : double           // 할인율
│   ├── policyStartDate       : LocalDateTime    // yyyy-MM-dd HH:mm:ss
│   ├── policyEndDate         : LocalDateTime    // yyyy-MM-dd HH:mm:ss
│   ├── memo                  : String           // nullable
│   ├── priority              : int              // 우선순위
│   └── activeYn              : Yn               // Y | N
├── insertDate                : LocalDateTime    // yyyy-MM-dd HH:mm:ss
├── updateDate                : LocalDateTime    // yyyy-MM-dd HH:mm:ss
├── insertOperator            : String
└── updateOperator            : String
```

**Enum 값 목록**

| Enum | 값 |
|------|-----|
| DiscountType | RATE, PRICE |
| PublisherType | ADMIN, SELLER |
| IssueType | PRODUCT, SELLER, BRAND |
| Yn | Y, N |

### JSON Example

```json
{
  "success": true,
  "data": {
    "discountPolicyId": 1,
    "discountDetails": {
      "discountPolicyName": "신규 회원 할인",
      "discountType": "RATE",
      "publisherType": "ADMIN",
      "issueType": "PRODUCT",
      "discountLimitYn": "Y",
      "maxDiscountPrice": 10000,
      "shareYn": "Y",
      "shareRatio": 0.5,
      "discountRatio": 0.1,
      "policyStartDate": "2026-01-01 00:00:00",
      "policyEndDate": "2026-12-31 23:59:59",
      "memo": "신규 회원 대상 10% 할인",
      "priority": 1,
      "activeYn": "Y"
    },
    "insertDate": "2026-01-01 10:00:00",
    "updateDate": "2026-01-15 14:30:00",
    "insertOperator": "admin",
    "updateOperator": "admin"
  }
}
```

---

## 4. 호출 흐름

```
DiscountController.fetchDiscountPolicy(discountPolicyId)
    @GetMapping("/discount/{discountPolicyId}")
        │
        └── DiscountFetchService.fetchDiscountPolicy(long discountPolicyId)
                │
                └── DiscountFetchServiceImpl.fetchDiscountPolicy(long discountPolicyId)
                        @Transactional(readOnly = true)
                        │
                        └── DiscountPolicyFetchRepository.fetchDiscountPolicy(long discountPolicyId)
                                │
                                └── DiscountPolicyFetchRepositoryImpl.fetchDiscountPolicy(long discountPolicyId)
                                        QueryDSL + GroupBy.transform()
                                        → Optional<DiscountPolicyResponseDto>
                                        (없으면 DiscountNotFoundException throw)
```

---

## 5. Database Query

### Tables

| 테이블 | JOIN 유형 | JOIN 조건 | 역할 |
|--------|-----------|-----------|------|
| discount_policy | FROM (단일 테이블) | - | 할인 정책 조회 |

### QueryDSL

```java
// DiscountPolicyFetchRepositoryImpl.fetchDiscountPolicy()
getQueryFactory()
    .from(discountPolicy)
    .where(
        discountPolicyIdEq(discountPolicyId)   // discountPolicy.id = :discountPolicyId
    )
    .transform(
        GroupBy.groupBy(discountPolicy.id).as(
            new QDiscountPolicyResponseDto(
                discountPolicy.id,
                discountPolicy.discountDetails,
                discountPolicy.insertDate,
                discountPolicy.updateDate,
                discountPolicy.insertOperator,
                discountPolicy.updateOperator
            )
        )
    )
    .get(discountPolicyId);
```

### WHERE 조건

| 조건 메서드 | 대상 컬럼 | 설명 |
|-------------|-----------|------|
| discountPolicyIdEq | discount_policy.discount_policy_id | Path Variable로 전달된 ID와 일치 |

### Entity - 테이블 매핑

| Entity | 테이블 |
|--------|--------|
| DiscountPolicy | discount_policy |

### 주요 컬럼 (discount_policy)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| discount_policy_id | BIGINT | PK |
| DISCOUNT_POLICY_NAME | VARCHAR(50) | 정책명 |
| DISCOUNT_TYPE | VARCHAR(20) | RATE / PRICE |
| PUBLISHER_TYPE | VARCHAR(20) | ADMIN / SELLER |
| ISSUE_TYPE | VARCHAR(50) | PRODUCT / SELLER / BRAND |
| DISCOUNT_LIMIT_YN | CHAR(1) | 할인 한도 여부 |
| MAX_DISCOUNT_PRICE | BIGINT | 최대 할인 금액 |
| SHARE_YN | CHAR(1) | 분담 여부 |
| SHARE_RATIO | DOUBLE | 분담 비율 |
| DISCOUNT_RATIO | DOUBLE | 할인율 |
| POLICY_START_DATE | DATETIME | 정책 시작일 |
| POLICY_END_DATE | DATETIME | 정책 종료일 |
| MEMO | VARCHAR(100) | 메모 (nullable) |
| PRIORITY | INT | 우선순위 |
| ACTIVE_YN | CHAR(1) | 활성화 여부 |
| INSERT_DATE | DATETIME | 생성일 |
| UPDATE_DATE | DATETIME | 수정일 |
| INSERT_OPERATOR | VARCHAR(50) | 생성자 |
| UPDATE_OPERATOR | VARCHAR(50) | 수정자 |
| DELETE_YN | CHAR(1) | 삭제 여부 (@JsonIgnore) |

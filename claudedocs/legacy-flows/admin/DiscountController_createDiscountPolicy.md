# API Flow: DiscountController.createDiscountPolicy

## 1. 기본 정보

- **HTTP**: POST /api/v1/discount
- **Controller**: `com.connectly.partnerAdmin.module.discount.controller.DiscountController`
- **Service**: `DiscountQueryService` → `DiscountQueryServiceImpl`
- **Repository**: `DiscountPolicyRepository` (JPA), `DiscountPolicyFetchRepository` → `DiscountPolicyFetchRepositoryImpl` (QueryDSL - 복사 생성 시)
- **권한**: `@PreAuthorize(HAS_AUTHORITY_MASTER)` - MASTER 권한 필요

---

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | 설명 |
|------|------|------|------|------|
| body | `CreateDiscount` | RequestBody | Y | 할인 정책 생성 요청 |

### CreateDiscount DTO

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| `discountPolicyId` | `Long` | N | - | 복사 생성 시 원본 정책 ID (null이면 신규 생성) |
| `discountDetails` | `CreateDiscountDetails` | Y | - | 할인 정책 상세 정보 |

### CreateDiscountDetails DTO

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| `discountPolicyName` | `String` | Y | `@NotBlank`, `@Length(max=50)` | 할인 정책명 |
| `discountType` | `DiscountType` | Y | `@NotNull` | 할인 타입 (RATE, PRICE) |
| `publisherType` | `PublisherType` | Y | `@NotNull` | 발행자 타입 (ADMIN, SELLER) |
| `issueType` | `IssueType` | Y | `@NotNull` | 발급 대상 타입 (PRODUCT, SELLER, BRAND) |
| `discountLimitYn` | `Yn` | Y | `@NotNull` | 할인 한도 적용 여부 (Y, N) |
| `maxDiscountPrice` | `long` | Y | `@Min(0)` | 최대 할인 금액 |
| `shareYn` | `Yn` | Y | `@NotNull` | 분담 여부 (Y, N) |
| `shareRatio` | `double` | Y | `@Min(0)`, `@Max(100)` | 분담 비율 (0~100) |
| `discountRatio` | `double` | Y | `@Min(0)`, `@Max(100)` | 할인율 (0~100) |
| `policyStartDate` | `LocalDateTime` | Y | `@ValidDateRange` | 정책 시작일 (yyyy-MM-dd HH:mm:ss) |
| `policyEndDate` | `LocalDateTime` | Y | `@ValidDateRange` | 정책 종료일 (yyyy-MM-dd HH:mm:ss) |
| `memo` | `String` | N | `@Length(max=100)` | 메모 |
| `priority` | `int` | Y | `@Min(0)`, `@Max(100)` | 우선순위 |
| `activeYn` | `Yn` | Y | `@NotNull` | 활성 여부 (Y, N) |

**클래스 레벨 Validation**: `@ValidDateRange(start="policyStartDate", end="policyEndDate")` - 시작일이 종료일보다 이전이어야 함

### JSON Example (신규 생성)

```json
{
  "discountDetails": {
    "discountPolicyName": "봄 시즌 할인",
    "discountType": "RATE",
    "publisherType": "ADMIN",
    "issueType": "PRODUCT",
    "discountLimitYn": "Y",
    "maxDiscountPrice": 50000,
    "shareYn": "N",
    "shareRatio": 0,
    "discountRatio": 10,
    "policyStartDate": "2024-03-01 00:00:00",
    "policyEndDate": "2024-03-31 23:59:59",
    "memo": "봄 신상 할인 이벤트",
    "priority": 10,
    "activeYn": "Y"
  }
}
```

### JSON Example (복사 생성)

```json
{
  "discountPolicyId": 123,
  "discountDetails": {
    "discountPolicyName": "봄 시즌 할인 복사본",
    "discountType": "RATE",
    "publisherType": "ADMIN",
    "issueType": "PRODUCT",
    "discountLimitYn": "Y",
    "maxDiscountPrice": 50000,
    "shareYn": "N",
    "shareRatio": 0,
    "discountRatio": 10,
    "policyStartDate": "2024-04-01 00:00:00",
    "policyEndDate": "2024-04-30 23:59:59",
    "memo": "4월 복사 정책",
    "priority": 10,
    "activeYn": "Y"
  }
}
```

---

## 3. Response

### DiscountPolicyResponseDto 구조

| 필드명 | 타입 | 설명 |
|--------|------|------|
| `discountPolicyId` | `long` | 할인 정책 ID |
| `discountDetails` | `DiscountDetails` | 할인 정책 상세 (임베디드) |
| `insertDate` | `LocalDateTime` | 등록일시 (yyyy-MM-dd HH:mm:ss) |
| `updateDate` | `LocalDateTime` | 수정일시 (yyyy-MM-dd HH:mm:ss) |
| `insertOperator` | `String` | 등록자 |
| `updateOperator` | `String` | 수정자 |

### DiscountDetails (Embedded) 구조

| 필드명 | 타입 | DB 컬럼 | 설명 |
|--------|------|---------|------|
| `discountPolicyName` | `String` | DISCOUNT_POLICY_NAME | 할인 정책명 |
| `discountType` | `DiscountType` | DISCOUNT_TYPE | RATE / PRICE |
| `publisherType` | `PublisherType` | PUBLISHER_TYPE | ADMIN / SELLER |
| `issueType` | `IssueType` | ISSUE_TYPE | PRODUCT / SELLER / BRAND |
| `discountLimitYn` | `Yn` | DISCOUNT_LIMIT_YN | Y / N |
| `maxDiscountPrice` | `long` | MAX_DISCOUNT_PRICE | 최대 할인 금액 |
| `shareYn` | `Yn` | SHARE_YN | Y / N |
| `shareRatio` | `double` | SHARE_RATIO | 분담 비율 |
| `discountRatio` | `double` | DISCOUNT_RATIO | 할인율 |
| `policyStartDate` | `LocalDateTime` | POLICY_START_DATE | 정책 시작일 |
| `policyEndDate` | `LocalDateTime` | POLICY_END_DATE | 정책 종료일 |
| `memo` | `String` | MEMO | 메모 |
| `priority` | `int` | PRIORITY | 우선순위 |
| `activeYn` | `Yn` | ACTIVE_YN | Y / N |

**참고**: `BaseEntity.deleteYn` 필드는 `@JsonIgnore` 적용으로 응답에서 제외됨

### JSON Example

```json
{
  "success": true,
  "data": {
    "discountPolicyId": 456,
    "discountDetails": {
      "discountPolicyName": "봄 시즌 할인",
      "discountType": "RATE",
      "publisherType": "ADMIN",
      "issueType": "PRODUCT",
      "discountLimitYn": "Y",
      "maxDiscountPrice": 50000,
      "shareYn": "N",
      "shareRatio": 0.0,
      "discountRatio": 10.0,
      "policyStartDate": "2024-03-01 00:00:00",
      "policyEndDate": "2024-03-31 23:59:59",
      "memo": "봄 신상 할인 이벤트",
      "priority": 10,
      "activeYn": "Y"
    },
    "insertDate": "2024-03-15 10:00:00",
    "updateDate": "2024-03-15 10:00:00",
    "insertOperator": "admin_user",
    "updateOperator": "admin_user"
  }
}
```

---

## 4. 호출 흐름

### 기본 흐름 (신규 생성: discountPolicyId = null)

```
DiscountController.createDiscountPolicy(CreateDiscount)
    └── DiscountQueryServiceImpl.createDiscount(CreateDiscount)
            ├── handleCopyCreateIfNeeded(createDiscount)
            │       └── [discountPolicyId == null → SKIP]
            ├── DiscountMapperImpl.toEntity(createDiscount)
            │       └── CreateDiscountDetails → DiscountDetails (빌더 변환)
            │           └── new DiscountPolicy(discountDetails)
            └── DiscountPolicyRepository.save(discountPolicy)
                    └── JPA INSERT → discount_policy 테이블
                        └── @PrePersist: insertDate, updateDate, insertOperator, updateOperator, deleteYn 자동 설정
                            (MDC.get("user") 기반 operator 설정)
```

### 복사 생성 흐름 (discountPolicyId != null)

```
DiscountController.createDiscountPolicy(CreateDiscount)
    └── DiscountQueryServiceImpl.createDiscount(CreateDiscount)
            ├── handleCopyCreateIfNeeded(createDiscount)
            │       └── [discountPolicyId != null → 복사 생성 처리]
            │           └── updateDiscountUseYn(UpdateUseDiscount(discountPolicyId, Yn.N))
            │               ├── DiscountFetchServiceImpl.fetchDiscountEntities([discountPolicyId])
            │               │       └── DiscountPolicyFetchRepositoryImpl.fetchDiscountEntities([ids])
            │               │           └── QueryDSL SELECT FROM discount_policy WHERE id IN (...)
            │               ├── updateActiveYn(discountPolicies, Yn.N)
            │               │       └── discountPolicy.setActiveYn(Yn.N)
            │               │           └── addHistory() → DiscountPolicyHistory INSERT
            │               └── DiscountTargetQueryService.updateDiscountTargetUseYn(...)
            ├── DiscountMapperImpl.toEntity(createDiscount)
            │       └── CreateDiscountDetails → DiscountDetails → new DiscountPolicy(discountDetails)
            └── DiscountPolicyRepository.save(discountPolicy)
                    └── JPA INSERT → discount_policy 테이블
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | 작업 | 조건 |
|--------|------|------|
| `discount_policy` | INSERT (항상) | 신규 레코드 생성 |
| `discount_policy` | SELECT (복사 생성 시) | WHERE id IN (...) |
| `discount_policy_history` | INSERT (복사 생성 시) | setActiveYn 호출 시 히스토리 추가 |

### JPA INSERT (DiscountPolicyRepository.save)

```sql
INSERT INTO discount_policy (
    DISCOUNT_POLICY_NAME,
    DISCOUNT_TYPE,
    PUBLISHER_TYPE,
    ISSUE_TYPE,
    DISCOUNT_LIMIT_YN,
    MAX_DISCOUNT_PRICE,
    SHARE_YN,
    SHARE_RATIO,
    DISCOUNT_RATIO,
    POLICY_START_DATE,
    POLICY_END_DATE,
    MEMO,
    PRIORITY,
    ACTIVE_YN,
    DELETE_YN,
    INSERT_DATE,
    UPDATE_DATE,
    INSERT_OPERATOR,
    UPDATE_OPERATOR
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N', NOW(), NOW(), ?, ?)
```

### QueryDSL SELECT (복사 생성 시 - fetchDiscountEntities)

```java
getQueryFactory()
    .selectFrom(discountPolicy)
    .where(discountPolicy.id.in(discountPolicyIds))
    .fetch()
```

```sql
SELECT *
FROM discount_policy
WHERE discount_policy_id IN (?)
```

---

## 6. 비즈니스 로직 상세

### 복사 생성 (Copy Create)

`CreateDiscount.discountPolicyId`가 존재하는 경우 복사 생성 모드로 동작:

1. **원본 정책 비활성화**: `discountPolicyId`에 해당하는 기존 정책의 `activeYn`을 `N`으로 변경
2. **히스토리 기록**: `setActiveYn` 호출 시 `addHistory()`를 통해 `discount_policy_history` 테이블에 변경 이력 자동 기록
3. **신규 정책 생성**: 요청의 `discountDetails`로 새 정책 생성

### @PrePersist (BaseEntity)

엔티티 저장 시 자동 설정:
- `insertDate`, `updateDate`: 현재 시각
- `insertOperator`, `updateOperator`: `MDC.get("user")` 값 (없으면 `"Anonymous"`)
- `deleteYn`: `Yn.N`

### @ValidDateRange

클래스 레벨 커스텀 어노테이션으로 `policyStartDate < policyEndDate` 제약 검증

---

## 7. Enum 값 목록

| Enum | 값 |
|------|-----|
| `DiscountType` | `RATE` (비율 할인), `PRICE` (금액 할인) |
| `PublisherType` | `ADMIN`, `SELLER` |
| `IssueType` | `PRODUCT`, `SELLER`, `BRAND` |
| `Yn` | `Y`, `N` |

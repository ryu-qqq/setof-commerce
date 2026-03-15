# API Flow: DiscountController.createDiscountsFromExcelUpload

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| API Path | /api/v1/discounts/excel |
| Controller | DiscountController |
| Service (인터페이스) | DiscountQueryService |
| Service (구현체) | DiscountQueryServiceImpl |
| Sub-Service 1 (인터페이스) | DiscountTargetQueryService |
| Sub-Service 1 (구현체) | DiscountTargetQueryServiceImpl |
| Repository (JPA) | DiscountPolicyRepository (JpaRepository) |
| Repository (JPA) | DiscountTargetRepository (JpaRepository) |
| Repository (QueryDSL) | DiscountPolicyFetchRepository |
| Repository (QueryDSL Impl) | DiscountPolicyFetchRepositoryImpl |
| Redis Service | DiscountRedisQueryService → DiscountRedisQueryServiceImpl |
| 인증 | @PreAuthorize(HAS_AUTHORITY_MASTER) |

---

## 2. Request

### 요청 형식

- Content-Type: application/json
- Body: `List<CreateDiscountFromExcel>`

### CreateDiscountFromExcel 구조

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| discountDetails | CreateDiscountDetails | O | @Valid | 할인 정책 상세 정보 |
| targetIds | List\<Long\> | O | @Size(min=1) | 할인 대상 ID 목록 (최소 1개) |

### CreateDiscountDetails 구조

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| discountPolicyName | String | O | @NotBlank, @Length(max=50) | 할인 정책명 |
| discountType | DiscountType | O | @NotNull | 할인 타입 (RATE / PRICE) |
| publisherType | PublisherType | O | @NotNull | 발행 주체 (ADMIN / SELLER) |
| issueType | IssueType | O | @NotNull | 발급 대상 타입 (PRODUCT / SELLER / BRAND) |
| discountLimitYn | Yn | O | @NotNull | 할인 한도 적용 여부 (Y / N) |
| maxDiscountPrice | long | - | @Min(0) | 최대 할인 금액 |
| shareYn | Yn | O | @NotNull | 분담 여부 (Y / N) |
| shareRatio | double | - | @Min(0), @Max(100) | 분담 비율 (0~100) |
| discountRatio | double | - | @Min(0), @Max(100) | 할인 비율 (0~100) |
| policyStartDate | LocalDateTime | - | @ValidDateRange | 정책 시작일 (yyyy-MM-dd HH:mm:ss) |
| policyEndDate | LocalDateTime | - | @ValidDateRange | 정책 종료일 (yyyy-MM-dd HH:mm:ss) |
| memo | String | - | @Length(max=100) | 메모 |
| priority | int | - | @Min(0), @Max(100) | 우선순위 (0~100) |
| activeYn | Yn | O | @NotNull | 활성화 여부 (Y / N) |

> 클래스 레벨 `@ValidDateRange(start="policyStartDate", end="policyEndDate")` 적용 — 시작일이 종료일보다 늦으면 유효성 실패

### JSON Request 예시

```json
[
  {
    "discountDetails": {
      "discountPolicyName": "여름 시즌 할인 정책",
      "discountType": "RATE",
      "publisherType": "ADMIN",
      "issueType": "PRODUCT",
      "discountLimitYn": "Y",
      "maxDiscountPrice": 5000,
      "shareYn": "N",
      "shareRatio": 0.0,
      "discountRatio": 10.0,
      "policyStartDate": "2024-07-01 00:00:00",
      "policyEndDate": "2024-07-31 23:59:59",
      "memo": "여름 시즌 특가",
      "priority": 10,
      "activeYn": "Y"
    },
    "targetIds": [1001, 1002, 1003]
  },
  {
    "discountDetails": {
      "discountPolicyName": "셀러 전용 할인",
      "discountType": "PRICE",
      "publisherType": "SELLER",
      "issueType": "SELLER",
      "discountLimitYn": "N",
      "maxDiscountPrice": 0,
      "shareYn": "Y",
      "shareRatio": 50.0,
      "discountRatio": 0.0,
      "policyStartDate": "2024-08-01 00:00:00",
      "policyEndDate": "2024-08-31 23:59:59",
      "memo": "",
      "priority": 5,
      "activeYn": "Y"
    },
    "targetIds": [200]
  }
]
```

---

## 3. Response

### DiscountPolicyResponseDto 구조

| 필드명 | 타입 | 설명 |
|--------|------|------|
| discountPolicyId | long | 생성된 할인 정책 PK |
| discountDetails | DiscountDetails | 할인 정책 상세 (Embedded) |
| insertDate | LocalDateTime | 등록일 (yyyy-MM-dd HH:mm:ss) |
| updateDate | LocalDateTime | 수정일 (yyyy-MM-dd HH:mm:ss) |
| insertOperator | String | 등록자 |
| updateOperator | String | 수정자 |

**discountDetails 내부 필드**

| 필드명 | 타입 | DB 컬럼 |
|--------|------|---------|
| discountPolicyName | String | DISCOUNT_POLICY_NAME |
| discountType | DiscountType | DISCOUNT_TYPE |
| publisherType | PublisherType | PUBLISHER_TYPE |
| issueType | IssueType | ISSUE_TYPE |
| discountLimitYn | Yn | DISCOUNT_LIMIT_YN |
| maxDiscountPrice | long | MAX_DISCOUNT_PRICE |
| shareYn | Yn | SHARE_YN |
| shareRatio | double | SHARE_RATIO |
| discountRatio | double | DISCOUNT_RATIO |
| policyStartDate | LocalDateTime | POLICY_START_DATE |
| policyEndDate | LocalDateTime | POLICY_END_DATE |
| memo | String | MEMO |
| priority | int | PRIORITY |
| activeYn | Yn | ACTIVE_YN |

> 응답 타입: `ResponseEntity<ApiResponse<List<DiscountPolicyResponseDto>>>`

### JSON Response 예시

```json
{
  "success": true,
  "data": [
    {
      "discountPolicyId": 101,
      "discountDetails": {
        "discountPolicyName": "여름 시즌 할인 정책",
        "discountType": "RATE",
        "publisherType": "ADMIN",
        "issueType": "PRODUCT",
        "discountLimitYn": "Y",
        "maxDiscountPrice": 5000,
        "shareYn": "N",
        "shareRatio": 0.0,
        "discountRatio": 10.0,
        "policyStartDate": "2024-07-01 00:00:00",
        "policyEndDate": "2024-07-31 23:59:59",
        "memo": "여름 시즌 특가",
        "priority": 10,
        "activeYn": "Y"
      },
      "insertDate": "2024-01-01 00:00:00",
      "updateDate": "2024-01-01 00:00:00",
      "insertOperator": "admin",
      "updateOperator": "admin"
    }
  ]
}
```

---

## 4. 호출 흐름

```
POST /api/v1/discounts/excel
    └── DiscountController.createDiscountsFromExcelUpload(List<CreateDiscountFromExcel>)
            └── DiscountQueryService.createDiscountFromExcel(List<CreateDiscountFromExcel>)
                    └── DiscountQueryServiceImpl.createDiscountFromExcel(...)
                            └── [각 CreateDiscountFromExcel 항목에 대해 반복]
                                    │
                                    ├── 1) createDiscount(new CreateDiscount(discountFromExcel.getDiscountDetails()))
                                    │       ├── handleCopyCreateIfNeeded(createDiscount)  // copyCreate 아닌 경우 skip
                                    │       ├── DiscountMapper.toEntity(createDiscount)
                                    │       │       └── DiscountMapperImpl.toEntity(...)
                                    │       │               → CreateDiscountDetails → DiscountDetails (Embedded) 변환
                                    │       │               → new DiscountPolicy(discountDetails) 반환
                                    │       ├── DiscountPolicyRepository.save(discountPolicy)
                                    │       │       → discount_policy 테이블 INSERT
                                    │       └── DiscountPolicyResponseDto.from(savedDiscountPolicy)
                                    │
                                    └── 2) DiscountTargetQueryService.createDiscountTargets(discountPolicyId, createDiscountTarget)
                                                └── DiscountTargetQueryServiceImpl.createDiscountTargets(...)
                                                        ├── DiscountFetchService.fetchDiscountEntity(discountPolicyId)
                                                        │       └── DiscountFetchServiceImpl.fetchDiscountEntity(...)
                                                        │               └── DiscountPolicyFetchRepository.fetchDiscountEntity(id)
                                                        │                       → QueryDSL: SELECT FROM discount_policy WHERE id = ?
                                                        ├── saveDiscountTargetsToCacheAndDb(discountPolicy, createDiscountTarget)
                                                        │       ├── targetIds → Set<DiscountTarget> 생성 (new DiscountTarget(policy, targetId))
                                                        │       ├── DiscountTargetRepository.saveAll(discountTargets)
                                                        │       │       → discount_target 테이블 INSERT (batch)
                                                        │       └── updateDiscountCache(discountPolicy, savedTargets)
                                                        │               └── DiscountRedisQueryService.updateDiscountCache(...)
                                                        │                       └── DiscountRedisQueryServiceImpl.updateDiscountCache(...)
                                                        │                               → Redis: key = DISCOUNT:{issueType}{targetId}
                                                        │                               → activeYn=N 이면 DELETE, Y 이면 SET with TTL
```

### 처리 순서 요약

1. 입력 리스트를 순회하며 각 항목에 대해 아래 작업 수행
2. `CreateDiscountDetails` → `DiscountMapper` → `DiscountPolicy` 엔티티 변환
3. `DiscountPolicyRepository.save()` 로 `discount_policy` 테이블에 INSERT
4. 저장된 정책의 ID와 `issueType`, `targetIds`로 `CreateDiscountTarget` 구성
5. `DiscountTargetQueryService.createDiscountTargets()` 호출
6. DB에서 `DiscountPolicy` 엔티티 재조회 (fetchDiscountEntity)
7. `targetIds` 각각에 대해 `DiscountTarget` 엔티티 생성 후 `saveAll()` batch INSERT
8. 저장된 target들을 Redis에 캐시 업데이트 (key: `DISCOUNT:{issueType}{targetId}`, TTL: 정책 종료일까지)

---

## 5. Database

### 관련 테이블

| 테이블 | 역할 | 주요 컬럼 |
|--------|------|----------|
| discount_policy | 할인 정책 저장 | discount_policy_id (PK, AUTO_INCREMENT), DISCOUNT_POLICY_NAME, DISCOUNT_TYPE, PUBLISHER_TYPE, ISSUE_TYPE, DISCOUNT_LIMIT_YN, MAX_DISCOUNT_PRICE, SHARE_YN, SHARE_RATIO, DISCOUNT_RATIO, POLICY_START_DATE, POLICY_END_DATE, MEMO, PRIORITY, ACTIVE_YN |
| discount_target | 할인 대상 저장 | discount_target_id (PK, AUTO_INCREMENT), issue_type, active_yn, target_id, discount_policy_id (FK) |

### 실행되는 쿼리 (QueryDSL 기반)

**1. discount_policy INSERT (JPA save)**

```sql
INSERT INTO discount_policy (
    DISCOUNT_POLICY_NAME, DISCOUNT_TYPE, PUBLISHER_TYPE, ISSUE_TYPE,
    DISCOUNT_LIMIT_YN, MAX_DISCOUNT_PRICE, SHARE_YN, SHARE_RATIO,
    DISCOUNT_RATIO, POLICY_START_DATE, POLICY_END_DATE, MEMO, PRIORITY, ACTIVE_YN,
    insert_date, update_date, insert_operator, update_operator
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ?, ?)
```

**2. discount_policy 조회 (QueryDSL - fetchDiscountEntity)**

```java
queryFactory.selectFrom(discountPolicy)
    .where(discountPolicy.id.eq(discountPolicyId))
    .fetchOne()
```

```sql
SELECT *
FROM discount_policy dp
WHERE dp.discount_policy_id = ?
```

**3. discount_target INSERT (JPA saveAll - batch)**

```sql
INSERT INTO discount_target (
    issue_type, active_yn, target_id, discount_policy_id,
    insert_date, update_date, insert_operator, update_operator
) VALUES (?, 'Y', ?, ?, NOW(), NOW(), ?, ?)
-- targetIds 수만큼 반복
```

### Redis 캐시 구조

| 항목 | 내용 |
|------|------|
| Key 패턴 | `DISCOUNT:{issueType}{targetId}` (예: `DISCOUNT:PRODUCT1001`) |
| Value | `BaseDiscountInfo` JSON 직렬화 |
| TTL | `Duration.between(LocalDateTime.now(), policyEndDate)` |
| 삭제 조건 | `activeYn = N` 인 target은 해당 key DELETE |

---

## 6. 주요 비즈니스 로직

### 엑셀 업로드 할인 생성 특성

- 단건 생성 API(`createDiscount`)와 달리 **copyCreate 기능을 사용하지 않음** — `CreateDiscountFromExcel`은 `discountPolicyId` 없이 `discountDetails`만 포함
- 리스트 단위 처리이나 내부적으로 **건별 개별 처리** (트랜잭션은 `DiscountQueryServiceImpl` 레벨에서 묶임)
- 정책 생성 → 타겟 생성 → Redis 캐시 갱신이 **동일 트랜잭션**에서 처리됨

### DiscountTarget 생성 규칙

```java
// DiscountTarget 생성 시 issueType은 DiscountPolicy에서 자동 상속
public DiscountTarget(DiscountPolicy discountPolicy, long targetId) {
    this.issueType = discountPolicy.getDiscountDetails().getIssueType();
    this.targetId = targetId;
    this.activeYn = Yn.Y;  // 신규 생성 시 항상 활성
    this.discountPolicyId = discountPolicy.getId();
}
```

### Redis 캐시 갱신 규칙

- `activeYn = Y` → `DISCOUNT:{issueType}{targetId}` key에 `BaseDiscountInfo` 저장, TTL = 정책 종료일까지
- `activeYn = N` → 해당 key 삭제
- TTL 음수 방지 로직 없음 (policyEndDate가 과거인 경우 주의 필요)

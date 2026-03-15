# API Flow: DiscountController.createDiscountTargets

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP | POST /api/v1/discount/{discountPolicyId}/targets |
| Controller | DiscountController |
| Service Interface | DiscountTargetQueryService |
| Service Impl | DiscountTargetQueryServiceImpl |
| Repository (저장) | DiscountTargetRepository (JpaRepository) |
| Repository (조회) | DiscountPolicyFetchRepository → DiscountPolicyFetchRepositoryImpl |
| Cache | DiscountRedisQueryService → DiscountRedisQueryServiceImpl |
| 권한 | @PreAuthorize(HAS_AUTHORITY_MASTER) |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| discountPolicyId | long | 필수 | 할인 정책 ID |

### Request Body (CreateDiscountTarget)

| 필드 | 타입 | 필수 | Validation | 설명 |
|------|------|------|------------|------|
| issueType | IssueType (Enum) | 필수 | @NotNull | 발급 대상 타입 |
| targetIds | List\<Long\> | 필수 | @Size(min=1) | 타겟 ID 목록 (최소 1개) |

**IssueType Enum 값**
- `PRODUCT` - 상품 대상
- `SELLER` - 셀러 대상
- `BRAND` - 브랜드 대상

### Request JSON Example

```json
{
  "issueType": "PRODUCT",
  "targetIds": [1001, 1002, 1003]
}
```

---

## 3. Response

### Response Type

`ResponseEntity<ApiResponse<List<DiscountTarget>>>`

응답은 저장된 DiscountTarget 엔티티 목록을 그대로 반환합니다.

### DiscountTarget 필드

| 필드 | 타입 | 컬럼명 | 설명 |
|------|------|--------|------|
| id | long | discount_target_id | PK (auto increment) |
| issueType | IssueType | issue_type | 발급 타입 |
| activeYn | Yn | active_yn | 활성화 여부 (생성 시 Y로 고정) |
| targetId | long | target_id | 타겟 식별자 |
| discountPolicyId | long | discount_policy_id | 할인 정책 FK |

### Response JSON Example

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "issueType": "PRODUCT",
      "activeYn": "Y",
      "targetId": 1001,
      "discountPolicyId": 100
    },
    {
      "id": 2,
      "issueType": "PRODUCT",
      "activeYn": "Y",
      "targetId": 1002,
      "discountPolicyId": 100
    }
  ]
}
```

---

## 4. 호출 흐름

```
POST /api/v1/discount/{discountPolicyId}/targets
    │
    └── DiscountController.createDiscountTargets(discountPolicyId, createDiscountTarget)
            │
            └── DiscountTargetQueryService.createDiscountTargets(discountPolicyId, createDiscountTarget)
                    │  [DiscountTargetQueryServiceImpl - @Transactional]
                    │
                    ├── DiscountFetchService.fetchDiscountEntity(discountPolicyId)
                    │       │  [DiscountFetchServiceImpl - @Transactional(readOnly=true)]
                    │       └── DiscountPolicyFetchRepository.fetchDiscountEntity(discountPolicyId)
                    │               └── [QueryDSL] SELECT * FROM discount_policy WHERE discount_policy_id = ?
                    │                   → DiscountPolicy 엔티티 반환 (없으면 DiscountNotFoundException)
                    │
                    └── saveDiscountTargetsToCacheAndDb(discountPolicy, createDiscountTarget)
                            │
                            ├── createDiscountTarget.getTargetIds().stream()
                            │       .map(targetId -> new DiscountTarget(discountPolicy, targetId))
                            │       → DiscountTarget 생성:
                            │           issueType = discountPolicy.discountDetails.issueType
                            │           targetId  = targetId
                            │           activeYn  = Yn.Y
                            │           discountPolicyId = discountPolicy.id
                            │
                            ├── DiscountTargetRepository.saveAll(discountTargets)
                            │       └── [JPA] INSERT INTO discount_target (...) VALUES (...)
                            │           * cascade = ALL → DiscountTargetHistory는 이 흐름에서 미삽입
                            │             (setActiveYn 호출 없으므로 addHistory 미실행)
                            │
                            └── DiscountRedisQueryService.updateDiscountCache(discountPolicy, savedDiscountTargets)
                                    └── [Redis] 각 DiscountTarget 순회
                                            key = "DISCOUNT:{issueType}{targetId}"
                                            activeYn.isNo() → delete(key)
                                            activeYn.isYes() → save(key, json, ttl)
                                                ttl = policyEndDate - now
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | 조작 |
|--------|------|------|
| discount_policy | 할인 정책 조회 | SELECT |
| discount_target | 할인 타겟 저장 | INSERT |
| discount_target_history | 이력 저장 | INSERT (cascade, setActiveYn 시에만 발생) |

### QueryDSL - fetchDiscountEntity

```java
// DiscountPolicyFetchRepositoryImpl.fetchDiscountEntity()
getQueryFactory()
    .selectFrom(discountPolicy)                          // FROM discount_policy
    .where(discountPolicy.id.eq(discountPolicyId))       // WHERE discount_policy_id = ?
    .fetchOne()
```

**생성 SQL:**
```sql
SELECT *
FROM   discount_policy
WHERE  discount_policy_id = ?
```

### JPA - saveAll (DiscountTargetRepository)

JpaRepository.saveAll() 호출 → JPA 배치 INSERT

**생성 SQL (단건 예시):**
```sql
INSERT INTO discount_target (
    issue_type,
    active_yn,
    target_id,
    discount_policy_id,
    insert_date,
    update_date
) VALUES (
    'PRODUCT',
    'Y',
    1001,
    100,
    NOW(),
    NOW()
)
```

---

## 6. Redis 캐시 동작

| 동작 | 조건 | Key 패턴 | Value |
|------|------|----------|-------|
| SAVE | activeYn = Y | `DISCOUNT:{issueType}{targetId}` | BaseDiscountInfo JSON |
| DELETE | activeYn = N | `DISCOUNT:{issueType}{targetId}` | - |

**TTL 계산:**
```
TTL = discountPolicy.discountDetails.policyEndDate - LocalDateTime.now()
```

**캐시 저장 Value 구조 (BaseDiscountInfo):**

| 필드 | 타입 | 설명 |
|------|------|------|
| discountType | DiscountType | 할인 타입 |
| issueType | IssueType | 발급 타입 |
| discountLimitYn | Yn | 할인 한도 여부 |
| maxDiscountPrice | long | 최대 할인금액 |
| discountRatio | double | 할인율 |
| policyStartDate | LocalDateTime | 정책 시작일 |
| policyEndDate | LocalDateTime | 정책 종료일 |
| priority | int | 우선순위 |
| targetId | long | 타겟 ID |
| shareRatio | double | 공유 비율 |

---

## 7. 비즈니스 로직 요약

1. `discountPolicyId`로 `DiscountPolicy` 엔티티 조회 (없으면 404 예외)
2. `createDiscountTarget.targetIds` 목록을 순회하여 `DiscountTarget` 객체 생성
   - `issueType`은 요청 DTO의 값이 아닌 **조회된 DiscountPolicy의 issueType을 그대로 사용**
   - `activeYn`은 항상 `Y`로 고정
3. `DiscountTargetRepository.saveAll()`로 DB에 일괄 저장
4. 저장된 타겟 목록을 Redis에 캐시 저장 (키: `DISCOUNT:{issueType}{targetId}`, TTL: 정책 만료일까지)
5. 저장된 `DiscountTarget` 엔티티 목록 반환

> **주의사항:** `DiscountTarget` 생성자 `new DiscountTarget(discountPolicy, targetId)` 내부에서
> issueType은 `createDiscountTarget.getIssueType()`이 아닌 `discountPolicy.getDiscountDetails().getIssueType()`에서 가져옴.
> 즉 Request Body의 `issueType`은 실제 생성 시 사용되지 않는 데드 필드.

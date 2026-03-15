# API Flow: DiscountController.updateDiscountPolicy

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | PUT |
| Path | /api/v1/discount/{discountPolicyId} |
| Controller | DiscountController |
| 권한 | @PreAuthorize(HAS_AUTHORITY_MASTER) — MASTER 권한 필요 |
| Service | DiscountQueryService → DiscountQueryServiceImpl |
| Sub-Service | DiscountTargetQueryService → DiscountTargetQueryServiceImpl |
| Fetch-Service | DiscountFetchService → DiscountFetchServiceImpl |
| Repository | DiscountPolicyFetchRepository → DiscountPolicyFetchRepositoryImpl |
| Repository | DiscountTargetFetchRepository → DiscountTargetFetchRepositoryImpl |
| JPA Repository | DiscountPolicyRepository (JpaRepository) |
| JPA Repository | DiscountTargetRepository (JpaRepository) |
| Cache | DiscountRedisQueryService → DiscountRedisQueryServiceImpl (Redis) |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| discountPolicyId | long | 필수 | 수정할 할인 정책 ID |

### Request Body: UpdateDiscount

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| discountDetails | DiscountDetails | 필수 | 할인 정책 상세 정보 (Embedded 객체) |
| targetIds | List\<Long\> | 필수 | 할인 적용 대상 ID 목록 |

### DiscountDetails 구조

| 필드 | 타입 | DB 컬럼 | 필수 | Validation | 설명 |
|------|------|---------|------|-----------|------|
| discountPolicyName | String | DISCOUNT_POLICY_NAME | 필수 | length=50 | 할인 정책명 |
| discountType | DiscountType (Enum) | DISCOUNT_TYPE | 필수 | length=20 | 할인 유형 |
| publisherType | PublisherType (Enum) | PUBLISHER_TYPE | 필수 | length=20 | 발행자 유형 |
| issueType | IssueType (Enum) | ISSUE_TYPE | 필수 | length=50 | 발급 유형 (PRODUCT/SELLER) |
| discountLimitYn | Yn (Enum) | DISCOUNT_LIMIT_YN | 필수 | length=1 | 할인 한도 여부 (Y/N) |
| maxDiscountPrice | long | MAX_DISCOUNT_PRICE | 필수 | - | 최대 할인 금액 |
| shareYn | Yn (Enum) | SHARE_YN | 필수 | length=1 | 분담 여부 (Y/N) |
| shareRatio | double | SHARE_RATIO | 필수 | - | 분담 비율 |
| discountRatio | double | DISCOUNT_RATIO | 필수 | - | 할인 비율 |
| policyStartDate | LocalDateTime | POLICY_START_DATE | 필수 | format=yyyy-MM-dd HH:mm:ss | 정책 시작일 |
| policyEndDate | LocalDateTime | POLICY_END_DATE | 필수 | format=yyyy-MM-dd HH:mm:ss | 정책 종료일 |
| memo | String | MEMO | 선택 | length=100 | 메모 |
| priority | int | PRIORITY | 필수 | - | 우선순위 |
| activeYn | Yn (Enum) | ACTIVE_YN | 필수 | length=1 | 활성 여부 (Y/N) |

### JSON 예시

```json
{
  "discountDetails": {
    "discountPolicyName": "여름 특가 할인",
    "discountType": "RATIO",
    "publisherType": "SELLER",
    "issueType": "PRODUCT",
    "discountLimitYn": "Y",
    "maxDiscountPrice": 10000,
    "shareYn": "Y",
    "shareRatio": 0.5,
    "discountRatio": 0.15,
    "policyStartDate": "2024-07-01 00:00:00",
    "policyEndDate": "2024-07-31 23:59:59",
    "memo": "여름 시즌 할인 정책",
    "priority": 1,
    "activeYn": "Y"
  },
  "targetIds": [1001, 1002, 1003]
}
```

---

## 3. Response

### Response DTO: DiscountPolicyResponseDto

| 필드 | 타입 | 설명 |
|------|------|------|
| discountPolicyId | long | 할인 정책 ID |
| discountDetails | DiscountDetails | 할인 정책 상세 (Embedded 객체 전체 반환) |
| insertDate | LocalDateTime | 등록일시 (format: yyyy-MM-dd HH:mm:ss) |
| updateDate | LocalDateTime | 수정일시 (format: yyyy-MM-dd HH:mm:ss) |
| insertOperator | String | 등록자 |
| updateOperator | String | 수정자 |

### Response Wrapper

```
ResponseEntity<ApiResponse<DiscountPolicyResponseDto>>
```

### JSON 예시

```json
{
  "success": true,
  "data": {
    "discountPolicyId": 42,
    "discountDetails": {
      "discountPolicyName": "여름 특가 할인",
      "discountType": "RATIO",
      "publisherType": "SELLER",
      "issueType": "PRODUCT",
      "discountLimitYn": "Y",
      "maxDiscountPrice": 10000,
      "shareYn": "Y",
      "shareRatio": 0.5,
      "discountRatio": 0.15,
      "policyStartDate": "2024-07-01 00:00:00",
      "policyEndDate": "2024-07-31 23:59:59",
      "memo": "여름 시즌 할인 정책",
      "priority": 1,
      "activeYn": "Y"
    },
    "insertDate": "2024-06-01 10:00:00",
    "updateDate": "2024-06-15 14:30:00",
    "insertOperator": "admin",
    "updateOperator": "admin"
  }
}
```

---

## 4. 호출 흐름

```
DiscountController.updateDiscountPolicy(discountPolicyId, UpdateDiscount)
    └── DiscountQueryService.updateDiscount(discountPolicyId, updateDiscount)
            └── DiscountQueryServiceImpl.updateDiscount()
                    │
                    ├── [1] DiscountFetchService.fetchDiscountEntity(discountPolicyId)
                    │       └── DiscountFetchServiceImpl.fetchDiscountEntity()
                    │               └── DiscountPolicyFetchRepository.fetchDiscountEntity(discountPolicyId)
                    │                       └── DiscountPolicyFetchRepositoryImpl
                    │                               QueryDSL: SELECT * FROM discount_policy WHERE id = {discountPolicyId}
                    │                               → DiscountPolicy 엔티티 반환 (없으면 DiscountNotFoundException)
                    │
                    ├── [2] discountPolicy.setDiscountDetails(updateDiscount.getDiscountDetails())
                    │       → DiscountPolicy.setDiscountDetails()
                    │           → 변경 감지: isDiscountDetailsChanged() == true 이면 addHistory() 후 갱신
                    │           → addHistory(): DiscountPolicyHistory 생성 (CascadeType.ALL → 자동 persist)
                    │
                    └── [3] DiscountTargetQueryService.updateDiscountTargets(discountPolicyId, CreateDiscountTarget)
                                └── DiscountTargetQueryServiceImpl.updateDiscountTargets()
                                        │
                                        ├── [3-1] DiscountTargetFetchService.fetchDiscountTargetEntities(List<discountPolicyId>)
                                        │           └── DiscountTargetFetchRepositoryImpl.fetchDiscountTargetEntities()
                                        │                   QueryDSL: SELECT * FROM discount_target
                                        │                             WHERE discount_policy_id IN (...) AND active_yn = 'Y'
                                        │
                                        ├── [3-2] processUpdateDiscountTarget()
                                        │           기존 target 비교:
                                        │           - 신규 targetId → DiscountTarget 생성 (newDiscountTargets)
                                        │           - 기존 activeYn=N → setActiveYn(Y), history 추가 (updateDiscountTargets)
                                        │           - 입력 목록에 없는 기존 target → setActiveYn(N), history 추가
                                        │
                                        ├── [3-3] DiscountFetchService.fetchDiscountEntity(discountPolicyId) [재조회]
                                        │           └── DiscountPolicyFetchRepositoryImpl (동일 쿼리)
                                        │
                                        ├── [3-4] DiscountTargetRepository.saveAll(newDiscountTargets)
                                        │           → JPA: INSERT INTO discount_target (...) 배치 저장
                                        │
                                        └── [3-5] DiscountRedisQueryService.updateDiscountCache(discountPolicy, savedDiscountTargets)
                                                    └── DiscountRedisQueryServiceImpl.updateDiscountCache()
                                                        각 DiscountTarget 순회:
                                                        - activeYn=N → Redis DELETE(DISCOUNT:{issueType}{targetId})
                                                        - activeYn=Y → Redis SET(DISCOUNT:{issueType}{targetId}, discountInfo, TTL=정책종료일까지)
```

### 트랜잭션 경계

| 서비스 | 트랜잭션 |
|--------|---------|
| DiscountQueryServiceImpl | @Transactional (쓰기) |
| DiscountFetchServiceImpl | @Transactional(readOnly=true) |
| DiscountTargetQueryServiceImpl | @Transactional (쓰기) |
| DiscountTargetFetchServiceImpl | @Transactional(readOnly=true) |

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | 비고 |
|--------|------|------|
| discount_policy | 할인 정책 메인 테이블 | DiscountPolicy 엔티티 |
| discount_policy_history | 할인 정책 변경 이력 | setDiscountDetails() 시 자동 생성 (CascadeType.ALL) |
| discount_target | 할인 적용 대상 | DiscountTarget 엔티티 |
| discount_target_history | 할인 대상 변경 이력 | setActiveYn() 시 자동 생성 (CascadeType.ALL) |

### QueryDSL: [1] fetchDiscountEntity

```java
// DiscountPolicyFetchRepositoryImpl.fetchDiscountEntity()
queryFactory.selectFrom(discountPolicy)
    .where(discountPolicy.id.eq(discountPolicyId))
    .fetchOne()
```

SQL:
```sql
SELECT *
FROM discount_policy
WHERE discount_policy_id = :discountPolicyId
```

---

### QueryDSL: [3-1] fetchDiscountTargetEntities

```java
// DiscountTargetFetchRepositoryImpl.fetchDiscountTargetEntities()
queryFactory.selectFrom(discountTarget)
    .where(
        discountTarget.discountPolicyId.in(discountPolicyIds),
        discountTarget.activeYn.eq(Yn.Y)
    )
    .fetch();
```

SQL:
```sql
SELECT *
FROM discount_target
WHERE discount_policy_id IN (:discountPolicyIds)
  AND active_yn = 'Y'
```

---

### JPA: [3-4] DiscountTargetRepository.saveAll()

```sql
-- 신규 target 생성
INSERT INTO discount_target (issue_type, active_yn, target_id, discount_policy_id, ...)
VALUES (:issueType, 'Y', :targetId, :discountPolicyId, ...)

-- 기존 target 활성화/비활성화 (dirty checking)
UPDATE discount_target
SET active_yn = :activeYn
WHERE discount_target_id = :id
```

---

### Redis: [3-5] updateDiscountCache

| 연산 | 키 패턴 | 값 | TTL |
|------|---------|---|-----|
| SET | DISCOUNT:{issueType}{targetId} | BaseDiscountInfo JSON | policyEndDate까지 |
| DEL | DISCOUNT:{issueType}{targetId} | - | - (activeYn=N인 경우) |

---

## 6. 비즈니스 로직 요약

1. **할인 정책 변경 감지**: `DiscountDetails.equals()` 로 기존값과 비교. 변경된 경우만 이력(DiscountPolicyHistory) 생성 후 갱신.

2. **할인 대상 업데이트 (3-Way Merge)**:
   - 입력 targetIds 기준으로 기존 대상과 비교
   - 신규 → INSERT
   - 기존 비활성(N) → UPDATE activeYn=Y + 이력 생성
   - 입력에 없는 기존 대상 → UPDATE activeYn=N + 이력 생성

3. **Redis 캐시 동기화**: 활성 대상은 캐시 갱신, 비활성 대상은 캐시 삭제. TTL은 policyEndDate까지 자동 만료.

# API Flow: DiscountController.updateDiscountPolicyUsageStatus

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP | PATCH /api/v1/discounts |
| Controller | DiscountController |
| Service Interface | DiscountQueryService |
| Service Impl | DiscountQueryServiceImpl |
| 보조 Service 1 | DiscountFetchService → DiscountFetchServiceImpl |
| 보조 Service 2 | DiscountTargetQueryService → DiscountTargetQueryServiceImpl |
| 보조 Service 3 | DiscountTargetFetchService → DiscountTargetFetchServiceImpl |
| 보조 Service 4 | DiscountRedisQueryService → DiscountRedisQueryServiceImpl |
| Repository 1 | DiscountPolicyFetchRepository → DiscountPolicyFetchRepositoryImpl |
| Repository 2 | DiscountTargetFetchRepository → DiscountTargetFetchRepositoryImpl |
| Repository 3 | DiscountTargetRepository (JpaRepository) |
| 권한 | @PreAuthorize(HAS_AUTHORITY_MASTER) |

---

## 2. Request

### Parameters

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|-----------|------|
| discountPolicyIds | List\<Long\> | 선택 | 없음 | 상태 변경 대상 할인 정책 ID 목록 |
| activeYn | Yn (enum) | 선택 | 없음 | 활성화 여부 (Y / N) |

### Yn Enum 값

| 값 | 설명 |
|----|------|
| Y | 활성화 |
| N | 비활성화 |

### JSON Example

```json
{
  "discountPolicyIds": [1, 2, 3],
  "activeYn": "Y"
}
```

---

## 3. Response

### DTO 구조

`List<DiscountPolicyResponseDto>`

**DiscountPolicyResponseDto**

| 필드명 | 타입 | 설명 |
|--------|------|------|
| discountPolicyId | long | 할인 정책 ID |
| discountDetails | DiscountDetails | 할인 상세 정보 (embedded) |
| insertDate | LocalDateTime | 생성일시 |
| updateDate | LocalDateTime | 수정일시 |
| insertOperator | String | 생성자 |
| updateOperator | String | 수정자 |

**DiscountDetails (Embedded)**

| 필드명 | 타입 | 설명 |
|--------|------|------|
| discountPolicyName | String | 할인 정책명 |
| discountType | DiscountType (enum) | 할인 유형 |
| publisherType | PublisherType (enum) | 발행 주체 유형 |
| issueType | IssueType (enum) | 발행 대상 유형 (PRODUCT / SELLER) |
| discountLimitYn | Yn | 할인 한도 여부 |
| maxDiscountPrice | long | 최대 할인 금액 |
| shareYn | Yn | 공유 여부 |
| shareRatio | double | 공유 비율 |
| discountRatio | double | 할인율 |
| policyStartDate | LocalDateTime | 정책 시작일시 |
| policyEndDate | LocalDateTime | 정책 종료일시 |
| memo | String | 메모 (nullable) |
| priority | int | 우선순위 |
| activeYn | Yn | 활성화 여부 |

### JSON Example

```json
[
  {
    "discountPolicyId": 1,
    "discountDetails": {
      "discountPolicyName": "example_discount_policy",
      "discountType": "DISCOUNT_TYPE_VALUE",
      "publisherType": "PUBLISHER_TYPE_VALUE",
      "issueType": "PRODUCT",
      "discountLimitYn": "Y",
      "maxDiscountPrice": 10000,
      "shareYn": "N",
      "shareRatio": 0.0,
      "discountRatio": 10.0,
      "policyStartDate": "2024-01-01 00:00:00",
      "policyEndDate": "2024-12-31 23:59:59",
      "memo": null,
      "priority": 1,
      "activeYn": "Y"
    },
    "insertDate": "2024-01-01 00:00:00",
    "updateDate": "2024-01-01 00:00:00",
    "insertOperator": "admin",
    "updateOperator": "admin"
  }
]
```

---

## 4. 호출 흐름

```
DiscountController.updateDiscountPolicyUsageStatus(UpdateUseDiscount)
    └── DiscountQueryService.updateDiscountUseYn(UpdateUseDiscount)
            └── DiscountQueryServiceImpl.updateDiscountUseYn(UpdateUseDiscount)
                    │
                    ├── [1] DiscountFetchService.fetchDiscountEntities(List<Long> discountPolicyIds)
                    │       └── DiscountFetchServiceImpl.fetchDiscountEntities(...)
                    │               └── DiscountPolicyFetchRepository.fetchDiscountEntities(List<Long>)
                    │                       └── DiscountPolicyFetchRepositoryImpl
                    │                               QueryDSL: SELECT * FROM discount_policy
                    │                               WHERE discount_policy_id IN (...)
                    │
                    ├── [2] updateActiveYn(discountPolicies, Yn.Y or Yn.N)
                    │       └── discountPolicy.setActiveYn(yn)  [Dirty Checking]
                    │               └── DiscountPolicyHistory 이력 추가 후 activeYn 변경
                    │
                    └── [3] DiscountTargetQueryService.updateDiscountTargetUseYn(discountPolicies, activeYn)
                                └── DiscountTargetQueryServiceImpl.updateDiscountTargetUseYn(...)
                                        │
                                        ├── [3-1] DiscountTargetFetchService.fetchDiscountTargetEntities(discountPolicyIds)
                                        │           └── DiscountTargetFetchServiceImpl
                                        │                   └── DiscountTargetFetchRepository.fetchDiscountTargetEntities(List<Long>)
                                        │                           └── DiscountTargetFetchRepositoryImpl
                                        │                                   QueryDSL: SELECT * FROM discount_target
                                        │                                   WHERE discount_policy_id IN (...)
                                        │                                   AND active_yn = 'Y'
                                        │
                                        ├── [3-2] discountTarget.setActiveYn(activeYn)  [Dirty Checking]
                                        │           └── DiscountTargetHistory 이력 추가 후 activeYn 변경
                                        │
                                        └── [3-3] DiscountRedisQueryService.updateDiscountCache(discountPolicy, targets)
                                                    └── DiscountRedisQueryServiceImpl.updateDiscountCache(...)
                                                            activeYn == N → Redis key 삭제
                                                            activeYn == Y → Redis key 저장 (TTL = policyEndDate까지)
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | 조건 |
|--------|------|------|
| discount_policy | 할인 정책 메인 테이블 | discount_policy_id IN (...) |
| discount_target | 할인 대상 테이블 | discount_policy_id IN (...), active_yn = 'Y' |

### QueryDSL - fetchDiscountEntities

```java
getQueryFactory()
    .selectFrom(discountPolicy)
    .where(discountPolicy.id.in(discountPolicyIds))
    .fetch();
```

**SQL 등가**:
```sql
SELECT *
FROM discount_policy
WHERE discount_policy_id IN (:discountPolicyIds)
```

### QueryDSL - fetchDiscountTargetEntities

```java
queryFactory
    .selectFrom(discountTarget)
    .where(
        discountTarget.discountPolicyId.in(discountPolicyIds),
        discountTarget.activeYn.eq(Yn.Y)
    )
    .fetch();
```

**SQL 등가**:
```sql
SELECT *
FROM discount_target
WHERE discount_policy_id IN (:discountPolicyIds)
  AND active_yn = 'Y'
```

### Redis 연동

| 동작 | 조건 | 키 패턴 | TTL |
|------|------|---------|-----|
| DELETE | activeYn = N | `DISCOUNT:{issueType}{targetId}` | - |
| SAVE | activeYn = Y | `DISCOUNT:{issueType}{targetId}` | policyEndDate까지 |

---

## 6. 트랜잭션 경계

| 클래스 | 트랜잭션 설정 | 비고 |
|--------|-------------|------|
| DiscountQueryServiceImpl | @Transactional | 쓰기 트랜잭션 |
| DiscountFetchServiceImpl | @Transactional(readOnly = true) | 읽기 전용 |
| DiscountTargetQueryServiceImpl | @Transactional | 쓰기 트랜잭션 |
| DiscountTargetFetchServiceImpl | @Transactional(readOnly = true) | 읽기 전용 |

Dirty Checking 방식으로 DiscountPolicy.activeYn, DiscountTarget.activeYn 모두 변경됩니다.
각 엔티티 변경 시 자동으로 이력(DiscountPolicyHistory, DiscountTargetHistory)이 생성됩니다.

---

## 7. 특이사항

1. **이력 자동 생성**: `setActiveYn()` 호출 시 `addHistory()`가 먼저 실행되어 변경 이력을 남긴다.
2. **Redis 동기화**: DB 상태 변경 후 Redis 캐시도 즉시 갱신된다. activeYn=N이면 캐시 삭제, activeYn=Y이면 새로운 할인 정보로 캐시 갱신.
3. **배치 처리**: 여러 discountPolicyId를 한 번에 처리하며, 각 정책에 연결된 모든 타겟에 동일한 activeYn이 적용된다.
4. **Request Validation 없음**: `UpdateUseDiscount`에 @Valid / @Validated 어노테이션이 없어 입력값 검증이 없다.

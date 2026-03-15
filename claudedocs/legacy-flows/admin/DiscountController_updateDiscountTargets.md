# API Flow: DiscountController.updateDiscountTargets

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | PUT |
| Path | /api/v1/discount/{discountPolicyId}/targets |
| Controller | DiscountController |
| Service Interface | DiscountTargetQueryService |
| Service Impl | DiscountTargetQueryServiceImpl |
| Repository (JPA) | DiscountTargetRepository (JpaRepository) |
| Repository (QueryDSL) | DiscountTargetFetchRepository → DiscountTargetFetchRepositoryImpl |
| FetchService | DiscountTargetFetchService → DiscountTargetFetchServiceImpl |
| FetchService (Policy) | DiscountFetchService → DiscountFetchServiceImpl |
| Redis Service | DiscountRedisQueryService → DiscountRedisQueryServiceImpl |
| 인증 | @PreAuthorize(HAS_AUTHORITY_MASTER) |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| discountPolicyId | long | Y | 할인 정책 ID |

### Request Body: `CreateDiscountTarget`

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| issueType | IssueType (Enum) | Y | @NotNull | 발급 대상 유형 |
| targetIds | List\<Long\> | Y | @Size(min=1) | 대상 ID 목록 (상품그룹 ID 또는 셀러 ID) |

**IssueType Enum 값**: `PRODUCT`, `SELLER`, `BRAND`

> 참고: `issueType` 필드는 Request Body에서 받지만, `updateDiscountTargets` 로직 내부에서는 직접 사용되지 않습니다.
> 실제 `issueType`은 `DiscountPolicy` 엔티티의 `discountDetails.issueType` 에서 가져옵니다.

### JSON Example

```json
{
  "issueType": "PRODUCT",
  "targetIds": [101, 202, 303]
}
```

---

## 3. Response

### 응답 타입

`ResponseEntity<ApiResponse<List<DiscountTarget>>>`

응답 본문은 `DiscountTarget` 엔티티 목록입니다.

### DiscountTarget 필드

| 필드명 | 타입 | JSON 직렬화 | 설명 |
|--------|------|-------------|------|
| id | long | O | PK (discount_target_id) |
| issueType | IssueType | O | 발급 대상 유형 |
| activeYn | Yn | O | 활성 여부 (Y/N) |
| targetId | long | O | 대상 ID |
| discountPolicyId | long | O | 할인 정책 ID |
| insertDate | LocalDateTime | O (NON_NULL) | 생성일시 |
| updateDate | LocalDateTime | O (NON_NULL) | 수정일시 |
| insertOperator | String | O (NON_NULL) | 생성자 |
| updateOperator | String | O (NON_NULL) | 수정자 |
| deleteYn | Yn | X (@JsonIgnore) | 삭제 여부 |
| histories | Set\<DiscountTargetHistory\> | O | 이력 목록 |

### JSON Example

```json
{
  "status": "SUCCESS",
  "data": [
    {
      "id": 1,
      "issueType": "PRODUCT",
      "activeYn": "Y",
      "targetId": 101,
      "discountPolicyId": 10,
      "insertDate": "2024-01-01 00:00:00",
      "updateDate": "2024-01-01 00:00:00",
      "insertOperator": "admin",
      "updateOperator": "admin",
      "histories": []
    },
    {
      "id": 2,
      "issueType": "PRODUCT",
      "activeYn": "N",
      "targetId": 999,
      "discountPolicyId": 10,
      "insertDate": "2024-01-01 00:00:00",
      "updateDate": "2024-01-01 12:00:00",
      "insertOperator": "admin",
      "updateOperator": "admin",
      "histories": [
        {
          "discountTargetHistoryId": 1
        }
      ]
    }
  ]
}
```

---

## 4. 호출 흐름

```
PUT /api/v1/discount/{discountPolicyId}/targets
    │
    ▼
DiscountController.updateDiscountTargets(discountPolicyId, createDiscountTarget)
    │
    ▼
DiscountTargetQueryService.updateDiscountTargets(discountPolicyId, createDiscountTarget)
    │   (DiscountTargetQueryServiceImpl)
    │
    ├─[1] DiscountTargetFetchService.fetchDiscountTargetEntities([discountPolicyId])
    │         │  (DiscountTargetFetchServiceImpl)
    │         ▼
    │     DiscountTargetFetchRepository.fetchDiscountTargetEntities([discountPolicyId])
    │         │  (DiscountTargetFetchRepositoryImpl - QueryDSL)
    │         ▼
    │     SELECT * FROM discount_target
    │     WHERE discount_policy_id IN (?) AND active_yn = 'Y'
    │
    ├─[2] processUpdateDiscountTarget(discountPolicyId, discountTargets, targetIds)
    │         │
    │         ├─[2-1] DiscountFetchService.fetchDiscountEntity(discountPolicyId)
    │         │           │  (DiscountFetchServiceImpl)
    │         │           ▼
    │         │       DiscountPolicyFetchRepository.fetchDiscountEntity(discountPolicyId)
    │         │           → SELECT * FROM discount_policy WHERE discount_policy_id = ?
    │         │
    │         ├─[2-2] 기존 타겟 vs 요청 targetIds 비교
    │         │       - 신규 targetId: new DiscountTarget(discountPolicy, targetId) 생성
    │         │       - 기존 비활성 targetId: activeYn = Y로 갱신
    │         │       - 요청에 없는 기존 타겟: activeYn = N으로 비활성화
    │         │       - activeYn 변경 시 addHistory() 호출 → DiscountTargetHistory 생성
    │         │
    │         ├─[2-3] DiscountTargetRepository.saveAll(newDiscountTargets)
    │         │       (JpaRepository - INSERT batch)
    │         │
    │         └─[2-4] updateDiscountTargets 에 updateDiscountTargets 추가 후 반환
    │
    └─[3] DiscountRedisQueryService.updateDiscountCache(discountPolicy, savedDiscountTargets)
              │  (DiscountRedisQueryServiceImpl)
              ▼
          savedDiscountTargets 순회:
          - activeYn = N → Redis DELETE (key: DISCOUNT:{issueType}{targetId})
          - activeYn = Y → Redis SAVE  (key: DISCOUNT:{issueType}{targetId}, TTL: 정책 종료일까지)
```

---

## 5. 핵심 비즈니스 로직: processUpdateDiscountTarget

```
입력: discountPolicyId, 기존 DiscountTarget 목록, 요청 targetIds 목록

1. 기존 타겟을 Map<targetId, DiscountTarget>으로 변환

2. 요청 targetIds 순회:
   ┌─ targetId가 Map에 없음 → 신규 DiscountTarget 생성 (newDiscountTargets 추가)
   ├─ targetId가 Map에 있고 activeYn = N → activeYn = Y 설정 (updateDiscountTargets 추가)
   └─ targetId가 Map에 있고 activeYn = Y → 변경 없음, Map에서 제거

3. Map에 남은 기존 타겟 (요청에 없는 타겟):
   → activeYn = N 설정 (updateDiscountTargets 추가)

4. newDiscountTargets.saveAll() → DB INSERT

5. savedDiscountTargets = 신규 저장분 + 상태 변경분

6. Redis 캐시 업데이트
```

---

## 6. Database Query

### 관련 테이블

| 테이블 | 용도 | 조건 |
|--------|------|------|
| discount_target | 할인 대상 조회/저장 | discount_policy_id IN (?), active_yn = 'Y' |
| discount_policy | 할인 정책 조회 | discount_policy_id = ? |
| discount_target_history | 상태 변경 이력 자동 생성 | cascade (엔티티 내부) |

### QueryDSL: fetchDiscountTargetEntities

```java
queryFactory.selectFrom(discountTarget)
    .where(
        discountTarget.discountPolicyId.in(discountPolicyIds),  // discount_policy_id IN (?)
        discountTarget.activeYn.eq(Yn.Y)                        // active_yn = 'Y'
    )
    .fetch();
```

**SQL (의사 코드)**:
```sql
SELECT *
FROM discount_target
WHERE discount_policy_id IN (?)
  AND active_yn = 'Y'
```

### JPA: DiscountTargetRepository.saveAll

```java
// 신규 타겟 INSERT
discountTargetRepository.saveAll(newDiscountTargets);
// → INSERT INTO discount_target (issue_type, active_yn, target_id, discount_policy_id, ...) VALUES (...)

// 상태 변경 타겟은 @Transactional Dirty Checking으로 자동 UPDATE
// → UPDATE discount_target SET active_yn = ?, update_date = ? WHERE discount_target_id = ?
```

### Redis 캐시 구조

| 항목 | 내용 |
|------|------|
| Key 형식 | `DISCOUNT:{issueType}{targetId}` (예: `DISCOUNT:PRODUCT101`) |
| Value | BaseDiscountInfo JSON 직렬화 |
| TTL | 현재 시각 ~ 정책 종료일(policyEndDate) 사이의 Duration |
| 비활성화 시 | key 삭제 (DELETE) |
| 활성화 시 | key 저장/갱신 (SET with TTL) |

---

## 7. 특이사항 및 주의점

1. **응답이 Entity 직접 반환**: `List<DiscountTarget>` 엔티티를 그대로 응답으로 반환합니다. `@JsonIgnore`가 `deleteYn`에만 적용되어 있어 민감한 필드가 노출될 수 있습니다.

2. **Upsert 방식**: PUT 요청이지만 실질적으로 기존 타겟 목록과 신규 요청을 비교하는 Upsert 로직입니다. 요청에 포함되지 않은 기존 활성 타겟은 `active_yn = 'N'`으로 비활성화됩니다.

3. **이력 관리**: `activeYn` 변경 시 `addHistory()`를 통해 `DiscountTargetHistory`가 자동으로 생성됩니다 (cascade).

4. **트랜잭션**: `@Transactional`이 ServiceImpl 클래스 레벨에 선언되어 있어, DB 저장과 상태 변경이 하나의 트랜잭션으로 처리됩니다. Redis 캐시 업데이트는 트랜잭션 외부 효과입니다.

5. **Request Body의 issueType 미사용**: `CreateDiscountTarget.issueType` 필드는 `updateDiscountTargets` 메서드 내부에서 직접 참조되지 않으며, `DiscountPolicy.discountDetails.issueType`이 실제 값으로 사용됩니다.

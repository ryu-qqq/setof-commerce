# API Flow: DiscountController.getDiscountHistories

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/discounts/history` |
| Controller | `DiscountController` |
| Service | `DiscountHistoryFetchService` → `DiscountHistoryFetchServiceImpl` |
| Repository | `DiscountPolicyHistoryFetchRepository` → `DiscountPolicyHistoryFetchRepositoryImpl` |
| 보안 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

---

## 2. Request

### Parameters

`@ModelAttribute @Validated DiscountFilter` + `Pageable`

`DiscountFilter extends SearchAndDateFilter` 구조:

| 이름 | 타입 | 필수 | Validation | 출처 |
|------|------|------|------------|------|
| startDate | LocalDateTime | 조건부 | `@ValidDateRange` (startDate-endDate 범위 검증) | SearchAndDateFilter |
| endDate | LocalDateTime | 조건부 | `@ValidDateRange` | SearchAndDateFilter |
| searchKeyword | SearchKeyword | 아니오 | - | SearchAndDateFilter |
| searchWord | String | 아니오 | - | SearchAndDateFilter |
| discountPolicyId | Long | 아니오 | - | DiscountFilter |
| periodType | PeriodType | 아니오 | - | DiscountFilter |
| activeYn | Yn | 아니오 | - | DiscountFilter |
| publisherType | PublisherType | 아니오 | - | DiscountFilter |
| issueType | IssueType | 아니오 | - | DiscountFilter |
| userId | Long | 아니오 | - | DiscountFilter |
| page | int | 아니오 | - | Pageable |
| size | int | 아니오 | - | Pageable |
| sort | String | 아니오 | - | Pageable |

### Enum 값

| Enum | 허용 값 |
|------|--------|
| PeriodType | `DISPLAY`, `INSERT`, `PAYMENT`, `POLICY`, `SETTLEMENT`, `ORDER_HISTORY` |
| Yn | `Y`, `N` |
| PublisherType | `ADMIN`, `SELLER` |
| IssueType | `PRODUCT`, `SELLER`, `BRAND` |
| SearchKeyword | `PRODUCT_GROUP_NAME`, `PRODUCT_GROUP_ID`, `INSERT_OPERATOR`, `UPDATE_OPERATOR`, `POLICY_NAME`, `POLICY_ID`, `DISCOUNT_TYPE`, ... |

### Query String 예시

```
GET /api/v1/discounts/history?startDate=2024-01-01 00:00:00&endDate=2024-12-31 23:59:59&activeYn=Y&publisherType=ADMIN&issueType=PRODUCT&page=0&size=20&sort=id,desc
```

---

## 3. Response

### DTO 구조

```
ApiResponse<Page<DiscountPolicyResponseDto>>
  └── DiscountPolicyResponseDto
        ├── discountPolicyId: long
        ├── discountDetails: DiscountDetails (Embedded)
        │     ├── discountPolicyName: String
        │     ├── discountType: DiscountType  [RATE, PRICE]
        │     ├── publisherType: PublisherType  [ADMIN, SELLER]
        │     ├── issueType: IssueType  [PRODUCT, SELLER, BRAND]
        │     ├── discountLimitYn: Yn  [Y, N]
        │     ├── maxDiscountPrice: long
        │     ├── shareYn: Yn  [Y, N]
        │     ├── shareRatio: double
        │     ├── discountRatio: double
        │     ├── policyStartDate: LocalDateTime
        │     ├── policyEndDate: LocalDateTime
        │     ├── memo: String (nullable)
        │     ├── priority: int
        │     └── activeYn: Yn  [Y, N]
        ├── insertDate: LocalDateTime
        ├── updateDate: LocalDateTime
        ├── insertOperator: String
        └── updateOperator: String
```

### JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "discountPolicyId": 1,
        "discountDetails": {
          "discountPolicyName": "여름 시즌 할인",
          "discountType": "RATE",
          "publisherType": "ADMIN",
          "issueType": "PRODUCT",
          "discountLimitYn": "Y",
          "maxDiscountPrice": 50000,
          "shareYn": "N",
          "shareRatio": 0.0,
          "discountRatio": 0.2,
          "policyStartDate": "2024-06-01 00:00:00",
          "policyEndDate": "2024-08-31 23:59:59",
          "memo": "여름 시즌 20% 할인",
          "priority": 1,
          "activeYn": "N"
        },
        "insertDate": "2024-05-01 10:00:00",
        "updateDate": "2024-09-01 00:00:00",
        "insertOperator": "admin",
        "updateOperator": "system"
      }
    ],
    "totalElements": 25,
    "totalPages": 2,
    "number": 0,
    "size": 20
  }
}
```

---

## 4. 호출 흐름

```
DiscountController.getDiscountHistories(DiscountFilter, Pageable)
  @GetMapping("/discounts/history")
  @PreAuthorize(HAS_AUTHORITY_MASTER)
    │
    └── DiscountHistoryFetchService.fetchDiscountPolicyHistories(filter, pageable)
          │
          └── DiscountHistoryFetchServiceImpl.fetchDiscountPolicyHistories()
                @Transactional(readOnly = true)
                │
                ├── DiscountPolicyHistoryFetchRepository.fetchDiscountPolicyHistories(filter, pageable)
                │     └── DiscountPolicyHistoryFetchRepositoryImpl
                │           QueryDSL: discount_use_history → JOIN discount_policy → JOIN discount_target
                │           transform(groupBy(discountPolicy.id).list(QDiscountPolicyResponseDto))
                │
                ├── DiscountPolicyHistoryFetchRepository.fetchDiscountPolicyCountQuery(filter)
                │     └── DiscountPolicyHistoryFetchRepositoryImpl
                │           QueryDSL: COUNT(discount_use_history)
                │           → JPAQuery<Long>.fetchOne()
                │
                └── PageableExecutionUtils.getPage(results, pageable, countSupplier)
                      → Page<DiscountPolicyResponseDto> 반환
```

---

## 5. Database Query

### 테이블

| 테이블 | 조인 유형 | 조인 조건 |
|--------|----------|----------|
| `discount_use_history` | FROM (기준) | - |
| `discount_policy` | JOIN | `discount_policy.discount_policy_id = discount_use_history.discount_policy_id` |
| `discount_target` | JOIN | `discount_target.discount_policy_id = discount_policy.discount_policy_id` |

### QueryDSL - 목록 조회

```java
getQueryFactory()
    .from(discountUseHistory)
    .join(discountPolicy).on(discountPolicy.id.eq(discountUseHistory.discountPolicyId))
    .join(discountTarget).on(discountTarget.discountPolicyId.eq(discountPolicy.id))
    .where(
        discountUseHistoryHasPolicyId(filter.getDiscountPolicyId()),  // optional: discountUseHistory.discountPolicyId = ?
        periodTypeEq(filter.getStartDate(), filter.getEndDate()),      // discountUseHistory.insertDate BETWEEN ? AND ?
        policyActiveYn(filter.getActiveYn()),                          // optional: discountDetails.activeYn = ?
        publisherTypeEq(filter.getPublisherType()),                    // optional: discountDetails.publisherType = ?
        issueTypeEq(filter.getIssueType())                             // optional: discountDetails.issueType = ?
    )
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .orderBy(ORDERS.toArray(OrderSpecifier[]::new))  // 기본 정렬: discountPolicy.id DESC
    .distinct()
    .transform(
        groupBy(discountPolicy.id).list(
            new QDiscountPolicyResponseDto(
                discountPolicy.id,
                discountPolicy.discountDetails,
                discountPolicy.insertDate,
                discountPolicy.updateDate,
                discountPolicy.insertOperator,
                discountPolicy.updateOperator
            )
        )
    );
```

### QueryDSL - 카운트 쿼리

```java
getQueryFactory()
    .select(discountUseHistory.count())
    .from(discountUseHistory)
    .join(discountPolicy).on(discountPolicy.id.eq(discountUseHistory.discountPolicyId))
    .join(discountTarget).on(discountTarget.discountPolicyId.eq(discountPolicy.id))
    .join(users).on(users.id.eq(discountUseHistory.userId))
    .where(
        periodTypeEq(filter.getStartDate(), filter.getEndDate()),
        policyActiveYn(filter.getActiveYn()),
        publisherTypeEq(filter.getPublisherType()),
        issueTypeEq(filter.getIssueType())
    )
    .distinct();
```

### WHERE 조건 상세

| 조건 메서드 | 적용 컬럼 | 동작 |
|------------|----------|------|
| `discountUseHistoryHasPolicyId` | `discount_use_history.discount_policy_id` | null 이면 조건 미적용 |
| `periodTypeEq` | `discount_use_history.insert_date` | BETWEEN startDate AND endDate (필수) |
| `policyActiveYn` | `discount_policy.ACTIVE_YN` (DiscountDetails 임베디드) | null 이면 조건 미적용 |
| `publisherTypeEq` | `discount_policy.PUBLISHER_TYPE` (DiscountDetails 임베디드) | null 이면 조건 미적용 |
| `issueTypeEq` | `discount_policy.ISSUE_TYPE` (DiscountDetails 임베디드) | null 이면 조건 미적용 |

### 주요 특징

- `discount_use_history`를 기준으로 조회하므로 **사용 이력이 존재하는 할인 정책**만 결과에 포함됨
- `groupBy(discountPolicy.id)` + `distinct()`로 동일 정책의 중복 제거
- 목록 쿼리와 카운트 쿼리의 WHERE 조건이 일부 다름: 카운트 쿼리에 `discountUseHistoryHasPolicyId` 없음, 대신 `users` 조인 추가
- 기본 정렬: `discountPolicy.id DESC` (정렬 조건 없을 경우 fallback)
- `periodTypeEq`는 null 체크 없이 항상 BETWEEN 조건 적용됨 (startDate/endDate 필수)

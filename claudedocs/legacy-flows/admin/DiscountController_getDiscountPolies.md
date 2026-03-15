# API Flow: DiscountController.getDiscountPolies

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP | GET /api/v1/discounts |
| Controller | `DiscountController` |
| Service Interface | `DiscountFetchService` |
| Service Impl | `DiscountFetchServiceImpl` |
| Repository Interface | `DiscountPolicyFetchRepository` |
| Repository Impl | `DiscountPolicyFetchRepositoryImpl` |
| 응답 타입 | `ResponseEntity<ApiResponse<CustomPageable<DiscountPolicyResponseDto>>>` |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` - MASTER 권한 필요 |

---

## 2. Request

### Parameters (Query String - @ModelAttribute @Validated)

| 이름 | 타입 | 필수 | 설명 | 비고 |
|------|------|------|------|------|
| startDate | LocalDateTime | 조건부 | 검색 시작일 | `yyyy-MM-dd HH:mm:ss` 포맷 |
| endDate | LocalDateTime | 조건부 | 검색 종료일 | `yyyy-MM-dd HH:mm:ss` 포맷 |
| searchKeyword | SearchKeyword | 선택 | 검색 키워드 유형 | `INSERT_OPERATOR`, `UPDATE_OPERATOR`, `POLICY_NAME`, `POLICY_ID`, `DISCOUNT_TYPE` 등 |
| searchWord | String | 선택 | 검색어 | |
| discountPolicyId | Long | 선택 | 할인 정책 ID | |
| periodType | PeriodType | 선택 | 기간 유형 | `POLICY` (정책 적용 기간), 그 외는 등록일 기준 |
| activeYn | Yn | 선택 | 활성화 여부 | `Y` / `N` |
| publisherType | PublisherType | 선택 | 발행 주체 | `ADMIN` / `SELLER` |
| issueType | IssueType | 선택 | 발급 대상 유형 | `PRODUCT` / `SELLER` / `BRAND` |
| userId | Long | 선택 | 사용자 ID | 쿼리 조건으로 미사용 |
| page | int | 선택 | 페이지 번호 (0-based) | Spring Pageable |
| size | int | 선택 | 페이지 크기 | Spring Pageable |
| sort | String | 선택 | 정렬 기준 | Spring Pageable (예: `id,desc`) |

**상속 구조**:
```
DiscountFilter
    extends SearchAndDateFilter  (@ValidDateRange - startDate/endDate 쌍 검증)
        implements DateRangeFilter, SearchFilter
```

**Enum 값 목록**:

| Enum | 가능한 값 |
|------|----------|
| PeriodType | `DISPLAY`, `INSERT`, `PAYMENT`, `POLICY`, `SETTLEMENT`, `ORDER_HISTORY` |
| Yn | `Y`, `N` |
| PublisherType | `ADMIN`, `SELLER` |
| IssueType | `PRODUCT`, `SELLER`, `BRAND` |
| SearchKeyword | `INSERT_OPERATOR`, `UPDATE_OPERATOR`, `POLICY_NAME`, `POLICY_ID`, `DISCOUNT_TYPE` 등 |

### Query String Example

```
GET /api/v1/discounts?startDate=2024-01-01 00:00:00&endDate=2024-12-31 23:59:59&periodType=INSERT&activeYn=Y&publisherType=ADMIN&issueType=PRODUCT&page=0&size=20&sort=id,desc
```

---

## 3. Response

### DTO 구조

```
CustomPageable<DiscountPolicyResponseDto>
├── content: List<DiscountPolicyResponseDto>
│   ├── discountPolicyId: long
│   ├── discountDetails: DiscountDetails (Embedded - @Embeddable)
│   │   ├── discountPolicyName: String          (DISCOUNT_POLICY_NAME)
│   │   ├── discountType: DiscountType          (DISCOUNT_TYPE) - RATE | PRICE
│   │   ├── publisherType: PublisherType        (PUBLISHER_TYPE) - ADMIN | SELLER
│   │   ├── issueType: IssueType                (ISSUE_TYPE) - PRODUCT | SELLER | BRAND
│   │   ├── discountLimitYn: Yn                 (DISCOUNT_LIMIT_YN) - Y | N
│   │   ├── maxDiscountPrice: long              (MAX_DISCOUNT_PRICE)
│   │   ├── shareYn: Yn                         (SHARE_YN) - Y | N
│   │   ├── shareRatio: double                  (SHARE_RATIO)
│   │   ├── discountRatio: double               (DISCOUNT_RATIO)
│   │   ├── policyStartDate: LocalDateTime      (POLICY_START_DATE, yyyy-MM-dd HH:mm:ss)
│   │   ├── policyEndDate: LocalDateTime        (POLICY_END_DATE, yyyy-MM-dd HH:mm:ss)
│   │   ├── memo: String (nullable)             (MEMO)
│   │   ├── priority: int                       (PRIORITY)
│   │   └── activeYn: Yn                        (ACTIVE_YN) - Y | N
│   ├── insertDate: LocalDateTime               (yyyy-MM-dd HH:mm:ss)
│   ├── updateDate: LocalDateTime               (yyyy-MM-dd HH:mm:ss)
│   ├── insertOperator: String
│   └── updateOperator: String
├── pageable: Pageable
├── totalElements: long
└── lastDomainId: Long
```

### JSON Example

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "discountPolicyId": 1,
        "discountDetails": {
          "discountPolicyName": "신규 회원 할인 정책",
          "discountType": "RATE",
          "publisherType": "ADMIN",
          "issueType": "PRODUCT",
          "discountLimitYn": "Y",
          "maxDiscountPrice": 10000,
          "shareYn": "N",
          "shareRatio": 0.0,
          "discountRatio": 0.1,
          "policyStartDate": "2024-01-01 00:00:00",
          "policyEndDate": "2024-12-31 23:59:59",
          "memo": "신규 회원 전용 할인",
          "priority": 1,
          "activeYn": "Y"
        },
        "insertDate": "2024-01-01 00:00:00",
        "updateDate": "2024-01-15 10:00:00",
        "insertOperator": "admin",
        "updateOperator": "admin"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": { "sorted": true, "unsorted": false }
    },
    "totalElements": 100,
    "lastDomainId": 1,
    "totalPages": 5,
    "numberOfElements": 20,
    "first": true,
    "last": false
  }
}
```

---

## 4. 호출 흐름

```
DiscountController.getDiscountPolies(DiscountFilter filter, Pageable pageable)
    [GET /api/v1/discounts]
    │
    └── DiscountFetchService.fetchDiscountPolicies(filter, pageable)
            │
            └── DiscountFetchServiceImpl.fetchDiscountPolicies(filter, pageable)
                    [@Transactional(readOnly = true)]
                    │
                    ├── (1) DiscountPolicyFetchRepository.fetchDiscountPolicies(filter, pageable)
                    │       └── DiscountPolicyFetchRepositoryImpl.fetchDiscounts(filter, pageable)
                    │               QueryDSL: SELECT QDiscountPolicyResponseDto
                    │               FROM discount_policy
                    │               JOIN discount_policy (버그: discountTarget 이어야 함)
                    │               LEFT JOIN product_group
                    │               LEFT JOIN seller
                    │               WHERE 조건들
                    │               ORDER BY id DESC (기본값)
                    │
                    ├── (2) DiscountPolicyFetchRepository.fetchDiscountPolicyCountQuery(filter)
                    │       └── DiscountPolicyFetchRepositoryImpl.fetchDiscountPolicyCountQuery(filter)
                    │               QueryDSL: SELECT COUNT(discountPolicy) DISTINCT
                    │               FROM discount_policy
                    │               JOIN discount_target
                    │               LEFT JOIN product_group
                    │               LEFT JOIN seller
                    │               WHERE 조건들 (searchKeyword 조건 누락)
                    │       └── JPAQuery<Long>.fetchOne() → totalCount
                    │
                    └── (3) DiscountPolicyPageableMapper.toProductCategoryContext(responses, pageable, total)
                                └── new CustomPageable<>(content, pageable, total, lastDomainId) 반환
```

---

## 5. Database Query

### 조회 테이블

| 테이블 | Entity (Q클래스) | JOIN 유형 | JOIN 조건 |
|--------|-----------------|----------|----------|
| discount_policy | QDiscountPolicy | FROM | - |
| discount_target | QDiscountTarget | INNER JOIN (카운트) / 누락 (목록) | `discount_target.DISCOUNT_POLICY_ID = discount_policy.ID` |
| product_group | QProductGroup | LEFT JOIN | `product_group.ID = discount_target.TARGET_ID` |
| seller | QSeller | LEFT JOIN | `seller.ID = discount_target.TARGET_ID` |

### QueryDSL - 목록 조회 (fetchDiscounts)

```java
List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable, discountPolicy);
// ORDERS 비어있으면: discountPolicy.id.desc() 추가

getQueryFactory()
    .select(
        new QDiscountPolicyResponseDto(
            discountPolicy.id,
            discountPolicy.discountDetails,
            discountPolicy.insertDate,
            discountPolicy.updateDate,
            discountPolicy.insertOperator,
            discountPolicy.updateOperator
        )
    )
    .from(discountPolicy)
    .join(discountPolicy)                                      // [버그] discountTarget 이어야 함
    .on(discountTarget.discountPolicyId.eq(discountPolicy.id))
    .leftJoin(productGroup).on(productGroup.id.eq(discountTarget.targetId))
    .leftJoin(seller).on(seller.id.eq(discountTarget.targetId))
    .where(
        periodTypeEq(filter.getPeriodType(), filter.getStartDate(), filter.getEndDate()),
        policyActiveYn(filter.getActiveYn()),
        publisherTypeEq(filter.getPublisherType()),
        issueTypeEq(filter.getIssueType()),
        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord())
    )
    .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
    .fetch()
```

### QueryDSL - 카운트 쿼리 (fetchDiscountPolicyCountQuery)

```java
getQueryFactory()
    .select(discountPolicy.count())
    .from(discountPolicy)
    .join(discountTarget)
    .on(discountTarget.discountPolicyId.eq(discountPolicy.id))
    .leftJoin(productGroup).on(productGroup.id.eq(discountTarget.targetId))
    .leftJoin(seller).on(seller.id.eq(discountTarget.targetId))
    .where(
        periodTypeEq(filter.getPeriodType(), filter.getStartDate(), filter.getEndDate()),
        policyActiveYn(filter.getActiveYn()),
        publisherTypeEq(filter.getPublisherType()),
        issueTypeEq(filter.getIssueType())
        // [누락] searchKeywordEq 없음
    )
    .distinct()
```

### WHERE 조건 상세

| 조건 메서드 | 적용 파라미터 | SQL 조건 | 목록 | 카운트 |
|-------------|-------------|---------|------|--------|
| `periodTypeEq` | periodType=POLICY | `POLICY_START_DATE < endDate AND POLICY_END_DATE > startDate` | O | O |
| `periodTypeEq` | periodType=null 또는 POLICY 외 | `INSERT_DATE BETWEEN startDate AND endDate` | O | O |
| `policyActiveYn` | activeYn | `ACTIVE_YN = 'Y'` 또는 `'N'` | O | O |
| `publisherTypeEq` | publisherType | `PUBLISHER_TYPE = 'ADMIN'` 또는 `'SELLER'` | O | O |
| `issueTypeEq` | issueType | `ISSUE_TYPE = 'PRODUCT'` 등 | O | O |
| `searchKeywordEq` | searchKeyword, searchWord | AbstractCommonRepository 전략 패턴 | O | X (누락) |

---

## 6. 코드 품질 이슈

### 이슈 1: fetchDiscounts 내 self-join (버그)

`DiscountPolicyFetchRepositoryImpl.java` 179라인:

```java
.from(discountPolicy)
.join(discountPolicy)   // 동일 엔티티를 self-join - discountTarget 이어야 함
.on(discountTarget.discountPolicyId.eq(discountPolicy.id))
```

카운트 쿼리에서는 정상적으로 `discountTarget` 을 join하나, 목록 조회에서는 self-join으로 인해 `discountTarget` join이 누락됨.

### 이슈 2: 카운트 쿼리 WHERE 불일치

목록 조회에는 `searchKeywordEq` 조건이 포함되지만 카운트 쿼리에는 포함되지 않아 `totalElements` 값이 실제 결과 수와 다를 수 있음.

### 이슈 3: periodType 미전달 시 NPE 가능성

`startDate`, `endDate` 가 null 이면서 `periodType` 도 null 인 경우 `periodTypeEq` 에서 `insertDate.between(null, null)` 이 발생할 수 있음.

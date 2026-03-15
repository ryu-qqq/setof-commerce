# API Flow: DiscountController.getDiscountUseHistories

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | GET |
| API Path | /api/v1/discount/history/{discountPolicyId}/use |
| Controller | DiscountController |
| Service Interface | DiscountHistoryFetchService |
| Service Impl | DiscountHistoryFetchServiceImpl |
| Repository Interface | DiscountPolicyHistoryFetchRepository |
| Repository Impl | DiscountPolicyHistoryFetchRepositoryImpl |
| 권한 | @PreAuthorize(HAS_AUTHORITY_MASTER) |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| discountPolicyId | long | Y | 할인 정책 ID (@PathVariable) |

### Query Parameters - DiscountFilter (@ModelAttribute)

`DiscountFilter`는 `SearchAndDateFilter`를 상속합니다.

#### 부모 클래스: SearchAndDateFilter

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|------------|------|
| startDate | LocalDateTime | Y | @ValidDateRange (startDate <= endDate) | 조회 시작일, format: yyyy-MM-dd HH:mm:ss |
| endDate | LocalDateTime | Y | @ValidDateRange (startDate <= endDate) | 조회 종료일, format: yyyy-MM-dd HH:mm:ss |
| searchKeyword | SearchKeyword (Enum) | N | - | 검색 키워드 타입 (COUNT 쿼리에만 적용) |
| searchWord | String | N | - | 검색어 (COUNT 쿼리에만 적용) |

SearchKeyword Enum 값: PRODUCT_GROUP_NAME, PRODUCT_GROUP_ID, ORDER_SNAPSHOT_PRODUCT_GROUP_ID, INSERT_OPERATOR, UPDATE_OPERATOR, ORDER_ID, PAYMENT_ID, SELLER_ID, SELLER_NAME, MEMBER_ID, MEMBER_NAME, MEMBER_PHONE_NUMBER, BUYER_NAME, POLICY_NAME, POLICY_ID, DISCOUNT_TYPE, CONTENT_TITLE, CONTENT_ID, BANNER_NAME, QUESTIONER_NAME, EXCEL_PRODUCT_GROUP_ID

#### DiscountFilter 고유 필드 (이 메서드에서 사용되는 필드)

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| userId | Long | N | 사용자 ID 필터 (데이터 쿼리에만 적용) |
| discountPolicyId | Long | N | (미사용 - PathVariable로 전달됨) |
| periodType | PeriodType | N | (미사용) |
| activeYn | Yn | N | (미사용) |
| publisherType | PublisherType | N | (미사용) |
| issueType | IssueType | N | (미사용) |

#### Pageable

| 이름 | 타입 | 설명 |
|------|------|------|
| page | Integer | 페이지 번호 (0-based) |
| size | Integer | 페이지 크기 |
| sort | String | 정렬 조건 |

### Request URL 예시

```
GET /api/v1/discount/history/1/use?startDate=2024-01-01 00:00:00&endDate=2024-12-31 23:59:59&userId=100&page=0&size=20
```

---

## 3. Response

### DTO 구조: DiscountUseHistoryDto

| 필드명 | 타입 | 직렬화 여부 | 설명 |
|--------|------|------------|------|
| discountUseHistoryId | long | @JsonIgnore (제외) | 할인 사용 이력 ID |
| userId | long | O | 사용자 ID |
| name | String | O | 사용자 이름 |
| orderId | long | O | 주문 ID |
| orderAmount | BigDecimal | O | 주문 금액 |
| paymentId | long | O | 결제 ID |
| productGroupId | long | O | 상품 그룹 ID |
| directDiscountPrice | BigDecimal | O | 직접 할인 금액 (단건) |
| useDate | LocalDateTime | O, @JsonFormat: yyyy-MM-dd HH:mm:ss | 사용 일시 (insertDate 기반) |
| totalDirectDiscountPrice | BigDecimal | O | 직접 할인 금액 합계 (SUM) |

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "userId": 100,
        "name": "홍길동",
        "orderId": 5001,
        "orderAmount": 150000.00,
        "paymentId": 3001,
        "productGroupId": 200,
        "directDiscountPrice": 10000.00,
        "useDate": "2024-06-15 14:30:00",
        "totalDirectDiscountPrice": 10000.00
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20
    },
    "totalElements": 150,
    "totalPages": 8,
    "last": false,
    "first": true
  }
}
```

---

## 4. 호출 흐름

```
[GET] /api/v1/discount/history/{discountPolicyId}/use
    |
    v
DiscountController.getDiscountUseHistories(discountPolicyId, DiscountFilter, Pageable)
    |
    v
DiscountHistoryFetchService.fetchDiscountUseHistories(discountPolicyId, filter, pageable)  [Interface]
    |
    v
DiscountHistoryFetchServiceImpl.fetchDiscountUseHistories(discountPolicyId, filter, pageable)
    |
    +-- DiscountPolicyHistoryFetchRepository.fetchDiscountUseHistories(discountPolicyId, filter, pageable)
    |       └── DiscountPolicyHistoryFetchRepositoryImpl → QueryDSL → 데이터 조회
    |
    +-- DiscountPolicyHistoryFetchRepository.fetchDiscountUsePolicyCountQuery(discountPolicyId, filter)
    |       └── DiscountPolicyHistoryFetchRepositoryImpl → QueryDSL → COUNT 조회
    |
    └── PageableExecutionUtils.getPage(results, pageable, countSupplier)
            └── Page<DiscountUseHistoryDto> 반환
```

---

## 5. Database Query

### 5-1. 메인 데이터 조회 쿼리

#### 참여 테이블

| 테이블 | Entity | JOIN 유형 | JOIN 조건 |
|--------|--------|-----------|-----------|
| discount_use_history | QDiscountUseHistory | FROM (기준) | - |
| orders | QOrder | INNER JOIN | order.id = discountUseHistory.orderId |
| order_snapshot_product_group | QOrderSnapShotProductGroup | INNER JOIN + fetchJoin | order.orderSnapShotProductGroup (OneToOne 연관) |
| users | QUsers | INNER JOIN | users.id = discountUseHistory.userId |

#### SELECT 컬럼

| 컬럼 | 테이블 | DTO 매핑 |
|------|--------|----------|
| discount_use_history_id | discount_use_history | discountUseHistoryId |
| user_id | discount_use_history | userId |
| name | users | name |
| order_id | discount_use_history | orderId |
| order_amount | orders | orderAmount |
| payment_id | discount_use_history | paymentId |
| product_group_id | discount_use_history | productGroupId |
| DIRECT_DISCOUNT_PRICE | order_snapshot_product_group.price | directDiscountPrice |
| insert_date | discount_use_history | useDate |
| SUM(DIRECT_DISCOUNT_PRICE) | order_snapshot_product_group.price | totalDirectDiscountPrice |

#### WHERE 조건

| 조건 메서드 | 필수 여부 | 조건 내용 |
|------------|----------|----------|
| discountUseHistoryHasPolicyId | Y (PathVariable) | discountUseHistory.discountPolicyId = :discountPolicyId |
| periodTypeEq | Y (null 체크 없음 - NPE 위험) | discountUseHistory.insertDate BETWEEN :startDate AND :endDate |
| userIdEq | N | discountUseHistory.userId = :userId (null이면 생략) |

#### QueryDSL 코드

```java
getQueryFactory()
    .select(
        discountUseHistory.id,
        discountUseHistory.userId,
        users.name,
        discountUseHistory.orderId,
        order.orderAmount,
        discountUseHistory.paymentId,
        discountUseHistory.productGroupId,
        orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice,
        discountUseHistory.insertDate
    )
    .from(discountUseHistory)
    .innerJoin(order)
        .on(order.id.eq(discountUseHistory.orderId))
    .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup).fetchJoin()
    .innerJoin(users).on(users.id.eq(discountUseHistory.userId))
    .where(
        discountUseHistory.discountPolicyId.eq(discountPolicyId),
        discountUseHistory.insertDate.between(startDate, endDate),
        userId != null ? discountUseHistory.userId.eq(userId) : null
    )
    .distinct()
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .groupBy(
        discountUseHistory.id,
        discountUseHistory.userId,
        users.name,
        discountUseHistory.orderId,
        order.orderAmount,
        discountUseHistory.paymentId,
        discountUseHistory.productGroupId,
        orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice,
        discountUseHistory.insertDate
    )
    .transform(
        groupBy(discountUseHistory.id).list(
            new QDiscountUseHistoryDto(
                discountUseHistory.id,
                discountUseHistory.userId,
                users.name,
                discountUseHistory.orderId,
                order.orderAmount,
                discountUseHistory.paymentId,
                discountUseHistory.productGroupId,
                orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice,
                discountUseHistory.insertDate,
                orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice.sum()
            )
        )
    )
```

---

### 5-2. COUNT 쿼리 (fetchDiscountUsePolicyCountQuery)

#### 참여 테이블

| 테이블 | JOIN 유형 | JOIN 조건 |
|--------|-----------|-----------|
| discount_use_history | FROM (기준) | - |
| orders | INNER JOIN | order.id = discountUseHistory.orderId |
| order_snapshot_product_group | INNER JOIN + fetchJoin | order.orderSnapShotProductGroup |

#### WHERE 조건

| 조건 | 필수 여부 | 설명 |
|------|----------|------|
| discountUseHistoryHasPolicyId | Y | 할인 정책 ID |
| periodTypeEq | Y | 기간 조건 |
| searchKeywordEq | N | 검색 조건 (AbstractCommonRepository 위임) - 데이터 쿼리에는 없음 |

```java
getQueryFactory()
    .select(discountUseHistory.count())
    .from(discountUseHistory)
    .innerJoin(order).on(order.id.eq(discountUseHistory.orderId))
    .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup).fetchJoin()
    .where(
        discountUseHistory.discountPolicyId.eq(discountPolicyId),
        discountUseHistory.insertDate.between(startDate, endDate),
        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord())
    )
    .distinct()
```

---

## 6. 관련 Entity 테이블 매핑

| Entity | 테이블명 | PK 컬럼 |
|--------|---------|---------|
| DiscountUseHistory | discount_use_history | discount_use_history_id |
| DiscountPolicy | discount_policy | discount_policy_id |
| Order | orders | order_id |
| OrderSnapShotProductGroup | order_snapshot_product_group | order_snapshot_product_group_id |
| Users | users | user_id |

---

## 7. 주요 이슈 및 특이 사항

### 이슈 1: 데이터 쿼리 vs COUNT 쿼리 WHERE 조건 불일치

- 데이터 쿼리: `userIdEq(filter.getUserId())` 적용
- COUNT 쿼리: `userIdEq` 미적용, 대신 `searchKeywordEq` 적용
- 결과: `userId` 필터를 사용할 경우 `totalElements`가 실제 결과 수보다 많게 계산될 수 있음 (페이지네이션 오류)

### 이슈 2: startDate/endDate NPE 위험

- `periodTypeEq(startDate, endDate)` 내부에서 null 체크 없이 `.between(startDate, endDate)` 호출
- startDate 또는 endDate가 null이면 NullPointerException 발생 가능
- @ValidDateRange 어노테이션이 선언되어 있으나 @Validated가 Controller 메서드에 없으므로 검증이 실제로 동작하지 않을 수 있음

### 이슈 3: totalDirectDiscountPrice 집계 의도와 동작 불일치 가능성

- GROUP BY 절에 `directDiscountPrice`가 포함되어 있어 각 행이 개별 그룹이 됨
- `directDiscountPrice.sum()`이 사실상 단건 값의 합계(= 해당 행의 값)와 동일하게 동작
- 정책별 전체 누적 할인 합산이 목적이라면 GROUP BY 재설계 필요

### 특징

- `order_snapshot_product_group`의 `price.directDiscountPrice` 사용: 주문 시점의 스냅샷 할인 금액 기준
- `fetchJoin` 적용으로 OneToOne 관계 N+1 방지
- QueryDSL `transform + groupBy` 패턴 사용

# API Flow: ProductController.fetchProductGroups

## 1. 기본 정보

- **HTTP**: GET /api/v1/products/group
- **Controller**: `ProductController`
- **Service**: `ProductGroupFindService` → `ProductGroupFindServiceImpl`
- **Repository**: `ProductGroupFindRepository` → `ProductGroupFindRepositoryImpl`
- **Mapper**: `ProductSliceMapper` → `ProductSliceMapperImpl` → `AbstractProductSliceMapper`

---

## 2. Request

### Parameters (@ModelAttribute ProductFilter)

`ProductFilter`는 `AbstractItemFilter`를 상속하며, 모든 필드는 선택(Optional)이다.

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| lastDomainId | Long | N | 커서 페이징 기준 ID (이전 페이지 마지막 productGroupId) |
| cursorValue | String | N | 커서 값 (정렬 기준 값: 날짜/가격/할인율 등) |
| lowestPrice | Long | N | 최저 판매가 필터 |
| highestPrice | Long | N | 최고 판매가 필터 |
| categoryId | Long | N | 단일 카테고리 ID 필터 |
| brandId | Long | N | 단일 브랜드 ID 필터 |
| sellerId | Long | N | 판매자 ID 필터 |
| categoryIds | List\<Long\> | N | 다중 카테고리 ID 필터 |
| brandIds | List\<Long\> | N | 다중 브랜드 ID 필터 |
| orderType | OrderType | N | 정렬 기준 (아래 Enum 참고) |

**Pageable 파라미터** (Spring 기본 지원)

| 이름 | 기본값 | 설명 |
|------|--------|------|
| page | 0 | 페이지 번호 |
| size | 20 | 페이지 크기 |
| sort | - | 정렬 (Spring Pageable, 실제 정렬은 OrderType 우선) |

**OrderType Enum**

| 값 | 정렬 필드 | 방향 |
|----|----------|------|
| NONE | id | DESC |
| RECOMMEND | score (product_score) | DESC |
| REVIEW | reviewCount (product_rating_stats) | DESC |
| RECENT | updateDate | DESC |
| HIGH_RATING | averageRating (product_rating_stats) | DESC |
| LOW_PRICE | salePrice | ASC |
| HIGH_PRICE | salePrice | DESC |
| LOW_DISCOUNT | discountRate | ASC |
| HIGH_DISCOUNT | discountRate | DESC |

### 요청 예시 (Query String)

```
GET /api/v1/products/group
  ?orderType=RECOMMEND
  &categoryIds=10,20
  &lowestPrice=10000
  &highestPrice=100000
  &lastDomainId=999
  &cursorValue=0.85
  &size=20
```

---

## 3. Response

### 응답 DTO 구조

**최상위**: `ApiResponse<CustomSlice<ProductGroupThumbnail>>`

#### ApiResponse\<T\>

```
success: boolean
data: T
```

#### CustomSlice\<ProductGroupThumbnail\>

| 필드 | 타입 | 설명 |
|------|------|------|
| content | List\<ProductGroupThumbnail\> | 상품 목록 |
| last | boolean | 마지막 페이지 여부 |
| first | boolean | 첫 번째 페이지 여부 |
| number | int | 현재 페이지 번호 |
| sort | Sort | 정렬 정보 |
| size | int | 요청 페이지 크기 |
| numberOfElements | int | 현재 페이지 실제 개수 |
| empty | boolean | 비어있는지 여부 |
| lastDomainId | Long | 다음 커서용 마지막 productGroupId |
| cursorValue | String | 다음 커서용 정렬 기준 값 |
| totalElements | Long | 필터 조건 기준 전체 상품 수 |

#### ProductGroupThumbnail

| 필드 | 타입 | JSON 직렬화 | 설명 |
|------|------|-------------|------|
| productGroupId | long | O | 상품 그룹 ID |
| sellerId | long | O | 판매자 ID |
| productGroupName | String | O | 상품명 |
| brand | BrandDto | O | 브랜드 정보 (중첩 객체) |
| productImageUrl | String | O | 대표 이미지 URL |
| price | Price | O | 가격 정보 (중첩 객체) |
| insertDate | LocalDateTime | O | 등록일 (format: "yyyy-MM-dd HH:mm:ss") |
| averageRating | double | O | 평균 평점 (없으면 0.0) |
| reviewCount | long | O | 리뷰 수 (없으면 0) |
| score | double | O | 추천 스코어 (없으면 0.0) |
| isFavorite | boolean | O | 즐겨찾기 여부 (항상 false, 목록 조회에서 미설정) |
| productStatus | ProductStatus | O | 판매 상태 |
| id | Long | X (@JsonIgnore) | CursorValueProvider 인터페이스용 |
| salePrice | long | X (@JsonIgnore) | DiscountOffer 인터페이스용 |
| discountRate | int | X (@JsonIgnore) | DiscountOffer 인터페이스용 |

#### BrandDto (중첩)

| 필드 | 타입 | 설명 |
|------|------|------|
| brandId | long | 브랜드 ID |
| brandName | String | 브랜드명 |

#### Price (중첩, @Embeddable)

| 필드 | 타입 | DB 컬럼 | 설명 |
|------|------|---------|------|
| regularPrice | long | regular_price | 정가 |
| currentPrice | long | current_price | 현재가 |
| salePrice | long | sale_price | 판매가 (할인 적용) |
| directDiscountRate | int | direct_discount_rate | 직접 할인율 |
| directDiscountPrice | long | direct_discount_price | 직접 할인액 |
| discountRate | int | discount_rate | 총 할인율 |

#### ProductStatus (중첩, @Embeddable)

| 필드 | 타입 | DB 컬럼 | 설명 |
|------|------|---------|------|
| soldOutYn | Yn (Y/N) | sold_out_yn | 품절 여부 |
| displayYn | Yn (Y/N) | display_yn | 전시 여부 |

### 응답 JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productGroupId": 1001,
        "sellerId": 50,
        "productGroupName": "프리미엄 티셔츠",
        "brand": {
          "brandId": 10,
          "brandName": "BRAND_A"
        },
        "productImageUrl": "https://cdn.example.com/images/product/1001_main.jpg",
        "price": {
          "regularPrice": 50000,
          "currentPrice": 45000,
          "salePrice": 36000,
          "directDiscountRate": 20,
          "directDiscountPrice": 9000,
          "discountRate": 28
        },
        "insertDate": "2024-01-15 10:30:00",
        "averageRating": 4.5,
        "reviewCount": 128,
        "score": 0.92,
        "isFavorite": false,
        "productStatus": {
          "soldOutYn": "N",
          "displayYn": "Y"
        }
      }
    ],
    "last": false,
    "first": true,
    "number": 0,
    "sort": { "empty": true, "sorted": false, "unsorted": true },
    "size": 20,
    "numberOfElements": 20,
    "empty": false,
    "lastDomainId": 980,
    "cursorValue": "0.85",
    "totalElements": 340
  }
}
```

---

## 4. 호출 흐름

```
[Client]
    |
    | GET /api/v1/products/group?orderType=RECOMMEND&categoryIds=10&size=20
    v
[ProductController.fetchProductGroups(ProductFilter filter, Pageable pageable)]
    |
    | productGroupFindService.fetchProductGroups(filter, pageable)
    v
[ProductGroupFindService (interface)]
    |
[ProductGroupFindServiceImpl.fetchProductGroups(filter, pageable)]
    |
    |--- (1) productGroupFindRepository.fetchProductGroups(filter, pageable.getPageSize())
    |         → List<ProductGroupThumbnail> (DB 조회, 커서 기반 페이징, pageSize+1 개 조회)
    |
    |--- (2) productGroupFindRepository.fetchProductCountQuery(filter).fetchCount()
    |         → long totalElements (카운트 쿼리)
    |
    | productSliceMapper.toSlice(thumbnails, pageable, totalElements, filter)
    v
[ProductSliceMapperImpl → AbstractProductSliceMapper.toSlice(...)]
    |
    | SliceUtils.toSlice(data, pageable)  → Slice<T> (hasNext 판별)
    | SortUtils.setCursorValue(lastItem, orderType) → 다음 커서 값 결정
    | SortUtils.getComparatorBasedOnOrderType(orderType) → 정렬 안정성 보장
    |
    | CustomSlice.build(content, last, first, lastDomainId, cursorValue, totalElements)
    v
[CustomSlice<ProductGroupThumbnail>]
    |
    v
[ApiResponse.success(customSlice)]
    |
    v
[Client - ResponseEntity 200 OK]
```

### 내부 의존 서비스 (fetchProductGroups 직접 경로)

```
ProductGroupFindServiceImpl
    ├── ProductGroupFindRepository     (DB 조회 - 두 번 호출)
    │     ├── fetchProductGroups()      - 목록 QueryDSL
    │     └── fetchProductCountQuery()  - 카운트 QueryDSL
    └── ProductSliceMapper             (커서 슬라이스 변환)
```

> 참고: `brandProductRedisFindService`, `sellerProductRedisFindService`, `productGroupRedisQueryService`는 `fetchProductGroupWithBrand`/`fetchProductGroupWithSeller` 경로에서만 사용되며, `fetchProductGroups`에서는 호출되지 않는다.

---

## 5. Database Query

### 관련 테이블

| 테이블 | Entity 클래스 | QueryDSL Q클래스 | 역할 |
|--------|-------------|-----------------|------|
| product_group | ProductGroup | QProductGroup | 메인 테이블 |
| product_group_image | ProductGroupImage | QProductGroupImage | 대표 이미지 (INNER JOIN) |
| brand | Brand | QBrand | 브랜드 정보 (INNER JOIN) |
| product_rating_stats | ProductRatingStats | QProductRatingStats | 평점/리뷰 통계 (LEFT JOIN) |
| product_score | ProductScore | QProductScore | 추천 스코어 (LEFT JOIN) |

### JOIN 관계

| JOIN 종류 | 테이블 | 조건 | 이유 |
|----------|--------|------|------|
| INNER JOIN | product_group_image | `product_group_image.product_group_id = product_group.product_group_id` AND `image_type = 'MAIN'` AND `delete_yn = 'N'` | 대표 이미지 필수 |
| INNER JOIN | brand | `brand.brand_id = product_group.brand_id` | 브랜드 정보 필수 |
| LEFT JOIN | product_rating_stats | `product_rating_stats.product_group_id = product_group.product_group_id` | 리뷰 없는 상품도 포함 |
| LEFT JOIN | product_score | `product_score.product_group_id = product_group.product_group_id` | 스코어 없는 상품도 포함 |

### WHERE 조건

| 조건 | 메서드 | 설명 |
|------|--------|------|
| `display_yn = 'Y'` | `onSaleProduct()` | 판매 중인 상품만 (필수, 항상 적용) |
| `seller_id = ?` | `sellerIdEq(filter.getSellerId())` | sellerId 파라미터가 있는 경우 |
| `category_id IN (?)` | `categoryIdIn(filter)` | categoryIds 파라미터가 있는 경우 |
| `brand_id = ?` | `brandIdEq(filter.getBrandId())` | brandId 파라미터가 있는 경우 |
| `sale_price BETWEEN ? AND ?` | `betweenPrice(filter)` | lowestPrice/highestPrice 파라미터가 있는 경우 |
| 커서 동적 조건 | `createDynamicWhereCondition()` | lastDomainId + cursorValue 있는 경우 커서 페이징, 없으면 ID 기반 |

### 커서 페이징 동적 WHERE 조건 (OrderType별)

| OrderType | 커서 조건 |
|-----------|----------|
| RECENT | `insertDate < cursorDate OR (insertDate = cursorDate AND id <= lastDomainId)` |
| HIGH_PRICE | `salePrice < cursorPrice OR (salePrice = cursorPrice AND id <= lastDomainId)` |
| LOW_PRICE | `salePrice > cursorPrice OR (salePrice = cursorPrice AND id <= lastDomainId)` |
| HIGH_DISCOUNT | `discountRate < cursorRate OR (discountRate = cursorRate AND id <= lastDomainId)` |
| LOW_DISCOUNT | `discountRate > cursorRate OR (discountRate = cursorRate AND id <= lastDomainId)` |
| HIGH_RATING | `averageRating < cursorRating OR (averageRating = cursorRating AND id <= lastDomainId)` |
| REVIEW | `reviewCount < cursorCount OR (reviewCount = cursorCount AND id <= lastDomainId)` |
| RECOMMEND | `score < cursorScore OR (score = cursorScore AND id <= lastDomainId)` |
| 커서 없음 | `id <= lastDomainId` (또는 조건 없음) |

### QueryDSL 코드 (목록 조회: fetchProductGroups)

```java
// ProductGroupFindRepositoryImpl.fetchProductGroups()

List<OrderSpecifier<?>> orders =
        createOrderSpecifiersFromPageable(productGroup, filter.getOrderType());
BooleanExpression dynamicWhere = createDynamicWhereCondition(filter, filter.getOrderType());

queryFactory
    .from(productGroup)
    .innerJoin(productGroupImage)
        .on(productGroupImage.productGroup.id.eq(productGroup.id))
        .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
        .on(productGroupImage.deleteYn.eq(Yn.N))
    .innerJoin(brand)
        .on(brand.id.eq(productGroup.productGroupDetails.brandId))
    .leftJoin(productRatingStats)
        .on(productRatingStats.id.eq(productGroup.id))
    .leftJoin(productScore)
        .on(productScore.id.eq(productGroup.id))
    .where(
        dynamicWhere,                        // 커서 페이징 조건
        onSaleProduct(),                     // display_yn = 'Y'
        sellerIdEq(filter.getSellerId()),    // seller_id = ?  (nullable)
        categoryIdIn(filter),               // category_id IN (?)  (nullable)
        brandIdEq(filter.getBrandId()),      // brand_id = ?  (nullable)
        betweenPrice(filter))               // sale_price BETWEEN ? AND ?  (nullable)
    .orderBy(orders.toArray(OrderSpecifier[]::new))
    .limit(pageSize + 1)                    // hasNext 판별을 위해 +1
    .transform(
        GroupBy.groupBy(productGroup.id)
            .list(new QProductGroupThumbnail(
                productGroup.id,
                productGroup.productGroupDetails.sellerId,
                productGroup.productGroupDetails.productGroupName,
                new QBrandDto(brand.id, brand.brandName),
                productGroupImage.imageDetail.imageUrl,
                productGroup.productGroupDetails.price,
                productGroup.insertDate,
                productRatingStats.averageRating.coalesce(0.0),
                productRatingStats.reviewCount.coalesce(0L),
                productScore.score.coalesce(0.0),
                productGroup.productGroupDetails.productStatus)));
```

### QueryDSL 코드 (카운트 쿼리: fetchProductCountQuery)

```java
// ProductGroupFindRepositoryImpl.fetchProductCountQuery(ProductFilter filter)

queryFactory
    .select(productGroup.count())
    .from(productGroup)
    .where(
        sellerIdEq(filter.getSellerId()),
        categoryIdIn(filter),
        brandIdEq(filter.getBrandId()),
        betweenPrice(filter),
        onSaleProduct())     // betweenPrice 중복 적용 (코드 버그)
    .distinct()
```

> 주의: 카운트 쿼리에서 `betweenPrice(filter)`가 두 번 적용되어 있다 (소스 코드 L471, L476). 실제 동작에는 영향이 없으나 중복 조건이다.

### OrderSpecifier 생성 규칙 (AbstractCommonRepository)

```
OrderType = RECOMMEND → ORDER BY product_score.score DESC, product_group.id DESC
OrderType = REVIEW    → ORDER BY product_rating_stats.reviewCount DESC, product_group.id DESC
OrderType = HIGH_RATING → ORDER BY product_rating_stats.averageRating DESC, product_group.id DESC
그 외 (가격/할인율/최신) → ORDER BY product_group.{field} {direction}, product_group.id DESC
OrderType = null / NONE → ORDER BY product_group.id DESC
```

---

## 6. 페이징 구조 (커서 기반 슬라이스)

```
[클라이언트 최초 요청]
GET /api/v1/products/group?orderType=RECOMMEND&size=20
  → lastDomainId=null, cursorValue=null
  → WHERE: display_yn='Y' (커서 조건 없음)
  → LIMIT 21 (20+1 로 hasNext 판별)

[응답에서 다음 커서 추출]
response.data.lastDomainId → 980
response.data.cursorValue  → "0.85"
response.data.last         → false (다음 페이지 있음)

[클라이언트 두 번째 요청]
GET /api/v1/products/group?orderType=RECOMMEND&size=20&lastDomainId=980&cursorValue=0.85
  → WHERE: (score < 0.85 OR (score = 0.85 AND id <= 980))
  → LIMIT 21
```

### CustomSlice 구성 과정 (AbstractProductSliceMapper)

```
1. SliceUtils.toSlice(data, pageable)
   → data.size() > pageable.pageSize → hasNext = true, last = false
   → data.size() <= pageable.pageSize → last = true

2. 마지막 아이템 추출 (data.get(data.size() - 1))
   → lastDomainId = lastItem.getId()          (productGroupId)
   → cursorValue  = SortUtils.setCursorValue(lastItem, orderType)
                    RECOMMEND → String.valueOf(score)
                    HIGH_PRICE/LOW_PRICE → String.valueOf(salePrice)
                    RECENT → insertDate.toString()
                    등...

3. 정렬 안정성 보장
   → SortUtils.getComparatorBasedOnOrderType(orderType) != null 이면
     data를 재정렬한 뒤 마지막 아이템으로 커서 재계산
```

---

## 7. 전체 클래스/인터페이스 목록

| 레이어 | 클래스/인터페이스 | 경로 |
|--------|-----------------|------|
| Controller | ProductController | module/product/controller/ |
| Service Interface | ProductGroupFindService | module/product/service/group/fetch/ |
| Service Impl | ProductGroupFindServiceImpl | module/product/service/group/fetch/ |
| Repository Interface | ProductGroupFindRepository | module/product/repository/group/ |
| Repository Impl | ProductGroupFindRepositoryImpl | module/product/repository/group/ |
| Repository Base | AbstractComponentRepository | module/display/repository/component/ |
| Repository Base | AbstractCommonRepository | module/common/repository/ |
| Mapper Interface | ProductSliceMapper | module/product/mapper/ |
| Mapper Impl | ProductSliceMapperImpl | module/product/mapper/ |
| Mapper Base | AbstractProductSliceMapper | module/common/mapper/ |
| Filter | ProductFilter | module/product/dto/filter/ |
| Filter Base | AbstractItemFilter | module/common/filter/ |
| Response DTO | CustomSlice\<T\> | module/common/dto/ |
| Response DTO | ProductGroupThumbnail | module/product/dto/ |
| Nested DTO | BrandDto | module/product/dto/brand/ |
| Embedded | Price | module/product/entity/group/embedded/ |
| Embedded | ProductStatus | module/product/entity/group/embedded/ |
| Entity | ProductGroup | module/product/entity/group/ |
| Entity | ProductGroupImage | module/product/entity/image/ |
| Entity | Brand | module/brand/entity/ |
| Entity | ProductRatingStats | module/review/entity/ |
| Entity | ProductScore | module/search/entity/ |
| Enum | OrderType | module/display/enums/component/ |

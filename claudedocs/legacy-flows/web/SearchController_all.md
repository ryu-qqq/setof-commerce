# API Flow: SearchController (전체 엔드포인트)

## 엔드포인트 목록

| # | HTTP | Path | 메서드 | 설명 |
|---|------|------|--------|------|
| 1 | GET | `/api/v1/search` | `fetchSearchResults` | 키워드 기반 상품 검색 (커서 페이징) |

---

## 1. GET /api/v1/search — fetchSearchResults

### 1-1. 기본 정보

| 항목 | 값 |
|------|----|
| HTTP Method | GET |
| Path | `/api/v1/search` |
| Controller | `SearchController` |
| Service Interface | `SearchFindService` |
| Service Impl | `SearchFindServiceImpl` |
| Repository Interface | `SearchRepository` |
| Repository Impl | `TempSearchRepositoryImpl` |

---

### 1-2. Request

#### Query Parameters — SearchFilter (@ModelAttribute)

SearchFilter는 `AbstractItemFilter`를 상속합니다.

**SearchFilter 고유 필드**

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| `searchWord` | String | 선택 | 검색 키워드 (MySQL ngram FULLTEXT 검색) |
| `productGroupId` | Long | 선택 | 특정 상품 그룹 ID 필터 |

**AbstractItemFilter 상속 필드**

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| `lastDomainId` | Long | 선택 | 커서 페이징 — 이전 페이지 마지막 ID |
| `cursorValue` | String | 선택 | 커서 페이징 — 정렬 기준 커서 값 |
| `lowestPrice` | Long | 선택 | 최저 가격 필터 |
| `highestPrice` | Long | 선택 | 최고 가격 필터 |
| `categoryId` | Long | 선택 | 카테고리 ID 단일 필터 |
| `brandId` | Long | 선택 | 브랜드 ID 단일 필터 |
| `sellerId` | Long | 선택 | 셀러 ID 필터 |
| `categoryIds` | List\<Long\> | 선택 | 카테고리 ID 다중 필터 |
| `brandIds` | List\<Long\> | 선택 | 브랜드 ID 다중 필터 |
| `orderType` | OrderType (Enum) | 선택 | 정렬 기준 (미지정 시 RECOMMEND 기본 적용) |

**Pageable Parameters**

| 필드명 | 타입 | 기본값 | 설명 |
|--------|------|--------|------|
| `page` | Integer | 0 | 페이지 번호 |
| `size` | Integer | 20 | 페이지 크기 |
| `sort` | String | - | 정렬 (OrderType으로 별도 제어) |

**OrderType Enum 값**

| 값 | 정렬 필드 | 방향 | 설명 |
|----|----------|------|------|
| `RECOMMEND` | score | DESC | 추천순 (기본값, 미지정 시 자동 적용) |
| `REVIEW` | reviewCount | DESC | 리뷰 많은순 |
| `RECENT` | updateDate | DESC | 최신순 |
| `HIGH_RATING` | averageRating | DESC | 평점 높은순 |
| `HIGH_PRICE` | salePrice | DESC | 높은 가격순 |
| `LOW_PRICE` | salePrice | ASC | 낮은 가격순 |
| `HIGH_DISCOUNT` | discountRate | DESC | 높은 할인율순 |
| `LOW_DISCOUNT` | discountRate | ASC | 낮은 할인율순 |

#### Query Parameter 예시

```
GET /api/v1/search?searchWord=나이키&orderType=RECOMMEND&size=20
GET /api/v1/search?searchWord=운동화&orderType=LOW_PRICE&lastDomainId=500&cursorValue=15000&size=20
GET /api/v1/search?searchWord=후드티&brandIds=1,2,3&orderType=HIGH_RATING&size=20
```

---

### 1-3. 호출 흐름

```
SearchController.fetchSearchResults(SearchFilter, Pageable)
    │
    └── SearchFindService.fetchSearchResults(filter, pageable)
            │
            └── SearchFindServiceImpl.fetchSearchResults()
                    │  filter.orderType == null → setOrderType(RECOMMEND)
                    │
                    └── fetchSearchExactlyResults(filter, pageable)
                            │
                            ├── SearchRepository.fetchResults(filter, pageSize + 1)
                            │       └── TempSearchRepositoryImpl.fetchResults()
                            │               ├── createOrderSpecifiersFromPageable()  [AbstractCommonRepository]
                            │               ├── createDynamicWhereCondition()        [AbstractCommonRepository]
                            │               └── QueryDSL: FROM product_group
                            │                            INNER JOIN product_group_image (MAIN, deleteYn=N)
                            │                            INNER JOIN brand
                            │                            LEFT  JOIN product_rating_stats
                            │                            LEFT  JOIN product_score
                            │                            WHERE fullTextSearch(searchWord)  ← MySQL ngram FULLTEXT
                            │                            ORDER BY [orderType] + product_group_id DESC
                            │                            LIMIT (pageSize + 1)
                            │
                            ├── SearchRepository.fetchSearchCountQuery(filter)
                            │       └── TempSearchRepositoryImpl.fetchSearchCountQuery()
                            │               └── QueryDSL: SELECT COUNT(product_group_id)
                            │                            FROM product_group
                            │                            INNER JOIN product_group_image (MAIN, deleteYn=N)
                            │                            INNER JOIN brand
                            │                            LEFT  JOIN product_rating_stats
                            │                            LEFT  JOIN product_score
                            │                            WHERE fullTextSearch(searchWord)
                            │
                            └── ProductSliceMapper.toSlice(exactlyHits, pageable, totalCount, filter)
                                    └── CustomSlice<ProductGroupThumbnail> 반환
```

---

### 1-4. 사용 Entity / DTO

#### Entity

| Entity 클래스 | 테이블명 | Q클래스 |
|---------------|----------|---------|
| `ProductGroup` | `product_group` | `QProductGroup` |
| `ProductGroupImage` | `product_group_image` | `QProductGroupImage` |
| `Brand` | `brand` | `QBrand` |
| `ProductRatingStats` | `product_rating_stats` | `QProductRatingStats` |
| `ProductScore` | `product_score` | `QProductScore` |

#### DTO

| DTO 클래스 | 역할 |
|------------|------|
| `SearchFilter` | 요청 필터 (AbstractItemFilter 상속) |
| `ProductGroupThumbnail` | 상품 썸네일 응답 |
| `BrandDto` | ProductGroupThumbnail 내 브랜드 정보 |
| `Price` (Embedded) | ProductGroupThumbnail 내 가격 정보 |
| `ProductStatus` (Embedded) | ProductGroupThumbnail 내 상품 상태 |
| `CustomSlice<T>` | 커서 페이징 래퍼 |
| `ApiResponse<T>` | 공통 API 응답 래퍼 |

---

### 1-5. DB 테이블 및 컬럼 매핑

#### product_group 테이블

| 컬럼명 | Java 필드 | 타입 | 설명 |
|--------|-----------|------|------|
| `product_group_id` | `id` | Long (PK) | 상품 그룹 ID |
| `product_group_name` | `productGroupDetails.productGroupName` | String | 상품명 (FULLTEXT 인덱스 대상) |
| `seller_id` | `productGroupDetails.sellerId` | long | 셀러 ID |
| `brand_id` | `productGroupDetails.brandId` | long | 브랜드 FK |
| `category_id` | `productGroupDetails.categoryId` | long | 카테고리 FK |
| `regular_price` | `productGroupDetails.price.regularPrice` | long | 정가 |
| `current_price` | `productGroupDetails.price.currentPrice` | long | 현재가 |
| `sale_price` | `productGroupDetails.price.salePrice` | long | 판매가 |
| `direct_discount_rate` | `productGroupDetails.price.directDiscountRate` | int | 직접 할인율 |
| `direct_discount_price` | `productGroupDetails.price.directDiscountPrice` | long | 직접 할인가 |
| `discount_rate` | `productGroupDetails.price.discountRate` | int | 총 할인율 |
| `sold_out_yn` | `productGroupDetails.productStatus.soldOutYn` | Yn (Enum) | 품절 여부 |
| `display_yn` | `productGroupDetails.productStatus.displayYn` | Yn (Enum) | 전시 여부 |
| `insert_date` | `insertDate` (BaseEntity) | LocalDateTime | 등록일시 |

**FULLTEXT 인덱스**:
```sql
ALTER TABLE product_group
  ADD FULLTEXT INDEX ft_product_group_name (product_group_name)
  WITH PARSER ngram;
```

#### product_group_image 테이블

| 컬럼명 | Java 필드 | 타입 | 설명 |
|--------|-----------|------|------|
| `product_group_image_id` | `id` | Long (PK) | 이미지 ID |
| `PRODUCT_GROUP_ID` | `productGroup.id` | Long (FK) | 상품 그룹 FK |
| `product_group_image_type` | `imageDetail.productGroupImageType` | Enum | 이미지 유형 (MAIN 필터링) |
| `image_url` | `imageDetail.imageUrl` | String | 이미지 URL |
| `delete_yn` | `deleteYn` (BaseEntity) | Yn (Enum) | 삭제 여부 (N 필터링) |

#### brand 테이블

| 컬럼명 | Java 필드 | 타입 | 설명 |
|--------|-----------|------|------|
| `brand_id` | `id` | Long (PK) | 브랜드 ID |
| `brand_name` | `brandName` | String | 브랜드명 |
| `brand_icon_image_url` | `brandIconImageUrl` | String | 브랜드 아이콘 URL |
| `display_english_name` | `displayEnglishName` | String | 영문 표시명 |
| `display_korean_name` | `displayKoreanName` | String | 한글 표시명 |
| `display_yn` | `displayYn` | Yn (Enum) | 전시 여부 |

#### product_rating_stats 테이블

| 컬럼명 | Java 필드 | 타입 | 설명 |
|--------|-----------|------|------|
| `product_group_id` | `id` | Long (PK = FK) | product_group.product_group_id |
| `average_rating` | `averageRating` | double | 평균 평점 |
| `review_count` | `reviewCount` | long | 리뷰 수 |

#### product_score 테이블

| 컬럼명 | Java 필드 | 타입 | 설명 |
|--------|-----------|------|------|
| `product_group_id` | `id` | Long (PK = FK) | product_group.product_group_id |
| `score` | `score` | double | 추천 점수 |

---

### 1-6. JOIN 관계

```
product_group (PK: product_group_id)
    │
    ├── INNER JOIN product_group_image
    │       ON product_group_image.PRODUCT_GROUP_ID = product_group.product_group_id
    │       AND product_group_image.product_group_image_type = 'MAIN'
    │       AND product_group_image.delete_yn = 'N'
    │
    ├── INNER JOIN brand
    │       ON brand.brand_id = product_group.brand_id
    │
    ├── LEFT JOIN product_rating_stats
    │       ON product_rating_stats.product_group_id = product_group.product_group_id
    │
    └── LEFT JOIN product_score
            ON product_score.product_group_id = product_group.product_group_id
```

**JOIN 전략 설명**:
- `product_group_image`: INNER JOIN — 메인 이미지가 없는 상품은 결과에서 제외
- `brand`: INNER JOIN — 브랜드 정보가 없는 상품은 결과에서 제외
- `product_rating_stats`: LEFT JOIN — 리뷰 없는 상품도 포함 (averageRating=0.0, reviewCount=0 기본값)
- `product_score`: LEFT JOIN — score 없는 상품도 포함 (score=0.0 기본값)

---

### 1-7. Database Query (QueryDSL)

#### fetchResults 쿼리

```java
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
        fullTextSearch(searchFilter.getSearchWord()),  // MySQL ngram FULLTEXT
        cursorCondition                                // 커서 페이징 조건
    )
    .orderBy(orders.toArray(OrderSpecifier[]::new))
    .limit(size)   // pageSize + 1
    .transform(
        GroupBy.groupBy(productGroup.id)
            .list(new QProductGroupThumbnail(...))
    )
```

#### fullTextSearch 구현

```java
// searchWord가 null 또는 blank이면 null 반환 (WHERE 조건에서 제외)
Expressions.numberTemplate(
    Double.class,
    "function('match_against', {0}, {1})",
    productGroup.productGroupDetails.productGroupName,
    "+" + searchWord + "*"    // Boolean Mode, prefix match
).gt(0)
```

실행되는 SQL:
```sql
MATCH(product_group_name) AGAINST('+{searchWord}*' IN BOOLEAN MODE) > 0
```

#### fetchSearchCountQuery 쿼리

```java
queryFactory
    .select(productGroup.id.count())
    .from(productGroup)
    .innerJoin(productGroupImage) ...
    .innerJoin(brand) ...
    .leftJoin(productRatingStats) ...
    .leftJoin(productScore) ...
    .where(fullTextSearch(filter.getSearchWord()))
    .orderBy(productScore.score.coalesce(0.0).desc())
    .fetchOne()
```

#### 커서 페이징 조건 (OrderType별)

| OrderType | cursorValue 형식 | 커서 조건 |
|-----------|-----------------|----------|
| RECOMMEND | Double (score) | score < cursor OR (score = cursor AND id <= lastId) |
| REVIEW | Long (reviewCount) | reviewCount < cursor OR (reviewCount = cursor AND id <= lastId) |
| RECENT | LocalDateTime | insertDate < cursor OR (insertDate = cursor AND id <= lastId) |
| HIGH_RATING | Double (averageRating) | averageRating < cursor OR (averageRating = cursor AND id <= lastId) |
| HIGH_PRICE | Long (salePrice) | salePrice < cursor OR (salePrice = cursor AND id <= lastId) |
| LOW_PRICE | Long (salePrice) | salePrice > cursor OR (salePrice = cursor AND id <= lastId) |
| HIGH_DISCOUNT | Integer (discountRate) | discountRate < cursor OR (discountRate = cursor AND id <= lastId) |
| LOW_DISCOUNT | Integer (discountRate) | discountRate > cursor OR (discountRate = cursor AND id <= lastId) |

---

### 1-8. Response

#### 응답 DTO 구조

```
ApiResponse<CustomSlice<ProductGroupThumbnail>>
├── ApiResponse
│   └── data: CustomSlice<ProductGroupThumbnail>
│
└── CustomSlice<ProductGroupThumbnail>
    ├── content: List<ProductGroupThumbnail>
    │   └── ProductGroupThumbnail
    │       ├── productGroupId: long
    │       ├── sellerId: long
    │       ├── productGroupName: String
    │       ├── brand: BrandDto
    │       │   ├── brandId: long
    │       │   └── brandName: String
    │       ├── productImageUrl: String
    │       ├── price: Price
    │       │   ├── regularPrice: long
    │       │   ├── currentPrice: long
    │       │   ├── salePrice: long
    │       │   ├── directDiscountRate: int
    │       │   ├── directDiscountPrice: long
    │       │   └── discountRate: int
    │       ├── insertDate: String  ("yyyy-MM-dd HH:mm:ss")
    │       ├── averageRating: double
    │       ├── reviewCount: long
    │       ├── score: double
    │       ├── isFavorite: boolean
    │       └── productStatus: ProductStatus
    │           ├── soldOutYn: String  ("Y"/"N")
    │           └── displayYn: String  ("Y"/"N")
    │
    ├── last: boolean
    ├── first: boolean
    ├── number: int
    ├── size: int
    ├── numberOfElements: int
    ├── empty: boolean
    ├── lastDomainId: Long      ← 다음 페이지 커서 ID
    ├── cursorValue: String     ← 다음 페이지 커서 값
    └── totalElements: Long     ← 전체 검색 결과 수
```

**@JsonIgnore 필드** (응답 JSON에서 제외):
- `ProductGroupThumbnail.getId()` → productGroupId의 별칭 메서드
- `ProductGroupThumbnail.getSalePrice()` → price.salePrice 위임 메서드
- `ProductGroupThumbnail.getDiscountRate()` → price.discountRate 위임 메서드
- `ProductStatus.isSoldOut()` → soldOutYn 기반 boolean 계산 메서드

#### 응답 JSON 예시

```json
{
  "data": {
    "content": [
      {
        "productGroupId": 1001,
        "sellerId": 50,
        "productGroupName": "나이키 에어맥스 90",
        "brand": {
          "brandId": 10,
          "brandName": "나이키"
        },
        "productImageUrl": "https://cdn.example.com/images/product_1001_main.jpg",
        "price": {
          "regularPrice": 150000,
          "currentPrice": 120000,
          "salePrice": 108000,
          "directDiscountRate": 10,
          "directDiscountPrice": 12000,
          "discountRate": 28
        },
        "insertDate": "2024-01-15 10:30:00",
        "averageRating": 4.5,
        "reviewCount": 320,
        "score": 87.5,
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
    "size": 20,
    "numberOfElements": 20,
    "empty": false,
    "lastDomainId": 982,
    "cursorValue": "87.5",
    "totalElements": 243
  }
}
```

---

### 1-9. 비즈니스 로직 특이사항

1. **기본 정렬 자동 지정**: `orderType`이 null이면 `RECOMMEND(score DESC)`로 자동 설정
2. **hasNext 판단**: Repository에서 `pageSize + 1`개를 조회 후 `ProductSliceMapper`에서 hasNext 여부를 판단
3. **MySQL ngram FULLTEXT**: `product_group_name` 컬럼에 ngram 파서로 구축된 FULLTEXT 인덱스 사용. Hibernate 6 커스텀 함수 `match_against`를 통해 `IN BOOLEAN MODE` + prefix match(`+keyword*`) 방식으로 검색
4. **searchWord 없는 경우**: `searchWord`가 null 또는 blank이면 fullTextSearch 조건이 null로 반환되어 WHERE절에서 제외 — 전체 상품 조회로 동작
5. **NULL 안전 집계**: `productRatingStats`, `productScore`는 LEFT JOIN이므로 `.coalesce(0.0)`, `.coalesce(0L)`로 null 방어
6. **GroupBy 중복 제거**: `transform(GroupBy.groupBy(productGroup.id).list(...))` 사용으로 다중 이미지 JOIN 시 중복 제거

---

### 1-10. 관련 소스 파일 경로

| 파일 | 경로 |
|------|------|
| Controller | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/search/controller/SearchController.java` |
| Service Interface | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/search/service/SearchFindService.java` |
| Service Impl | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/search/service/SearchFindServiceImpl.java` |
| Repository Interface | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/search/repository/SearchRepository.java` |
| Repository Impl | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/search/repository/TempSearchRepositoryImpl.java` |
| Abstract Repository | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/common/repository/AbstractCommonRepository.java` |
| Request DTO | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/search/dto/SearchFilter.java` |
| Abstract Filter | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/common/filter/AbstractItemFilter.java` |
| Response DTO | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/product/dto/ProductGroupThumbnail.java` |
| Brand DTO | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/product/dto/brand/BrandDto.java` |
| CustomSlice | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/common/dto/CustomSlice.java` |
| ProductGroup Entity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/product/entity/group/ProductGroup.java` |
| ProductGroupImage Entity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/product/entity/image/ProductGroupImage.java` |
| Brand Entity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/brand/entity/Brand.java` |
| ProductRatingStats Entity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/review/entity/ProductRatingStats.java` |
| ProductScore Entity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/search/entity/ProductScore.java` |
| OrderType Enum | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/display/enums/component/OrderType.java` |

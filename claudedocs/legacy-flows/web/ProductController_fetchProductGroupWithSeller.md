# API Flow: ProductController.fetchProductGroupWithSeller

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | GET |
| Path | /api/v1/product/group/seller/{sellerId} |
| Controller | ProductController |
| Service Interface | ProductGroupFindService |
| Service Impl | ProductGroupFindServiceImpl |
| Repository Interface | ProductGroupFindRepository |
| Repository Impl | ProductGroupFindRepositoryImpl |
| Redis Service (조회) | SellerProductRedisFindService → SellerProductRedisFindServiceImpl |
| Redis Service (저장) | ProductGroupRedisQueryService → ProductGroupRedisQueryServiceImpl |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| sellerId | long | Y | 셀러 식별자 |

### Query Parameter

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| page | int | N | 페이지 번호 (Spring Pageable, 0-based) |
| size | int | N | 페이지 사이즈 (기본값 20) |
| sort | String | N | 정렬 기준 |

### Request 예시

```
GET /api/v1/product/group/seller/1?page=0&size=20
```

---

## 3. Response

### DTO 구조

#### ApiResponse\<List\<ProductGroupThumbnail\>\>

| 필드 | 타입 | 설명 |
|------|------|------|
| data | List\<ProductGroupThumbnail\> | 상품 그룹 썸네일 목록 |
| response.status | int | HTTP 상태 코드 |
| response.message | String | 응답 메시지 |

#### ProductGroupThumbnail

| 필드 | 타입 | @JsonIgnore | 설명 |
|------|------|-------------|------|
| productGroupId | long | N | 상품 그룹 ID |
| sellerId | long | N | 셀러 ID |
| productGroupName | String | N | 상품 그룹명 |
| brand | BrandDto | N | 브랜드 정보 |
| productImageUrl | String | N | 대표 이미지 URL |
| price | Price | N | 가격 정보 |
| insertDate | LocalDateTime | N | 등록일 (yyyy-MM-dd HH:mm:ss) |
| averageRating | double | N | 평균 평점 |
| reviewCount | long | N | 리뷰 수 |
| score | double | N | 상품 점수 |
| isFavorite | boolean | N | 즐겨찾기 여부 (기본값 false) |
| productStatus | ProductStatus | N | 판매 상태 |
| id | Long | Y (@JsonIgnore) | CursorValueProvider 구현 필드 |
| salePrice | long | Y (@JsonIgnore) | DiscountOffer 구현 필드 |
| discountRate | int | Y (@JsonIgnore) | DiscountOffer 구현 필드 |

#### BrandDto

| 필드 | 타입 | 설명 |
|------|------|------|
| brandId | long | 브랜드 ID |
| brandName | String | 브랜드명 |

#### Price (Embedded)

| 필드 | 타입 | DB 컬럼 | 설명 |
|------|------|---------|------|
| regularPrice | long | regular_price | 정가 |
| currentPrice | long | current_price | 현재 판매가 |
| salePrice | long | sale_price | 할인 적용가 |
| directDiscountRate | int | direct_discount_rate | 직접 할인율 |
| directDiscountPrice | long | direct_discount_price | 직접 할인 금액 |
| discountRate | int | discount_rate | 총 할인율 |

#### ProductStatus (Embedded)

| 필드 | 타입 | DB 컬럼 | 설명 |
|------|------|---------|------|
| soldOutYn | Yn (enum) | sold_out_yn | 품절 여부 (Y/N) |
| displayYn | Yn (enum) | display_yn | 노출 여부 (Y/N) |

### Response JSON 예시

```json
{
  "data": [
    {
      "productGroupId": 1001,
      "sellerId": 1,
      "productGroupName": "example_product_group_name",
      "brand": {
        "brandId": 10,
        "brandName": "example_brand"
      },
      "productImageUrl": "https://example.com/image.jpg",
      "price": {
        "regularPrice": 50000,
        "currentPrice": 45000,
        "salePrice": 40000,
        "directDiscountRate": 11,
        "directDiscountPrice": 5000,
        "discountRate": 20
      },
      "insertDate": "2024-01-01 00:00:00",
      "averageRating": 4.5,
      "reviewCount": 100,
      "score": 0.9,
      "isFavorite": false,
      "productStatus": {
        "soldOutYn": "N",
        "displayYn": "Y"
      }
    }
  ],
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

---

## 4. 호출 흐름

### 전체 호출 스택

```
[Client] GET /api/v1/product/group/seller/{sellerId}
    │
    ▼
ProductController.fetchProductGroupWithSeller(sellerId, pageable)
    │
    ▼
ProductGroupFindService.fetchProductGroupWithSeller(sellerId, pageable)
    │  [implements]
    ▼
ProductGroupFindServiceImpl.fetchProductGroupWithSeller(sellerId, pageable)
    │
    ├─── [1단계: Redis 캐시 조회]
    │       SellerProductRedisFindService.fetchProductGroupWithSeller(sellerId)
    │           └── SellerProductRedisFindServiceImpl
    │                   └── AbstractRedisService.getValue(key)
    │                           └── StringRedisTemplate.opsForValue().get(key)
    │                               key = "productsSeller::{sellerId}"
    │
    ├─── [캐시 HIT] → List<ProductGroupThumbnail> 반환 (DB 조회 생략)
    │
    └─── [캐시 MISS] → DB 조회 경로
            │
            ▼
        ProductGroupFindRepositoryImpl.fetchProductsWithSeller(sellerId, pageable)
            │  [QueryDSL]
            └── SELECT ... FROM product_group
                    INNER JOIN product_group_image
                    INNER JOIN brand
                    LEFT JOIN product_rating_stats
                    LEFT JOIN product_score
                    WHERE product_group.seller_id = {sellerId}
                    ORDER BY product_score.score DESC
                    LIMIT {pageSize}
            │
            ▼
        ProductGroupRedisQueryService.saveSellerProductGroupThumbnail(sellerId, result)
            └── ProductGroupRedisQueryServiceImpl
                    └── AbstractRedisService.save(key, value, ttl)
                            └── StringRedisTemplate.opsForValue().set(key, value, 1h)
                                key = "productsSeller::{sellerId}"
            │
            ▼
        List<ProductGroupThumbnail> 반환
```

### Redis 캐시 전략

| 항목 | 내용 |
|------|------|
| Redis Key 패턴 | `productsSeller::{sellerId}` |
| TTL | 1시간 |
| Cache-Aside 패턴 | 조회 시 캐시 없으면 DB 조회 후 캐시 저장 |
| 직렬화 | JsonUtils.toJson / JsonUtils.fromJsonList |

---

## 5. Database Query

### 사용 테이블

| 테이블 | Entity | JOIN 유형 | JOIN 조건 |
|--------|--------|-----------|----------|
| product_group | QProductGroup | FROM (기준) | - |
| product_group_image | QProductGroupImage | INNER JOIN | product_group_image.product_group_id = product_group.product_group_id AND image_type = 'MAIN' AND delete_yn = 'N' |
| brand | QBrand | INNER JOIN | brand.brand_id = product_group.brand_id |
| product_rating_stats | QProductRatingStats | LEFT JOIN | product_rating_stats.product_group_id = product_group.product_group_id |
| product_score | QProductScore | LEFT JOIN | product_score.product_group_id = product_group.product_group_id |

### WHERE 조건

| 조건 | 내용 |
|------|------|
| sellerIdEq | `product_group.seller_id = :sellerId` |

### ORDER BY

| 정렬 기준 | 방향 | NULL 처리 |
|-----------|------|----------|
| product_score.score | DESC | COALESCE(score, 0.0) |

### LIMIT

| 항목 | 내용 |
|------|------|
| 기준 | `pageable.getPageSize()` |
| 비고 | fetchProductsWithSeller는 pageSize 그대로 적용 (fetchProductGroups는 +1 오버페치) |

### GROUP BY (QueryDSL transform)

```
GroupBy.groupBy(productGroup.id)
    .list(new QProductGroupThumbnail(...))
```

### QueryDSL 코드 (fetchProductsWithSeller)

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
    .where(sellerIdEq(sellerId))         // product_group.seller_id = :sellerId
    .orderBy(productScore.score.coalesce(0.0).desc())
    .limit(pageable.getPageSize())
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

### 동등 SQL

```sql
SELECT
    pg.product_group_id,
    pg.seller_id,
    pg.product_group_name,
    b.brand_id,
    b.brand_name,
    pgi.image_url,
    pg.regular_price,
    pg.current_price,
    pg.sale_price,
    pg.direct_discount_rate,
    pg.direct_discount_price,
    pg.discount_rate,
    pg.insert_date,
    COALESCE(prs.average_rating, 0.0) AS average_rating,
    COALESCE(prs.review_count, 0) AS review_count,
    COALESCE(ps.score, 0.0) AS score,
    pg.sold_out_yn,
    pg.display_yn
FROM product_group pg
INNER JOIN product_group_image pgi
    ON pgi.product_group_id = pg.product_group_id
    AND pgi.product_group_image_type = 'MAIN'
    AND pgi.delete_yn = 'N'
INNER JOIN brand b
    ON b.brand_id = pg.brand_id
LEFT JOIN product_rating_stats prs
    ON prs.product_group_id = pg.product_group_id
LEFT JOIN product_score ps
    ON ps.product_group_id = pg.product_group_id
WHERE pg.seller_id = :sellerId
ORDER BY COALESCE(ps.score, 0.0) DESC
LIMIT :pageSize
```

---

## 6. Entity - 테이블 매핑

| Entity 클래스 | 테이블명 | PK 컬럼 |
|--------------|---------|---------|
| ProductGroup | product_group | product_group_id |
| ProductGroupImage | product_group_image | product_group_image_id |
| Brand | brand | brand_id |
| ProductRatingStats | product_rating_stats | product_group_id |
| ProductScore | product_score | product_group_id |
| Seller | seller | seller_id |

### product_group 주요 컬럼 (Embedded 포함)

| 컬럼명 | 타입 | Embedded 출처 |
|--------|------|--------------|
| product_group_id | BIGINT (PK) | - |
| product_group_name | VARCHAR | ProductGroupDetails |
| seller_id | BIGINT | ProductGroupDetails |
| brand_id | BIGINT | ProductGroupDetails |
| category_id | BIGINT | ProductGroupDetails |
| option_type | VARCHAR (Enum) | ProductGroupDetails |
| regular_price | BIGINT | Price |
| current_price | BIGINT | Price |
| sale_price | BIGINT | Price |
| direct_discount_rate | INT | Price |
| direct_discount_price | BIGINT | Price |
| discount_rate | INT | Price |
| sold_out_yn | VARCHAR (Enum) | ProductStatus |
| display_yn | VARCHAR (Enum) | ProductStatus |

### product_group_image 주요 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| product_group_image_id | BIGINT (PK) | - |
| product_group_id | BIGINT (FK) | product_group 참조 |
| product_group_image_type | VARCHAR (Enum) | MAIN / DETAIL 등 |
| image_url | VARCHAR | 이미지 URL |
| delete_yn | VARCHAR (Enum) | 소프트 삭제 여부 |

---

## 7. 관련 클래스 파일 경로

| 분류 | 파일 경로 |
|------|----------|
| Controller | bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/product/controller/ProductController.java |
| Service Interface | .../product/service/group/fetch/ProductGroupFindService.java |
| Service Impl | .../product/service/group/fetch/ProductGroupFindServiceImpl.java |
| Redis 조회 Interface | .../product/service/group/fetch/SellerProductRedisFindService.java |
| Redis 조회 Impl | .../product/service/group/fetch/SellerProductRedisFindServiceImpl.java |
| Redis 저장 Interface | .../product/service/group/query/ProductGroupRedisQueryService.java |
| Redis 저장 Impl | .../product/service/group/query/ProductGroupRedisQueryServiceImpl.java |
| Repository Interface | .../product/repository/group/ProductGroupFindRepository.java |
| Repository Impl | .../product/repository/group/ProductGroupFindRepositoryImpl.java |
| Response DTO | .../product/dto/ProductGroupThumbnail.java |
| Brand DTO | .../product/dto/brand/BrandDto.java |
| Entity: ProductGroup | .../product/entity/group/ProductGroup.java |
| Embedded: Price | .../product/entity/group/embedded/Price.java |
| Embedded: ProductStatus | .../product/entity/group/embedded/ProductStatus.java |
| Embedded: ProductGroupDetails | .../product/entity/group/embedded/ProductGroupDetails.java |
| Entity: ProductGroupImage | .../product/entity/image/ProductGroupImage.java |
| Embedded: ImageDetail | .../product/entity/image/embedded/ImageDetail.java |
| Entity: Brand | .../brand/entity/Brand.java |
| Entity: Seller | .../seller/entity/Seller.java |
| Entity: ProductRatingStats | .../review/entity/ProductRatingStats.java |
| Entity: ProductScore | .../search/entity/ProductScore.java |
| Redis 공통 추상 | .../common/service/AbstractRedisService.java |
| Redis Key Enum | .../common/enums/RedisKey.java |
| API Wrapper | .../payload/ApiResponse.java |

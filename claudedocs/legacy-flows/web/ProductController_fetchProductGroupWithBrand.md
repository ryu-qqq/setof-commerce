# API Flow: ProductController.fetchProductGroupWithBrand

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| API Path | `/api/v1/product/group/brand/{brandId}` |
| Controller | `ProductController` |
| Service Interface | `ProductGroupFindService` |
| Service Impl | `ProductGroupFindServiceImpl` |
| Repository Interface | `ProductGroupFindRepository` |
| Repository Impl | `ProductGroupFindRepositoryImpl` |

---

## 2. Request

### Path Variables

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| brandId | long | Y | 브랜드 식별자 |

### Query Parameters (Pageable)

| 이름 | 타입 | 필수 | 기본값 | 설명 |
|------|------|------|--------|------|
| page | int | N | 0 | 페이지 번호 |
| size | int | N | 20 | 페이지 크기 (limit으로 사용됨) |
| sort | String | N | - | 정렬 기준 |

### Request 예시

```
GET /api/v1/product/group/brand/1?size=20
```

---

## 3. Response

### DTO 구조

```
ApiResponse<List<ProductGroupThumbnail>>
└── ProductGroupThumbnail
    ├── productGroupId     : long
    ├── sellerId           : long
    ├── productGroupName   : String
    ├── brand              : BrandDto
    │   ├── brandId        : long
    │   └── brandName      : String
    ├── productImageUrl    : String
    ├── price              : Price (Embedded)
    │   ├── regularPrice         : long
    │   ├── currentPrice         : long
    │   ├── salePrice            : long
    │   ├── directDiscountRate   : int
    │   ├── directDiscountPrice  : long
    │   └── discountRate         : int
    ├── insertDate         : LocalDateTime  (@JsonFormat "yyyy-MM-dd HH:mm:ss")
    ├── averageRating      : double
    ├── reviewCount        : long
    ├── score              : double
    ├── isFavorite         : boolean        (항상 false로 초기화)
    └── productStatus      : ProductStatus (Embedded)
        ├── soldOutYn      : Yn  (Y|N)
        └── displayYn      : Yn  (Y|N)
```

**@JsonIgnore 필드** (응답 JSON에서 제외):
- `getId()` → productGroupId 반환 (CursorValueProvider용)
- `getSalePrice()` → price.salePrice 반환 (DiscountOffer용)
- `getDiscountRate()` → price.discountRate 반환 (DiscountOffer용)
- `ProductStatus.isSoldOut()`

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "productGroupId": 123,
      "sellerId": 45,
      "productGroupName": "example_product_name",
      "brand": {
        "brandId": 1,
        "brandName": "example_brand"
      },
      "productImageUrl": "https://cdn.example.com/image.jpg",
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
      "reviewCount": 128,
      "score": 87.3,
      "isFavorite": false,
      "productStatus": {
        "soldOutYn": "N",
        "displayYn": "Y"
      }
    }
  ]
}
```

---

## 4. 호출 흐름

### 전체 호출 스택

```
ProductController.fetchProductGroupWithBrand(brandId, pageable)
    └── ProductGroupFindService.fetchProductGroupWithBrand(brandId, pageable)
            └── ProductGroupFindServiceImpl.fetchProductGroupWithBrand(brandId, pageable)
                    │
                    ├── [1단계: Redis 캐시 조회]
                    │   BrandProductRedisFindService.fetchProductGroupWithBrand(brandId)
                    │       └── BrandProductRedisFindServiceImpl.fetchProductGroupWithBrand(brandId)
                    │               ├── generateKey(RedisKey.PRODUCT_BRAND, brandId)
                    │               │   → key: "productsBrand::{brandId}"
                    │               ├── getValue(key)  [Redis GET]
                    │               └── JsonUtils.fromJsonList(data, ProductGroupThumbnail.class)
                    │
                    └── [2단계: Redis MISS 시 DB 조회 + 캐시 저장]
                        ProductGroupFindServiceImpl.fetchProductGroupWithBrandInDb(brandId, pageable)
                            ├── ProductGroupFindRepository.fetchProductsWithBrand(brandId, pageable)
                            │       └── ProductGroupFindRepositoryImpl.fetchProductsWithBrand(brandId, pageable)
                            │               [QueryDSL - 아래 5번 섹션 참조]
                            │
                            └── ProductGroupRedisQueryService.saveBrandProductGroupThumbnail(brandId, thumbnails)
                                    └── ProductGroupRedisQueryServiceImpl.saveBrandProductGroupThumbnail()
                                            ├── generateKey(RedisKey.PRODUCT_BRAND, brandId)
                                            ├── JsonUtils.toJson(productGroupThumbnails)
                                            └── save(key, value, Duration.ofHours(1))
                                                [Redis SET with TTL 1시간]
```

### 분기 로직

```
brandProductRedisFindService.fetchProductGroupWithBrand(brandId) 결과
    ├── 비어있지 않음 → Redis 캐시 데이터 즉시 반환
    └── 비어있음    → DB 조회 후 Redis에 저장 → 반환
```

---

## 5. Database Query

### 조회 대상 테이블

| 테이블 | Entity 클래스 | QueryDSL 변수 | JOIN 타입 | JOIN 조건 |
|--------|--------------|--------------|----------|----------|
| `product_group` | `ProductGroup` | `productGroup` | FROM (기준) | - |
| `product_group_image` | `ProductGroupImage` | `productGroupImage` | INNER JOIN | `productGroupImage.product_group_id = productGroup.product_group_id` |
| `brand` | `Brand` | `brand` | INNER JOIN | `brand.brand_id = productGroup.brand_id` |
| `product_rating_stats` | `ProductRatingStats` | `productRatingStats` | LEFT JOIN | `productRatingStats.product_group_id = productGroup.product_group_id` |
| `product_score` | `ProductScore` | `productScore` | LEFT JOIN | `productScore.product_group_id = productGroup.product_group_id` |

### WHERE 조건

| 조건 | 내용 |
|------|------|
| brandId 필터 | `productGroup.brand_id = :brandId` |
| 이미지 타입 필터 | `productGroupImage.product_group_image_type = 'MAIN'` |
| 이미지 삭제 여부 | `productGroupImage.delete_yn = 'N'` |

### ORDER BY / LIMIT

| 항목 | 내용 |
|------|------|
| 정렬 | `productScore.score DESC` (null인 경우 0.0으로 처리) |
| LIMIT | `pageable.getPageSize()` |

### QueryDSL 코드 (`fetchProductsWithBrand`)

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
    .where(brandIdEq(brandId))                          // productGroup.brand_id = :brandId
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
                productGroup.productGroupDetails.productStatus
            )))
```

### 테이블-컬럼 매핑

#### product_group

| 컬럼명 | Java 필드 | 사용 목적 |
|--------|----------|----------|
| `product_group_id` | `ProductGroup.id` | PK, JOIN 기준 |
| `product_group_name` | `ProductGroupDetails.productGroupName` | 상품그룹명 |
| `seller_id` | `ProductGroupDetails.sellerId` | 판매자 ID |
| `brand_id` | `ProductGroupDetails.brandId` | 브랜드 ID (WHERE 조건) |
| `category_id` | `ProductGroupDetails.categoryId` | 카테고리 ID |
| `regular_price` | `Price.regularPrice` | 정가 |
| `current_price` | `Price.currentPrice` | 현재가 |
| `sale_price` | `Price.salePrice` | 판매가 |
| `direct_discount_rate` | `Price.directDiscountRate` | 즉시할인율 |
| `direct_discount_price` | `Price.directDiscountPrice` | 즉시할인금액 |
| `discount_rate` | `Price.discountRate` | 할인율 |
| `sold_out_yn` | `ProductStatus.soldOutYn` | 품절 여부 (Y/N) |
| `display_yn` | `ProductStatus.displayYn` | 노출 여부 (Y/N) |
| `insert_date` | `BaseEntity.insertDate` | 등록일시 |

#### product_group_image

| 컬럼명 | Java 필드 | 사용 목적 |
|--------|----------|----------|
| `product_group_image_id` | `ProductGroupImage.id` | PK |
| `product_group_id` | FK (productGroup) | JOIN 조건 |
| `product_group_image_type` | `ImageDetail.productGroupImageType` | 이미지 유형 필터 (`MAIN`) |
| `image_url` | `ImageDetail.imageUrl` | 대표 이미지 URL |
| `delete_yn` | `BaseEntity.deleteYn` | 소프트 삭제 여부 |

#### brand

| 컬럼명 | Java 필드 | 사용 목적 |
|--------|----------|----------|
| `brand_id` | `Brand.id` | PK, JOIN 조건 |
| `brand_name` | `Brand.brandName` | 브랜드명 |

#### product_rating_stats

| 컬럼명 | Java 필드 | 사용 목적 |
|--------|----------|----------|
| `product_group_id` | `ProductRatingStats.id` | PK (= product_group_id), LEFT JOIN |
| `average_rating` | `ProductRatingStats.averageRating` | 평균 평점 (null → 0.0) |
| `review_count` | `ProductRatingStats.reviewCount` | 리뷰 수 (null → 0) |

#### product_score

| 컬럼명 | Java 필드 | 사용 목적 |
|--------|----------|----------|
| `product_group_id` | `ProductScore.id` | PK (= product_group_id), LEFT JOIN |
| `score` | `ProductScore.score` | 상품 점수, ORDER BY 기준 (null → 0.0) |

---

## 6. Redis 캐시 전략

| 항목 | 내용 |
|------|------|
| Redis Key 패턴 | `productsBrand::{brandId}` |
| TTL | 1시간 (`RedisKey.PRODUCT_BRAND.hour = 1`) |
| 캐시 Hit | Redis에서 JSON 역직렬화 후 즉시 반환 |
| 캐시 Miss | DB 조회 → Redis 저장 → 반환 |
| 직렬화 | `JsonUtils.toJson()` / `JsonUtils.fromJsonList()` |

---

## 7. 사용 클래스 목록

### Controller Layer

| 클래스 | 경로 |
|--------|------|
| `ProductController` | `module/product/controller/ProductController.java` |

### Service Layer

| 클래스 | 역할 |
|--------|------|
| `ProductGroupFindService` (interface) | Service 계약 정의 |
| `ProductGroupFindServiceImpl` | 캐시 분기 및 DB 조회 조율 |
| `BrandProductRedisFindService` (interface) | Redis 조회 계약 |
| `BrandProductRedisFindServiceImpl` | Redis에서 브랜드별 상품 목록 조회 |
| `ProductGroupRedisQueryService` (interface) | Redis 저장 계약 |
| `ProductGroupRedisQueryServiceImpl` | Redis에 브랜드별 상품 목록 저장 |

### Repository Layer

| 클래스 | 역할 |
|--------|------|
| `ProductGroupFindRepository` (interface) | DB 조회 계약 |
| `ProductGroupFindRepositoryImpl` | QueryDSL 기반 DB 조회 구현 |

### DTO

| 클래스 | 역할 |
|--------|------|
| `ProductGroupThumbnail` | 응답 최상위 DTO |
| `BrandDto` | 브랜드 정보 (Projection) |
| `Price` | 가격 정보 (Embeddable) |
| `ProductStatus` | 상품 상태 (Embeddable) |
| `QProductGroupThumbnail` | QueryDSL Projection |
| `QBrandDto` | QueryDSL Projection |

### Entity

| 클래스 | 테이블 |
|--------|--------|
| `ProductGroup` | `product_group` |
| `ProductGroupDetails` | `product_group` (Embedded) |
| `Price` | `product_group` (Embedded) |
| `ProductStatus` | `product_group` (Embedded) |
| `Brand` | `brand` |
| `ProductGroupImage` | `product_group_image` |
| `ProductRatingStats` | `product_rating_stats` |
| `ProductScore` | `product_score` |

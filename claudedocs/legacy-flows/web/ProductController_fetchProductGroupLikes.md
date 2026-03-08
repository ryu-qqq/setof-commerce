# API Flow: ProductController.fetchProductGroupLikes

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | GET |
| API Path | /api/v1/products/group/recent |
| Controller | ProductController |
| Service Interface | ProductGroupFindService |
| Service Impl | ProductGroupFindServiceImpl |
| Repository Interface | ProductGroupFindRepository |
| Repository Impl | ProductGroupFindRepositoryImpl |
| Mapper | ProductGroupMapper → ProductGroupMapperImpl |

---

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | 설명 |
|------|------|------|------|------|
| productGroupIds | List\<Long\> | @RequestParam | 필수 | 조회할 상품 그룹 ID 목록 (찜 목록 등에서 전달) |

### 요청 예시

```
GET /api/v1/products/group/recent?productGroupIds=1,2,3
```

---

## 3. Response

### DTO 구조

```
ApiResponse<List<ProductGroupThumbnail>>
├── data: List<ProductGroupThumbnail>
│   └── ProductGroupThumbnail
│       ├── productGroupId: long               // 상품 그룹 ID
│       ├── sellerId: long                     // 판매자 ID
│       ├── productGroupName: String           // 상품 그룹명
│       ├── brand: BrandDto
│       │   ├── brandId: long                  // 브랜드 ID
│       │   └── brandName: String              // 브랜드명
│       ├── productImageUrl: String            // 메인 이미지 URL (MAIN 타입)
│       ├── price: Price
│       │   ├── regularPrice: long             // 정가 (regular_price)
│       │   ├── currentPrice: long             // 현재가 (current_price)
│       │   ├── salePrice: long                // 판매가 (sale_price)
│       │   ├── directDiscountRate: int        // 직접 할인율 (direct_discount_rate)
│       │   ├── directDiscountPrice: long      // 직접 할인액 (direct_discount_price)
│       │   └── discountRate: int             // 할인율 (discount_rate)
│       ├── insertDate: LocalDateTime          // 등록일 (yyyy-MM-dd HH:mm:ss)
│       ├── averageRating: double             // 평균 평점 (없으면 0.0)
│       ├── reviewCount: long                  // 리뷰 수 (없으면 0)
│       ├── score: double                      // 상품 점수 (없으면 0.0)
│       ├── isFavorite: boolean               // 찜 여부 (QueryDSL 조회 시 항상 false)
│       └── productStatus: ProductStatus
│           ├── soldOutYn: Yn (Y/N)           // 품절 여부 (sold_out_yn)
│           └── displayYn: Yn (Y/N)           // 노출 여부 (display_yn)
└── response: Response
    ├── status: int                            // 200
    └── message: String                        // "success"
```

**@JsonIgnore 제외 필드**: `getId()`, `getSalePrice()`, `getDiscountRate()`, `isSoldOut()`

### 응답 JSON 예시

```json
{
  "data": [
    {
      "productGroupId": 1,
      "sellerId": 10,
      "productGroupName": "클래식 린넨 셔츠",
      "brand": {
        "brandId": 5,
        "brandName": "BRAND_NAME"
      },
      "productImageUrl": "https://cdn.example.com/images/main.jpg",
      "price": {
        "regularPrice": 89000,
        "currentPrice": 89000,
        "salePrice": 71200,
        "directDiscountRate": 20,
        "directDiscountPrice": 17800,
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
  ],
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

**참고**: 응답 순서는 요청한 `productGroupIds` 순서와 동일하게 재정렬됨 (`reOrderProductGroupThumbnail`).

---

## 4. 호출 흐름

```
HTTP GET /api/v1/products/group/recent?productGroupIds=1,2,3
    │
    ▼
ProductController.fetchProductGroupLikes(@RequestParam List<Long> productGroupIds)
    │
    ▼
ProductGroupFindService.fetchProductGroupRecent(List<Long> productGroupIds)
    │
    ▼
ProductGroupFindServiceImpl.fetchProductGroupRecent(List<Long> productGroupIds)
    │
    ├─ 1. ProductGroupFindRepository.fetchProductGroupsRecent(productGroupIds)
    │       └── ProductGroupFindRepositoryImpl.fetchProductGroupsRecent(List<Long> productGroupIds)
    │               └── QueryDSL: FROM product_group
    │                             INNER JOIN product_group_image (MAIN 타입, deleteYn=N)
    │                             INNER JOIN brand
    │                             LEFT JOIN product_rating_stats
    │                             LEFT JOIN product_score
    │                             WHERE product_group.id IN (productGroupIds)
    │                               AND display_yn = 'Y'
    │                             → List<ProductGroupThumbnail> (순서 미보장)
    │
    └─ 2. ProductGroupMapper.reOrderProductGroupThumbnail(productGroupIds, productGroupThumbnails)
            └── ProductGroupMapperImpl.reOrderProductGroupThumbnail(...)
                    └── productGroupIds 순서 기준으로 Map 조회 후 LinkedList 재구성
                        → List<ProductGroupThumbnail> (요청 ID 순서 보장)
    │
    ▼
ResponseEntity<ApiResponse<List<ProductGroupThumbnail>>>
```

---

## 5. Database Query

### 사용 테이블

| 테이블 | Entity | Q클래스 | JOIN 종류 | JOIN 조건 |
|--------|--------|---------|----------|----------|
| product_group | ProductGroup | QProductGroup | FROM (기준) | - |
| product_group_image | ProductGroupImage | QProductGroupImage | INNER JOIN | productGroup.id = product_group_image.product_group_id AND image_type = 'MAIN' AND delete_yn = 'N' |
| brand | Brand | QBrand | INNER JOIN | brand.id = product_group.brand_id |
| product_rating_stats | ProductRatingStats | QProductRatingStats | LEFT JOIN | product_rating_stats.product_group_id = product_group.id |
| product_score | ProductScore | QProductScore | LEFT JOIN | product_score.product_group_id = product_group.id |

### WHERE 조건

| 조건 | 컬럼 | 값 |
|------|------|-----|
| 상품 그룹 ID 필터 | product_group.product_group_id | IN (productGroupIds) |
| 노출 상품만 조회 | product_group.display_yn | = 'Y' |

### ORDER BY

없음 (정렬 없이 조회 후 애플리케이션 레벨에서 `productGroupIds` 순서로 재정렬)

### QueryDSL 코드 (fetchProductGroupsRecent)

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
        productGroup.id.in(productGroupIds),   // productGroupIdIn()
        productGroup.productGroupDetails.productStatus.displayYn.eq(Yn.Y)  // onSaleProduct()
    )
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
            ))
    );
```

### DB 컬럼 매핑

#### product_group 테이블

| 컬럼명 | Entity 필드 | 설명 |
|--------|------------|------|
| product_group_id | ProductGroup.id | PK |
| product_group_name | ProductGroupDetails.productGroupName | 상품 그룹명 |
| seller_id | ProductGroupDetails.sellerId | 판매자 ID (FK) |
| brand_id | ProductGroupDetails.brandId | 브랜드 ID (FK) |
| category_id | ProductGroupDetails.categoryId | 카테고리 ID (FK) |
| regular_price | Price.regularPrice | 정가 |
| current_price | Price.currentPrice | 현재가 |
| sale_price | Price.salePrice | 판매가 |
| direct_discount_rate | Price.directDiscountRate | 직접 할인율 |
| direct_discount_price | Price.directDiscountPrice | 직접 할인액 |
| discount_rate | Price.discountRate | 할인율 |
| sold_out_yn | ProductStatus.soldOutYn | 품절 여부 |
| display_yn | ProductStatus.displayYn | 노출 여부 |
| insert_date | BaseEntity.insertDate | 등록일 |

#### product_group_image 테이블

| 컬럼명 | Entity 필드 | 설명 |
|--------|------------|------|
| product_group_image_id | ProductGroupImage.id | PK |
| product_group_id | ProductGroupImage.productGroup.id | FK (product_group) |
| image_url | ImageDetail.imageUrl | 이미지 URL |
| product_group_image_type | ImageDetail.productGroupImageType | 이미지 타입 (MAIN 고정) |
| delete_yn | BaseEntity.deleteYn | 삭제 여부 |

#### brand 테이블

| 컬럼명 | Entity 필드 | 설명 |
|--------|------------|------|
| brand_id | Brand.id | PK |
| brand_name | Brand.brandName | 브랜드명 |

#### product_rating_stats 테이블

| 컬럼명 | Entity 필드 | 설명 |
|--------|------------|------|
| product_group_id | ProductRatingStats.id | PK (= product_group.product_group_id) |
| average_rating | ProductRatingStats.averageRating | 평균 평점 |
| review_count | ProductRatingStats.reviewCount | 리뷰 수 |

#### product_score 테이블

| 컬럼명 | Entity 필드 | 설명 |
|--------|------------|------|
| product_group_id | ProductScore.id | PK (= product_group.product_group_id) |
| score | ProductScore.score | 상품 점수 |

---

## 6. 주요 특징

| 항목 | 내용 |
|------|------|
| 트랜잭션 | @Transactional(readOnly = true) |
| 캐시 레이어 | 없음 (Redis 미사용, 직접 DB 조회) |
| 이미지 필터 | ProductGroupImageType.MAIN 타입만 조회 |
| 정렬 방식 | DB 정렬 없음, 요청 ID 순서로 애플리케이션 재정렬 |
| NULL 처리 | averageRating, reviewCount, score는 coalesce(0) 기본값 처리 |
| displayYn 필터 | display_yn = 'Y' (노출 상품만 반환) |
| isFavorite | QueryDSL Projection 생성자에서 항상 false로 초기화 |

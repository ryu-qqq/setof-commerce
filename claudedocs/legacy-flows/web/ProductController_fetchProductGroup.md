# API Flow: ProductController.fetchProductGroup

## 1. 기본 정보

| 항목 | 값 |
|------|----|
| HTTP Method | GET |
| API Path | /api/v1/product/group/{productGroupId} |
| Controller | ProductController |
| Service Interface | ProductGroupFindService |
| Service Impl | ProductGroupFindServiceImpl |
| Repository Interface | ProductGroupFindRepository |
| Repository Impl | ProductGroupFindRepositoryImpl |
| 트랜잭션 | @Transactional(readOnly = true) |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| productGroupId | long | 필수 | 상품 그룹 ID |

### Request Body

없음

### 요청 예시

```
GET /api/v1/product/group/123
```

---

## 3. Response

### 최상위 구조: `ApiResponse<ProductGroupResponse>`

```json
{
  "success": true,
  "data": {
    "productGroup": { ... },
    "productNotices": { ... },
    "productGroupImages": [ ... ],
    "products": [ ... ],
    "categories": [ ... ],
    "detailDescription": "https://cdn.example.com/detail/123.html",
    "mileageRate": 0.0,
    "expectedMileageAmount": 0.0,
    "isFavorite": false,
    "eventProductType": "NORMAL"
  }
}
```

### ProductGroupResponse 필드

| 필드명 | 타입 | JsonIgnore | 설명 |
|--------|------|------------|------|
| productGroup | ProductGroupDto | - | 상품 그룹 기본 정보 |
| productNotices | ProductNoticeDto | - | 상품 고시 정보 |
| productGroupImages | Set\<ProductImageDto\> | - | 상품 이미지 목록 (MAIN 이미지 우선 정렬) |
| products | Set\<ProductDto\> | - | 개별 상품 목록 (옵션 포함) |
| categories | Set\<ProductCategoryDto\> | - | 카테고리 계층 목록 |
| detailDescription | String | - | 상세 설명 이미지 URL |
| mileageRate | double | - | 마일리지 적립률 |
| expectedMileageAmount | double | - | 예상 마일리지 금액 |
| isFavorite | boolean | - | 즐겨찾기 여부 |
| eventProductType | EventProductType | - | 이벤트 상품 유형 (NORMAL / RAFFLE 등) |

### ProductGroupDto 필드

| 필드명 | 타입 | 설명 |
|--------|------|------|
| productGroupId | long | 상품 그룹 ID |
| productGroupName | String | 상품 그룹명 |
| sellerId | long | 셀러 ID |
| sellerName | String | 셀러명 |
| brand | BrandDto | 브랜드 정보 |
| categoryId | long | 카테고리 ID |
| price | Price | 가격 정보 (Embedded) |
| optionType | OptionType | 옵션 유형 (SINGLE / MULTI / NONE) |
| clothesDetailInfo | ClothesDetailDto | 의류 상세 정보 |
| deliveryNotice | DeliveryNotice | 배송 안내 (Embedded) |
| refundNotice | RefundNoticeDto | 반품 안내 |
| productGroupMainImageUrl | String | 대표 이미지 URL |
| productStatus | ProductStatus | 판매 상태 |
| insertDate | LocalDateTime | 등록일 |
| updateDate | LocalDateTime | 수정일 |
| averageRating | double | 평균 평점 |
| reviewCount | long | 리뷰 수 |

### Price Embedded 필드

| 필드명 | 컬럼명 | 타입 | 설명 |
|--------|--------|------|------|
| regularPrice | regular_price | long | 정가 |
| currentPrice | current_price | long | 현재가 |
| salePrice | sale_price | long | 판매가 (할인 적용) |
| directDiscountRate | direct_discount_rate | int | 직접 할인율 (%) |
| directDiscountPrice | direct_discount_price | long | 직접 할인 금액 |
| discountRate | discount_rate | int | 총 할인율 (%) |

### ProductStatus Embedded 필드

| 필드명 | 컬럼명 | 타입 | 설명 |
|--------|--------|------|------|
| soldOutYn | sold_out_yn | Yn (Y/N) | 품절 여부 |
| displayYn | display_yn | Yn (Y/N) | 노출 여부 |

### BrandDto 필드

| 필드명 | 타입 | 설명 |
|--------|------|------|
| brandId | long | 브랜드 ID |
| brandName | String | 브랜드명 |

### ProductNoticeDto 필드

| 필드명 | JsonIgnore | 타입 | 설명 |
|--------|------------|------|------|
| productNoticeId | Y | long | 고시 ID (응답 미포함) |
| material | - | String | 소재 |
| color | - | String | 색상 |
| size | - | String | 사이즈 |
| maker | - | String | 제조사 |
| origin | - | String | 원산지 (Origin enum displayName) |
| washingMethod | - | String | 세탁방법 |
| yearMonth | - | String | 제조연월 |
| assuranceStandard | - | String | 품질보증기준 |
| asPhone | - | String | A/S 전화번호 |

### ProductImageDto 필드

| 필드명 | JsonIgnore | 타입 | 설명 |
|--------|------------|------|------|
| productGroupImageId | Y | long | 이미지 ID (응답 미포함) |
| productGroupImageType | - | ProductGroupImageType | 이미지 유형 (MAIN / SUB 등) |
| imageUrl | - | String | 이미지 URL |

### ProductDto 필드

| 필드명 | 타입 | JsonInclude | 설명 |
|--------|------|-------------|------|
| productId | long | - | 개별 상품 ID |
| stockQuantity | int | - | 재고 수량 |
| productStatus | ProductStatus | - | 개별 상품 판매 상태 |
| option | String | NON_DEFAULT | 옵션값 문자열 (옵션 없으면 빈 문자열) |
| options | Set\<OptionDto\> | NON_EMPTY | 옵션 상세 목록 |

### OptionDto 필드

| 필드명 | 타입 | 설명 |
|--------|------|------|
| optionGroupId | long | 옵션 그룹 ID |
| optionDetailId | long | 옵션 상세 ID |
| optionName | OptionName | 옵션명 (SIZE / COLOR 등) |
| optionValue | String | 옵션값 ("XL", "RED" 등) |

### ProductCategoryDto 필드

| 필드명 | JsonIgnore | 타입 | 설명 |
|--------|------------|------|------|
| productGroupId | Y | long | 상품 그룹 ID (응답 미포함) |
| categoryId | - | long | 카테고리 ID |
| categoryName | - | String | 카테고리명 |
| displayName | - | String | 표시명 |
| parentCategoryId | - | long | 부모 카테고리 ID |
| categoryDepth | - | int | 카테고리 깊이 |

### RefundNoticeDto 필드

| 필드명 | 타입 | 설명 |
|--------|------|------|
| returnMethodDomestic | String | 반품 방법 (ReturnMethod enum displayName) |
| returnCourierDomestic | String | 반품 택배사 (ShipmentCompanyCode enum displayName) |
| returnChargeDomestic | int | 반품 배송비 |
| returnExchangeAreaDomestic | String | 반품/교환 가능 지역 |

### 응답 JSON 전체 예시

```json
{
  "success": true,
  "data": {
    "productGroup": {
      "productGroupId": 123,
      "productGroupName": "example_product_name",
      "sellerId": 1,
      "sellerName": "example_seller",
      "brand": {
        "brandId": 10,
        "brandName": "example_brand"
      },
      "categoryId": 50,
      "price": {
        "regularPrice": 50000,
        "currentPrice": 45000,
        "salePrice": 40500,
        "directDiscountRate": 10,
        "directDiscountPrice": 4500,
        "discountRate": 19
      },
      "optionType": "SINGLE",
      "clothesDetailInfo": {
        "productCondition": "NEW",
        "origin": "KOREA"
      },
      "deliveryNotice": {
        "deliveryType": "PARCEL",
        "deliveryCompany": "CJ",
        "deliveryCharge": 3000,
        "freeDeliveryCondition": 50000
      },
      "refundNotice": {
        "returnMethodDomestic": "택배",
        "returnCourierDomestic": "CJ대한통운",
        "returnChargeDomestic": 3000,
        "returnExchangeAreaDomestic": "전국"
      },
      "productGroupMainImageUrl": "https://cdn.example.com/main/123.jpg",
      "productStatus": {
        "soldOutYn": "N",
        "displayYn": "Y"
      },
      "insertDate": "2024-01-01 00:00:00",
      "updateDate": "2024-06-01 00:00:00",
      "averageRating": 4.5,
      "reviewCount": 120
    },
    "productNotices": {
      "material": "면 100%",
      "color": "블랙",
      "size": "S/M/L/XL",
      "maker": "example_maker",
      "origin": "국내산",
      "washingMethod": "손세탁",
      "yearMonth": "2024-01",
      "assuranceStandard": "소비자분쟁해결기준에 의거",
      "asPhone": "1588-0000"
    },
    "productGroupImages": [
      {
        "productGroupImageType": "MAIN",
        "imageUrl": "https://cdn.example.com/main/123.jpg"
      },
      {
        "productGroupImageType": "SUB",
        "imageUrl": "https://cdn.example.com/sub/123_1.jpg"
      }
    ],
    "products": [
      {
        "productId": 1001,
        "stockQuantity": 50,
        "productStatus": {
          "soldOutYn": "N",
          "displayYn": "Y"
        },
        "options": [
          {
            "optionGroupId": 1,
            "optionDetailId": 11,
            "optionName": "SIZE",
            "optionValue": "M"
          }
        ]
      }
    ],
    "categories": [
      {
        "categoryId": 1,
        "categoryName": "의류",
        "displayName": "의류",
        "parentCategoryId": 0,
        "categoryDepth": 1
      },
      {
        "categoryId": 50,
        "categoryName": "티셔츠",
        "displayName": "티셔츠",
        "parentCategoryId": 1,
        "categoryDepth": 2
      }
    ],
    "detailDescription": "https://cdn.example.com/detail/123.html",
    "mileageRate": 0.0,
    "expectedMileageAmount": 0.0,
    "isFavorite": false,
    "eventProductType": "NORMAL"
  }
}
```

---

## 4. 호출 흐름

```
ProductController.fetchProductGroup(productGroupId)
    └── ProductGroupFindService.fetchProductGroup(productGroupId)
            └── ProductGroupFindServiceImpl.fetchProductGroup(productGroupId)
                    │
                    ├── [1] fetchProductGroupDto(productGroupId)
                    │       └── ProductGroupFindRepository.fetchProductGroupDto(productGroupId)
                    │               └── ProductGroupFindRepositoryImpl.fetchProductGroupDto(productGroupId)
                    │                       │
                    │                       ├── [쿼리 1] fetchBasicInfo(productGroupId)
                    │                       │   → product_group, seller, brand, category,
                    │                       │     product_delivery, product_notice,
                    │                       │     product_group_detail_description,
                    │                       │     product_rating_stats 조회
                    │                       │   → ProductGroupFetchDto 생성
                    │                       │
                    │                       ├── [쿼리 2] fetchProducts(productGroupId)
                    │                       │   → product, product_stock,
                    │                       │     product_option, option_group, option_detail 조회
                    │                       │   → Set<ProductFetchDto> 생성
                    │                       │   → basicInfo.setProducts(products) 주입
                    │                       │
                    │                       └── [쿼리 3] fetchImages(productGroupId)
                    │                           → product_group_image 조회
                    │                           → Set<ProductImageDto> 생성
                    │                           → basicInfo.setProductImages(images) 주입
                    │
                    ├── [2] getCategoryListForProductGroup(productGroup.getPath())
                    │       └── ProductCategoryFetchService.fetchProductCategories(path)
                    │               └── ProductCategoryFetchServiceImpl.fetchProductCategories(path)
                    │                       → path를 "," 기준으로 split → Set<Long> categoryIds
                    │                       └── CategoryFindServiceImpl.fetchCategoryList(categoryIds)
                    │                               └── CategoryFindRepository.fetchProductCategoryList(categoryIds)
                    │                                   → category 테이블 조회
                    │                                   → List<ProductCategoryDto> 반환
                    │
                    └── [3] ProductGroupMapper.toProductGroupResponse(productGroup, productCategoryList)
                            └── ProductGroupMapperImpl.toProductGroupResponse(...)
                                    ├── productGroup.setCategories(productCategoryList)
                                    ├── toProductGroupDto(productGroup) → ProductGroupDto 생성
                                    ├── transProductDto(products)
                                    │   → ProductFetchDto 목록을 productId 기준으로 그룹핑
                                    │   → OptionDto Set 생성
                                    │   → ProductDto Set 변환
                                    ├── sortProductImages(images)
                                    │   → MAIN 이미지 우선 정렬 (LinkedHashSet)
                                    └── ProductGroupResponse.builder().build()
```

---

## 5. Database Query

### 쿼리 1: 기본 정보 조회 (fetchBasicInfo)

**조회 테이블**

| 테이블 | Entity | JOIN 유형 | JOIN 조건 |
|--------|--------|-----------|-----------|
| product_group | ProductGroup | FROM (기준) | - |
| seller | Seller | INNER JOIN | seller.id = product_group.seller_id |
| category | Category | INNER JOIN | category.id = product_group.category_id |
| brand | Brand | INNER JOIN | brand.id = product_group.brand_id |
| product_delivery | ProductDelivery | INNER JOIN | product_delivery.product_group_id = product_group.product_group_id |
| product_notice | ProductNotice | INNER JOIN | product_notice.product_group_id = product_group.product_group_id |
| product_group_detail_description | ProductGroupDetailDescription | INNER JOIN | product_group_detail_description.id = product_group.product_group_id AND delete_yn = 'N' |
| product_rating_stats | ProductRatingStats | LEFT JOIN | product_rating_stats.id = product_group.product_group_id |

**WHERE 조건**

```sql
product_group.product_group_id = :productGroupId
```

**SELECT 컬럼**

| 컬럼 | 테이블 | 설명 |
|------|--------|------|
| product_group_id | product_group | 상품 그룹 ID |
| product_group_name | product_group | 상품 그룹명 |
| seller_id (id) | seller | 셀러 ID |
| seller_name | seller | 셀러명 |
| brand_id (id) | brand | 브랜드 ID |
| brand_name | brand | 브랜드명 |
| category_id (id) | category | 카테고리 ID |
| path | category | 카테고리 경로 (쉼표 구분 ID 문자열) |
| regular_price | product_group | 정가 |
| current_price | product_group | 현재가 |
| sale_price | product_group | 판매가 |
| direct_discount_rate | product_group | 직접 할인율 |
| direct_discount_price | product_group | 직접 할인 금액 |
| discount_rate | product_group | 총 할인율 |
| sold_out_yn | product_group | 품절 여부 |
| display_yn | product_group | 노출 여부 |
| option_type | product_group | 옵션 유형 |
| product_condition | product_group | 상품 상태 (의류 상세) |
| origin (clothesDetail) | product_group | 원산지 (의류 상세) |
| delivery_notice | product_delivery | 배송 안내 (Embedded) |
| return_method_domestic | product_delivery | 반품 방법 |
| return_courier_domestic | product_delivery | 반품 택배사 |
| return_charge_domestic | product_delivery | 반품 비용 |
| return_exchange_area_domestic | product_delivery | 반품/교환 지역 |
| average_rating | product_rating_stats | 평균 평점 (NULL 시 0.0) |
| review_count | product_rating_stats | 리뷰 수 (NULL 시 0) |
| image_url (detail) | product_group_detail_description | 상세 설명 이미지 URL |
| notice.id | product_notice | 고시 ID |
| material | product_notice | 소재 |
| color | product_notice | 색상 |
| size | product_notice | 사이즈 |
| maker | product_notice | 제조사 |
| origin (notice) | product_notice | 원산지 |
| washing_method | product_notice | 세탁방법 |
| year_month | product_notice | 제조연월 |
| assurance_standard | product_notice | 품질보증기준 |
| as_phone | product_notice | A/S 전화번호 |

**QueryDSL**

```java
queryFactory
    .select(
        productGroup.id,
        productGroup.productGroupDetails.productGroupName,
        seller.id,
        seller.sellerName,
        brand.id,
        brand.brandName,
        category.id,
        category.path,
        productGroup.productGroupDetails.price,
        productGroup.productGroupDetails.optionType,
        productGroup.productGroupDetails.clothesDetailInfo.productCondition,
        productGroup.productGroupDetails.clothesDetailInfo.origin,
        productGroup.productGroupDetails.productStatus,
        productDelivery.deliveryNotice,
        productDelivery.refundNotice.returnMethodDomestic,
        productDelivery.refundNotice.returnCourierDomestic,
        productDelivery.refundNotice.returnChargeDomestic,
        productDelivery.refundNotice.returnExchangeAreaDomestic,
        productRatingStats.averageRating.coalesce(0.0),
        productRatingStats.reviewCount.coalesce(0L),
        productGroupDetailDescription.imageDetail.imageUrl,
        productNotice.id,
        productNotice.noticeDetail.material,
        productNotice.noticeDetail.color,
        productNotice.noticeDetail.size,
        productNotice.noticeDetail.maker,
        productNotice.noticeDetail.origin,
        productNotice.noticeDetail.washingMethod,
        productNotice.noticeDetail.yearMonth,
        productNotice.noticeDetail.assuranceStandard,
        productNotice.noticeDetail.asPhone)
    .from(productGroup)
    .innerJoin(seller).on(seller.id.eq(productGroup.productGroupDetails.sellerId))
    .innerJoin(category).on(category.id.eq(productGroup.productGroupDetails.categoryId))
    .innerJoin(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))
    .innerJoin(productDelivery).on(productDelivery.productGroup.id.eq(productGroup.id))
    .innerJoin(productNotice).on(productNotice.productGroup.id.eq(productGroup.id))
    .innerJoin(productGroupDetailDescription)
        .on(productGroupDetailDescription.id.eq(productGroup.id))
        .on(productGroupDetailDescription.deleteYn.eq(Yn.N))
    .leftJoin(productRatingStats).on(productRatingStats.id.eq(productGroup.id))
    .where(productGroup.id.eq(productGroupId))
    .fetchOne()
```

---

### 쿼리 2: 개별 상품 + 옵션 조회 (fetchProducts)

**조회 테이블**

| 테이블 | Entity | JOIN 유형 | JOIN 조건 |
|--------|--------|-----------|-----------|
| product | Product | FROM (기준) | - |
| product_stock | ProductStock | INNER JOIN | product_stock.product_id = product.product_id AND delete_yn = 'N' |
| product_option | ProductOption | LEFT JOIN | product_option.product_id = product.product_id AND delete_yn = 'N' |
| option_group | OptionGroup | LEFT JOIN | option_group.id = product_option.option_group_id AND delete_yn = 'N' |
| option_detail | OptionDetail | LEFT JOIN | option_detail.id = product_option.option_detail_id AND delete_yn = 'N' |

**WHERE 조건**

```sql
product.product_group_id = :productGroupId
AND product.delete_yn = 'N'
```

**GROUP BY**

```java
GroupBy.groupBy(product.id, optionDetail.id)
```

**Projection: QProductFetchDto**

| 필드 | 소스 컬럼 |
|------|----------|
| productGroupId | product.product_group_id |
| productId | product.product_id |
| stockQuantity | product_stock.stock_quantity |
| additionalPrice | product_option.additional_price (NULL 시 0) |
| productStatus | product.sold_out_yn, product.display_yn |
| optionGroupId | option_group.id (NULL 시 0) |
| optionDetailId | option_detail.id (NULL 시 0) |
| optionName | option_group.option_name |
| optionValue | option_detail.option_value (NULL 시 "") |

**QueryDSL**

```java
queryFactory
    .from(product)
    .innerJoin(productStock).on(productStock.product.id.eq(product.id))
        .on(productStock.deleteYn.eq(Yn.N))
    .leftJoin(productOption).on(productOption.product.id.eq(product.id))
        .on(productOption.deleteYn.eq(Yn.N))
    .leftJoin(optionGroup).on(optionGroup.id.eq(productOption.optionGroup.id))
        .on(optionGroup.deleteYn.eq(Yn.N))
    .leftJoin(optionDetail).on(optionDetail.id.eq(productOption.optionDetail.id))
        .on(optionDetail.deleteYn.eq(Yn.N))
    .where(
        product.productGroup.id.eq(productGroupId),
        product.deleteYn.eq(Yn.N))
    .transform(
        GroupBy.groupBy(product.id, optionDetail.id)
            .list(new QProductFetchDto(
                product.productGroup.id,
                product.id,
                productStock.stockQuantity,
                productOption.additionalPrice.coalesce(0L),
                product.productStatus,
                optionGroup.id.coalesce(0L),
                optionDetail.id.coalesce(0L),
                optionGroup.optionName,
                optionDetail.optionValue.coalesce(""))))
```

---

### 쿼리 3: 이미지 조회 (fetchImages)

**조회 테이블**

| 테이블 | Entity | JOIN 유형 | 설명 |
|--------|--------|-----------|------|
| product_group_image | ProductGroupImage | FROM (기준) | 단독 조회 |

**WHERE 조건**

```sql
product_group_image.product_group_id = :productGroupId
AND product_group_image.delete_yn = 'N'
```

**Projection: QProductImageDto**

| 필드 | 소스 컬럼 |
|------|----------|
| productGroupImageId | product_group_image.id |
| productGroupImageType | product_group_image.product_group_image_type |
| imageUrl | product_group_image.image_url |

**QueryDSL**

```java
queryFactory
    .select(new QProductImageDto(
        productGroupImage.id,
        productGroupImage.imageDetail.productGroupImageType,
        productGroupImage.imageDetail.imageUrl))
    .from(productGroupImage)
    .where(
        productGroupImage.productGroup.id.eq(productGroupId),
        productGroupImage.deleteYn.eq(Yn.N))
    .fetch()
```

---

### 쿼리 4: 카테고리 계층 조회 (CategoryFindRepository.fetchProductCategoryList)

**조회 테이블**

| 테이블 | Entity | 설명 |
|--------|--------|------|
| category | Category | path 문자열에서 파싱된 categoryId Set으로 IN 조회 |

**입력 조건**

- `ProductGroupFetchDto.path` 필드 (예: "1,10,50") 를 쉼표로 split
- 각 ID를 Long으로 변환한 Set을 IN 조건으로 사용

**Projection: ProductCategoryDto**

| 필드 | 설명 |
|------|------|
| categoryId | 카테고리 ID |
| categoryName | 카테고리명 |
| displayName | 표시명 |
| parentCategoryId | 부모 카테고리 ID |
| categoryDepth | 카테고리 깊이 |

---

## 6. 전체 DB 테이블 요약

| 테이블명 | Entity 클래스 | 역할 | 쿼리 번호 |
|----------|--------------|------|-----------|
| product_group | ProductGroup | 상품 그룹 기본 정보 | 쿼리 1 |
| seller | Seller | 셀러 정보 | 쿼리 1 |
| brand | Brand | 브랜드 정보 | 쿼리 1 |
| category | Category | 카테고리 정보 | 쿼리 1, 쿼리 4 |
| product_delivery | ProductDelivery | 배송/반품 안내 | 쿼리 1 |
| product_notice | ProductNotice | 상품 고시 정보 | 쿼리 1 |
| product_group_detail_description | ProductGroupDetailDescription | 상세 설명 이미지 | 쿼리 1 |
| product_rating_stats | ProductRatingStats | 평점/리뷰 통계 | 쿼리 1 |
| product | Product | 개별 상품 | 쿼리 2 |
| product_stock | ProductStock | 재고 | 쿼리 2 |
| product_option | ProductOption | 상품-옵션 연결 | 쿼리 2 |
| option_group | OptionGroup | 옵션 그룹 (SIZE/COLOR 등) | 쿼리 2 |
| option_detail | OptionDetail | 옵션 상세값 (XL/RED 등) | 쿼리 2 |
| product_group_image | ProductGroupImage | 상품 이미지 | 쿼리 3 |

---

## 7. 예외 처리

| 조건 | 예외 클래스 | 설명 |
|------|------------|------|
| productGroupId에 해당하는 데이터 없음 | ProductGroupNotFoundException | fetchProductGroupDto에서 Optional.orElseThrow로 발생 |
| 대표 이미지(MAIN) 없음 | RuntimeException | ProductGroupFetchDto.getMainImageUrl()에서 발생 |

---

## 8. 분리 쿼리 설계 이유

`fetchProductGroupDto`는 단일 JOIN 쿼리 대신 3개의 쿼리로 분리되어 있습니다.

| 쿼리 | 관계 | 이유 |
|------|------|------|
| fetchBasicInfo | 1:1 관계 테이블 8개 | 단일 row 반환, SELECT만으로 충분 |
| fetchProducts | 1:N 관계 (product, stock, option) | Cartesian Product 방지, GroupBy transform 사용 |
| fetchImages | 1:N 관계 (image) | 독립적 이미지 조회, 쿼리 복잡도 분리 |

단일 JOIN으로 구성하면 product × option_detail 행이 image 수만큼 곱해져 결과 행이 폭발적으로 증가하기 때문에 분리 쿼리 패턴을 채택했습니다.

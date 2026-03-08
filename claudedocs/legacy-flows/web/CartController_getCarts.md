# API Flow: CartController.getCarts

## 1. 기본 정보

- **HTTP**: GET /api/v1/carts
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 일반 회원 권한 필수
- **Controller**: `com.setof.connectly.module.cart.controller.CartController`
- **Service**: `CartFindService` → `CartFindServiceImpl`
- **Repository (장바구니)**: `CartFindRepository` → `CartFindRepositoryImpl`
- **Repository (카테고리)**: `CategoryFindRepository` → `CategoryFindRepositoryImpl`
- **Redis**: `CartCountRedisFindService` / `CartCountRedisQueryService` (카운트 캐시)

---

## 2. Request

### Query Parameters

| 이름 | 타입 | 필수 | 설명 | 비고 |
|------|------|------|------|------|
| `lastDomainId` | `Long` | 아니오 | 커서 페이징용 마지막 장바구니 ID | null이면 첫 페이지 |
| `cursorValue` | `String` | 아니오 | 커서 값 (현재 미사용, 필드만 존재) | - |
| `orderType` | `OrderType` | 아니오 | 정렬 타입 (현재 미사용, 필드만 존재) | - |
| `page` | `int` | 아니오 | Pageable 페이지 번호 | Spring Pageable |
| `size` | `int` | 아니오 | Pageable 페이지 크기 | Spring Pageable |
| `sort` | `String` | 아니오 | Pageable 정렬 | Spring Pageable |

> 현재 구현에서 실질적으로 사용되는 파라미터는 `lastDomainId`(커서)와 Pageable의 `size`입니다.
> 정렬은 QueryDSL에서 `cart.id.desc()` 고정값으로 처리되어 `orderType`은 미사용입니다.

**CartFilter 상속 구조:**
```
AbstractLastDomainIdFilter
  └── CartFilter (extends AbstractLastDomainIdFilter)
       - lastDomainId: Long
       - cursorValue: String
       - orderType: OrderType
```

### Request 예시

```
GET /api/v1/carts?size=20
GET /api/v1/carts?lastDomainId=5000&size=20   (두 번째 페이지 이후)
```

**신규 아키텍처 대응 DTO** (`SearchCartsCursorV1ApiRequest`):

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|-----------|------|
| `lastCartId` | `Long` | 아니오 | - | 커서: 마지막 장바구니 ID |
| `size` | `Integer` | 아니오 | @Min(1), @Max(100) | 페이지 크기 |

---

## 3. Response

### 응답 구조 (`CustomSlice<CartResponse>`)

**CustomSlice 래퍼:**

| 필드 | 타입 | 설명 |
|------|------|------|
| `content` | `List<CartResponse>` | 장바구니 아이템 목록 |
| `last` | `boolean` | 마지막 페이지 여부 |
| `first` | `boolean` | 첫 번째 페이지 여부 |
| `number` | `int` | 현재 페이지 번호 |
| `sort` | `Sort` | 정렬 정보 |
| `size` | `int` | 페이지 크기 |
| `numberOfElements` | `int` | 현재 페이지 요소 수 |
| `empty` | `boolean` | 비어있는지 여부 |
| `lastDomainId` | `Long` | 다음 커서용 마지막 ID |
| `cursorValue` | `String` | 다음 커서 값 |
| `totalElements` | `Long` | 전체 장바구니 수 (Redis 캐시 or DB 카운트) |

**CartResponse 필드:**

| 필드 | 타입 | JSON 직렬화 | 설명 |
|------|------|------------|------|
| `cartId` | `long` | 포함 | 장바구니 ID |
| `brandId` | `long` | 포함 | 브랜드 ID |
| `brandName` | `String` | 포함 | 브랜드명 |
| `productGroupId` | `long` | 포함 | 상품그룹 ID |
| `productGroupName` | `String` | 포함 | 상품그룹명 |
| `sellerId` | `long` | 포함 | 판매자 ID |
| `sellerName` | `String` | 포함 | 판매자명 |
| `productId` | `long` | 포함 | 상품(SKU) ID |
| `price` | `Price` | 포함 | 가격 정보 (embedded) |
| `quantity` | `int` | 포함 | 장바구니 수량 |
| `stockQuantity` | `int` | 포함 | 재고 수량 |
| `optionValue` | `String` | 포함 | 옵션 조합 문자열 (예: "블랙 270") |
| `imageUrl` | `String` | 포함 | 대표 이미지 URL |
| `productStatus` | `ProductStatus` | 포함 | 상품 상태 (soldOutYn + displayYn embedded) |
| `mileageRate` | `double` | 포함 | 마일리지 적립률 |
| `expectedMileageAmount` | `double` | 포함 | 예상 적립 마일리지 |
| `categories` | `Set<ProductCategoryDto>` | 포함 | 카테고리 목록 |
| `path` | `String` | **@JsonIgnore** | 카테고리 경로 문자열 - 내부 처리용 (예: "1,5,20,201") |
| `options` | `Set<OptionDto>` | **@JsonIgnore** | 원시 옵션 목록 - optionValue 조합 후 응답에서 제외 |

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "cartId": 1001,
        "brandId": 10,
        "brandName": "나이키",
        "productGroupId": 100,
        "productGroupName": "에어맥스 90",
        "sellerId": 5,
        "sellerName": "공식스토어",
        "productId": 500,
        "price": {
          "regularPrice": 150000,
          "currentPrice": 129000,
          "discountRate": 0.14
        },
        "quantity": 2,
        "stockQuantity": 50,
        "optionValue": "블랙 270",
        "imageUrl": "https://cdn.example.com/image.jpg",
        "productStatus": {
          "soldOutYn": "N",
          "displayYn": "Y"
        },
        "mileageRate": 0.01,
        "expectedMileageAmount": 1290.0,
        "categories": [
          {"categoryId": 1, "categoryName": "신발"},
          {"categoryId": 10, "categoryName": "운동화"}
        ]
      }
    ],
    "last": false,
    "first": true,
    "number": 0,
    "size": 20,
    "numberOfElements": 1,
    "empty": false,
    "lastDomainId": 1001,
    "cursorValue": null,
    "totalElements": 5
  }
}
```

**신규 아키텍처 응답 DTO** (`CartV1ApiResponse`) 구조:

| 필드 | 타입 | 설명 |
|------|------|------|
| `cartId` | `long` | 장바구니 ID |
| `productGroupId` | `long` | 상품그룹 ID |
| `productGroupName` | `String` | 상품그룹명 |
| `productId` | `long` | 상품(SKU) ID |
| `quantity` | `int` | 장바구니 수량 |
| `stockQuantity` | `int` | 재고 수량 |
| `optionValue` | `String` | 옵션 값 조합 문자열 |
| `imageUrl` | `String` | 대표 이미지 URL |
| `productStatus` | `String` | 상품 상태 (ON_SALE, SOLD_OUT, HIDDEN) |
| `brand` | `BrandResponse` | {brandId, brandName} |
| `seller` | `SellerResponse` | {sellerId, sellerName} |
| `price` | `PriceResponse` | {regularPrice, currentPrice, salePrice} |
| `mileage` | `MileageResponse` | {mileageRate, expectedMileageAmount} |
| `categories` | `Set<CategoryResponse>` | [{categoryId, categoryName}] |

---

## 4. 호출 흐름

```
GET /api/v1/carts?lastDomainId=5000&size=20
    |
    v
[CartController.getCarts(CartFilter, Pageable)]
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/carts")
    |
    v
[CartFindService.fetchCartList(filter, pageable)]
    (Interface: CartFindService)
    (Impl: CartFindServiceImpl)
    @Transactional(readOnly = true)
    |
    +-- [1] fetchCarts(filter, pageable)
    |       └── CartFindRepository.fetchCartList(SecurityUtils.currentUserId(), filter, pageable)
    |               └── CartFindRepositoryImpl.fetchCartList()
    |                   → QueryDSL: cart + 10개 테이블 JOIN (8 INNER + 3 LEFT)
    |                   → GroupBy.groupBy(cart.id) → List<CartResponse> (옵션 중복 제거)
    |
    +-- [2] fetchCartCountQuery(userId)
    |       └── CartCountRedisFindService.fetchCartCountInCache(userId)
    |           ├── [캐시 HIT] Redis GET "CART_COUNT:{userId}" → Long 반환
    |           └── [캐시 MISS] CartFindRepository.fetchCartCountQuery(userId)
    |                           → QueryDSL: SELECT COUNT(DISTINCT cart.id) FROM cart WHERE ...
    |                           └── CartCountRedisQueryService.insertCartCountInCache()
    |                               → Redis SET "CART_COUNT:{userId}"
    |                           → CartCountDto(userId, count) 반환
    |
    +-- [3] fetchCategories(findCarts)
    |       └── path 필드에서 categoryId Set 추출 (예: "1,5,20,201" → {1,5,20,201})
    |           └── ProductCategoryFetchService.fetchProductCategories(categoryIds)
    |               └── CategoryFindServiceImpl.fetchCategoryList(categoryIdSet)
    |                   └── CategoryFindRepository.fetchProductCategoryList(categoryIds)
    |                       → QueryDSL: category LEFT JOIN product_group
    |                       → GroupBy.groupBy(category.id) → List<ProductCategoryDto>
    |
    +-- [4] CartMapper.toCartResponses(findCarts, productCategories)
    |       └── CartMapperImpl.toCartResponses()
    |           ├── options Set → optionValue 조합 (optionGroupId 오름차순 정렬 후 공백 연결)
    |           └── path 문자열 → Set<ProductCategoryDto> 매핑 (categoryMap lookup)
    |
    └── [5] CartSliceMapper.toSlice(cartResponses, pageable, cartCountDto.cartQuantity)
            └── CartSliceMapperImpl extends AbstractGeneralSliceMapper
                → hasNext 판단: content.size > pageSize
                → 다음 페이지 있으면 마지막 요소 제거
                → lastDomainId: 마지막 요소의 cartId (LastDomainIdProvider.getId())
                → totalElements: Redis 카운트 or DB 카운트
                → CustomSlice<CartResponse> 반환
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | Entity (레거시) | JOIN 방식 | JOIN 조건 |
|--------|----------------|----------|----------|
| `cart` | QCart | FROM (기준) | - |
| `product_group` | QProductGroup | INNER JOIN | `product_group.id = cart.product_group_id` |
| `product` | QProduct | INNER JOIN | `product.id = cart.product_id` |
| `seller` | QSeller | INNER JOIN | `seller.id = product_group.seller_id` |
| `product_group_image` | QProductGroupImage | INNER JOIN | `product_group_image.product_group_id = product_group.id` AND `image_type = MAIN` AND `delete_yn = N` |
| `product_stock` | QProductStock | INNER JOIN | `product_stock.product_id = cart.product_id` |
| `brand` | QBrand | INNER JOIN | `brand.id = product_group.brand_id` |
| `category` | QCategory | INNER JOIN | `category.id = product_group.category_id` |
| `product_option` | QProductOption | LEFT JOIN | `product_option.product_id = cart.product_id` |
| `option_group` | QOptionGroup | LEFT JOIN | `option_group.id = product_option.option_group_id` |
| `option_detail` | QOptionDetail | LEFT JOIN | `option_detail.id = product_option.option_detail_id` |

**카테고리 보조 쿼리 테이블 (별도 실행):**

| 테이블 | JOIN 방식 | 조건 |
|--------|----------|------|
| `category` | FROM (기준) | `category.id IN (categoryIds)` |
| `product_group` | LEFT JOIN | `category.id = product_group.category_id` |

### QueryDSL - 장바구니 목록 조회 (CartFindRepositoryImpl.fetchCartList)

```java
queryFactory
    .from(cart)
    .innerJoin(productGroup)
        .on(productGroup.id.eq(cart.cartDetails.productGroupId))
    .innerJoin(product)
        .on(product.id.eq(cart.cartDetails.productId))
    .innerJoin(seller)
        .on(seller.id.eq(productGroup.productGroupDetails.sellerId))
    .innerJoin(productGroupImage)
        .on(productGroup.id.eq(productGroupImage.productGroup.id))
        .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
        .on(productGroupImage.deleteYn.eq(Yn.N))
    .innerJoin(productStock)
        .on(productStock.product.id.eq(cart.cartDetails.productId))
    .innerJoin(brand)
        .on(brand.id.eq(productGroup.productGroupDetails.brandId))
    .innerJoin(category)
        .on(category.id.eq(productGroup.productGroupDetails.categoryId))
    .leftJoin(productOption)
        .on(productOption.product.id.eq(cart.cartDetails.productId))
    .leftJoin(optionGroup)
        .on(optionGroup.id.eq(productOption.optionGroup.id))
    .leftJoin(optionDetail)
        .on(optionDetail.id.eq(productOption.optionDetail.id))
    .where(
        cart.userId.eq(userId),             // SecurityUtils.currentUserId()
        cart.deleteYn.eq(Yn.N),             // 삭제되지 않은 항목
        cart.id.lt(filter.lastDomainId)     // 커서 조건 (null이면 미적용)
    )
    .distinct()
    .limit(pageable.pageSize + 1)           // hasNext 판단용 +1
    .orderBy(cart.id.desc())
    .transform(
        GroupBy.groupBy(cart.id)
            .list(new QCartResponse(
                productGroup.productGroupDetails.brandId,
                brand.brandName,
                productGroup.productGroupDetails.productGroupName,
                seller.id,
                seller.sellerName,
                productStock.product.id,
                productGroup.productGroupDetails.price,      // Price embedded
                cart.cartDetails.quantity,
                productStock.stockQuantity,
                GroupBy.set(new QOptionDto(                  // 옵션 Set 집합
                    optionGroup.id,
                    optionDetail.id,
                    optionGroup.optionName,
                    optionDetail.optionValue.coalesce("")
                )),
                cart.id,
                productGroupImage.imageDetail.imageUrl,
                productGroup.id,
                product.productStatus,
                category.path                                // "1,5,20,201" 형태
            ))
    )
```

### QueryDSL - 장바구니 카운트 조회 (Redis Miss 시, CartFindRepositoryImpl.fetchCartCountQuery)

```java
queryFactory
    .select(cart.count())
    .from(cart)
    .where(
        cart.userId.eq(userId),
        cart.deleteYn.eq(Yn.N)
    )
    .distinct()
// → JPAQuery<Long> 반환 후 .fetchCount() 호출
```

### QueryDSL - 카테고리 목록 조회 (CategoryFindRepositoryImpl.fetchProductCategoryList)

```java
queryFactory
    .from(category)
    .leftJoin(productGroup)
        .on(category.id.eq(productGroup.productGroupDetails.categoryId))
    .where(category.id.in(categoryIds))
    .orderBy(category.id.asc())
    .transform(
        GroupBy.groupBy(category.id)
            .list(new QProductCategoryDto(
                productGroup.id,
                category.id,
                category.categoryName,
                category.displayName,
                category.parentCategoryId,
                category.categoryDepth
            ))
    )
```

---

## 6. 후처리 로직 (Service Layer)

### CartMapperImpl.toCartResponses()

QueryDSL `@QueryProjection`으로 직접 매핑된 `List<CartResponse>`는 이 시점에 `options`(원시 옵션 Set)와 `path`(카테고리 경로 문자열)가 채워져 있지만, `optionValue`와 `categories`는 비어있습니다.

두 가지 후처리를 수행합니다:

1. **optionValue 조합**: `options` Set을 `optionGroupId` 오름차순으로 정렬한 뒤 `optionValue`를 공백으로 연결합니다.
   - 예: `[{optionGroupId:1, value:"블랙"}, {optionGroupId:2, value:"270"}]` → `"블랙 270"`

2. **categories 매핑**: `path` 문자열(`"1,5,20,201"`)을 `,`로 분리 → 카테고리 ID 배열 → 별도 조회한 `categoryMap`에서 `ProductCategoryDto` 찾아 `categories` Set에 설정

### CartSliceMapperImpl.toSlice()

`AbstractGeneralSliceMapper`를 상속한 구현체로 다음과 같이 동작합니다:

- `content.size > pageSize`: `hasNext = true`, `last = false`
- 다음 페이지 존재 시 마지막 요소를 제거하여 실제 content 크기를 pageSize로 맞춥니다.
- `lastDomainId`는 content 마지막 요소의 `cartId`로 설정됩니다 (`LastDomainIdProvider.getId()` 구현).
- `totalElements`는 Redis 캐시 또는 DB COUNT 쿼리 결과값입니다.

---

## 7. Redis 캐시 전략

| 키 패턴 | 값 타입 | 목적 | 캐시 미스 시 |
|---------|---------|------|------------|
| `CART_COUNT:{userId}` | `Long` (JSON 직렬화) | 장바구니 개수 캐싱 | DB COUNT 쿼리 후 Redis SET |

- 캐시 HIT: Redis GET → JSON 역직렬화 → `Long` 반환 → `CartCountDto(userId, count)` 생성
- 캐시 MISS: `CartFindRepository.fetchCartCountQuery(userId).fetchCount()` 실행 → 결과를 Redis에 저장

---

## 8. 신규 아키텍처와의 비교

### 레거시 vs 신규 구조 차이

| 항목 | 레거시 (bootstrap-legacy-web-api) | 신규 (hexagonal) |
|------|----------------------------------|-----------------|
| Request DTO | `CartFilter extends AbstractLastDomainIdFilter` (mutable) | `SearchCartsCursorV1ApiRequest` (record, 불변) |
| Response DTO | `CartResponse` (mutable, @Builder, @QueryProjection 재사용) | `CartV1ApiResponse` (record, 불변) |
| Service 역할 | 조회 + 카운트 + 카테고리 조합 + Slice 생성 통합 | UseCase 분리 예정 (TODO 상태) |
| Repository | `CartFindRepositoryImpl` (`@QueryProjection QCartResponse`) | `LegacyWebCartCompositeQueryDslRepository` (`Projections.constructor`) |
| Persistence DTO | `CartResponse` 직접 응답 재사용 | `LegacyWebCartQueryDto` (record, 계층 분리) |
| Application Result | `CartResponse` 직접 사용 | `LegacyCartResult` (record, Domain Result 분리) |
| 가격 정보 | `Price` embedded (regularPrice + currentPrice + discountRate) | `LegacyCartPriceResult` (regularPrice, currentPrice, salePrice 명시적 분리) |
| 상품 상태 | `ProductStatus` embedded (soldOutYn + displayYn) | `String` 단일값 (ON_SALE / SOLD_OUT / HIDDEN) |
| 옵션 처리 위치 | Service Layer (CartMapper.toCartResponses) | Persistence Mapper Layer (LegacyWebCartMapper.buildOptionValue) |
| 카테고리 처리 | Service에서 별도 DB 조회 후 후처리 매핑 | categoryPath를 Result에 그대로 유지 |
| Port 연결 | 없음 (직접 구현체 의존) | `LegacyWebCartCompositeQueryAdapter` (TODO: Port implements 추가 필요) |

---

## 9. 주의 사항 및 잠재적 이슈

1. **fetchCount() 이중 호출 (버그)**: `CartFindServiceImpl.fetchCartCountQueryInDb()`에서 `countQuery.fetchCount()`가 `insertCartCountInCache()`와 `new CartCountDto()` 생성 시 각각 한 번씩 총 두 번 실행됩니다. 동일한 COUNT 쿼리가 DB에 두 번 요청됩니다.

2. **카테고리 IN 절 크기 증가 가능성**: `fetchCategories()`에서 모든 카트 아이템의 `path` 문자열을 파싱하여 카테고리 ID Set을 구성한 후 한 번의 IN 쿼리로 조회합니다. 카트 항목이 많고 각 항목이 깊은 카테고리 계층을 가질수록 IN 절이 커집니다.

3. **옵션 Set 정렬 비결정성**: QueryDSL `GroupBy.set()`으로 생성된 `Set<OptionDto>`는 삽입 순서가 보장되지 않습니다. `CartMapperImpl`에서 `optionGroupId`로 재정렬하여 `optionValue` 결과는 일관되지만, 원시 Set 자체의 순서는 비결정적입니다.

4. **DISTINCT + LIMIT와 transform 불일치**: 11개 테이블 JOIN 쿼리에 `.distinct().limit(pageSize + 1)`을 사용합니다. `transform(GroupBy.groupBy())`는 JPA 레벨의 메모리 처리이므로 실제 DB에서 가져오는 행 수는 옵션 수에 따라 limit 값보다 많을 수 있습니다. 옵션이 없는 상품은 1행, 옵션이 3개인 상품은 3행이 반환됩니다.

5. **신규 아키텍처 Port 연결 미완**: `LegacyWebCartCompositeQueryAdapter`에 `// TODO: Application Layer의 LegacyCartCompositeQueryPort implements 추가` 주석이 남아있어 포트 인터페이스 연결이 완료되지 않은 상태입니다.

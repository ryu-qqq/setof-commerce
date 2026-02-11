# API Flow Documentation: CartController.getCarts

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/carts` |
| Controller | `CartController` |
| Service | `CartFindService` → `CartFindServiceImpl` |
| Repository | `CartFindRepository` → `CartFindRepositoryImpl` |
| Mapper | `CartMapper`, `CartSliceMapper` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| lastDomainId | Long | ❌ | 커서 페이징용 마지막 ID | - |
| cursorValue | String | ❌ | 커서 값 | - |
| orderType | OrderType | ❌ | 정렬 타입 | - |
| page | int | ❌ | 페이지 번호 (Pageable) | - |
| size | int | ❌ | 페이지 크기 (Pageable) | - |
| userId | long | ✅ (자동) | 현재 로그인 사용자 ID | SecurityContext |

### Request DTO 구조

```java
// CartFilter.java
public class CartFilter extends AbstractLastDomainIdFilter {
    private Long lastDomainId;    // 커서 페이징: 마지막으로 조회한 cart.id
    private String cursorValue;   // 커서 값 (미사용)
    private OrderType orderType;  // 정렬 타입 (미사용)
}

// AbstractLastDomainIdFilter.java
public class AbstractLastDomainIdFilter implements LastDomainIdFilter {
    private Long lastDomainId;
    private String cursorValue;
    private OrderType orderType;
}
```

### Security

```java
@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
```

---

## 📤 Response

### Response DTO 구조

```java
// CustomSlice.java - 페이징 Wrapper
public class CustomSlice<T> {
    private List<T> content;           // 데이터 목록
    private boolean last;              // 마지막 페이지 여부
    private boolean first;             // 첫 페이지 여부
    private int number;                // 현재 페이지 번호
    private Sort sort;                 // 정렬 정보
    private int size;                  // 페이지 크기
    private int numberOfElements;      // 현재 페이지 요소 수
    private boolean empty;             // 빈 페이지 여부
    private Long lastDomainId;         // 다음 페이지용 커서 ID
    private String cursorValue;        // 다음 페이지용 커서 값
    private Long totalElements;        // 전체 요소 수 (장바구니 총 개수)
}

// CartResponse.java - 장바구니 아이템
public class CartResponse implements LastDomainIdProvider, DiscountOffer {
    private long brandId;                        // 브랜드 ID
    private String brandName;                    // 브랜드명
    private String productGroupName;             // 상품그룹명
    private long sellerId;                       // 판매자 ID
    private String sellerName;                   // 판매자명
    private long productId;                      // 상품(SKU) ID
    private Price price;                         // 가격 정보 (Embedded)
    private int quantity;                        // 장바구니 수량
    private int stockQuantity;                   // 재고 수량
    private String optionValue;                  // 옵션 값 (조합된 문자열)
    private long cartId;                         // 장바구니 ID
    private String imageUrl;                     // 대표 이미지 URL
    private long productGroupId;                 // 상품그룹 ID
    private ProductStatus productStatus;         // 상품 상태
    private double mileageRate;                  // 마일리지 적립률
    private double expectedMileageAmount;        // 예상 마일리지
    private Set<ProductCategoryDto> categories;  // 카테고리 목록

    @JsonIgnore
    private String path;                         // 카테고리 경로 (내부용)
    @JsonIgnore
    private Set<OptionDto> options;              // 옵션 목록 (내부용)
}

// Price.java (Embedded)
public class Price {
    private int regularPrice;    // 정가
    private int currentPrice;    // 판매가
    private int salePrice;       // 할인가
}

// OptionDto.java
public class OptionDto {
    private long optionGroupId;   // 옵션그룹 ID
    private long optionDetailId;  // 옵션상세 ID
    private OptionName optionName; // 옵션명 (COLOR, SIZE 등)
    private String optionValue;   // 옵션값 ("레드", "XL" 등)
}

// ProductCategoryDto.java
public class ProductCategoryDto {
    private long categoryId;
    private String categoryName;
    private int depth;
    // ...
}
```

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
        "productGroupName": "에어맥스 90",
        "sellerId": 5,
        "sellerName": "공식스토어",
        "productId": 500,
        "productGroupId": 100,
        "price": {
          "regularPrice": 150000,
          "currentPrice": 129000,
          "salePrice": 129000
        },
        "quantity": 2,
        "stockQuantity": 50,
        "optionValue": "블랙 270",
        "imageUrl": "https://cdn.example.com/image.jpg",
        "productStatus": "ON_SALE",
        "mileageRate": 0.01,
        "expectedMileageAmount": 1290,
        "categories": [
          {"categoryId": 1, "categoryName": "신발", "depth": 1},
          {"categoryId": 10, "categoryName": "운동화", "depth": 2}
        ]
      }
    ],
    "last": false,
    "first": true,
    "number": 0,
    "size": 20,
    "numberOfElements": 20,
    "empty": false,
    "lastDomainId": 981,
    "totalElements": 45
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────┐
│   CartController                                         │
│   getCarts(@ModelAttribute CartFilter, Pageable)         │
│   @GetMapping("/carts")                                  │
└────────────────────┬─────────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────────┐
│   CartFindServiceImpl                                    │
│   fetchCartList(filter, pageable)                        │
│   @Transactional(readOnly=true)                          │
└────────────────────┬─────────────────────────────────────┘
                     │
    ┌────────────────┼────────────────┐
    │                │                │
    ▼                ▼                ▼
┌─────────────┐ ┌──────────────┐ ┌─────────────────────┐
│ fetchCarts  │ │fetchCartCount│ │ fetchCategories     │
│ (목록조회)  │ │ Query        │ │ (카테고리 조회)     │
└──────┬──────┘ └──────┬───────┘ └──────────┬──────────┘
       │               │                    │
       ▼               ▼                    ▼
┌─────────────────────────────────────────────────────────┐
│   CartFindRepositoryImpl                                │
│   fetchCartList(userId, filter, pageable)               │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│   Database                                              │
│   Complex JOIN Query (8개 테이블)                        │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│   CartMapperImpl                                        │
│   toCartResponses(carts, productCategories)             │
│   - optionValue 조합                                     │
│   - categories 매핑                                      │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│   CartSliceMapperImpl                                   │
│   toSlice(cartResponses, pageable, totalCount)          │
│   - 커서 페이징 처리                                      │
│   - hasNext 판단                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| cart | cart | FROM | - |
| product_group | productGroup | INNER JOIN | `productGroup.id = cart.cartDetails.productGroupId` |
| product | product | INNER JOIN | `product.id = cart.cartDetails.productId` |
| seller | seller | INNER JOIN | `seller.id = productGroup.productGroupDetails.sellerId` |
| product_group_image | productGroupImage | INNER JOIN | `productGroup.id = productGroupImage.productGroup.id` AND `type = MAIN` AND `deleteYn = N` |
| product_stock | productStock | INNER JOIN | `productStock.product.id = cart.cartDetails.productId` |
| brand | brand | INNER JOIN | `brand.id = productGroup.productGroupDetails.brandId` |
| category | category | INNER JOIN | `category.id = productGroup.productGroupDetails.categoryId` |
| product_option | productOption | LEFT JOIN | `productOption.product.id = cart.cartDetails.productId` |
| option_group | optionGroup | LEFT JOIN | `optionGroup.id = productOption.optionGroup.id` |
| option_detail | optionDetail | LEFT JOIN | `optionDetail.id = productOption.optionDetail.id` |

### QueryDSL 코드

```java
// CartFindRepositoryImpl.fetchCartList()
@Override
public List<CartResponse> fetchCartList(long userId, CartFilter filter, Pageable pageable) {
    return queryFactory
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
            .where(userIdEq(userId), deleteYn(), isCursorRead(filter))
            .distinct()
            .limit(pageable.getPageSize() + 1)
            .orderBy(cart.id.desc())
            .transform(
                    GroupBy.groupBy(cart.id)
                            .list(
                                    new QCartResponse(
                                            productGroup.productGroupDetails.brandId,
                                            brand.brandName,
                                            productGroup.productGroupDetails.productGroupName,
                                            seller.id,
                                            seller.sellerName,
                                            productStock.product.id,
                                            productGroup.productGroupDetails.price,
                                            cart.cartDetails.quantity,
                                            productStock.stockQuantity,
                                            GroupBy.set(
                                                    new QOptionDto(
                                                            optionGroup.id,
                                                            optionDetail.id,
                                                            optionGroup.optionName,
                                                            optionDetail.optionValue.coalesce(""))),
                                            cart.id,
                                            productGroupImage.imageDetail.imageUrl,
                                            productGroup.id,
                                            product.productStatus,
                                            category.path)));
}
```

### WHERE 조건

| 조건 메서드 | 필드 | 설명 |
|-------------|------|------|
| `userIdEq(userId)` | `cart.userId` | 사용자 ID 필터 |
| `deleteYn()` | `cart.deleteYn` | 삭제되지 않은 항목만 (`Yn.N`) |
| `isCursorRead(filter)` | `cart.id` | 커서 페이징 (`cart.id < lastDomainId`) |

### 생성 SQL (예상)

```sql
SELECT DISTINCT
    pg.brand_id,
    b.brand_name,
    pg.product_group_name,
    s.seller_id,
    s.seller_name,
    ps.product_id,
    pg.regular_price, pg.current_price, pg.sale_price,
    c.quantity,
    ps.stock_quantity,
    og.option_group_id, od.option_detail_id, og.option_name, od.option_value,
    c.cart_id,
    pgi.image_url,
    pg.product_group_id,
    p.product_status,
    cat.path
FROM cart c
INNER JOIN product_group pg ON pg.product_group_id = c.product_group_id
INNER JOIN product p ON p.product_id = c.product_id
INNER JOIN seller s ON s.seller_id = pg.seller_id
INNER JOIN product_group_image pgi ON pg.product_group_id = pgi.product_group_id
    AND pgi.product_group_image_type = 'MAIN'
    AND pgi.delete_yn = 'N'
INNER JOIN product_stock ps ON ps.product_id = c.product_id
INNER JOIN brand b ON b.brand_id = pg.brand_id
INNER JOIN category cat ON cat.category_id = pg.category_id
LEFT JOIN product_option po ON po.product_id = c.product_id
LEFT JOIN option_group og ON og.option_group_id = po.option_group_id
LEFT JOIN option_detail od ON od.option_detail_id = po.option_detail_id
WHERE c.user_id = ?
  AND c.delete_yn = 'N'
  AND c.cart_id < ?  -- 커서 조건 (있을 경우)
ORDER BY c.cart_id DESC
LIMIT ?  -- pageSize + 1
```

---

## 📊 후처리 로직

### 1. Option Value 조합
```java
// CartMapperImpl.getOptionName()
private String getOptionName(Set<OptionDto> options) {
    return options.stream()
            .sorted(Comparator.comparing(OptionDto::getOptionGroupId))
            .map(OptionDto::getOptionValue)
            .collect(Collectors.joining(" "));
}
// 결과: "블랙 270" (COLOR + SIZE)
```

### 2. Category Path 매핑
```java
// CartMapperImpl.buildCategoryPath()
public void buildCategoryPath(CartResponse cartResponse, Map<Long, ProductCategoryDto> categoryMap) {
    String[] categoryIds = cartResponse.getPath().split(",");  // "1,10,100"
    Set<ProductCategoryDto> categoryDtoList = Arrays.stream(categoryIds)
            .mapToLong(Long::parseLong)
            .mapToObj(categoryMap::get)
            .collect(Collectors.toSet());
    cartResponse.setCategories(categoryDtoList);
}
```

### 3. 커서 페이징 처리
```java
// CartSliceMapperImpl (AbstractGeneralSliceMapper 상속)
// - pageSize + 1 조회 → hasNext 판단
// - lastDomainId 설정 (마지막 요소의 cart.id)
```

---

## 📊 성능 특성

| 항목 | 값 |
|------|-----|
| 조인 테이블 수 | 11개 (8 INNER + 3 LEFT) |
| 그룹핑 | `GroupBy.groupBy(cart.id)` - 옵션 집합 처리 |
| 페이징 방식 | 커서 기반 (No-Offset) |
| 인덱스 권장 | `cart(user_id, delete_yn, cart_id DESC)` |

---

## ⚠️ 주의사항

1. **복잡한 JOIN**: 11개 테이블 조인으로 성능 주의 필요
2. **N+1 방지**: `GroupBy.set()`으로 옵션을 한 번에 조회
3. **커서 페이징**: `lastDomainId`로 다음 페이지 조회
4. **카테고리 추가 조회**: `ProductCategoryFetchService` 별도 호출

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert web:CartController.getCarts

# Persistence Layer 생성
/legacy-query web:CartController.getCarts
```

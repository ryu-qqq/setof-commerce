# API Flow: DiscountController.getDiscountTargets

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/discount/{discountPolicyId}/targets` |
| Controller | `DiscountController` |
| Service (Interface) | `DiscountTargetFetchService` |
| Service (Impl) | `DiscountTargetFetchServiceImpl` |
| Repository (Interface) | `DiscountTargetFetchRepository` |
| Repository (Impl) | `DiscountTargetFetchRepositoryImpl` |
| 보안 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |
| 트랜잭션 | `@Transactional(readOnly = true)` |

---

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | 설명 |
|------|------|------|------|------|
| discountPolicyId | long | @PathVariable | Y | 할인 정책 ID |
| issueType | IssueType | @RequestParam | Y | 적용 대상 유형 (PRODUCT / SELLER / BRAND) |
| page | int | Pageable | N | 페이지 번호 (0부터 시작, 기본값 0) |
| size | int | Pageable | N | 페이지 크기 (기본값 20) |
| sort | String | Pageable | N | 정렬 조건 |

### IssueType Enum

| 값 | 설명 |
|----|------|
| PRODUCT | 상품 그룹 단위 적용 |
| SELLER | 셀러 단위 적용 |
| BRAND | 브랜드 단위 적용 (현재 서비스 미구현 - switch default 예외 발생) |

### 요청 예시

```
GET /api/v1/discount/1/targets?issueType=PRODUCT&page=0&size=20
GET /api/v1/discount/1/targets?issueType=SELLER&page=0&size=20
```

---

## 3. Response

### DTO 구조 (다형성)

`DiscountTargetResponseDto`는 인터페이스로 `@JsonTypeInfo` / `@JsonSubTypes`를 통해 타입 기반 직렬화를 수행한다.

```
DiscountTargetResponseDto (interface)
├── ProductDiscountTarget  (@JsonTypeName = "productDiscountTarget")
└── SellerDiscountTarget   (@JsonTypeName = "sellerDiscountTarget")
```

#### ProductDiscountTarget (issueType = PRODUCT)

| 필드명 | 타입 | 설명 | 비고 |
|--------|------|------|------|
| type | IssueType | 항상 PRODUCT | @JsonTypeInfo 타입 식별자 |
| discountPolicyId | long | 할인 정책 ID | QueryDSL Projection |
| discountTargetId | long | 할인 타겟 ID | QueryDSL Projection |
| productGroupId | long | 상품 그룹 ID (= targetId) | QueryDSL Projection |
| insertOperator | String | 등록자 | QueryDSL Projection |
| insertDate | LocalDateTime | 등록일시 | format: yyyy-MM-dd HH:mm:ss |
| productGroup | ProductGroupInfo | 상품 그룹 상세 | Service Layer 추가 조회 후 @Setter 주입 |
| products | Set\<ProductFetchResponse\> | 상품 목록 | Service Layer 추가 조회 후 @Setter 주입 |

#### ProductGroupInfo (중첩 객체)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| productGroupId | long | 상품 그룹 ID |
| productGroupName | String | 상품 그룹명 |
| sellerId | long | 셀러 ID |
| sellerName | String | 셀러명 |
| categoryId | long | 카테고리 ID |
| optionType | OptionType | 옵션 유형 |
| managementType | ManagementType | 관리 유형 |
| brand | BaseBrandContext | 브랜드 정보 |
| price | Price | 가격 정보 |
| clothesDetailInfo | ClothesDetail | 의류 상세 정보 |
| deliveryNotice | DeliveryNotice | 배송 안내 |
| refundNotice | RefundNotice | 환불 안내 |
| productGroupMainImageUrl | String | 대표 이미지 URL |
| categoryFullName | String | 카테고리 전체 경로명 |
| path | String | 카테고리 경로 ID 문자열 (@JsonIgnore) |
| productStatus | ProductStatus | 상품 상태 |
| insertDate | LocalDateTime | 등록일시 |
| updateDate | LocalDateTime | 수정일시 |
| insertOperator | String | 등록자 |
| updateOperator | String | 수정자 |
| crawlProductSku | long | 크롤링 SKU |
| externalProductUuId | String | 외부 상품 UUID |
| crawlProductInfo | CrawlProductDto | 크롤링 상품 정보 |
| externalProductInfos | Set\<ExternalProductDto\> | 외부 상품 목록 |

#### ProductFetchResponse (중첩 객체)

| 필드명 | 타입 | 설명 | 비고 |
|--------|------|------|------|
| productGroupId | long | 상품 그룹 ID | @JsonIgnore |
| productId | long | 상품 ID | |
| stockQuantity | int | 재고 수량 | |
| productStatus | ProductStatus | 상품 상태 | |
| option | String | 옵션 문자열 | |
| options | Set\<OptionDto\> | 옵션 상세 목록 | @JsonInclude(NON_EMPTY) |
| additionalPrice | BigDecimal | 추가 가격 | |

#### SellerDiscountTarget (issueType = SELLER)

| 필드명 | 타입 | 설명 | 비고 |
|--------|------|------|------|
| type | IssueType | 항상 SELLER | @JsonTypeInfo 타입 식별자 |
| discountPolicyId | long | 할인 정책 ID | QueryDSL Projection |
| discountTargetId | long | 할인 타겟 ID | QueryDSL Projection |
| sellerName | String | 셀러명 | seller 테이블 JOIN 후 조회 |
| insertOperator | String | 등록자 | QueryDSL Projection |
| insertDate | LocalDateTime | 등록일시 | format: yyyy-MM-dd HH:mm:ss |

### JSON 예시 (PRODUCT)

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "type": "productDiscountTarget",
        "discountPolicyId": 1,
        "discountTargetId": 101,
        "productGroupId": 5001,
        "insertOperator": "admin",
        "insertDate": "2024-01-01 10:00:00",
        "productGroup": {
          "productGroupId": 5001,
          "productGroupName": "example_product_group",
          "sellerId": 10,
          "sellerName": "example_seller",
          "categoryId": 100,
          "optionType": "OPTION",
          "managementType": "SELF",
          "price": {
            "regularPrice": 50000,
            "currentPrice": 40000
          },
          "productStatus": {
            "displayYn": "Y",
            "soldOutYn": "N"
          },
          "insertDate": "2024-01-01 00:00:00",
          "updateDate": "2024-01-01 00:00:00",
          "insertOperator": "admin",
          "updateOperator": "admin",
          "crawlProductSku": 0,
          "externalProductUuId": "example-uuid"
        },
        "products": [
          {
            "productId": 10001,
            "stockQuantity": 100,
            "productStatus": {
              "displayYn": "Y",
              "soldOutYn": "N"
            },
            "option": "L",
            "additionalPrice": 0
          }
        ]
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "number": 0,
    "size": 20
  }
}
```

### JSON 예시 (SELLER)

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "type": "sellerDiscountTarget",
        "discountPolicyId": 1,
        "discountTargetId": 201,
        "sellerName": "example_seller",
        "insertOperator": "admin",
        "insertDate": "2024-01-01 10:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "number": 0,
    "size": 20
  }
}
```

---

## 4. 호출 흐름

```
DiscountController.getDiscountTargets(discountPolicyId, issueType, pageable)
    GET /api/v1/discount/{discountPolicyId}/targets
    @PathVariable discountPolicyId, @RequestParam issueType, Pageable pageable
        |
        v
DiscountTargetFetchService.fetchDiscountTargets(discountPolicyId, issueType, pageable)
        |
        v
DiscountTargetFetchServiceImpl.fetchDiscountTargets(...)
    @Transactional(readOnly = true)
    switch (issueType) {
        |
        +-- PRODUCT --> fetchProductTargets(discountPolicyId, pageable)
        |                   |
        |                   +-- DiscountTargetFetchRepository.fetchProductTargets(...)
        |                   |       QueryDSL: discount_target INNER JOIN product_group
        |                   |
        |                   +-- (결과 비어있지 않으면)
        |                   |   ProductGroupFetchService.fetchProductGroups(productGroupIds)
        |                   |       |
        |                   |       +-- ProductGroupFetchRepository.fetchProductGroups(ids)
        |                   |       +-- ProductFetchService.fetchProducts(productGroupIds)
        |                   |
        |                   +-- DiscountTargetFetchRepository.fetchProductTargetCountQuery(...)
        |                           QueryDSL: COUNT discount_target JOIN product_group
        |
        +-- SELLER --> fetchSellerTargets(discountPolicyId, pageable)
                            |
                            +-- DiscountTargetFetchRepository.fetchSellerTargets(...)
                            |       QueryDSL: discount_target JOIN seller
                            |
                            +-- DiscountTargetFetchRepository.fetchSellerTargetCountQuery(...)
                                    QueryDSL: COUNT discount_target JOIN seller
```

---

## 5. Database Query

### 사용 테이블

| 테이블 | Entity | 용도 |
|--------|--------|------|
| discount_target | DiscountTarget | 할인 적용 대상 메인 테이블 |
| product_group | ProductGroup | PRODUCT 타입 JOIN 대상 |
| seller | Seller | SELLER 타입 JOIN 대상 |

### PRODUCT 타입 - 목록 조회

```java
queryFactory
    .select(
        new QProductDiscountTarget(
            discountTarget.discountPolicyId,
            discountTarget.id,
            discountTarget.targetId,       // productGroupId
            discountTarget.insertOperator,
            discountTarget.insertDate
        )
    )
    .from(discountTarget)
    .innerJoin(productGroup).on(discountTarget.targetId.eq(productGroup.id))
    .where(
        discountTarget.discountPolicyId.eq(discountPolicyId),  // 정책 ID 조건
        discountTarget.deleteYn.eq(Yn.N),                      // 삭제 제외
        discountTarget.activeYn.eq(Yn.Y)                       // 활성화된 것만
    )
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .fetch();
```

### PRODUCT 타입 - 카운트 조회

```java
queryFactory
    .select(discountTarget.count())
    .from(discountTarget)
    .join(productGroup).on(productGroup.id.eq(discountTarget.targetId))
    .where(
        discountTarget.discountPolicyId.eq(discountPolicyId),
        discountTarget.deleteYn.eq(Yn.N),
        discountTarget.activeYn.eq(Yn.Y)
    )
    .distinct();
```

### SELLER 타입 - 목록 조회

```java
queryFactory
    .select(
        new QSellerDiscountTarget(
            discountTarget.discountPolicyId,
            discountTarget.targetId,       // sellerId (discountTargetId 아님 - 주의)
            seller.sellerName,
            discountTarget.insertOperator,
            discountTarget.insertDate
        )
    )
    .from(discountTarget)
    .join(seller).on(seller.id.eq(discountTarget.targetId))
    .where(
        discountTarget.discountPolicyId.eq(discountPolicyId),
        discountTarget.deleteYn.eq(Yn.N),
        discountTarget.activeYn.eq(Yn.Y)
    )
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .fetch();
```

### SELLER 타입 - 카운트 조회

```java
queryFactory
    .select(discountTarget.count())
    .from(discountTarget)
    .join(seller).on(seller.id.eq(discountTarget.targetId))
    .where(
        discountTarget.discountPolicyId.eq(discountPolicyId),
        discountTarget.deleteYn.eq(Yn.N),
        discountTarget.activeYn.eq(Yn.Y)
    )
    .distinct();
```

### WHERE 조건 정리

| 조건 메서드 | SQL 조건 | 설명 |
|-------------|----------|------|
| discountTargetHasPolicyEq | discount_policy_id = {id} | 대상 정책 필터 |
| deleteYn() | DELETE_YN = 'N' | 논리 삭제 제외 |
| activeYn() | active_yn = 'Y' | 비활성화 제외 |

---

## 6. 특이사항 및 주의점

### QSellerDiscountTarget Projection 버그 의심

SELLER 타입 쿼리에서 `QSellerDiscountTarget` 생성자 인자 순서를 확인할 것.

```java
// DiscountTargetFetchRepositoryImpl.fetchSellerTargets() 실제 코드
new QSellerDiscountTarget(
    discountTarget.discountPolicyId,
    discountTarget.targetId,    // <- sellerId가 discountTargetId 위치에 매핑됨
    seller.sellerName,
    discountTarget.insertOperator,
    discountTarget.insertDate
)

// SellerDiscountTarget 생성자
public SellerDiscountTarget(long discountPolicyId, long discountTargetId, String sellerName, String insertOperator, LocalDateTime insertDate)
```

`discountTarget.targetId`가 `discountTargetId` 필드에 매핑되어 있으므로, 응답의 `discountTargetId` 값은 실제로 `seller_id` 값이 된다. (레거시 코드 버그 가능성)

### BRAND issueType 미지원

`IssueType.BRAND`로 요청 시 서비스 레이어에서 `IllegalArgumentException("Unsupported issueType: BRAND")`가 발생한다.

### PRODUCT 타입 N+1 구조

PRODUCT 타입 조회 시 Service Layer에서 `ProductGroupFetchService.fetchProductGroups(ids)` 를 별도 호출하며, 이 내부에서 다시 `ProductFetchService.fetchProducts(ids)`를 호출하는 2단계 추가 조회가 발생한다.

# API Flow Documentation: QnaController.fetchMyQnas

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/qna/my-page` |
| Controller | `QnaController` |
| Service | `QnaFindService` → `QnaFindServiceImpl` |
| Repository | `QnaFindRepository` → `QnaFindRepositoryImpl` |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| lastDomainId | Long | ❌ | 마지막 조회 Q&A ID (커서) | QnaFilter |
| qnaType | QnaType | ✅ | Q&A 유형 (PRODUCT / ORDER) | QnaFilter |
| startDate | LocalDateTime | ✅ | 조회 시작일 | @ValidDateRange |
| endDate | LocalDateTime | ✅ | 조회 종료일 | @ValidDateRange |
| page | int | ❌ | 페이지 번호 | Pageable |
| size | int | ❌ | 페이지 크기 | Pageable |

### Request DTO 구조

```java
// QnaFilter (extends SearchAndDateFilter)
public class QnaFilter extends SearchAndDateFilter {
    private Long lastDomainId;      // 커서 기반 페이징
    private QnaType qnaType;        // PRODUCT | ORDER

    public boolean isProductQna() {
        return qnaType.isProductQna();
    }
}

// 부모 클래스
@ValidDateRange(start = "startDate", end = "endDate")
public abstract class SearchAndDateFilter implements DateRangeFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime endDate;
}

// QnaType enum
public enum QnaType implements EnumType {
    PRODUCT,  // 상품 Q&A
    ORDER     // 주문 Q&A
}
```

### Request 예시

```
GET /api/v1/qna/my-page?qnaType=PRODUCT&startDate=2024-01-01 00:00:00&endDate=2024-12-31 23:59:59&size=10
GET /api/v1/qna/my-page?qnaType=ORDER&lastDomainId=1000&startDate=2024-01-01 00:00:00&endDate=2024-12-31 23:59:59&size=10
```

---

## 📤 Response

### Response DTO 구조

```java
// 최상위 응답 (Slice 기반)
public class CustomSlice<T> {
    private List<T> content;
    private boolean hasNext;
    private int size;
    // ... pageable info
}

// QnaResponse
public class QnaResponse implements LastDomainIdProvider {
    private FetchQnaResponse qna;
    private Set<AnswerQnaResponse> answerQnas;
}

// FetchQnaResponse (상품 Q&A용)
public class FetchQnaResponse {
    private long qnaId;
    private QnaContents qnaContents;
    private Yn privateYn;
    private QnaStatus qnaStatus;
    private QnaType qnaType;
    private QnaDetailType qnaDetailType;
    private UserType userType;
    private List<QnaImageDto> qnaImages;
    private QnaTarget qnaTarget;      // ProductQnaTarget 또는 OrderQnaTarget
    private String userName;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;
}

// ProductQnaTarget (상품 Q&A 대상 정보)
public class ProductQnaTarget implements QnaTarget {
    private long productGroupId;
    private String productGroupName;
    private String productGroupMainImageUrl;
    private BrandDto brand;           // { id, brandName }
}

// OrderQnaTarget (주문 Q&A 대상 정보) - ProductQnaTarget 상속
public class OrderQnaTarget extends ProductQnaTarget {
    private long paymentId;
    private long orderId;
    private long orderAmount;
    private int quantity;
    private String option;            // 옵션 문자열 (조합)
}
```

### Response JSON 예시 (상품 Q&A)

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "qna": {
          "qnaId": 1001,
          "qnaContents": {
            "title": "배송 문의",
            "content": "언제 발송되나요?"
          },
          "privateYn": "N",
          "qnaStatus": "PENDING",
          "qnaType": "PRODUCT",
          "qnaDetailType": "DELIVERY",
          "userType": "BUYER",
          "qnaImages": [],
          "qnaTarget": {
            "productGroupId": 5001,
            "productGroupName": "클래식 코튼 티셔츠",
            "productGroupMainImageUrl": "https://cdn.example.com/images/5001_main.jpg",
            "brand": {
              "id": 100,
              "brandName": "브랜드A"
            }
          },
          "userName": "홍길동",
          "insertDate": "2024-01-15 10:30:00",
          "updateDate": "2024-01-15 10:30:00"
        },
        "answerQnas": []
      }
    ],
    "hasNext": true,
    "size": 10
  }
}
```

### Response JSON 예시 (주문 Q&A)

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "qna": {
          "qnaId": 2001,
          "qnaContents": {
            "title": "교환 요청",
            "content": "사이즈 교환 가능한가요?"
          },
          "privateYn": "Y",
          "qnaStatus": "ANSWERED",
          "qnaType": "ORDER",
          "qnaDetailType": "EXCHANGE",
          "userType": "BUYER",
          "qnaImages": [
            { "imageUrl": "https://cdn.example.com/qna/img1.jpg" }
          ],
          "qnaTarget": {
            "productGroupId": 5001,
            "productGroupName": "클래식 코튼 티셔츠",
            "productGroupMainImageUrl": "https://cdn.example.com/images/5001_main.jpg",
            "brand": {
              "id": 100,
              "brandName": "브랜드A"
            },
            "paymentId": 8001,
            "orderId": 9001,
            "orderAmount": 35000,
            "quantity": 1,
            "option": "블랙 M"
          },
          "userName": "홍길동",
          "insertDate": "2024-01-20 15:00:00",
          "updateDate": "2024-01-21 09:00:00"
        },
        "answerQnas": [
          {
            "qnaAnswerId": 3001,
            "qnaAnswerParentId": null,
            "qnaWriterType": "SELLER",
            "qnaContents": {
              "title": "교환 안내",
              "content": "교환 접수되었습니다."
            },
            "qnaImages": [],
            "insertDate": "2024-01-21 09:00:00",
            "updateDate": "2024-01-21 09:00:00"
          }
        ]
      }
    ],
    "hasNext": false,
    "size": 10
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────┐
│   Controller                                                  │
│   QnaController.fetchMyQnas(QnaFilter, Pageable)             │
│   @GetMapping("/qna/my-page")                                │
│   @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")           │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Service                                                     │
│   QnaFindServiceImpl.fetchMyQnas()                           │
│   @Transactional(readOnly=true)                              │
│                                                              │
│   1. SecurityUtils.currentUserId() → 현재 사용자 ID          │
│   2. qnaFindRepository.fetchMyQnas() → Q&A 조회              │
│   3. [ORDER인 경우] qnaImageFindService → 이미지 조회        │
│   4. qnaMapper.toQnaResponse() → 이미지 매핑                 │
│   5. qnaMapper.toQnaList() → 응답 변환                       │
│   6. qnaSliceMapper.toSlice() → Slice 변환                   │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Repository                                                  │
│   QnaFindRepositoryImpl.fetchMyQnas()                        │
│                                                              │
│   분기: qnaFilter.isProductQna()                             │
│   ├─ true  → fetchProductQnas()                              │
│   └─ false → fetchOrderQnas()                                │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Database                                                    │
│   [PRODUCT] qna, qna_product, product_group, brand,          │
│             product_group_image, seller, users, qna_answer   │
│   [ORDER] qna, qna_order, order, order_snapshot_*,           │
│           brand, seller, users, qna_answer, qna_image        │
└──────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 공통 Query: fetchQnaIds (ID 목록 조회)

```java
queryFactory
    .select(qna.id)
    .from(qna)
    .join(seller).on(seller.id.eq(qna.sellerId))
    .leftJoin(users).on(users.id.eq(qna.userId))
    .where(
        userIdEq(userId),
        qnaIdLt(filter.getLastDomainId()),   // 커서 기반 페이징
        qnaTypeEq(filter.getQnaType()),
        betweenTime(filter)                   // 기간 필터
    )
    .limit(pageable.getPageSize() + 1)        // hasNext 판단용 +1
    .orderBy(qna.id.desc())
    .fetch();
```

---

### Branch A: fetchProductQnas (상품 Q&A)

#### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| qna | qna | FROM | - |
| qna_answer | qnaAnswer | LEFT JOIN | qnaAnswer.qnaId = qna.id |
| qna_product | qnaProduct | JOIN | qna.id = qnaProduct.qnaId |
| product_group | productGroup | JOIN | qnaProduct.productGroupId = productGroup.id |
| brand | brand | JOIN | brand.id = productGroup.brandId |
| product_group_image | productGroupImage | JOIN | productGroupImage.productGroup.id = productGroup.id AND imageType = MAIN AND deleteYn = N |
| seller | seller | JOIN | seller.id = qna.sellerId |
| users | users | LEFT JOIN | users.id = qna.userId |

#### QueryDSL 코드

```java
queryFactory.from(qna)
    .leftJoin(qnaAnswer).on(qnaAnswer.qnaId.eq(qna.id))
    .join(qnaProduct).on(qna.id.eq(qnaProduct.qnaId))
    .join(productGroup).on(qnaProduct.productGroupId.eq(productGroup.id))
    .join(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))
    .join(productGroupImage)
        .on(productGroupImage.productGroup.id.eq(productGroup.id))
        .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
        .on(productGroupImage.deleteYn.eq(Yn.N))
    .join(seller).on(seller.id.eq(qna.sellerId))
    .leftJoin(users).on(users.id.eq(qna.userId))
    .where(qnaIdIn(qnaIds))
    .orderBy(qna.id.desc())
    .transform(
        GroupBy.groupBy(qna.id).list(
            new QQnaResponse(
                new QFetchQnaResponse(
                    qna.id, qna.qnaContents, qna.privateYn, qna.qnaStatus,
                    qna.qnaType, qna.qnaDetailType, qna.userType,
                    new QProductQnaTarget(
                        productGroup.id,
                        productGroup.productGroupDetails.productGroupName,
                        productGroupImage.imageDetail.imageUrl,
                        new QBrandDto(brand.id, brand.brandName)
                    ),
                    qna.userId, users.name, qna.insertDate, qna.updateDate
                ),
                GroupBy.set(new QAnswerQnaResponse(...))
            )
        )
    );
```

---

### Branch B: fetchOrderQnas (주문 Q&A)

#### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| qna | qna | FROM | - |
| qna_answer | qnaAnswer | LEFT JOIN | qnaAnswer.qnaId = qna.id |
| qna_order | qnaOrder | JOIN | qna.id = qnaOrder.qnaId |
| qna_image | qnaImage | LEFT JOIN | qnaImage.qnaId = qna.id AND qnaImage.qnaAnswerId = qnaAnswer.id AND deleteYn = N |
| order | order | JOIN | order.id = qnaOrder.orderId |
| order_snapshot_product_group | orderSnapShotProductGroup | JOIN | qnaOrder.orderId = orderSnapShotProductGroup.orderId |
| brand | brand | JOIN | brand.id = orderSnapShotProductGroup.brandId |
| seller | seller | JOIN | seller.id = qna.sellerId |
| users | users | LEFT JOIN | users.id = qna.userId |
| order_snapshot_product_group_image | orderSnapShotProductGroupImage | JOIN | orderId AND imageType = MAIN |
| order_snapshot_option_detail | orderSnapShotOptionDetail | LEFT JOIN | orderId |

#### QueryDSL 코드

```java
queryFactory.from(qna)
    .leftJoin(qnaAnswer).on(qnaAnswer.qnaId.eq(qna.id))
    .join(qnaOrder).on(qna.id.eq(qnaOrder.qnaId))
    .leftJoin(qnaImage)
        .on(qnaImage.qnaId.eq(qna.id))
        .on(qnaImage.qnaAnswerId.eq(qnaAnswer.id))
        .on(qnaImage.deleteYn.eq(Yn.N))
    .join(order).on(order.id.eq(qnaOrder.orderId))
    .join(orderSnapShotProductGroup).on(qnaOrder.orderId.eq(orderSnapShotProductGroup.orderId))
    .join(brand).on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
    .join(seller).on(seller.id.eq(qna.sellerId))
    .leftJoin(users).on(users.id.eq(qna.userId))
    .join(orderSnapShotProductGroupImage)
        .on(orderSnapShotProductGroupImage.orderId.eq(qnaOrder.orderId))
        .on(orderSnapShotProductGroupImage.imageType.eq(ProductGroupImageType.MAIN))
    .leftJoin(orderSnapShotOptionDetail).on(orderSnapShotOptionDetail.orderId.eq(qnaOrder.orderId))
    .where(qnaIdIn(qnaIds))
    .orderBy(qna.id.desc())
    .transform(
        GroupBy.groupBy(qna.id).list(
            new QQnaResponse(
                new QFetchQnaResponse(
                    ...,
                    new QOrderQnaTarget(
                        orderSnapShotProductGroup.productGroupId,
                        orderSnapShotProductGroup.productGroupName,
                        orderSnapShotProductGroupImage.imageUrl,
                        new QBrandDto(brand.id, brand.brandName),
                        order.paymentId,
                        order.id,
                        order.orderAmount,
                        order.quantity,
                        GroupBy.list(orderSnapShotOptionDetail.optionValue)
                    ),
                    ...
                ),
                GroupBy.set(new QAnswerQnaResponse(...))
            )
        )
    );
```

---

## 📋 WHERE 조건 요약

| 조건 | 필드 | 설명 |
|------|------|------|
| userIdEq | qna.userId | 현재 로그인 사용자 |
| qnaIdLt | qna.id | 커서 기반 페이징 (lastDomainId) |
| qnaTypeEq | qna.qnaType | Q&A 유형 (PRODUCT/ORDER) |
| betweenTime | qna.insertDate | 기간 필터 (startDate ~ endDate) |

---

## 📋 관련 Entity

### 공통
| Entity | 테이블 | 설명 |
|--------|--------|------|
| Qna | qna | Q&A 본문 |
| QnaAnswer | qna_answer | Q&A 답변 |
| QnaImage | qna_image | Q&A 이미지 |
| Users | users | 사용자 |
| Seller | seller | 판매자 |
| Brand | brand | 브랜드 |

### 상품 Q&A 전용
| Entity | 테이블 | 설명 |
|--------|--------|------|
| QnaProduct | qna_product | Q&A-상품 매핑 |
| ProductGroup | product_group | 상품그룹 |
| ProductGroupImage | product_group_image | 상품 이미지 |

### 주문 Q&A 전용
| Entity | 테이블 | 설명 |
|--------|--------|------|
| QnaOrder | qna_order | Q&A-주문 매핑 |
| Order | order | 주문 |
| OrderSnapShotProductGroup | order_snapshot_product_group | 주문 스냅샷 (상품그룹) |
| OrderSnapShotProductGroupImage | order_snapshot_product_group_image | 주문 스냅샷 (이미지) |
| OrderSnapShotOptionDetail | order_snapshot_option_detail | 주문 스냅샷 (옵션) |

---

## ⚠️ 마이그레이션 시 주의사항

1. **분기 로직**: `qnaType`에 따라 완전히 다른 쿼리 실행 (상품/주문)
2. **커서 기반 페이징**: `lastDomainId`로 무한 스크롤 지원
3. **스냅샷 테이블**: 주문 Q&A는 원본 상품이 아닌 스냅샷 테이블에서 조회
4. **옵션 조합**: `OrderQnaTarget.setOption()`으로 옵션값 문자열 조합
5. **이미지 별도 조회**: 주문 Q&A는 `QnaImageFindService`로 이미지 별도 조회

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert web:QnaController.fetchMyQnas

# Persistence Layer 생성
/legacy-query web:QnaController.fetchMyQnas
```

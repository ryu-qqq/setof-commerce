# API Flow: QnaController.getMyQnas

## 1. 기본 정보

- HTTP: GET /api/v1/qna/my-page
- Controller: QnaController
- Service: QnaFindService → QnaFindServiceImpl
- Repository (메인): QnaFindRepository → QnaFindRepositoryImpl
- Repository (이미지): QnaImageFindRepository → QnaImageFindRepositoryImpl
- 인증: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 로그인한 일반 회원 전용

---

## 2. Request

### Query Parameters

`@ModelAttribute QnaFilter` + `Pageable`

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| qnaType | QnaType (PRODUCT \| ORDER) | 필수 | QNA 유형 구분 |
| lastDomainId | Long | 선택 | 커서 기반 페이징용 마지막 QNA ID |
| startDate | LocalDateTime | 필수 | 조회 시작 날짜 (yyyy-MM-dd HH:mm:ss) |
| endDate | LocalDateTime | 필수 | 조회 종료 날짜 (yyyy-MM-dd HH:mm:ss) |
| page | Integer | 선택 | Pageable - 페이지 번호 (기본값 0) |
| size | Integer | 선택 | Pageable - 페이지 크기 |
| sort | String | 선택 | Pageable - 정렬 조건 |

> `startDate`와 `endDate`는 `@ValidDateRange` 커스텀 어노테이션으로 범위 유효성 검사를 수행합니다.

### Query String Example (상품 QNA)

```
GET /api/v1/qna/my-page?qnaType=PRODUCT&startDate=2024-01-01 00:00:00&endDate=2024-12-31 23:59:59&size=10
```

### Query String Example (주문 QNA, 커서 페이징)

```
GET /api/v1/qna/my-page?qnaType=ORDER&lastDomainId=500&startDate=2024-01-01 00:00:00&endDate=2024-12-31 23:59:59&size=10
```

---

## 3. Response

### DTO 구조

```
ApiResponse<CustomSlice<QnaResponse>>
├── CustomSlice
│   ├── content: List<QnaResponse>
│   │   └── QnaResponse
│   │       ├── qna: FetchQnaResponse
│   │       │   ├── qnaId: long
│   │       │   ├── qnaContents: QnaContents
│   │       │   │   ├── title: String
│   │       │   │   └── content: String
│   │       │   ├── privateYn: Yn (Y | N)
│   │       │   ├── qnaStatus: QnaStatus (OPEN | CLOSED)
│   │       │   ├── qnaType: QnaType (PRODUCT | ORDER)
│   │       │   ├── qnaDetailType: QnaDetailType
│   │       │   ├── userType: UserType
│   │       │   ├── userName: String (마스킹 처리: 타인의 경우 끝 2자리 **)
│   │       │   ├── qnaImages: List<QnaImageDto>  ← 주문 QNA일 때만 포함
│   │       │   │   ├── qnaIssueType: QnaIssueType (QUESTION | ANSWER)
│   │       │   │   ├── qnaImageId: Long
│   │       │   │   ├── qnaId: Long
│   │       │   │   ├── qnaAnswerId: Long
│   │       │   │   ├── imageUrl: String
│   │       │   │   └── displayOrder: int
│   │       │   ├── qnaTarget: QnaTarget (상품/주문에 따라 구현체 다름)
│   │       │   │   [PRODUCT] ProductQnaTarget
│   │       │   │   ├── productGroupId: long
│   │       │   │   ├── productGroupName: String
│   │       │   │   ├── productGroupMainImageUrl: String
│   │       │   │   └── brand: BrandDto { brandId, brandName }
│   │       │   │
│   │       │   │   [ORDER] OrderQnaTarget extends ProductQnaTarget
│   │       │   │   ├── productGroupId: long
│   │       │   │   ├── productGroupName: String
│   │       │   │   ├── productGroupMainImageUrl: String
│   │       │   │   ├── brand: BrandDto { brandId, brandName }
│   │       │   │   ├── paymentId: long
│   │       │   │   ├── orderId: long
│   │       │   │   ├── orderAmount: long
│   │       │   │   ├── quantity: int
│   │       │   │   └── option: String (옵션값 공백 join)
│   │       │   ├── insertDate: LocalDateTime
│   │       │   └── updateDate: LocalDateTime
│   │       │
│   │       └── answerQnas: Set<AnswerQnaResponse>
│   │           ├── qnaAnswerId: long
│   │           ├── qnaAnswerParentId: Long
│   │           ├── qnaWriterType: QnaWriterType
│   │           ├── qnaContents: QnaContents { title, content }
│   │           ├── qnaImages: List<QnaImageDto>  ← 주문 QNA일 때만 포함
│   │           ├── insertDate: LocalDateTime
│   │           └── updateDate: LocalDateTime
│   │
│   ├── last: boolean
│   ├── first: boolean
│   ├── number: int
│   ├── size: int
│   ├── numberOfElements: int
│   ├── empty: boolean
│   ├── lastDomainId: Long   ← 다음 커서 값
│   ├── cursorValue: String
│   └── totalElements: Long
```

### JSON Example (상품 QNA)

```json
{
  "result": "SUCCESS",
  "data": {
    "content": [
      {
        "qna": {
          "qnaId": 123,
          "qnaContents": {
            "title": "사이즈 문의드립니다",
            "content": "M 사이즈 착용 가능한가요?"
          },
          "privateYn": "N",
          "qnaStatus": "OPEN",
          "qnaType": "PRODUCT",
          "qnaDetailType": "SIZE",
          "userType": "MEMBERS",
          "userName": "홍길**",
          "qnaImages": [],
          "qnaTarget": {
            "productGroupId": 456,
            "productGroupName": "기본 반팔 티셔츠",
            "productGroupMainImageUrl": "https://example.com/images/product_main.jpg",
            "brand": {
              "brandId": 10,
              "brandName": "브랜드명"
            }
          },
          "insertDate": "2024-06-01 10:00:00",
          "updateDate": "2024-06-01 10:00:00"
        },
        "answerQnas": []
      }
    ],
    "last": false,
    "first": true,
    "number": 0,
    "size": 10,
    "numberOfElements": 10,
    "empty": false,
    "lastDomainId": 113,
    "cursorValue": null,
    "totalElements": null
  }
}
```

### JSON Example (주문 QNA)

```json
{
  "result": "SUCCESS",
  "data": {
    "content": [
      {
        "qna": {
          "qnaId": 200,
          "qnaContents": {
            "title": "배송 관련 문의",
            "content": "주문한 상품이 아직 미배송 상태입니다"
          },
          "privateYn": "Y",
          "qnaStatus": "CLOSED",
          "qnaType": "ORDER",
          "qnaDetailType": "SHIPMENT",
          "userType": "MEMBERS",
          "userName": "김철**",
          "qnaImages": [
            {
              "qnaIssueType": "QUESTION",
              "qnaImageId": 1,
              "qnaId": 200,
              "qnaAnswerId": null,
              "imageUrl": "https://example.com/qna/question_img.jpg",
              "displayOrder": 1
            }
          ],
          "qnaTarget": {
            "productGroupId": 789,
            "productGroupName": "남성 청바지",
            "productGroupMainImageUrl": "https://example.com/images/jean_main.jpg",
            "brand": {
              "brandId": 20,
              "brandName": "진브랜드"
            },
            "paymentId": 111,
            "orderId": 222,
            "orderAmount": 59000,
            "quantity": 1,
            "option": "블루 L"
          },
          "insertDate": "2024-05-15 14:30:00",
          "updateDate": "2024-05-16 09:00:00"
        },
        "answerQnas": [
          {
            "qnaAnswerId": 301,
            "qnaAnswerParentId": null,
            "qnaWriterType": "CUSTOMER",
            "qnaContents": {
              "title": "답변 드립니다",
              "content": "확인 후 빠르게 처리하겠습니다"
            },
            "qnaImages": [],
            "insertDate": "2024-05-16 09:00:00",
            "updateDate": "2024-05-16 09:00:00"
          }
        ]
      }
    ],
    "last": true,
    "first": true,
    "number": 0,
    "size": 10,
    "numberOfElements": 1,
    "empty": false,
    "lastDomainId": 200,
    "cursorValue": null,
    "totalElements": null
  }
}
```

---

## 4. 호출 흐름

```
[Client] GET /api/v1/qna/my-page?qnaType=PRODUCT|ORDER&...
    │
    ├── [Security] @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    │       └── 미인증/권한 없음 시 403 반환
    │
    └── QnaController.getMyQnas(QnaFilter qnaFilter, Pageable pageable)
            │
            └── QnaFindService.fetchMyQnas(qnaFilter, pageable)
                    └── QnaFindServiceImpl.fetchMyQnas()
                            │
                            ├── SecurityUtils.currentUserId()
                            │       └── Spring Security Context에서 userId 추출
                            │
                            ├── QnaFindRepository.fetchMyQnas(userId, qnaFilter, pageable)
                            │       └── QnaFindRepositoryImpl.fetchMyQnas()
                            │               │
                            │               ├── [qnaType == PRODUCT]
                            │               │   fetchProductQnas(userId, filter, pageable)
                            │               │       ├── fetchQnaIds()  ← 커서 페이징 ID 목록 조회
                            │               │       └── fetchProductQnas 본 쿼리
                            │               │           (qna + qnaAnswer + qnaProduct + productGroup
                            │               │            + brand + productGroupImage + seller + users)
                            │               │
                            │               └── [qnaType == ORDER]
                            │                   fetchOrderQnas(userId, filter, pageable)
                            │                       ├── fetchQnaIds()  ← 커서 페이징 ID 목록 조회
                            │                       └── fetchOrderQnas 본 쿼리
                            │                           (qna + qnaAnswer + qnaOrder + qnaImage
                            │                            + order + orderSnapShotProductGroup + brand
                            │                            + seller + users + orderSnapShotProductGroupImage
                            │                            + orderSnapShotOptionDetail)
                            │
                            ├── [qnaType == ORDER 일 때만 실행]
                            │   QnaImageFindService.fetchQnaImageEntitiesByQnaId(qnaIds)
                            │       └── QnaImageFindServiceImpl
                            │               └── QnaImageFindRepository.fetchQnaImageEntitiesByQnaId(qnaIds)
                            │                       └── QnaImageFindRepositoryImpl
                            │                           SELECT * FROM qna_image
                            │                           WHERE qna_id IN (:qnaIds) AND delete_yn = 'N'
                            │
                            ├── QnaMapper.toQnaResponse(qnaResponses, qnaImages)  ← ORDER일 때만
                            │       └── QnaMapperImpl: 이미지 매핑 + 비공개/타인 글 접근 제한 처리
                            │
                            ├── QnaMapper.toQnaList(qnaResponses)
                            │       └── QnaMapperImpl: 이름 마스킹 + 비밀글 내용 제한
                            │
                            └── QnaSliceMapper.toSlice(results, pageable)
                                    └── QnaSliceMapperImpl: CustomSlice 생성 (hasNext 판단 포함)
```

---

## 5. Database Query

### 5-1. 커서 페이징 ID 조회 (fetchQnaIds - 공통)

**대상 테이블**: `qna`, `seller`, `users`

```sql
SELECT qna.qna_id
FROM qna
JOIN seller ON seller.seller_id = qna.seller_id
LEFT JOIN users ON users.user_id = qna.user_id
WHERE qna.user_id = :userId
  AND (:lastDomainId IS NULL OR qna.qna_id < :lastDomainId)   -- 커서 조건
  AND qna.qna_type = :qnaType                                   -- PRODUCT | ORDER
  AND qna.insert_date BETWEEN :startDate AND :endDate
ORDER BY qna.qna_id DESC
LIMIT :pageSize + 1
```

> `pageSize + 1`로 조회하여 다음 페이지 존재 여부를 판단합니다 (커서 기반 슬라이스 패턴).

---

### 5-2. 상품 QNA 본 쿼리 (fetchProductQnas)

**대상 테이블**: `qna`, `qna_answer`, `qna_product`, `product_group`, `brand`, `product_group_image`, `seller`, `users`

```sql
-- QueryDSL transform + GroupBy 방식
SELECT
    qna.qna_id,
    qna.title,
    qna.content,
    qna.private_yn,
    qna.qna_status,
    qna.qna_type,
    qna.qna_detail_type,
    qna.user_type,
    -- ProductQnaTarget
    product_group.product_group_id,
    product_group.product_group_name,
    product_group_image.image_url,
    brand.brand_id,
    brand.brand_name,
    -- User
    qna.user_id,
    users.name,
    qna.insert_date,
    qna.update_date,
    -- AnswerQnaResponse
    qna_answer.qna_answer_id,
    qna_answer.qna_parent_id,
    qna_answer.qna_writer_type,
    qna_answer.title AS answer_title,
    qna_answer.content AS answer_content,
    qna_answer.insert_date AS answer_insert_date,
    qna_answer.update_date AS answer_update_date
FROM qna
LEFT JOIN qna_answer ON qna_answer.qna_id = qna.qna_id
JOIN qna_product ON qna.qna_id = qna_product.qna_id
JOIN product_group ON qna_product.product_group_id = product_group.product_group_id
JOIN brand ON brand.brand_id = product_group.brand_id
JOIN product_group_image
    ON product_group_image.product_group_id = product_group.product_group_id
    AND product_group_image.product_group_image_type = 'MAIN'
    AND product_group_image.delete_yn = 'N'
JOIN seller ON seller.seller_id = qna.seller_id
LEFT JOIN users ON users.user_id = qna.user_id
WHERE qna.qna_id IN (:qnaIds)
ORDER BY qna.qna_id DESC
-- GroupBy.groupBy(qna.id) → QnaResponse 객체로 집계
```

---

### 5-3. 주문 QNA 본 쿼리 (fetchOrderQnas)

**대상 테이블**: `qna`, `qna_answer`, `qna_order`, `qna_image`, `order`, `order_snap_shot_product_group`, `brand`, `seller`, `users`, `order_snap_shot_product_group_image`, `order_snap_shot_option_detail`

```sql
SELECT
    qna.qna_id,
    qna.title, qna.content,
    qna.private_yn, qna.qna_status, qna.qna_type, qna.qna_detail_type, qna.user_type,
    -- OrderQnaTarget
    order_snap_shot_product_group.product_group_id,
    order_snap_shot_product_group.product_group_name,
    order_snap_shot_product_group_image.image_url,
    brand.brand_id, brand.brand_name,
    order.payment_id,
    order.order_id,
    order.order_amount,
    order.quantity,
    order_snap_shot_option_detail.option_value,   -- GroupBy.list()
    -- User
    qna.user_id, users.name,
    qna.insert_date, qna.update_date,
    -- AnswerQnaResponse
    qna_answer.qna_answer_id,
    qna_answer.qna_parent_id,
    qna_answer.qna_writer_type,
    qna_answer.title AS answer_title,
    qna_answer.content AS answer_content,
    qna_answer.insert_date, qna_answer.update_date
FROM qna
LEFT JOIN qna_answer ON qna_answer.qna_id = qna.qna_id
JOIN qna_order ON qna.qna_id = qna_order.qna_id
LEFT JOIN qna_image
    ON qna_image.qna_id = qna.qna_id
    AND qna_image.qna_answer_id = qna_answer.qna_answer_id
    AND qna_image.delete_yn = 'N'
JOIN `order` ON order.order_id = qna_order.order_id
JOIN order_snap_shot_product_group
    ON qna_order.order_id = order_snap_shot_product_group.order_id
JOIN brand ON brand.brand_id = order_snap_shot_product_group.brand_id
JOIN seller ON seller.seller_id = qna.seller_id
LEFT JOIN users ON users.user_id = qna.user_id
JOIN order_snap_shot_product_group_image
    ON order_snap_shot_product_group_image.order_id = qna_order.order_id
    AND order_snap_shot_product_group_image.product_group_image_type = 'MAIN'
LEFT JOIN order_snap_shot_option_detail
    ON order_snap_shot_option_detail.order_id = qna_order.order_id
WHERE qna.qna_id IN (:qnaIds)
ORDER BY qna.qna_id DESC
-- GroupBy.groupBy(qna.id) → QnaResponse 객체로 집계
-- GroupBy.list(orderSnapShotOptionDetail.optionValue) → 옵션 목록
```

---

### 5-4. QNA 이미지 조회 (ORDER QNA 전용)

**대상 테이블**: `qna_image`

```sql
SELECT *
FROM qna_image
WHERE qna_id IN (:qnaIds)
  AND delete_yn = 'N'
```

---

### 테이블 요약

| 테이블 | JOIN 방식 | 역할 |
|--------|-----------|------|
| qna | FROM (기준) | QNA 기본 정보 |
| qna_answer | LEFT JOIN | QNA 답변 목록 |
| qna_product | JOIN (PRODUCT 전용) | 상품 QNA 연결 |
| qna_order | JOIN (ORDER 전용) | 주문 QNA 연결 |
| qna_image | LEFT JOIN (ORDER 전용) | QNA 이미지 |
| product_group | JOIN (PRODUCT 전용) | 상품 그룹 정보 |
| product_group_image | JOIN (PRODUCT 전용, MAIN 타입) | 상품 대표 이미지 |
| brand | JOIN | 브랜드 정보 |
| seller | JOIN | 판매자 정보 |
| users | LEFT JOIN | 작성자 이름 |
| order | JOIN (ORDER 전용) | 주문 정보 |
| order_snap_shot_product_group | JOIN (ORDER 전용) | 주문 시점 상품 스냅샷 |
| order_snap_shot_product_group_image | JOIN (ORDER 전용, MAIN 타입) | 주문 시점 이미지 스냅샷 |
| order_snap_shot_option_detail | LEFT JOIN (ORDER 전용) | 주문 시점 옵션 스냅샷 |

---

## 6. 비즈니스 로직 특이사항

### 커서 기반 페이징 (Cursor Pagination)

- `lastDomainId`를 커서로 사용하는 2-step 쿼리 패턴
- Step 1: `fetchQnaIds()` - `qna_id < lastDomainId` 조건으로 ID 목록만 조회 (`pageSize + 1`개)
- Step 2: ID 목록으로 본 데이터 조회 (JOIN이 많은 쿼리)
- `pageSize + 1`개 조회 후 `CustomSlice`에서 `hasNext` 판단

### 비밀글 / 이름 마스킹 처리 (QnaMapperImpl)

- 작성자 본인 여부: `SecurityUtils.currentUserId() != qna.userId`이면 타인 글
- 타인의 비밀글(`privateYn = Y`): 제목/내용을 "비밀글 입니다."로 대체, 답변 Set 비움
- 타인 글 이름 마스킹: 이름 끝 2자리를 `**`로 치환 (예: `홍길동` → `홍길**`)
- 본인 글에도 이름 마스킹 적용 (자신의 이름도 동일하게 마스킹)

### QNA 이미지 처리 (ORDER 전용)

- `qnaType == ORDER`일 때만 `QnaImageFindService`를 호출하여 별도 이미지 조회
- 이미지는 `QnaIssueType`(QUESTION / ANSWER)으로 분류하여 QNA 본문 또는 답변에 각각 매핑
- `PRODUCT` QNA는 이미지 기능 미지원

### QNA 타입별 분기

| qnaType | 조회 방식 | 연결 테이블 | 이미지 조회 |
|---------|-----------|------------|------------|
| PRODUCT | fetchProductQnas | qna_product, product_group | 없음 |
| ORDER | fetchOrderQnas | qna_order, order, 스냅샷 3종 | 있음 (qna_image 별도 조회) |

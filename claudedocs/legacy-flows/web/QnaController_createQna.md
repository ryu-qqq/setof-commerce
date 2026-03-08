# API Flow: QnaController.doQuestion

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| API Path | /api/v1/qna |
| Controller | QnaController |
| Service Interface | QnaQueryService |
| Service Impl | QnaQueryServiceImpl |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 2. Request

### Parameters

`@RequestBody @Validated CreateQna`

`CreateQna`는 인터페이스이며, `type` 필드에 따라 두 가지 구현체로 역직렬화됩니다.

```json
{
  "type": "productQna" | "orderQna"
}
```

### Case A: productQna (CreateProductQna)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|-----------|------|
| type | String | 필수 | `"productQna"` 고정 | Jackson @JsonTypeInfo 판별자 |
| qnaContents.title | String | 선택 | max 100자 | 질문 제목 |
| qnaContents.content | String | 선택 | max 500자 | 질문 내용 |
| privateYn | Yn | 선택 | `Y` / `N` | 비공개 여부 |
| qnaType | QnaType | 선택 | `PRODUCT` / `ORDER` | QnA 유형 |
| qnaDetailType | QnaDetailType | 선택 | SIZE / SHIPMENT / RESTOCK / ORDER_PAYMENT / CANCEL / EXCHANGE / AS / REFUND / ETC | 상세 유형 |
| sellerId | Long | 필수 | @NotNull | 셀러 아이디 |
| productGroupId | long | 필수 | @Min(1) | 상품 그룹 아이디 |

**JSON 예시 (productQna)**:
```json
{
  "type": "productQna",
  "qnaContents": {
    "title": "사이즈 문의",
    "content": "L 사이즈 재고 있나요?"
  },
  "privateYn": "N",
  "qnaType": "PRODUCT",
  "qnaDetailType": "SIZE",
  "sellerId": 1,
  "productGroupId": 100
}
```

### Case B: orderQna (CreateOrderQna)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|-----------|------|
| type | String | 필수 | `"orderQna"` 고정 | Jackson @JsonTypeInfo 판별자 |
| qnaContents.title | String | 선택 | max 100자 | 질문 제목 |
| qnaContents.content | String | 선택 | max 500자 | 질문 내용 |
| privateYn | Yn | 선택 | `Y` / `N` | 비공개 여부 |
| qnaType | QnaType | 선택 | `PRODUCT` / `ORDER` | QnA 유형 |
| qnaDetailType | QnaDetailType | 선택 | (위 동일) | 상세 유형 |
| sellerId | Long | 필수 | @NotNull | 셀러 아이디 |
| orderId | long | 필수 | @Min(1) | 주문 아이디 |
| qnaImages | List\<CreateQnaImage\> | 선택 | @Size(max=3) | 첨부 이미지 목록 (최대 3장) |
| qnaImages[].imageUrl | String | 선택 | - | 이미지 URL (S3 Pre-signed URL 등) |
| qnaImages[].displayOrder | int | 필수 | @NotNull | 이미지 표시 순서 |
| qnaImages[].qnaImageId | Long | 선택 | - | 기존 이미지 ID (수정 시 사용) |

**JSON 예시 (orderQna)**:
```json
{
  "type": "orderQna",
  "qnaContents": {
    "title": "배송 문의",
    "content": "주문한 상품이 아직 안 왔어요."
  },
  "privateYn": "Y",
  "qnaType": "ORDER",
  "qnaDetailType": "SHIPMENT",
  "sellerId": 1,
  "orderId": 5000,
  "qnaImages": [
    {
      "imageUrl": "https://cdn.example.com/qna/image1.jpg",
      "displayOrder": 1
    }
  ]
}
```

---

## 3. Response

### DTO Structure

`ResponseEntity<ApiResponse<Qna>>`

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id (qna_id) | Long | 생성된 QnA 아이디 |
| qnaContents.title | String | 질문 제목 |
| qnaContents.content | String | 질문 내용 |
| privateYn | Yn | 비공개 여부 (`Y`/`N`) |
| qnaStatus | QnaStatus | 상태 (`OPEN` / `CLOSED`) |
| qnaType | QnaType | QnA 유형 (`PRODUCT` / `ORDER`) |
| qnaDetailType | QnaDetailType | 상세 유형 |
| sellerId | long | 셀러 아이디 |
| userId | long | 작성 회원 아이디 (SecurityUtils 추출) |
| userType | UserType | 작성자 유형 (`MEMBERS` 고정) |
| insertDate | LocalDateTime | 생성 일시 (BaseEntity) |
| updateDate | LocalDateTime | 수정 일시 (BaseEntity) |

### JSON 예시

```json
{
  "success": true,
  "data": {
    "id": 789,
    "qnaContents": {
      "title": "배송 문의",
      "content": "주문한 상품이 아직 안 왔어요."
    },
    "privateYn": "Y",
    "qnaStatus": "OPEN",
    "qnaType": "ORDER",
    "qnaDetailType": "SHIPMENT",
    "sellerId": 1,
    "userId": 42,
    "userType": "MEMBERS",
    "insertDate": "2024-01-01 00:00:00",
    "updateDate": "2024-01-01 00:00:00"
  }
}
```

---

## 4. 호출 흐름

### 공통 흐름 (productQna / orderQna 분기 전)

```
POST /api/v1/qna
    └── QnaController.doQuestion(@RequestBody @Validated CreateQna)
            │ @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
            │
            └── QnaQueryService.doQuestion(createQna)
                    └── QnaQueryServiceImpl.doQuestion(createQna)
                            │
                            ├── [1] QnaMapper.toEntity(createQna)
                            │       └── QnaMapperImpl.toEntity()
                            │           → SecurityUtils.currentUserId()로 userId 추출
                            │           → Qna 엔티티 생성 (qnaStatus = OPEN, userType = MEMBERS)
                            │
                            ├── [2] QnaRepository.save(qna)
                            │       → INSERT INTO qna (...)
                            │
                            └── [3] AskStrategy.get(savedQna.getQnaType())
                                    → QnaType에 따라 AskService 구현체 선택
                                    │
                                    ├── [PRODUCT 분기] AskProductService.doAsk(qnaId, createQna)
                                    │       └── QnaProductRepository.save(qnaProduct)
                                    │           → INSERT INTO qna_product (qna_id, product_group_id)
                                    │
                                    └── [ORDER 분기] AskOrderService.doAsk(qnaId, createQna)
                                            │
                                            ├── (이미지가 있는 경우)
                                            │   QnaImageQueryService.saveQnaImages(...)
                                            │       └── QnaImageQueryServiceImpl.saveQnaImages()
                                            │               ├── QnaMapper.toQnaImageEntities(...)
                                            │               │   → CompletableFuture 병렬 이미지 업로드
                                            │               │   → ImageUploadService.uploadImage(ImagePath.QNA, url)
                                            │               └── QnaImageJdbcRepository.saveAll(qnaImages)
                                            │                   → INSERT INTO qna_image (...) [batch]
                                            │
                                            └── QnaOrderRepository.save(qnaOrder)
                                                → INSERT INTO qna_order (qna_id, order_id)
```

### 전략 패턴 (AskStrategy)

`AskStrategy`는 `AbstractProvider<QnaType, AskService>`를 상속하여 QnaType 기반 Map으로 서비스를 관리합니다.

| QnaType | AskService 구현체 | 수행 작업 |
|---------|-----------------|---------|
| PRODUCT | AskProductService | qna_product 테이블에 레코드 저장 |
| ORDER | AskOrderService | qna_order 테이블에 레코드 저장 + 이미지 처리 |

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | 작업 |
|--------|------|------|
| qna | QnA 기본 정보 | INSERT (JPA save) |
| qna_product | 상품 QnA 연관 | INSERT (JPA save) - PRODUCT 타입 시 |
| qna_order | 주문 QnA 연관 | INSERT (JPA save) - ORDER 타입 시 |
| qna_image | QnA 첨부 이미지 | INSERT batch (JDBC) - ORDER 타입 + 이미지 있을 때 |

### qna 테이블 INSERT (JPA)

```sql
INSERT INTO qna (
  title, content,
  private_yn, qna_status, qna_type, qna_detail_type,
  seller_id, user_id, user_type,
  insert_date, update_date
)
VALUES (
  :title, :content,
  :privateYn, 'OPEN', :qnaType, :qnaDetailType,
  :sellerId, :userId, 'MEMBERS',
  NOW(), NOW()
)
```

### qna_product 테이블 INSERT (JPA - PRODUCT 타입)

```sql
INSERT INTO qna_product (qna_id, product_group_id, insert_date, update_date)
VALUES (:qnaId, :productGroupId, NOW(), NOW())
```

### qna_order 테이블 INSERT (JPA - ORDER 타입)

```sql
INSERT INTO qna_order (qna_id, order_id, insert_date, update_date)
VALUES (:qnaId, :orderId, NOW(), NOW())
```

### qna_image 테이블 INSERT (JDBC batch - ORDER 타입 + 이미지 있을 때)

```sql
INSERT INTO qna_image (
  QNA_issue_type, qna_id, qna_answer_id,
  image_url, display_order,
  insert_operator, update_operator
)
VALUES (
  :qnaIssueType, :qnaId, :qnaAnswerId,
  :imageUrl, :displayOrder,
  :insertOperator, :updateOperator
)
-- batchUpdate로 다건 처리
```

---

## 6. 특이 사항

### 다형성 역직렬화 (Polymorphic Deserialization)

요청 JSON의 `type` 필드에 따라 다른 DTO로 역직렬화됩니다.

```
"type": "productQna" → CreateProductQna (productGroupId 포함)
"type": "orderQna"   → CreateOrderQna  (orderId + qnaImages 포함)
```

### 이미지 업로드 비동기 처리

ORDER 타입에서 이미지가 있을 경우, `CompletableFuture`를 사용하여 이미지 URL을 병렬로 S3에 업로드한 후 JDBC batch insert합니다.

```
List<CompletableFuture<QnaImage>> futures
    → CompletableFuture.allOf(...).join()
    → QnaImageJdbcRepository.saveAll(List<QnaImage>)
```

### 이미지 제한

ORDER 타입 QnA에 첨부 가능한 이미지는 최대 3장입니다. (`@Size(max=3)`)

PRODUCT 타입은 현재 이미지 미지원 (`// Todo Product는 아직 이미지 지원 안함`).

### 인증 및 사용자 식별

- `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`: 일반 등급 회원만 접근 가능
- `SecurityUtils.currentUserId()`: 현재 인증된 사용자 ID를 SecurityContext에서 추출하여 `Qna.userId`에 설정

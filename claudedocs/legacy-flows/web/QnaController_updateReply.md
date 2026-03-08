# API Flow: QnaController.updateReply

## 1. 기본 정보

- HTTP: PUT /api/v1/qna/{qnaId}/reply/{qnaAnswerId}
- Controller: QnaController
- 인증: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`
- Service: QnaAnswerQueryService → QnaAnswerQueryServiceImpl
- 조회 Service: QnaAnswerFindService → QnaAnswerFindServiceImpl
- Repository (조회): QnaAnswerFindRepository → QnaAnswerFindRepositoryImpl (QueryDSL)
- Repository (저장 없음): 더티 체킹(Dirty Checking)으로 qna_answer 업데이트
- 부가 처리 (orderQna인 경우): AskOrderService → QnaImageQueryService → QnaImageJdbcRepository (JDBC)

---

## 2. Request

### Path Variables

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| qnaId | long | 필수 | Q&A 식별자 |
| qnaAnswerId | long | 필수 | Q&A 답변 식별자 |

### Request Body (Polymorphic - @JsonTypeInfo)

`type` 필드 값에 따라 두 가지 구현체로 역직렬화됩니다.

#### 공통 상위 필드 (AbstractCreateQna)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| type | String | 필수 | "productQna" or "orderQna" | Jackson 다형성 타입 구분자 |
| qnaContents.title | String | 선택 | @Length(max=100) | 답변 제목 |
| qnaContents.content | String | 선택 | @Length(max=500) | 답변 내용 |
| privateYn | String | 선택 | - | 비공개 여부 ("Y"/"N") |
| qnaType | String | 선택 | - | QnaType enum (PRODUCT, ORDER) |
| qnaDetailType | String | 선택 | - | QnaDetailType enum |
| sellerId | Long | 필수 | @NotNull | 셀러 식별자 |

#### type = "productQna" (CreateProductQna 추가 필드)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| productGroupId | long | 필수 | @Min(1) | 상품 그룹 식별자 |

#### type = "orderQna" (CreateOrderQna 추가 필드)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| orderId | long | 필수 | @Min(1) | 주문 식별자 |
| qnaImages | List<CreateQnaImage> | 선택 | @Size(max=3) | 이미지 목록 (최대 3장) |
| qnaImages[].qnaImageId | Long | 선택 | - | 기존 이미지 ID (null이면 신규) |
| qnaImages[].imageUrl | String | 선택 | - | 이미지 URL |
| qnaImages[].displayOrder | int | 필수 | @NotNull | 노출 순서 |

### JSON 예시 - productQna

```json
{
  "type": "productQna",
  "qnaContents": {
    "title": "답변 제목입니다",
    "content": "답변 내용을 수정합니다"
  },
  "privateYn": "N",
  "qnaType": "PRODUCT",
  "qnaDetailType": "ETC",
  "sellerId": 1,
  "productGroupId": 123
}
```

### JSON 예시 - orderQna (이미지 포함)

```json
{
  "type": "orderQna",
  "qnaContents": {
    "title": "주문 관련 답변 수정",
    "content": "주문 답변 내용을 수정합니다"
  },
  "privateYn": "N",
  "qnaType": "ORDER",
  "qnaDetailType": "ORDER_PAYMENT",
  "sellerId": 1,
  "orderId": 456,
  "qnaImages": [
    {
      "qnaImageId": 10,
      "imageUrl": "https://example.com/image1.jpg",
      "displayOrder": 1
    },
    {
      "qnaImageId": null,
      "imageUrl": "https://example.com/image_new.jpg",
      "displayOrder": 2
    }
  ]
}
```

---

## 3. Response

### DTO 구조

`ResponseEntity<ApiResponse<QnaAnswer>>`

| 필드명 | 타입 | 설명 |
|--------|------|------|
| qna_answer_id | Long | 답변 식별자 |
| qna_id | long | Q&A 식별자 |
| qna_parent_id | Long | 부모 답변 ID |
| qna_writer_type | QnaWriterType | 작성자 유형 (SELLER, CUSTOMER) |
| qna_status | QnaStatus | 답변 상태 (OPEN, CLOSED) |
| qnaContents.title | String | 답변 제목 |
| qnaContents.content | String | 답변 내용 |

### JSON 예시

```json
{
  "success": true,
  "data": {
    "qna_answer_id": 99,
    "qna_id": 1,
    "qna_parent_id": null,
    "qna_writer_type": "SELLER",
    "qna_status": "OPEN",
    "qna_contents": {
      "title": "답변 제목입니다",
      "content": "답변 내용을 수정합니다"
    }
  }
}
```

---

## 4. 호출 흐름

```
QnaController.updateReply(qnaId, qnaAnswerId, createQna)
    │
    └── QnaAnswerQueryService.updateReplyQna(qnaId, qnaAnswerId, createQna)
            │  [QnaAnswerQueryServiceImpl - @Transactional]
            │
            ├── 1. QnaAnswerFindService.fetchQnaAnswerEntity(qnaAnswerId)
            │       └── QnaAnswerFindServiceImpl
            │               └── QnaAnswerFindRepository.fetchQnaAnswerEntity(qnaAnswerId)
            │                       └── QnaAnswerFindRepositoryImpl (QueryDSL)
            │                           SELECT * FROM qna_answer WHERE qna_answer_id = ?
            │                           → 없으면 QnaAnswerNotFoundException 발생
            │
            ├── 2. qnaAnswer.isClosed() 검증
            │       └── qnaStatus.isOpen() == false → QnaContentsUpdateException 발생
            │           (CLOSED 상태의 답변은 수정 불가)
            │
            ├── 3. qnaAnswer.update(createQna.getQnaContents())
            │       └── Dirty Checking으로 qna_answer.title, qna_answer.content 업데이트
            │           (별도 save() 호출 없음, 트랜잭션 커밋 시 자동 반영)
            │
            └── 4. [orderQna인 경우에만] AskStrategy.get(QnaType.ORDER)
                        └── AskOrderService.doReplyUpdate(qnaId, qnaAnswer.getId(), createQna)
                                └── [qnaImages가 비어있지 않은 경우]
                                    QnaImageQueryService.updateQnaImages(qnaId, qnaAnswerId, images, ANSWER)
                                        └── QnaImageQueryServiceImpl
                                                ├── QnaImageFindService.fetchQnaImageEntitiesByQnaId(qnaId)
                                                │       └── QnaImageFindRepositoryImpl (QueryDSL)
                                                │           SELECT * FROM qna_image
                                                │           WHERE qna_id = ? AND delete_yn = 'N'
                                                │
                                                └── processQnaImages(...) 이미지 diff 처리
                                                        ├── imagesToAdd → QnaImageJdbcRepository.saveAll()
                                                        │   INSERT INTO qna_image (...)
                                                        ├── imagesToUpdate → QnaImageJdbcRepository.updateAll()
                                                        │   UPDATE qna_image SET image_url, display_order WHERE qna_image_id IN (...)
                                                        └── imagesToDelete → QnaImageJdbcRepository.deleteAll()
                                                            UPDATE qna_image SET delete_yn = 'Y' WHERE qna_image_id IN (...)
```

---

## 5. 핵심 비즈니스 로직

### 수정 가능 여부 검증

```
qnaAnswer.isClosed() == true  → QnaContentsUpdateException
```

`isClosed()` 는 `qnaStatus != OPEN` 을 의미합니다. 즉, 답변 상태가 CLOSED인 경우 수정이 불가합니다. OPEN 상태일 때만 수정이 가능합니다.

### Dirty Checking 업데이트

`qnaAnswer.update(createQna.getQnaContents())` 는 엔티티 필드를 직접 변경하며, 별도 `save()` 호출 없이 트랜잭션 커밋 시 자동으로 UPDATE 쿼리가 발생합니다.

### QnaType에 따른 조건부 이미지 처리

- `type = "productQna"` : 이미지 처리 로직 없음. 제목/내용만 수정.
- `type = "orderQna"` : `AskOrderService.doReplyUpdate()` 호출. qnaImages 목록이 비어있지 않으면 이미지 diff 처리.

### 이미지 Diff 처리 규칙

| 조건 | 처리 |
|------|------|
| qnaImageId == null | 신규 이미지 INSERT |
| qnaImageId != null이고 url/order 변경됨 | 이미지 UPDATE |
| 기존 이미지가 요청 목록에 없음 | 소프트 DELETE (delete_yn = 'Y') |
| 추가+수정 합계 > 3 | QnaImagesExceedException 발생 |

---

## 6. Database Query

### 관련 테이블

| 테이블 | 작업 | 조건 |
|--------|------|------|
| qna_answer | SELECT | qna_answer_id = {qnaAnswerId} |
| qna_answer | UPDATE (Dirty Checking) | qna_answer_id = {qnaAnswerId} |
| qna_image | SELECT | qna_id = {qnaId} AND delete_yn = 'N' (orderQna, 이미지 있는 경우) |
| qna_image | INSERT | 신규 이미지 배치 (JDBC) |
| qna_image | UPDATE | image_url, display_order 변경된 이미지 배치 (JDBC) |
| qna_image | UPDATE (소프트 삭제) | delete_yn = 'Y' WHERE qna_image_id IN (...) |

### QueryDSL - QnaAnswerFindRepositoryImpl.fetchQnaAnswerEntity

```java
queryFactory.selectFrom(qnaAnswer)
    .where(qnaAnswer.id.eq(qnaAnswerId))
    .fetchOne()
```

**생성 SQL:**
```sql
SELECT *
FROM qna_answer
WHERE qna_answer_id = ?
```

### QueryDSL - QnaImageFindRepositoryImpl.fetchQnaImageEntitiesByQnaId (orderQna 한정)

```java
queryFactory.selectFrom(qnaImage)
    .where(qnaImage.qnaId.eq(qnaId), qnaImage.deleteYn.eq(Yn.N))
    .fetch()
```

**생성 SQL:**
```sql
SELECT *
FROM qna_image
WHERE qna_id = ?
  AND delete_yn = 'N'
```

### JDBC - QnaImageJdbcRepositoryImpl.saveAll (신규 이미지)

```sql
INSERT INTO qna_image (QNA_issue_type, qna_id, qna_answer_id, image_url, display_order, insert_operator, update_operator)
VALUES (:qnaIssueType, :qnaId, :qnaAnswerId, :imageUrl, :displayOrder, :insertOperator, :updateOperator)
```

### JDBC - QnaImageJdbcRepositoryImpl.updateAll (이미지 수정)

```sql
UPDATE qna_image
SET image_url       = :imageUrl,
    display_order   = :displayOrder,
    update_operator = :updateOperator,
    update_date     = :updateDate
WHERE qna_image_id = :qnaImage
```

### JDBC - QnaImageJdbcRepositoryImpl.deleteAll (이미지 소프트 삭제)

```sql
UPDATE qna_image
SET delete_yn       = 'Y',
    update_operator = :updateOperator,
    update_date     = :updateDate
WHERE qna_image_id IN (:ids)
```

---

## 7. 예외 처리

| 예외 클래스 | 발생 조건 |
|-------------|-----------|
| QnaAnswerNotFoundException | qnaAnswerId에 해당하는 QnaAnswer 엔티티가 없을 때 |
| QnaContentsUpdateException | qnaAnswer.isClosed() == true (CLOSED 상태 답변 수정 시도) |
| QnaImagesExceedException | 이미지 추가+수정 합계가 3장을 초과할 때 |

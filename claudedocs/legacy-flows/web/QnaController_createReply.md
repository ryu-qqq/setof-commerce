# API Flow: QnaController.doReply

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| API Path | /api/v1/qna/{qnaId}/reply |
| Controller | QnaController |
| 인증 | @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')") |
| Service Interface | QnaAnswerQueryService |
| Service Impl | QnaAnswerQueryServiceImpl |
| 연관 Service | QnaFindService (QnaFindServiceImpl), QnaAnswerFindService (QnaAnswerFindServiceImpl) |
| Repository | QnaAnswerRepository (JPA), QnaAnswerFindRepository (QueryDSL), QnaFindRepository (QueryDSL) |
| 조건부 Repository | QnaImageJdbcRepository (주문 QnA 이미지, JDBC) |

---

## 2. Request

### Path Variables

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| qnaId | long | 필수 | 답변 대상 QnA ID |

### Request Body - CreateQna (인터페이스, 다형성 역직렬화)

`@JsonTypeInfo(use = Id.NAME, property = "type")` 기반으로 두 가지 타입으로 역직렬화됩니다.

#### 공통 필드 (AbstractCreateQna)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|-----------|------|
| type | String | 필수 | "productQna" 또는 "orderQna" | 타입 구분자 |
| qnaContents.title | String | 선택 | @Length(max=100) | 답변 제목 |
| qnaContents.content | String | 선택 | @Length(max=500) | 답변 내용 |
| privateYn | Yn | 선택 | - | 비공개 여부 (Y/N) |
| qnaType | QnaType | 선택 | - | PRODUCT / ORDER |
| qnaDetailType | QnaDetailType | 선택 | - | 세부 유형 |
| sellerId | Long | 필수 | @NotNull | 셀러 ID |

#### type = "productQna" (CreateProductQna 추가 필드)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|-----------|------|
| productGroupId | long | 필수 | @Min(1) | 상품 그룹 ID |

#### type = "orderQna" (CreateOrderQna 추가 필드)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|-----------|------|
| orderId | long | 필수 | @Min(1) | 주문 ID |
| qnaImages | List\<CreateQnaImage\> | 선택 | @Size(max=3) | 첨부 이미지 목록 (최대 3장) |

##### CreateQnaImage 구조

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| qnaImageId | Long | 선택 | 기존 이미지 ID (null이면 신규) |
| qnaId | Long | 선택 | QnA ID |
| qnaAnswerId | Long | 선택 | QnA 답변 ID |
| imageUrl | String | 선택 | 이미지 URL |
| displayOrder | int | 필수 | @NotNull - 표시 순서 |

### JSON 예시 - productQna

```json
{
  "type": "productQna",
  "qnaContents": {
    "title": "문의 답변드립니다",
    "content": "해당 상품은 S, M, L 사이즈 모두 재고 있습니다."
  },
  "privateYn": "N",
  "qnaType": "PRODUCT",
  "qnaDetailType": "SIZE",
  "sellerId": 1,
  "productGroupId": 100
}
```

### JSON 예시 - orderQna

```json
{
  "type": "orderQna",
  "qnaContents": {
    "title": "주문 관련 답변드립니다",
    "content": "고객님 주문 건 확인 후 처리해드리겠습니다."
  },
  "privateYn": "N",
  "qnaType": "ORDER",
  "qnaDetailType": "ORDER_PAYMENT",
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

### Response DTO - QnaAnswer (Entity 직접 반환)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id (qna_answer_id) | Long | 답변 ID |
| qnaId | long | 연결된 QnA ID |
| qnaParentId | Long | 부모 답변 ID (nullable) |
| qnaWriterType | QnaWriterType | 작성자 타입 (SELLER / CUSTOMER) |
| qnaStatus | QnaStatus | 답변 상태 (OPEN / CLOSED) |
| qnaContents.title | String | 답변 제목 |
| qnaContents.content | String | 답변 내용 |
| insertDate | LocalDateTime | 생성일시 (BaseEntity) |
| updateDate | LocalDateTime | 수정일시 (BaseEntity) |

### JSON 예시

```json
{
  "success": true,
  "data": {
    "id": 321,
    "qnaId": 42,
    "qnaParentId": null,
    "qnaWriterType": "CUSTOMER",
    "qnaStatus": "OPEN",
    "qnaContents": {
      "title": "문의 답변드립니다",
      "content": "해당 상품은 S, M, L 사이즈 모두 재고 있습니다."
    },
    "insertDate": "2024-01-01 00:00:00",
    "updateDate": "2024-01-01 00:00:00"
  }
}
```

---

## 4. 호출 흐름

```
QnaController.doReply(qnaId, createQna)
    │
    ├─ [1] QnaAnswerQueryServiceImpl.replyQna(qnaId, createQna)
    │       │
    │       ├─ [1-1] QnaFindService.fetchQnaStatus(qnaId)
    │       │         └── QnaFindServiceImpl.fetchQnaStatus(qnaId)
    │       │                 └── QnaFindRepository.fetchQnaStatus(qnaId)
    │       │                         └── [QueryDSL] SELECT qna.qna_status FROM qna WHERE qna_id = :qnaId
    │       │                 ※ QnaStatus.OPEN이면 QnaReplyException 발생 (이미 답변 대기 상태)
    │       │
    │       ├─ [1-2] QnaAnswerFindService.fetchLastQnaAnswerBySeller(qnaId)
    │       │         └── QnaAnswerFindServiceImpl.fetchLastQnaAnswerBySeller(qnaId)
    │       │                 └── QnaAnswerFindRepository.fetchQnaAnswerOpenStatus(qnaId)
    │       │                         └── [QueryDSL] SELECT * FROM qna_answer
    │       │                                        WHERE qna_id = :qnaId
    │       │                                          AND qna_writer_type = 'SELLER'
    │       │                                          AND qna_status = 'OPEN'
    │       │                                        ORDER BY qna_answer_id DESC
    │       │                                        LIMIT 1
    │       │                 ※ 결과 없으면 QnaReplyException 발생
    │       │
    │       ├─ [1-3] QnaMapper.toQnaAnswerEntity(qnaId, createQna, Optional.empty())
    │       │         → QnaAnswer 엔티티 생성
    │       │           - qnaWriterType = CUSTOMER
    │       │           - qnaStatus = OPEN
    │       │           - qnaParentId = null (Optional.empty)
    │       │
    │       ├─ [1-4] QnaAnswerRepository.save(qnaAnswer)
    │       │         └── [JPA] INSERT INTO qna_answer (...)
    │       │
    │       ├─ [1-5] (조건부) createQna.getQnaType().isOrderQna() == true 인 경우
    │       │         └── AskStrategy.get(QnaType.ORDER)
    │       │                 └── AskOrderService.doReply(qnaId, savedQnaAnswer.getId(), createQna)
    │       │                         └── (createQna.getQnaImages() 비어있지 않으면)
    │       │                             QnaImageQueryServiceImpl.saveQnaImages(qnaId, qnaAnswerId, images, ANSWER)
    │       │                                 └── QnaMapper.toQnaImageEntities(...) [비동기 이미지 업로드]
    │       │                                         └── ImageUploadService.uploadImage(ImagePath.QNA, imageUrl) [CompletableFuture]
    │       │                                 └── QnaImageJdbcRepository.saveAll(qnaImages)
    │       │                                         └── [JDBC] INSERT INTO qna_image (...) batch
    │       │
    │       └─ [1-6] findQnaAnswer.reply()
    │                 └── [JPA Dirty Checking] UPDATE qna_answer SET qna_status = 'CLOSED'
    │                       (1-2에서 조회된 마지막 SELLER 답변의 상태를 CLOSED로 변경)
    │
    └─ ResponseEntity.ok(ApiResponse.success(savedQnaAnswer))
```

### 비즈니스 로직 요약

1. 대상 QnA의 상태가 `OPEN`이면 예외 발생 (이미 답변 대기 상태 = 회원이 아직 문의를 하지 않은 상태)
2. 해당 QnA에 연결된 가장 최신 셀러(SELLER) 답변이 `OPEN` 상태인 것을 조회 (답변 스레드의 부모)
   - 결과 없으면 예외 발생
3. 신규 QnaAnswer 엔티티 생성 및 저장 (작성자 타입: CUSTOMER, 상태: OPEN)
4. 주문 QnA인 경우 이미지가 있으면 비동기로 이미지 업로드 후 JDBC batch insert
5. 1-2에서 조회한 셀러 답변의 상태를 `CLOSED`로 변경 (Dirty Checking으로 UPDATE)

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | 조작 |
|--------|------|------|
| qna | QnA 원본 | SELECT (상태 조회) |
| qna_answer | QnA 답변 | SELECT (셀러 답변 조회), INSERT (신규 답변), UPDATE (셀러 답변 상태 변경) |
| qna_image | QnA 이미지 | INSERT (주문 QnA 이미지, 조건부) |

### QueryDSL - fetchQnaStatus

```java
// QnaFindRepositoryImpl.fetchQnaStatus
queryFactory
    .select(qna.qnaStatus)
    .from(qna)
    .where(qna.id.eq(qnaId))
    .fetchFirst()

// 대응 SQL
SELECT qna_status
FROM qna
WHERE qna_id = :qnaId
LIMIT 1
```

### QueryDSL - fetchQnaAnswerOpenStatus

```java
// QnaAnswerFindRepositoryImpl.fetchQnaAnswerOpenStatus
queryFactory
    .selectFrom(qnaAnswer)
    .where(
        qnaAnswer.qnaId.eq(qnaId),
        qnaAnswer.qnaWriterType.eq(QnaWriterType.SELLER),
        qnaAnswer.qnaStatus.eq(QnaStatus.OPEN)
    )
    .orderBy(qnaAnswer.id.desc())
    .fetchFirst()

// 대응 SQL
SELECT *
FROM qna_answer
WHERE qna_id = :qnaId
  AND qna_writer_type = 'SELLER'
  AND qna_status = 'OPEN'
ORDER BY qna_answer_id DESC
LIMIT 1
```

### JPA - QnaAnswer INSERT

```java
// QnaAnswerRepository.save(qnaAnswer)
// 생성되는 엔티티 값:
// qna_id          = :qnaId
// qna_parent_id   = null
// qna_writer_type = 'CUSTOMER'
// qna_status      = 'OPEN'
// title           = createQna.getQnaContents().getTitle()
// content         = createQna.getQnaContents().getContent()

INSERT INTO qna_answer
    (qna_id, qna_parent_id, qna_writer_type, qna_status, title, content)
VALUES
    (:qnaId, null, 'CUSTOMER', 'OPEN', :title, :content)
```

### JPA Dirty Checking - QnaAnswer UPDATE (셀러 답변 CLOSED 처리)

```java
// findQnaAnswer.reply() 호출 시
// qnaAnswer.qnaStatus = QnaStatus.CLOSED 로 변경 후 트랜잭션 커밋 시점에 flush

UPDATE qna_answer
SET qna_status = 'CLOSED'
WHERE qna_answer_id = :findQnaAnswerId
```

### JDBC batch - QnaImage INSERT (주문 QnA 이미지, 조건부)

```java
// QnaImageJdbcRepository.saveAll(qnaImages)
// 조건: createQna.getQnaType() == ORDER && !createQna.getQnaImages().isEmpty()

INSERT INTO qna_image
    (qna_issue_type, qna_id, qna_answer_id, image_url, display_order)
VALUES
    ('ANSWER', :qnaId, :qnaAnswerId, :imageUrl, :displayOrder),
    ...
```

---

## 6. 예외 처리

| 예외 클래스 | 발생 조건 | 의미 |
|------------|----------|------|
| QnaReplyException | QnaStatus == OPEN (fetchQnaStatus) | QnA가 아직 열린(문의 대기) 상태 - 답변 불가 |
| QnaReplyException | fetchQnaAnswerOpenStatus 결과 없음 | 답변 가능한 셀러 답변 없음 |

---

## 7. 특이사항

- **다형성 Request**: `CreateQna`는 인터페이스이며 `@JsonTypeInfo`/`@JsonSubTypes`로 `type` 필드에 따라 `CreateProductQna` 또는 `CreateOrderQna`로 역직렬화됨
- **QnaStatus 로직**: `isOpen()` == true이면 예외 발생. 즉, QnA 상태가 OPEN(미답변)인 경우 회원이 답변을 달 수 없음 (셀러가 먼저 답변해야 함)
- **작성자 타입**: `toQnaAnswerEntity`에서 `qnaWriterType = CUSTOMER`로 하드코딩. 이 엔드포인트는 고객(CUSTOMER) 대화 스레드용
- **이미지 업로드**: `CompletableFuture`로 비동기 처리 후 `join()`으로 대기. 업로드 완료 후 JDBC batch insert
- **셀러 답변 상태 변경**: 신규 고객 답변 저장 후 기존 셀러 답변(`qnaWriterType=SELLER, qnaStatus=OPEN`)을 CLOSED로 변경하여 스레드를 닫음
- **이미지 첨부**: 상품 QnA(PRODUCT) 타입은 이미지 첨부 없음. 주문 QnA(ORDER) 타입만 최대 3장 첨부 가능

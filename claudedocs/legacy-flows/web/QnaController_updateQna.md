# API Flow: QnaController.updateQuestion

## 1. 기본 정보

- **HTTP**: PUT /api/v1/qna/{qnaId}
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 일반 회원 전용
- **Controller**: `QnaController`
- **Service**: `QnaQueryService` → `QnaQueryServiceImpl`
- **Sub-Service (PRODUCT)**: `AskProductService` (이미지 미지원, no-op)
- **Sub-Service (ORDER)**: `AskOrderService` → `QnaImageQueryServiceImpl`
- **Repository (조회)**: `QnaFindRepository` → `QnaFindRepositoryImpl`
- **Repository (저장)**: `QnaRepository` (JPA)
- **Repository (이미지, JDBC)**: `QnaImageJdbcRepository` → `QnaImageJdbcRepositoryImpl`

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| qnaId | long | Y | 수정할 QNA ID |

### Request Body

`CreateQna`는 인터페이스이며, `@JsonTypeInfo`로 `type` 필드에 따라 구현체가 결정됨.

#### 공통 부모 필드 (AbstractCreateQna)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| type | String | Y | - | 다형성 식별자: `"productQna"` 또는 `"orderQna"` |
| qnaContents.title | String | N | @Length(max=100) | 질문 제목 |
| qnaContents.content | String | N | @Length(max=500) | 질문 내용 |
| privateYn | Yn | N | - | 비공개 여부 (Y/N) |
| qnaType | QnaType | N | - | `PRODUCT` / `ORDER` |
| qnaDetailType | QnaDetailType | N | - | 세부 유형 (하단 Enum 참고) |
| sellerId | Long | Y | @NotNull | 셀러 ID |

#### CreateProductQna (type = "productQna") 추가 필드

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| productGroupId | long | Y | @Min(1) | 상품 그룹 ID |

#### CreateOrderQna (type = "orderQna") 추가 필드

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| orderId | long | Y | @Min(1) | 주문 ID |
| qnaImages | List\<CreateQnaImage\> | N | @Size(max=3) | 첨부 이미지 목록 (최대 3장) |
| qnaImages[].qnaImageId | Long | N | - | 기존 이미지 ID (null이면 신규) |
| qnaImages[].imageUrl | String | N | - | 이미지 URL |
| qnaImages[].displayOrder | int | Y | @NotNull | 이미지 표시 순서 |

### QnaDetailType Enum 값

| 값 | 설명 |
|----|------|
| SIZE | 사이즈 |
| SHIPMENT | 배송 |
| RESTOCK | 재고 |
| ORDER_PAYMENT | 주문/결제 |
| CANCEL | 취소 |
| EXCHANGE | 교환 |
| AS | AS |
| REFUND | 반품 |
| ETC | 기타 |

### Request JSON 예시 (상품 문의)

```json
{
  "type": "productQna",
  "qnaContents": {
    "title": "사이즈 문의드립니다",
    "content": "XL 사이즈 재고가 있나요?"
  },
  "privateYn": "N",
  "qnaType": "PRODUCT",
  "qnaDetailType": "SIZE",
  "sellerId": 1,
  "productGroupId": 42
}
```

### Request JSON 예시 (주문 문의)

```json
{
  "type": "orderQna",
  "qnaContents": {
    "title": "배송 관련 문의",
    "content": "주문 후 며칠 걸리나요?"
  },
  "privateYn": "N",
  "qnaType": "ORDER",
  "qnaDetailType": "SHIPMENT",
  "sellerId": 1,
  "orderId": 999,
  "qnaImages": [
    {
      "qnaImageId": null,
      "imageUrl": "https://cdn.example.com/qna/image1.jpg",
      "displayOrder": 1
    }
  ]
}
```

---

## 3. Response

### HTTP 200 OK

```java
ResponseEntity<ApiResponse<Qna>>
```

### Response 필드 (Qna Entity 직렬화)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | Long | QNA ID |
| qnaContents.title | String | 제목 |
| qnaContents.content | String | 내용 |
| privateYn | String | 비공개 여부 (Y/N) |
| qnaStatus | String | 상태: OPEN / CLOSED |
| qnaType | String | PRODUCT / ORDER |
| qnaDetailType | String | 세부 유형 |
| sellerId | long | 셀러 ID |
| userId | long | 작성자 유저 ID |
| userType | String | 유저 타입 |
| insertDate | LocalDateTime | 생성일시 |
| updateDate | LocalDateTime | 수정일시 |

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "id": 101,
    "qnaContents": {
      "title": "사이즈 문의드립니다",
      "content": "XL 사이즈 재고가 있나요?"
    },
    "privateYn": "N",
    "qnaStatus": "OPEN",
    "qnaType": "PRODUCT",
    "qnaDetailType": "SIZE",
    "sellerId": 1,
    "userId": 55,
    "userType": "NORMAL",
    "insertDate": "2024-01-01 10:00:00",
    "updateDate": "2024-01-02 11:00:00"
  }
}
```

---

## 4. 호출 흐름

```
PUT /api/v1/qna/{qnaId}
    └── QnaController.updateQuestion(qnaId, createQna)
            └── QnaQueryServiceImpl.updateQuestion(qnaId, createQna)  [@Transactional]
                    ├── 1) QnaFindServiceImpl.fetchQnaEntity(qnaId)
                    │       └── QnaFindRepositoryImpl.fetchQnaEntity(qnaId, SecurityUtils.currentUserId())
                    │               └── [QueryDSL] SELECT FROM qna WHERE qna_id = ? AND user_id = ?
                    │               └── Optional<Qna> → QnaNotFoundException 발생 가능
                    │
                    ├── 2) qna.isClosed() 검증
                    │       └── qnaStatus == CLOSED → QnaContentsUpdateException(qnaId) 발생
                    │
                    ├── 3) qna.updateContents(qna.getQnaContents())
                    │       [주의] 요청 바디의 내용이 아닌 자기 자신의 내용을 다시 세팅하는 버그
                    │       실질적으로 qnaContents 가 변경되지 않음
                    │
                    └── 4) AskStrategy.get(qna.getQnaType()).doAskUpdate(qnaId, createQna)
                            ├── [PRODUCT] AskProductService.doAskUpdate(qnaId, createQna)
                            │       └── no-op (상품 QNA 이미지 미지원, 빈 메서드)
                            │
                            └── [ORDER] AskOrderService.doAskUpdate(qnaId, createQna)
                                    └── qnaImages가 비어있지 않으면:
                                        QnaImageQueryServiceImpl.updateQnaImages(qnaId, null, qnaImages, QUESTION)
                                                ├── QnaImageFindService.fetchQnaImageEntitiesByQnaId(qnaId)
                                                │       └── [QueryDSL] SELECT FROM qna_image WHERE qna_id = ?
                                                ├── processQnaImages() - 이미지 diff 처리
                                                │       ├── qnaImageId == null → imagesToAdd
                                                │       ├── qnaImageId 존재 & 변경됨 → imagesToUpdate
                                                │       └── 기존에 있었으나 요청에 없음 → imagesToDelete
                                                ├── imagesToAdd.size() + imagesToUpdate.size() > 3 → QnaImagesExceedException
                                                ├── [JDBC] QnaImageJdbcRepositoryImpl.saveAll(imagesToAdd)
                                                │       └── INSERT INTO qna_image (...)
                                                ├── [JDBC] QnaImageJdbcRepositoryImpl.updateAll(imagesToUpdate)
                                                │       └── UPDATE qna_image SET image_url=?, display_order=? WHERE qna_image_id=?
                                                └── [JDBC] QnaImageJdbcRepositoryImpl.deleteAll(imagesToDelete)
                                                        └── UPDATE qna_image SET delete_yn='Y' WHERE qna_image_id IN (?)
```

---

## 5. Database Query

### 사용 테이블

| 테이블 | 용도 | 작업 |
|--------|------|------|
| qna | QNA 본문 조회 및 Dirty Checking 업데이트 | SELECT, (UPDATE via JPA) |
| qna_image | 이미지 저장/수정/소프트 삭제 (ORDER 타입 한정) | SELECT, INSERT, UPDATE |

### QueryDSL: fetchQnaEntity (소유자 검증 조회)

```java
queryFactory.selectFrom(qna)
    .where(
        qna.id.eq(qnaId),       // qna_id = :qnaId
        qna.userId.eq(userId)   // user_id = :userId (현재 로그인 유저)
    )
    .fetchFirst()
```

- FROM: `qna`
- WHERE: `qna_id = ? AND user_id = ?`
- 본인 소유 QNA만 수정 가능 (타인 소유 시 404 반환)

### JPA Dirty Checking: Qna 엔티티 업데이트

`@Transactional` 범위 내에서 `qna.updateContents()` 호출 후 트랜잭션 커밋 시 변경 감지로 자동 UPDATE 실행.

```sql
-- 실행되는 SQL (예시)
UPDATE qna
SET title = ?, content = ?, update_date = ?
WHERE qna_id = ?
```

### JDBC Batch: qna_image INSERT (신규 이미지)

```sql
INSERT INTO qna_image
    (QNA_issue_type, qna_id, qna_answer_id, image_url, display_order, insert_operator, update_operator)
VALUES
    (:qnaIssueType, :qnaId, :qnaAnswerId, :imageUrl, :displayOrder, :insertOperator, :updateOperator)
```

- `qna_answer_id` = NULL (질문 이미지이므로)
- `QNA_issue_type` = `QUESTION`

### JDBC Batch: qna_image UPDATE (기존 이미지 변경)

```sql
UPDATE qna_image
SET image_url = :imageUrl,
    display_order = :displayOrder,
    update_operator = :updateOperator,
    update_date = :updateDate
WHERE qna_image_id = :qnaImage
```

### JDBC: qna_image 소프트 삭제 (제거된 이미지)

```sql
UPDATE qna_image
SET delete_yn = 'Y',
    update_operator = :updateOperator,
    update_date = :updateDate
WHERE qna_image_id IN (:ids)
```

---

## 6. 주요 예외 처리

| 예외 클래스 | 조건 | HTTP Status | 코드 |
|------------|------|-------------|------|
| `QnaNotFoundException` | qnaId & userId 조합으로 QNA 미조회 (본인 소유 아님 or 미존재) | 404 | - |
| `QnaContentsUpdateException` | qnaStatus == CLOSED (이미 답변 완료된 QNA) | 404 | QNA-ANSWER-400 |
| `QnaImagesExceedException` | 추가/수정 이미지 합계 > 3장 | - | - |

---

## 7. 코드 버그 발견

`QnaQueryServiceImpl.updateQuestion()` 내 아래 라인에 버그가 존재함.

```java
// 현재 코드 (버그)
qna.updateContents(qna.getQnaContents());
//                 ^^^^^^^^^^^^^^^^^^^^ 요청 바디가 아닌 엔티티 자신의 기존 contents를 전달

// 의도된 코드 (추정)
qna.updateContents(createQna.getQnaContents());
//                 ^^^^^^^^^^^^^^^^^^^^^^^^^ 요청 바디의 새 내용을 전달해야 함
```

현재 구현에서는 `qnaContents`가 실질적으로 업데이트되지 않음. `title`, `content` 수정이 반영되지 않는 결함이 있으며, 이미지 처리(ORDER 타입)만 정상 동작함.

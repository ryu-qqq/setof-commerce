# API Flow: QnaController.fetchProductQnas

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/qna/product/{productGroupId}` |
| Controller | `QnaController` |
| Service Interface | `QnaFindService` |
| Service Impl | `QnaFindServiceImpl` |
| Repository Interface | `QnaFindRepository` |
| Repository Impl | `QnaFindRepositoryImpl` |
| 인증 | 없음 (공개 API, @PreAuthorize 없음) |
| 트랜잭션 | `@Transactional(readOnly = true)` |

---

## 2. Request

### Parameters

| 이름 | 위치 | 타입 | 필수 | 설명 |
|------|------|------|------|------|
| productGroupId | @PathVariable | long | 필수 | 상품그룹 ID |
| page | Query | int | 선택 | 페이지 번호 (0부터 시작, 기본값 0) |
| size | Query | int | 선택 | 페이지 크기 (기본값 20) |
| sort | Query | String | 선택 | 정렬 조건 (예: `qnaId,desc`) |

### Request 예시

```
GET /api/v1/qna/product/12345?page=0&size=10
```

---

## 3. Response

### DTO 구조

```
ApiResponse<Page<QnaResponse>>
└── data: Page<QnaResponse>
    └── QnaResponse
        ├── qna: FetchQnaResponse
        │   ├── qnaId: long
        │   ├── qnaContents: QnaContents (Embedded)
        │   │   ├── title: String   (최대 100자)
        │   │   └── content: String (최대 500자)
        │   ├── privateYn: Yn       (Y | N)
        │   ├── qnaStatus: QnaStatus (OPEN | CLOSED)
        │   ├── qnaType: QnaType    (PRODUCT | ORDER)
        │   ├── qnaDetailType: QnaDetailType
        │   │   (SIZE | SHIPMENT | RESTOCK | ORDER_PAYMENT | CANCEL | EXCHANGE | AS | REFUND | ETC)
        │   ├── userType: UserType
        │   ├── qnaImages: List<QnaImageDto>  (PRODUCT Q&A는 항상 빈 목록)
        │   ├── qnaTarget: QnaTarget          (@JsonInclude NON_NULL, 이 엔드포인트에서는 null)
        │   ├── userId: long                  (@JsonIgnore - 직렬화 제외)
        │   ├── userName: String              (비작성자는 마스킹: 뒤 2자리 **)
        │   ├── insertDate: LocalDateTime     (yyyy-MM-dd HH:mm:ss)
        │   └── updateDate: LocalDateTime     (yyyy-MM-dd HH:mm:ss)
        └── answerQnas: Set<AnswerQnaResponse>
            └── AnswerQnaResponse
                ├── qnaAnswerId: long
                ├── qnaAnswerParentId: Long   (대댓글 부모 ID, null 가능)
                ├── qnaWriterType: QnaWriterType (SELLER | CUSTOMER)
                ├── qnaContents: QnaContents
                │   ├── title: String
                │   └── content: String
                ├── qnaImages: List<QnaImageDto> (PRODUCT Q&A는 항상 빈 목록)
                ├── insertDate: LocalDateTime
                └── updateDate: LocalDateTime
```

### QnaImageDto 구조 (ORDER Q&A 전용, 이 엔드포인트에서는 미사용)

```
QnaImageDto
├── qnaIssueType: QnaIssueType
├── qnaImageId: Long    (@JsonInclude NON_NULL)
├── qnaId: Long         (@JsonInclude NON_NULL)
├── qnaAnswerId: Long   (@JsonInclude NON_NULL)
├── imageUrl: String
└── displayOrder: int
```

### Response JSON 예시

```json
{
  "data": {
    "content": [
      {
        "qna": {
          "qnaId": 1001,
          "qnaContents": {
            "title": "사이즈 문의",
            "content": "M 사이즈 어깨 실측이 어떻게 되나요?"
          },
          "privateYn": "N",
          "qnaStatus": "OPEN",
          "qnaType": "PRODUCT",
          "qnaDetailType": "SIZE",
          "userType": "MEMBERS",
          "qnaImages": [],
          "userName": "홍길**",
          "insertDate": "2024-01-15 10:30:00",
          "updateDate": "2024-01-15 10:30:00"
        },
        "answerQnas": [
          {
            "qnaAnswerId": 2001,
            "qnaAnswerParentId": null,
            "qnaWriterType": "SELLER",
            "qnaContents": {
              "title": "답변드립니다",
              "content": "M 사이즈 어깨 실측은 45cm입니다."
            },
            "qnaImages": [],
            "insertDate": "2024-01-15 14:00:00",
            "updateDate": "2024-01-15 14:00:00"
          }
        ]
      },
      {
        "qna": {
          "qnaId": 1002,
          "qnaContents": {
            "title": "비밀글 입니다.",
            "content": "비밀글 입니다."
          },
          "privateYn": "Y",
          "qnaStatus": "OPEN",
          "qnaType": "PRODUCT",
          "qnaDetailType": "ETC",
          "userType": "MEMBERS",
          "qnaImages": [],
          "userName": "이영**",
          "insertDate": "2024-01-16 09:00:00",
          "updateDate": "2024-01-16 09:00:00"
        },
        "answerQnas": []
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0
    },
    "totalElements": 50,
    "totalPages": 5,
    "last": false,
    "first": true
  },
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

---

## 4. 호출 흐름

```
QnaController.fetchProductQnas(productGroupId, pageable)
    GET /api/v1/qna/product/{productGroupId}
    │
    └── QnaFindService.fetchProductQuestions(productGroupId, pageable)
            [인터페이스]
            │
            └── QnaFindServiceImpl.fetchProductQuestions()
                    @Transactional(readOnly = true)
                    │
                    ├── 1. QnaFindRepository.fetchQnaProductIds(productGroupId, pageable)
                    │       → List<Long> qnaIds   (페이징 적용된 Q&A ID 목록)
                    │
                    ├── 2. QnaFindServiceImpl.fetchQnas(QnaType.PRODUCT, qnaIds)
                    │       │
                    │       ├── QnaFindRepository.fetchQnas(qnaIds)
                    │       │       → List<QnaResponse>  (Q&A + 답변 GroupBy 조회)
                    │       │
                    │       └── [QnaType.PRODUCT이므로 qnaImageFindService 호출 생략]
                    │           (ORDER Q&A에서만 이미지 조회)
                    │
                    ├── 3. QnaMapper.toQnaList(qnaResponses)
                    │       → 비작성자 userName 마스킹 (뒤 2자리 **)
                    │       → privateYn=Y AND 본인 아닌 경우 내용 "비밀글 입니다." 처리
                    │
                    └── 4. PageableExecutionUtils.getPage(
                                qnaResponses, pageable,
                                () -> QnaFindRepository.fetchQnaCountQuery(productGroupId).fetchCount()
                            )
                            → Page<QnaResponse> 반환 (count 쿼리는 lazily 실행)
```

---

## 5. Database Query 분석

### Query 1: fetchQnaProductIds - 페이징 ID 목록 조회

**조회 테이블**

| 테이블 | JOIN 유형 | JOIN 조건 |
|--------|----------|----------|
| qna_product | FROM (기준) | - |
| qna | LEFT JOIN | qna.qna_id = qna_product.qna_id |
| users | LEFT JOIN | users.id = qna.user_id |

**QueryDSL**

```java
queryFactory
    .select(qna.id)
    .from(qnaProduct)
    .leftJoin(qna).on(qna.id.eq(qnaProduct.qnaId))
    .leftJoin(users).on(users.id.eq(qna.userId))
    .where(
        qnaProduct.productGroupId.eq(productGroupId)  // productGroupIdEq()
    )
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .fetch();
```

**WHERE 조건**

| 조건명 | 컬럼 | 설명 |
|--------|------|------|
| productGroupIdEq | qna_product.product_group_id | 상품그룹 ID 일치 |

---

### Query 2: fetchQnas - Q&A 상세 + 답변 GroupBy 조회

**조회 테이블**

| 테이블 | JOIN 유형 | JOIN 조건 |
|--------|----------|----------|
| qna | FROM (기준) | - |
| qna_answer | LEFT JOIN | qna_answer.qna_id = qna.qna_id |
| qna_image | LEFT JOIN | qna_image.qna_id = qna.qna_id AND qna_image.qna_answer_id = qna_answer.qna_answer_id AND qna_image.delete_yn = 'N' |
| users | LEFT JOIN | users.id = qna.user_id |

**QueryDSL**

```java
queryFactory.from(qna)
    .leftJoin(qnaAnswer).on(qnaAnswer.qnaId.eq(qna.id))
    .leftJoin(qnaImage)
        .on(qnaImage.qnaId.eq(qna.id))
        .on(qnaImage.qnaAnswerId.eq(qnaAnswer.id))
        .on(qnaImage.deleteYn.eq(Yn.N))
    .leftJoin(users).on(users.id.eq(qna.userId))
    .where(
        qna.id.in(qnaIds)  // qnaIdIn()
    )
    .transform(
        GroupBy.groupBy(qna.id).list(
            new QQnaResponse(
                new QFetchQnaResponse(
                    qna.id,
                    qna.qnaContents,       // Embedded: title, content
                    qna.privateYn,
                    qna.qnaStatus,
                    qna.qnaType,
                    qna.qnaDetailType,
                    qna.userType,
                    qna.userId,
                    users.name,
                    qna.insertDate,
                    qna.updateDate
                ),
                GroupBy.set(
                    new QAnswerQnaResponse(
                        qnaAnswer.id,
                        qnaAnswer.qnaParentId,
                        qnaAnswer.qnaWriterType,
                        qnaAnswer.qnaContents,
                        qnaAnswer.insertDate,
                        qnaAnswer.updateDate
                    )
                )
            )
        )
    );
```

**WHERE 조건**

| 조건명 | 컬럼 | 설명 |
|--------|------|------|
| qnaIdIn | qna.qna_id | Query 1에서 얻은 ID 목록 IN |

**참고**: `GroupBy.groupBy(qna.id).list()` + `GroupBy.set()` 패턴으로 N+1 없이 Q&A와 답변을 한 번에 조회. `QnaType.PRODUCT`이므로 `qnaImageFindService` 호출 없음.

---

### Query 3: fetchQnaCountQuery - 전체 건수 조회 (Lazy)

```java
queryFactory
    .select(qnaProduct.count())
    .from(qnaProduct)
    .where(
        qnaProduct.productGroupId.eq(productGroupId)  // productGroupIdEq()
    )
    .distinct();
```

`PageableExecutionUtils.getPage()`에 의해 마지막 페이지인 경우 실행이 생략됩니다.

---

## 6. 비즈니스 로직 상세

### 비밀글 처리 (QnaMapperImpl.toQnaList)

```
현재 로그인 사용자 ID != Q&A 작성자 ID (SecurityUtils.currentUserId() 비교)
    → userName 마스킹: 뒤 2자리를 ** 로 치환 (예: "홍길동" → "홍길**")
    → privateYn = Y 이면: qnaContents.content를 "비밀글 입니다." 로 덮어쓰기
                          answerQnas를 빈 Set으로 교체 (notPermissionReadQna())
```

비로그인 사용자의 경우 `SecurityUtils.currentUserId()`가 특정 값을 반환하므로 모든 비밀글이 마스킹됩니다.

### 이미지 처리

`QnaType.PRODUCT` Q&A는 이미지 첨부를 지원하지 않습니다. `fetchQnas()` 내에서 `qnaType.isOrderQna()` 분기에 의해 `qnaImageFindService` 호출이 건너뛰어지며, 응답의 `qnaImages`는 항상 빈 목록입니다.

---

## 7. 관련 Entity 및 테이블 매핑

| Entity | 테이블 | PK 컬럼 | 설명 |
|--------|--------|---------|------|
| Qna | qna | qna_id | Q&A 본문 (title, content Embedded) |
| QnaProduct | qna_product | qna_product_id | Q&A-상품그룹 매핑 |
| QnaAnswer | qna_answer | qna_answer_id | Q&A 답변 (대댓글 지원: qna_parent_id) |
| QnaImage | qna_image | - | Q&A 이미지 (ORDER Q&A 전용) |
| Users | users | id | 회원 정보 (userName 조회용) |

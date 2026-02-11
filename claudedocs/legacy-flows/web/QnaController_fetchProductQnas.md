# API Flow Documentation: QnaController.fetchProductQnas

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/qna/product/{productGroupId}` |
| Controller | `QnaController` |
| Service | `QnaFindService` → `QnaFindServiceImpl` |
| Repository | `QnaFindRepository` → `QnaFindRepositoryImpl` |
| 인증 | 없음 (공개 API) |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| productGroupId | long | ✅ | 상품그룹 ID | @PathVariable |
| page | int | ❌ | 페이지 번호 (0부터 시작) | Pageable |
| size | int | ❌ | 페이지 크기 | Pageable |
| sort | String | ❌ | 정렬 조건 | Pageable |

### Request 예시

```
GET /api/v1/qna/product/12345?page=0&size=10
```

---

## 📤 Response

### Response DTO 구조

```java
// 최상위 응답
public class QnaResponse implements LastDomainIdProvider {
    private FetchQnaResponse qna;           // Q&A 본문
    private Set<AnswerQnaResponse> answerQnas;  // 답변 목록
}

// Q&A 본문
public class FetchQnaResponse {
    private long qnaId;
    private QnaContents qnaContents;        // Embedded: title, content
    private Yn privateYn;                   // 비밀글 여부
    private QnaStatus qnaStatus;            // 상태 (PENDING, ANSWERED 등)
    private QnaType qnaType;                // PRODUCT | ORDER
    private QnaDetailType qnaDetailType;    // 상세 유형
    private UserType userType;              // 사용자 유형
    private List<QnaImageDto> qnaImages;    // Q&A 이미지 목록
    private QnaTarget qnaTarget;            // 대상 정보 (상품/주문)
    private long userId;                    // @JsonIgnore
    private String userName;                // 작성자 이름
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;
}

// 답변
public class AnswerQnaResponse {
    private long qnaAnswerId;
    private Long qnaAnswerParentId;         // 부모 답변 ID (대댓글)
    private QnaWriterType qnaWriterType;    // 작성자 유형
    private QnaContents qnaContents;        // Embedded: title, content
    private List<QnaImageDto> qnaImages;    // 답변 이미지 목록
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;
}

// Embedded
@Embeddable
public class QnaContents {
    private String title;    // 최대 100자
    private String content;  // 최대 500자
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "qna": {
          "qnaId": 1001,
          "qnaContents": {
            "title": "사이즈 문의",
            "content": "이 상품 M 사이즈 어깨 실측이 어떻게 되나요?"
          },
          "privateYn": "N",
          "qnaStatus": "ANSWERED",
          "qnaType": "PRODUCT",
          "qnaDetailType": "SIZE",
          "userType": "BUYER",
          "qnaImages": [],
          "userName": "홍길동",
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
              "content": "M 사이즈 어깨 실측 45cm입니다."
            },
            "qnaImages": [],
            "insertDate": "2024-01-15 14:00:00",
            "updateDate": "2024-01-15 14:00:00"
          }
        ]
      }
    ],
    "pageable": { ... },
    "totalElements": 50,
    "totalPages": 5
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────┐
│   Controller                                                  │
│   QnaController.fetchProductQnas(productGroupId, pageable)   │
│   @GetMapping("/qna/product/{productGroupId}")               │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Service                                                     │
│   QnaFindServiceImpl.fetchProductQuestions()                 │
│   @Transactional(readOnly=true)                              │
│                                                              │
│   1. fetchQnaProductIds() → Q&A ID 목록 조회                 │
│   2. fetchQnas() → Q&A 상세 + 답변 조회                      │
│   3. QnaMapper.toQnaList() → 응답 변환                       │
│   4. PageableExecutionUtils.getPage() → 페이징 처리          │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Repository                                                  │
│   QnaFindRepositoryImpl                                      │
│                                                              │
│   Query 1: fetchQnaProductIds()                              │
│   Query 2: fetchQnas()                                       │
│   Query 3: fetchQnaCountQuery() (count)                      │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Database                                                    │
│   Tables: qna_product, qna, users, qna_answer, qna_image     │
└──────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### Query 1: fetchQnaProductIds (ID 목록 조회)

#### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| qna_product | qnaProduct | FROM | - |
| qna | qna | LEFT JOIN | qna.id = qnaProduct.qnaId |
| users | users | LEFT JOIN | users.id = qna.userId |

#### QueryDSL 코드

```java
queryFactory
    .select(qna.id)
    .from(qnaProduct)
    .leftJoin(qna).on(qna.id.eq(qnaProduct.qnaId))
    .leftJoin(users).on(users.id.eq(qna.userId))
    .where(productGroupIdEq(productGroupId))
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .fetch();
```

#### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| productGroupIdEq | qnaProduct.productGroupId | 상품그룹 ID 필터 |

---

### Query 2: fetchQnas (상세 조회)

#### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| qna | qna | FROM | - |
| qna_answer | qnaAnswer | LEFT JOIN | qnaAnswer.qnaId = qna.id |
| qna_image | qnaImage | LEFT JOIN | qnaImage.qnaId = qna.id AND qnaImage.qnaAnswerId = qnaAnswer.id AND deleteYn = 'N' |
| users | users | LEFT JOIN | users.id = qna.userId |

#### QueryDSL 코드

```java
queryFactory.from(qna)
    .leftJoin(qnaAnswer).on(qnaAnswer.qnaId.eq(qna.id))
    .leftJoin(qnaImage)
        .on(qnaImage.qnaId.eq(qna.id))
        .on(qnaImage.qnaAnswerId.eq(qnaAnswer.id))
        .on(qnaImage.deleteYn.eq(Yn.N))
    .leftJoin(users).on(users.id.eq(qna.userId))
    .where(qnaIdIn(qnaIds))
    .transform(
        GroupBy.groupBy(qna.id).list(
            new QQnaResponse(
                new QFetchQnaResponse(...),
                GroupBy.set(new QAnswerQnaResponse(...))
            )
        )
    );
```

#### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| qnaIdIn | qna.id | Q&A ID 목록 필터 |

---

### Query 3: fetchQnaCountQuery (카운트)

```java
queryFactory
    .select(qnaProduct.count())
    .from(qnaProduct)
    .where(productGroupIdEq(productGroupId))
    .distinct();
```

---

## 📋 관련 Entity

| Entity | 테이블 | 설명 |
|--------|--------|------|
| Qna | qna | Q&A 본문 |
| QnaProduct | qna_product | Q&A-상품 매핑 |
| QnaAnswer | qna_answer | Q&A 답변 |
| QnaImage | qna_image | Q&A 이미지 |
| Users | users | 사용자 |

---

## ⚠️ 마이그레이션 시 주의사항

1. **N+1 방지**: `GroupBy.groupBy()` 사용하여 Q&A + 답변 한 번에 조회
2. **페이징 전략**: ID 목록 먼저 조회 → 상세 조회 (2단계)
3. **Soft Delete**: `qnaImage.deleteYn = 'N'` 조건 유지 필요
4. **비밀글 처리**: `notPermissionReadQna()` 메서드로 콘텐츠 마스킹

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert web:QnaController.fetchProductQnas

# Persistence Layer 생성
/legacy-query web:QnaController.fetchProductQnas
```

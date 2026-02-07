# API Flow Documentation: NewsController.fetchBoard

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/board` |
| Controller | `NewsController` |
| Service | `BoardFindService` → `BoardFindServiceImpl` |
| Repository | `BoardFindRepository` → `BoardFindRepositoryImpl` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | 기본값 |
|----------|------|------|------|--------|
| page | int | ❌ | 페이지 번호 (0부터 시작) | 0 |
| size | int | ❌ | 페이지 크기 | 20 |
| sort | String | ❌ | 정렬 기준 | - |

### Request 예시

```
GET /api/v1/board?page=0&size=10
```

---

## 📤 Response

### Response DTO 구조

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {
    private String title;
    private String contents;

    @QueryProjection
    public BoardDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "title": "공지사항 제목 1",
        "contents": "공지사항 내용..."
      },
      {
        "title": "공지사항 제목 2",
        "contents": "공지사항 내용..."
      }
    ],
    "pageable": {
      "sort": { "sorted": false, "unsorted": true, "empty": true },
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 5,
    "totalElements": 50,
    "last": false,
    "first": true,
    "size": 10,
    "number": 0,
    "numberOfElements": 10,
    "empty": false
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────┐
│   Controller                                                  │
│   NewsController.fetchBoard(Pageable pageable)               │
│   @GetMapping("/board")                                       │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Service                                                     │
│   BoardFindServiceImpl.fetchBoards(Pageable)                 │
│   @Cacheable(cacheNames="board", key="#pageable.getPageSize()")│
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ├─────────────────┐
                         ▼                 ▼
┌─────────────────────────────┐  ┌─────────────────────────────┐
│   Repository                 │  │   Repository                 │
│   fetchBoards(pageable)      │  │   fetchBoardCounts()         │
│   (Content Query)            │  │   (Count Query)              │
└─────────────────────────────┘  └─────────────────────────────┘
                         │                 │
                         ▼                 ▼
┌──────────────────────────────────────────────────────────────┐
│   PageableExecutionUtils.getPage()                           │
│   → Page<BoardDto> 생성                                       │
└──────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Database                                                    │
│   Table: board                                                │
└──────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| board | board | FROM | - |

### Entity 구조

```java
@Table(name = "board")
@Entity
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String contents;
}
```

### QueryDSL 코드

#### Content Query
```java
@Override
public List<BoardDto> fetchBoards(Pageable pageable) {
    return queryFactory
            .select(new QBoardDto(board.title, board.contents))
            .from(board)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
}
```

#### Count Query
```java
@Override
public JPAQuery<Long> fetchBoardCounts() {
    return queryFactory.select(board.count()).from(board);
}
```

### 페이징 처리

```java
// Service Layer
List<BoardDto> boards = boardFindRepository.fetchBoards(pageable);
JPAQuery<Long> longJPAQuery = boardFindRepository.fetchBoardCounts();
return PageableExecutionUtils.getPage(boards, pageable, longJPAQuery::fetchCount);
```

- `PageableExecutionUtils.getPage()` 사용
- Count 쿼리는 지연 실행 (필요시에만 실행)

---

## 🔧 특이사항

### 캐싱
- **Cache Name**: `board`
- **Cache Key**: `pageSize` 값
- ⚠️ 주의: pageSize만으로 캐시 키를 만들어 page 번호가 무시될 수 있음

### 정렬
- 현재 기본 정렬 없음 (DB 기본 순서)
- Pageable의 sort 파라미터가 QueryDSL에 적용되지 않음

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert web:NewsController.fetchBoard

# Persistence Layer 생성
/legacy-query web:NewsController.fetchBoard
```

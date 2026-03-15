# API Flow: ContentController.updateContentDisplayYn

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | PATCH |
| Path | `/api/v1/content/{contentId}/display-status` |
| Controller | `ContentController` |
| Service | `ContentQueryService` → `ContentQueryServiceImpl` |
| Sub-Service | `ContentFetchService` → `ContentFetchServiceImpl` (내부 의존) |
| Repository | `ContentFetchRepository` (조회), JPA Dirty Checking (수정) |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | 설명 |
|------|------|------|------|------|
| contentId | long | PathVariable | 필수 | 수정할 컨텐츠 ID |

### Request Body (UpdateContentDisplayYn)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| displayYn | Yn | 필수 | - | 변경할 전시 여부 (Y/N) |

### JSON Example

```
PATCH /api/v1/content/1/display-status
```

```json
{
  "displayYn": "N"
}
```

## 3. Response

### DTO Structure (ContentResponse)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| contentId | long | 컨텐츠 ID |
| displayYn | Yn | 변경된 전시 여부 (Y/N) |
| title | String | 타이틀 |
| displayPeriod | DisplayPeriod | 전시 기간 |
| insertOperator | String | 등록자 |
| updateOperator | String | 수정자 |
| insertDate | LocalDateTime | 등록일시 (yyyy-MM-dd HH:mm:ss) |
| updateDate | LocalDateTime | 수정일시 (yyyy-MM-dd HH:mm:ss) |

### JSON Example

```json
{
  "success": true,
  "data": {
    "contentId": 1,
    "displayYn": "N",
    "title": "메인 컨텐츠",
    "displayPeriod": {
      "displayStartDate": "2024-01-01 00:00:00",
      "displayEndDate": "2024-12-31 23:59:59"
    },
    "insertOperator": "admin",
    "updateOperator": "admin",
    "insertDate": "2024-01-01 10:00:00",
    "updateDate": "2024-06-01 09:00:00"
  }
}
```

## 4. 호출 흐름

```
ContentController.updateContentDisplayYn(@PathVariable long contentId, @RequestBody UpdateContentDisplayYn)
    └── ContentQueryService.updateDisplayYn(contentId, updateContentDisplayYn)
            └── ContentQueryServiceImpl.updateDisplayYn(contentId, updateContentDisplayYn)
                    ├── ContentFetchService.fetchContentEntity(contentId)
                    │       └── ContentFetchServiceImpl.fetchContentEntity(contentId)
                    │               └── ContentFetchRepositoryImpl.fetchContentEntity(contentId)
                    │                   SELECT * FROM content WHERE id = :contentId AND deleteYn = N
                    ├── content.updateDisplayYn(updateContentDisplayYn.getDisplayYn())  [JPA Dirty Checking]
                    └── ContentMapper.toContentResponse(content)
```

## 5. Database Query

### Tables

| 테이블 | 조작 | 설명 |
|--------|------|------|
| content | SELECT | 컨텐츠 조회 |
| content | UPDATE | displayYn 변경 (JPA Dirty Checking) |

### QueryDSL (ContentFetchRepositoryImpl.fetchContentEntity)

```java
getQueryFactory().selectFrom(content)
    .where(
        content.id.eq(contentId),
        content.deleteYn.eq(Yn.N)
    )
    .fetchOne()
```

### 비고

- `content.updateDisplayYn(Yn)` 호출 → JPA Dirty Checking으로 UPDATE 자동 실행
- 배너와 달리 Redis 캐시 삭제 없음 (Content는 Redis 캐시 미관여)
- displayYn 단건 변경 전용 경량 PATCH API
- 삭제된 컨텐츠(deleteYn=Y)는 조회 불가 → ContentNotFoundException 발생

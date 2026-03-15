# API Flow: ContentController.enrollContent

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | POST |
| Path | `/api/v1/content` |
| Controller | `ContentController` |
| Service | `ContentQueryService` → `ContentQueryServiceImpl` |
| Repository | `ContentRepository` (JPA), `ContentJdbcRepository`, `ComponentQueryStrategy` |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

## 2. Request

### Request Body (CreateContent)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| contentId | Long | 선택 | @JsonInclude(NON_NULL) | null이면 신규 등록, 값 있으면 수정으로 분기 |
| displayPeriod | DisplayPeriod | 선택 | @Valid | 전시 기간 |
| displayPeriod.displayStartDate | LocalDateTime | 필수 | @NotNull | 전시 시작일시 |
| displayPeriod.displayEndDate | LocalDateTime | 필수 | @NotNull | 전시 종료일시 |
| title | String | 선택 | @Length(max=50) | 컨텐트 타이틀 (최대 50자) |
| memo | String | 선택 | @Length(max=50) | 컨텐트 메모 (최대 200자, 메시지는 200이나 max=50) |
| imageUrl | String | 선택 | - | 이미지 URL |
| displayYn | Yn | 필수 | @NotNull | 전시 여부 (Y/N) |
| components | List\<SubComponent\> | 필수 | @Size(min=1) | 하위 컴포넌트 목록 (최소 1개) |

### JSON Example

```json
{
  "displayPeriod": {
    "displayStartDate": "2024-01-01 00:00:00",
    "displayEndDate": "2024-12-31 23:59:59"
  },
  "title": "메인 컨텐츠",
  "memo": "운영 메모",
  "imageUrl": "https://cdn.example.com/image.jpg",
  "displayYn": "Y",
  "components": [
    {
      "componentType": "PRODUCT",
      "displayOrder": 1
    }
  ]
}
```

## 3. Response

### DTO Structure (ContentResponse)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| contentId | long | 컨텐츠 ID |
| displayYn | Yn | 전시 여부 (Y/N) |
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
    "contentId": 10,
    "displayYn": "Y",
    "title": "메인 컨텐츠",
    "displayPeriod": {
      "displayStartDate": "2024-01-01 00:00:00",
      "displayEndDate": "2024-12-31 23:59:59"
    },
    "insertOperator": "admin",
    "updateOperator": null,
    "insertDate": "2024-01-01 10:00:00",
    "updateDate": null
  }
}
```

## 4. 호출 흐름

```
ContentController.enrollContent(@RequestBody @Validated CreateContent)
    └── ContentQueryService.enrollContent(createContent)
            └── ContentQueryServiceImpl.enrollContent(createContent)
                    [신규 등록: contentId == null]
                    ├── ContentQueryMapper.toEntity(createContent)
                    ├── ContentRepository.save(content)  [JPA]
                    ├── createAllSubComponents(contentId, componentsByType)
                    │       └── ComponentQueryStrategy.get(componentType) → SubComponentQueryService.createComponents(...)
                    └── ContentMapper.toContentResponse(content)

                    [수정: contentId > 0]
                    ├── ContentFetchService.fetchContent(contentId)
                    ├── ContentQueryMapper.toEntity(createContent)
                    ├── ContentJdbcRepository.update(content)  [JDBC]
                    ├── classifyComponents() → 추가/수정/삭제 분류
                    ├── addNewComponents() → SubComponentQueryService.createComponents(...)
                    ├── removeComponents() → ComponentDeleteService.deleteAll(...)
                    ├── updateComponents() → SubComponentQueryService.updateComponents(...)
                    └── ContentMapper.toContentResponse(content)
```

## 5. Database Query

### Tables

| 테이블 | 조작 | 설명 |
|--------|------|------|
| content | INSERT / UPDATE | 컨텐츠 등록/수정 |
| component_* | INSERT / UPDATE / DELETE | ComponentType별 하위 컴포넌트 (전략 패턴) |

### Repository 패턴

```java
// 신규 등록 - JPA
ContentRepository.save(content)

// 수정 - JDBC (직접 UPDATE)
ContentJdbcRepository.update(content)

// 컴포넌트 생성 - 전략 패턴
SubComponentQueryService<T> service = componentQueryStrategy.get(componentType);
service.createComponents(contentId, components);

// 컴포넌트 삭제 - 전략 패턴
ComponentDeleteService.deleteAll(componentsToBeRemoved);

// 컴포넌트 수정 - 전략 패턴
SubComponentQueryService<T> service = componentQueryStrategy.get(componentType);
service.updateComponents(pairs);
```

### 신규/수정 분기 로직

```java
// contentId가 null이거나 0이면 신규, 0 초과면 수정
if (createContent.getContentId() != null && createContent.getContentId() > 0) {
    return updateContent(createContent);
}
// 신규 등록 흐름
Content content = contentQueryMapper.toEntity(createContent);
Content savedContent = contentRepository.save(content);
createAllSubComponents(savedContent.getId(), createContent.getComponentsByType());
return contentFetchMapper.toContentResponse(savedContent);
```

### 컴포넌트 분류 로직 (수정 시)

```
기존 컴포넌트 맵 생성 (key: [componentId, componentType])
    요청에 없는 기존 컴포넌트 → 삭제 목록
    요청에 있으나 기존에 없는 컴포넌트 → 추가 목록
    요청에 있고 기존에도 있는 컴포넌트 → 수정 목록
```

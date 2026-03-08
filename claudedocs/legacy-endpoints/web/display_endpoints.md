# 엔드포인트 목록: display 모듈

## 요약

| 분류 | 개수 |
|------|------|
| Query | 6개 |
| Command | 0개 |
| **총계** | **6개** |

## 분석 대상

- **소스**: `bootstrap-legacy-web-api`
- **Controller**: `ContentController`
- **Base Path**: `/api/v1/content`

---

## Query (조회성) - 6개

| # | 메서드명 | HTTP Method | Full Path | Request Type | Response Type |
|---|----------|-------------|-----------|--------------|---------------|
| 1 | fetchOnDisplayContents | GET | `/api/v1/content/on-display` | 없음 | `ApiResponse<OnDisplayContent>` |
| 2 | fetchContentMetaData | GET | `/api/v1/content/meta/{contentId}` | `@PathVariable` | `ApiResponse<ContentGroupResponse>` |
| 3 | fetchContent | GET | `/api/v1/content/{contentId}` | `@PathVariable`, `@RequestParam` | `ApiResponse<ContentGroupResponse>` |
| 4 | fetchContentProducts | GET | `/api/v1/content/component/{componentId}/products` | `@PathVariable`, `@ModelAttribute` | `ApiResponse<CustomSlice<ProductGroupThumbnail>>` |
| 5 | getBannerGroups | GET | `/api/v1/content/banner` | `@ModelAttribute` | `ApiResponse<List<BannerItemDto>>` |
| 6 | getGnbs | GET | `/api/v1/content/gnbs` | 없음 | `ApiResponse<List<GnbResponse>>` |

### 상세

#### Q1. fetchOnDisplayContents

```yaml
controller: ContentController
method: fetchOnDisplayContents
http_method: GET
path: /on-display
full_path: /api/v1/content/on-display
request_type: 없음
request_class: 없음
response_class: ApiResponse<OnDisplayContent>
classification: Query
```

#### Q2. fetchContentMetaData

```yaml
controller: ContentController
method: fetchContentMetaData
http_method: GET
path: /meta/{contentId}
full_path: /api/v1/content/meta/{contentId}
request_type: "@PathVariable"
request_class: contentId (long)
response_class: ApiResponse<ContentGroupResponse>
classification: Query
```

#### Q3. fetchContent

```yaml
controller: ContentController
method: fetchContent
http_method: GET
path: /{contentId}
full_path: /api/v1/content/{contentId}
request_type: "@PathVariable, @RequestParam"
request_class: contentId (long), bypass (Yn)
response_class: ApiResponse<ContentGroupResponse>
classification: Query
```

#### Q4. fetchContentProducts

```yaml
controller: ContentController
method: fetchContentProducts
http_method: GET
path: /component/{componentId}/products
full_path: /api/v1/content/component/{componentId}/products
request_type: "@PathVariable, @ModelAttribute"
request_class: componentId (long), ComponentFilter, Pageable
response_class: ApiResponse<CustomSlice<ProductGroupThumbnail>>
classification: Query
```

#### Q5. getBannerGroups

```yaml
controller: ContentController
method: getBannerGroups
http_method: GET
path: /banner
full_path: /api/v1/content/banner
request_type: "@ModelAttribute"
request_class: BannerFilter
response_class: ApiResponse<List<BannerItemDto>>
classification: Query
```

#### Q6. getGnbs

```yaml
controller: ContentController
method: getGnbs
http_method: GET
path: /gnbs
full_path: /api/v1/content/gnbs
request_type: 없음
request_class: 없음
response_class: ApiResponse<List<GnbResponse>>
classification: Query
```

---

## Command (커맨드성) - 0개

없음

---

## 다음 단계

개별 엔드포인트 플로우 분석:

```bash
/legacy-flow web:ContentController.fetchOnDisplayContents
/legacy-flow web:ContentController.fetchContentMetaData
/legacy-flow web:ContentController.fetchContent
/legacy-flow web:ContentController.fetchContentProducts
/legacy-flow web:ContentController.getBannerGroups
/legacy-flow web:ContentController.getGnbs
```

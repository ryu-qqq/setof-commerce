# 엔드포인트 목록: display 모듈

- 대상: bootstrap-legacy-web-api-admin
- Controller: ContentController
- Base Path: /api/v1

## 요약

| 분류 | 개수 |
|------|------|
| Query | 5개 |
| Command | 6개 |
| **총계** | **11개** |

---

## Query (조회성) - 5개

| # | 메서드명 | HTTP | Full Path | Request Type | Request Class | Response Class |
|---|----------|------|-----------|--------------|---------------|----------------|
| 1 | getContent | GET | /api/v1/content/{contentId} | @PathVariable | long contentId | ApiResponse\<ContentGroupResponse\> |
| 2 | getContents | GET | /api/v1/contents | @ModelAttribute | ContentFilter, Pageable | ApiResponse\<CustomPageable\<ContentResponse\>\> |
| 3 | getBannerGroups (single) | GET | /api/v1/content/banner/{bannerId} | @PathVariable + @ModelAttribute | long bannerId, BannerItemFilter | ApiResponse\<List\<BannerItemDto\>\> |
| 4 | getBannerGroups (list) | GET | /api/v1/content/banners | @ModelAttribute | BannerFilter, Pageable | ApiResponse\<CustomPageable\<BannerGroupDto\>\> |
| 5 | getGnbs | GET | /api/v1/content/gnbs | @ModelAttribute | GnbFilter | ApiResponse\<List\<GnbResponse\>\> |

### 상세

**1. getContent**
```
GET /api/v1/content/{contentId}
@PathVariable long contentId
→ ApiResponse<ContentGroupResponse>
```

**2. getContents**
```
GET /api/v1/contents
@ModelAttribute ContentFilter, Pageable
→ ApiResponse<CustomPageable<ContentResponse>>
```

**3. getBannerGroups (단건)**
```
GET /api/v1/content/banner/{bannerId}
@PathVariable long bannerId, @ModelAttribute BannerItemFilter
→ ApiResponse<List<BannerItemDto>>
```

**4. getBannerGroups (목록)**
```
GET /api/v1/content/banners
@ModelAttribute BannerFilter, Pageable
→ ApiResponse<CustomPageable<BannerGroupDto>>
```

**5. getGnbs**
```
GET /api/v1/content/gnbs
@ModelAttribute GnbFilter
→ ApiResponse<List<GnbResponse>>
```

---

## Command (커맨드성) - 6개

| # | 메서드명 | HTTP | Full Path | Request Type | Request Class | Response Class |
|---|----------|------|-----------|--------------|---------------|----------------|
| 1 | enrollContent | POST | /api/v1/content | @RequestBody | CreateContent | ApiResponse\<ContentResponse\> |
| 2 | enrollBanner | POST | /api/v1/content/banner | @RequestBody | CreateBanner | ApiResponse\<BannerResponse\> |
| 3 | updateBanner | PUT | /api/v1/content/banner/{bannerId} | @PathVariable + @RequestBody | long bannerId, CreateBanner | ApiResponse\<BannerResponse\> |
| 4 | enrollBannerItems | POST | /api/v1/content/banner/items | @RequestBody | List\<CreateBannerItem\> | ApiResponse\<List\<CreateBannerItem\>\> |
| 5 | updateBannerDisplayYn | PATCH | /api/v1/content/banner/{bannerId}/display-status | @PathVariable + @RequestBody | long bannerId, UpdateBannerDisplayYn | ApiResponse\<BannerResponse\> |
| 6 | updateContentDisplayYn | PATCH | /api/v1/content/{contentId}/display-status | @PathVariable + @RequestBody | long contentId, UpdateContentDisplayYn | ApiResponse\<ContentResponse\> |

### 상세

**1. enrollContent**
```
POST /api/v1/content
@RequestBody @Validated CreateContent
→ ApiResponse<ContentResponse>
```

**2. enrollBanner**
```
POST /api/v1/content/banner
@RequestBody @Validated CreateBanner
→ ApiResponse<BannerResponse>
```

**3. updateBanner**
```
PUT /api/v1/content/banner/{bannerId}
@PathVariable long bannerId, @RequestBody @Validated CreateBanner
→ ApiResponse<BannerResponse>
```

**4. enrollBannerItems**
```
POST /api/v1/content/banner/items
@RequestBody @Validated List<CreateBannerItem>
→ ApiResponse<List<CreateBannerItem>>
```

**5. updateBannerDisplayYn**
```
PATCH /api/v1/content/banner/{bannerId}/display-status
@PathVariable long bannerId, @RequestBody UpdateBannerDisplayYn
→ ApiResponse<BannerResponse>
```

**6. updateContentDisplayYn**
```
PATCH /api/v1/content/{contentId}/display-status
@PathVariable long contentId, @RequestBody UpdateContentDisplayYn
→ ApiResponse<ContentResponse>
```

---

## 다음 단계

개별 엔드포인트 플로우 분석:
```bash
/legacy-flow admin:ContentController.getContent
/legacy-flow admin:ContentController.getContents
/legacy-flow admin:ContentController.getBannerGroups
/legacy-flow admin:ContentController.getGnbs
/legacy-flow admin:ContentController.enrollContent
/legacy-flow admin:ContentController.enrollBanner
/legacy-flow admin:ContentController.updateBanner
/legacy-flow admin:ContentController.enrollBannerItems
/legacy-flow admin:ContentController.updateBannerDisplayYn
/legacy-flow admin:ContentController.updateContentDisplayYn
```

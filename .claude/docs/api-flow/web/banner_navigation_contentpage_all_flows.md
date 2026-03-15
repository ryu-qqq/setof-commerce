# Banner / Navigation / ContentPage API Flow Analysis

## 분석 개요

| 항목 | 값 |
|------|-----|
| 분석 일시 | 2026-03-14 |
| 대상 모듈 | `adapter-in/rest-api` (v1 Public API) |
| 엔드포인트 수 | 5개 (Query Only) |
| Adapter-Out | `persistence-mysql-legacy` (레거시 DB) |
| 활성화 조건 | `@ConditionalOnProperty` — legacy 프로퍼티별 활성화 |

---

## 엔드포인트 목록

| # | Domain | Method | Path | Controller | UseCase |
|---|--------|--------|------|------------|---------|
| 1 | Banner | GET | /api/v1/content/banner | BannerQueryV1Controller | BannerSlideQueryUseCase |
| 2 | Navigation | GET | /api/v1/content/gnbs | NavigationQueryV1Controller | NavigationMenuQueryUseCase |
| 3 | ContentPage | GET | /api/v1/content/on-display | ContentQueryV1Controller | GetOnDisplayContentPageIdsUseCase |
| 4 | ContentPage | GET | /api/v1/content/meta/{contentId} | ContentQueryV1Controller | GetContentPageMetaUseCase |
| 5 | ContentPage | GET | /api/v1/content/{contentId} | ContentQueryV1Controller | GetContentPageDetailUseCase |

---

---

# 1. Banner — getBanners

## 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | /api/v1/content/banner |
| Request | `bannerType` (@RequestParam, @NotBlank) |
| Response | `V1ApiResponse<List<BannerSlideV1ApiResponse>>` |
| Controller | BannerQueryV1Controller |
| UseCase (Port-In) | BannerSlideQueryUseCase |
| Service | BannerSlideQueryService |
| Manager | BannerSlideQueryManager |
| Port-Out | BannerSlideQueryPort |
| Adapter-Out | LegacyBannerQueryAdapter |
| Repository | LegacyBannerQueryDslRepository |
| 활성화 조건 | persistence.legacy.banner.enabled=true |

---

## 호출 흐름 다이어그램

```
BannerQueryV1Controller.getBanners(String bannerType)
  |- BannerType.valueOf(bannerType)                         [컨트롤러 내 Domain enum 변환]
  |- BannerSlideQueryUseCase.fetchDisplayBannerSlides(BannerType)  [Port-In]
  |   +-- BannerSlideQueryService.fetchDisplayBannerSlides()        [Impl]
  |       +-- BannerSlideQueryManager.fetchDisplayBannerSlides()    [@Transactional readOnly]
  |           +-- BannerSlideQueryPort.fetchDisplayBannerSlides()   [Port-Out]
  |               +-- LegacyBannerQueryAdapter.fetchDisplayBannerSlides()  [Impl]
  |                   |- LegacyBannerQueryDslRepository.fetchBannerItems(bannerType.name())
  |                   +-- LegacyBannerMapper.toDomains(entities)
  |                       +-- -> List<BannerSlide>
  |- isEmpty() → BannerGroupNotFoundException (404)
  |- BannerV1ApiMapper.toListResponse(slides)
  |   +-- slide.idValue(), title(), imageUrl(), linkUrl()
  +-- -> ResponseEntity<V1ApiResponse<List<BannerSlideV1ApiResponse>>>
```

---

## Layer별 상세

### Adapter-In Layer

| 항목 | 상세 |
|------|------|
| Controller | `BannerQueryV1Controller` |
| Mapping | `@GetMapping` @ `BannerV1Endpoints.BANNERS` (`/api/v1/content/banner`) |
| Request | `@RequestParam @NotBlank String bannerType` |
| Domain enum 변환 | `BannerType.valueOf(bannerType)` — 컨트롤러 직접 변환 |
| Response DTO | `BannerSlideV1ApiResponse(long bannerItemId, String title, String imageUrl, String linkUrl)` |
| Mapper | `BannerV1ApiMapper.toListResponse(List<BannerSlide>)` |
| 예외 처리 | 결과 빈 리스트 → `BannerGroupNotFoundException(bannerType)` |
| Wrapper | `V1ApiResponse.success(response)` |

**BannerSlideV1ApiResponse 필드**

| 필드 | 타입 | 매핑 소스 |
|------|------|----------|
| bannerItemId | long | BannerSlide.idValue() |
| title | String | BannerSlide.title() |
| imageUrl | String | BannerSlide.imageUrl() |
| linkUrl | String | BannerSlide.linkUrl() |

### Application Layer

| 항목 | 상세 |
|------|------|
| UseCase (Port-In) | `BannerSlideQueryUseCase.fetchDisplayBannerSlides(BannerType)` |
| Service | `BannerSlideQueryService implements BannerSlideQueryUseCase` |
| Manager | `BannerSlideQueryManager` — Port-Out 래핑, `@Transactional(readOnly = true)` |
| 트랜잭션 경계 | `BannerSlideQueryManager.fetchDisplayBannerSlides()` |
| 입력 타입 | `BannerType` (Domain enum) |
| 출력 타입 | `List<BannerSlide>` (Domain 객체) |

### Domain Layer

| 항목 | 상세 |
|------|------|
| Port-Out | `BannerSlideQueryPort.fetchDisplayBannerSlides(BannerType)` |
| Domain 객체 | `BannerSlide` — id, title, imageUrl, linkUrl, displayOrder, displayPeriod |
| Domain VO | `BannerType` enum (CATEGORY, MY_PAGE, CART, PRODUCT_DETAIL_DESCRIPTION, RECOMMEND, LOGIN) |

### Adapter-Out Layer

| 항목 | 상세 |
|------|------|
| Adapter | `LegacyBannerQueryAdapter implements BannerSlideQueryPort` |
| 활성화 | `@ConditionalOnProperty(persistence.legacy.banner.enabled=true)` |
| Repository | `LegacyBannerQueryDslRepository.fetchBannerItems(String bannerType)` |
| Entity Mapper | `LegacyBannerMapper.toDomains(List<LegacyBannerItemEntity>)` |
| 변환 | `LegacyBannerItemEntity` → `BannerSlide.reconstitute(...)` |
| LocalDateTime 변환 | `Asia/Seoul` ZoneId → `Instant` |

---

## Database Query 분석 — Banner

| 항목 | 상세 |
|------|------|
| 대상 테이블 | `banner`, `banner_item` |
| JOIN | `banner_item INNER JOIN banner ON banner_item.banner_id = banner.banner_id` |
| WHERE | `banner.banner_type = ?` (bannerType) + onDisplayBanner + onDisplayBannerItem |
| ORDER BY | `banner_item.display_order ASC` |
| 전시 조건 | banner.display_yn, banner_item.display_yn + 전시 기간 |

**관련 JPA 엔티티**

| 엔티티 | 테이블 | 주요 컬럼 |
|--------|--------|-----------|
| LegacyBannerEntity | banner | banner_id, title, banner_type, display_yn, delete_yn, display_start_date, display_end_date |
| LegacyBannerItemEntity | banner_item | banner_item_id, banner_id, title, image_url, link_url, display_order, display_yn, delete_yn, display_start_date, display_end_date |

---

---

# 2. Navigation — getGnbs

## 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | /api/v1/content/gnbs |
| Request | 없음 |
| Response | `V1ApiResponse<List<NavigationMenuV1ApiResponse>>` |
| Controller | NavigationQueryV1Controller |
| UseCase (Port-In) | NavigationMenuQueryUseCase |
| Service | NavigationMenuQueryService |
| Manager | NavigationMenuQueryManager |
| Port-Out | NavigationMenuQueryPort |
| Adapter-Out | LegacyGnbQueryAdapter |
| Repository | LegacyGnbQueryDslRepository |
| 활성화 조건 | persistence.legacy.navigation.enabled=true |

---

## 호출 흐름 다이어그램

```
NavigationQueryV1Controller.getGnbs()
  |- NavigationMenuQueryUseCase.fetchNavigationMenus()              [Port-In]
  |   +-- NavigationMenuQueryService.fetchNavigationMenus()         [Impl]
  |       +-- NavigationMenuQueryManager.fetchNavigationMenus()     [@Transactional readOnly]
  |           +-- NavigationMenuQueryPort.fetchNavigationMenus()    [Port-Out]
  |               +-- LegacyGnbQueryAdapter.fetchNavigationMenus()  [Impl]
  |                   |- LegacyGnbQueryDslRepository.fetchGnbs()
  |                   +-- LegacyGnbMapper.toDomains(entities)
  |                       +-- -> List<NavigationMenu>
  |- NavigationV1ApiMapper.toListResponse(menus)
  |   +-- menu.idValue(), title(), linkUrl()
  |   +-- displayOrder, displayPeriod 제외 (내부 처리용)
  +-- -> ResponseEntity<V1ApiResponse<List<NavigationMenuV1ApiResponse>>>
```

---

## Layer별 상세

### Adapter-In Layer

| 항목 | 상세 |
|------|------|
| Controller | `NavigationQueryV1Controller` |
| Mapping | `@GetMapping` @ `NavigationV1Endpoints.GNBS` (`/api/v1/content/gnbs`) |
| Request | 없음 (파라미터 없음) |
| Response DTO | `NavigationMenuV1ApiResponse(long gnbId, String title, String linkUrl)` |
| Mapper | `NavigationV1ApiMapper.toListResponse(List<NavigationMenu>)` |
| 필드 제외 | `displayOrder`, `displayPeriod` — 응답에서 의도적 제외 |
| Wrapper | `V1ApiResponse.success(response)` |

**NavigationMenuV1ApiResponse 필드**

| 필드 | 타입 | 매핑 소스 |
|------|------|----------|
| gnbId | long | NavigationMenu.idValue() |
| title | String | NavigationMenu.title() |
| linkUrl | String | NavigationMenu.linkUrl() |

### Application Layer

| 항목 | 상세 |
|------|------|
| UseCase (Port-In) | `NavigationMenuQueryUseCase.fetchNavigationMenus()` |
| Service | `NavigationMenuQueryService implements NavigationMenuQueryUseCase` |
| Manager | `NavigationMenuQueryManager` — Port-Out 래핑, `@Transactional(readOnly = true)` |
| 트랜잭션 경계 | `NavigationMenuQueryManager.fetchNavigationMenus()` |
| 입력 타입 | 없음 |
| 출력 타입 | `List<NavigationMenu>` (Domain Aggregate) |

### Domain Layer

| 항목 | 상세 |
|------|------|
| Port-Out | `NavigationMenuQueryPort.fetchNavigationMenus()` |
| Domain 객체 | `NavigationMenu` (Aggregate) — id, title, linkUrl, displayOrder, displayPeriod |
| ID VO | `NavigationMenuId` |

### Adapter-Out Layer

| 항목 | 상세 |
|------|------|
| Adapter | `LegacyGnbQueryAdapter implements NavigationMenuQueryPort` |
| 활성화 | `@ConditionalOnProperty(persistence.legacy.navigation.enabled=true)` |
| Repository | `LegacyGnbQueryDslRepository.fetchGnbs()` |
| Entity Mapper | `LegacyGnbMapper.toDomains(List<LegacyGnbEntity>)` |
| 변환 | `LegacyGnbEntity` → `NavigationMenu.reconstitute(...)` |
| LocalDateTime 변환 | `Asia/Seoul` ZoneId → `Instant` |

---

## Database Query 분석 — Navigation

| 항목 | 상세 |
|------|------|
| 대상 테이블 | `gnb` |
| WHERE | `onDisplayGnb()` — display_yn=Y + 전시 기간 조건 |
| ORDER BY | `gnb.display_order ASC` |
| DISTINCT | 적용 |

**관련 JPA 엔티티**

| 엔티티 | 테이블 | 주요 컬럼 |
|--------|--------|-----------|
| LegacyGnbEntity | gnb | gnb_id, title, link_url, display_order, display_yn, delete_yn, display_start_date, display_end_date |

---

---

# 3. ContentPage — fetchOnDisplayContents (Q1)

## 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | /api/v1/content/on-display |
| Request | 없음 |
| Response | `V1ApiResponse<OnDisplayContentV1ApiResponse>` |
| Controller | ContentQueryV1Controller |
| UseCase (Port-In) | GetOnDisplayContentPageIdsUseCase |
| Service | GetOnDisplayContentPageIdsService |
| Manager | ContentPageQueryManager |
| Port-Out | ContentPageQueryPort |
| Adapter-Out | LegacyWebContentCompositeQueryAdapter |
| Repository | LegacyWebContentCompositeQueryDslRepository |
| 활성화 조건 | persistence.legacy.content.enabled=true |

---

## 호출 흐름 다이어그램

```
ContentQueryV1Controller.fetchOnDisplayContents()
  |- GetOnDisplayContentPageIdsUseCase.execute()                           [Port-In]
  |   +-- GetOnDisplayContentPageIdsService.execute()                       [Impl]
  |       +-- ContentPageQueryManager.fetchOnDisplayContentPageIds()         [@Transactional readOnly]
  |           +-- ContentPageQueryPort.fetchOnDisplayContentPageIds()        [Port-Out]
  |               +-- LegacyWebContentCompositeQueryAdapter.fetchOnDisplayContentPageIds()  [Impl]
  |                   +-- LegacyWebContentCompositeQueryDslRepository.fetchOnDisplayContentIds()
  |                       +-- -> List<Long> → new HashSet<>(contentIds)
  |                       +-- -> Set<Long>
  |- ContentV1ApiMapper.toOnDisplayResponse(contentIds)
  |   +-- new OnDisplayContentV1ApiResponse(contentIds)
  +-- -> ResponseEntity<V1ApiResponse<OnDisplayContentV1ApiResponse>>
```

---

## Layer별 상세

### Adapter-In Layer

| 항목 | 상세 |
|------|------|
| Controller | `ContentQueryV1Controller` |
| Mapping | `@GetMapping("/on-display")` @ `/api/v1/content` |
| Request | 없음 |
| Response DTO | `OnDisplayContentV1ApiResponse(Set<Long> contentIds)` |
| Mapper | `ContentV1ApiMapper.toOnDisplayResponse(Set<Long>)` |
| Wrapper | `V1ApiResponse.success(response)` |

### Application Layer

| 항목 | 상세 |
|------|------|
| UseCase (Port-In) | `GetOnDisplayContentPageIdsUseCase.execute()` |
| Service | `GetOnDisplayContentPageIdsService implements GetOnDisplayContentPageIdsUseCase` |
| Manager | `ContentPageQueryManager.fetchOnDisplayContentPageIds()` — `@Transactional(readOnly = true)` |
| 출력 타입 | `Set<Long>` |

### Domain Layer

| 항목 | 상세 |
|------|------|
| Port-Out | `ContentPageQueryPort.fetchOnDisplayContentPageIds()` |

### Adapter-Out Layer

| 항목 | 상세 |
|------|------|
| Adapter | `LegacyWebContentCompositeQueryAdapter implements ContentPageQueryPort` |
| 활성화 | `@ConditionalOnProperty(persistence.legacy.content.enabled=true)` |
| Repository 호출 | `fetchOnDisplayContentIds()` → `List<Long>` → `new HashSet<>()` |

---

## Database Query 분석 — on-display

| 항목 | 상세 |
|------|------|
| 대상 테이블 | `content`, `component` |
| JOIN | `content INNER JOIN component ON component.content_id = content.content_id` |
| WHERE | `onDisplayContent(bypass=false)` + `onDisplayComponent(bypass=false)` |
| SELECT | `content.content_id` |
| DISTINCT | 적용 |

---

---

# 4. ContentPage — fetchContentMetaData (Q2)

## 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | /api/v1/content/meta/{contentId} |
| Request | `contentId` (@PathVariable, long) |
| Response | `V1ApiResponse<ContentMetaV1ApiResponse>` |
| Controller | ContentQueryV1Controller |
| UseCase (Port-In) | GetContentPageMetaUseCase |
| Service | GetContentPageMetaService |
| Manager | ContentPageQueryManager |
| Port-Out | ContentPageQueryPort |
| Adapter-Out | LegacyWebContentCompositeQueryAdapter |
| Repository | LegacyWebContentCompositeQueryDslRepository |
| 활성화 조건 | persistence.legacy.content.enabled=true |

---

## 호출 흐름 다이어그램

```
ContentQueryV1Controller.fetchContentMetaData(long contentId)
  |- GetContentPageMetaUseCase.execute(contentId)                               [Port-In]
  |   +-- GetContentPageMetaService.execute(contentId)                           [Impl]
  |       +-- ContentPageQueryManager.fetchContentPageMeta(contentId)            [@Transactional readOnly]
  |           +-- ContentPageQueryPort.fetchContentPageMeta(contentId)           [Port-Out]
  |           |   +-- LegacyWebContentCompositeQueryAdapter.fetchContentPageMeta()  [Impl]
  |           |       +-- LegacyWebContentCompositeQueryDslRepository.fetchContentById(contentId)
  |           |           +-- -> Optional<LegacyWebContentQueryDto>
  |           |           +-- LegacyWebContentMapper.toContentPage(dto)
  |           |           +-- -> Optional<ContentPage>
  |           +-- .orElseThrow(ContentPageNotFoundException)
  |           +-- -> ContentPage
  |- ContentV1ApiMapper.toMetaResponse(page)
  |   +-- page.idValue(), displayPeriod, title, memo, imageUrl
  |   +-- componentDetails = List.of() (고정 빈 리스트)
  |   +-- DateTimeFormatUtils.format(Instant) → "yyyy-MM-dd HH:mm:ss"
  +-- -> ResponseEntity<V1ApiResponse<ContentMetaV1ApiResponse>>
```

---

## Layer별 상세

### Adapter-In Layer

| 항목 | 상세 |
|------|------|
| Controller | `ContentQueryV1Controller` |
| Mapping | `@GetMapping("/meta/{contentId}")` |
| Request | `@PathVariable long contentId` |
| Response DTO | `ContentMetaV1ApiResponse` |
| Mapper | `ContentV1ApiMapper.toMetaResponse(ContentPage)` |
| 특이사항 | `componentDetails` 항상 빈 리스트 (레거시 응답 포맷 호환) |

**ContentMetaV1ApiResponse 필드**

| 필드 | 타입 | 매핑 소스 |
|------|------|----------|
| contentId | long | ContentPage.idValue() |
| displayPeriod.displayStartDate | String | ContentPage.displayPeriod().startDate() → format |
| displayPeriod.displayEndDate | String | ContentPage.displayPeriod().endDate() → format |
| title | String | ContentPage.title() |
| memo | String | ContentPage.memo() |
| imageUrl | String | ContentPage.imageUrl() |
| componentDetails | List | 항상 빈 리스트 고정 |

### Application Layer

| 항목 | 상세 |
|------|------|
| UseCase (Port-In) | `GetContentPageMetaUseCase.execute(long contentPageId)` |
| Service | `GetContentPageMetaService implements GetContentPageMetaUseCase` |
| Manager | `ContentPageQueryManager.fetchContentPageMeta(long)` — `@Transactional(readOnly = true)` |
| 예외 처리 | `Optional.orElseThrow(ContentPageNotFoundException)` |
| 출력 타입 | `ContentPage` (Domain Aggregate) |

### Domain Layer

| 항목 | 상세 |
|------|------|
| Port-Out | `ContentPageQueryPort.fetchContentPageMeta(long)` → `Optional<ContentPage>` |

### Adapter-Out Layer

| 항목 | 상세 |
|------|------|
| Adapter | `LegacyWebContentCompositeQueryAdapter.fetchContentPageMeta()` |
| Repository 호출 | `fetchContentById(contentPageId)` → `Optional<LegacyWebContentQueryDto>` |
| 변환 | `LegacyWebContentMapper.toContentPage(dto)` → `ContentPage` |

---

## Database Query 분석 — meta

| 항목 | 상세 |
|------|------|
| 대상 테이블 | `content` |
| WHERE | `content.content_id = ?` |
| 조건 없음 | 전시 기간 체크 없이 ID만으로 단건 조회 |

---

---

# 5. ContentPage — fetchContent (Q3)

## 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | /api/v1/content/{contentId} |
| Request | `contentId` (@PathVariable) + `bypass` (@RequestParam, optional) |
| Response | `V1ApiResponse<ContentV1ApiResponse>` |
| Controller | ContentQueryV1Controller |
| UseCase (Port-In) | GetContentPageDetailUseCase |
| Service | GetContentPageDetailService |
| Facade (ReadFacade) | ContentPageDetailReadFacade |
| Manager 1 | ContentPageQueryManager |
| Manager 2 | DisplayComponentReadManager |
| Manager 3 (Facade) | ComponentProductReadFacade |
| Manager 4 | FixedProductReadManager |
| Manager 5 | AutoProductReadManager |
| Port-Out 목록 | ContentPageQueryPort, DisplayComponentQueryPort, ComponentFixedProductQueryPort, ComponentAutoProductQueryPort, CategoryQueryPort |
| Adapter-Out 목록 | LegacyWebContentCompositeQueryAdapter, LegacyWebDisplayComponentQueryAdapter, LegacyWebFixedProductQueryAdapter, LegacyWebAutoProductQueryAdapter |
| Repository | LegacyWebContentCompositeQueryDslRepository |
| 활성화 조건 | persistence.legacy.content.enabled=true |

---

## 호출 흐름 다이어그램

```
ContentQueryV1Controller.fetchContent(long contentId, String bypass)
  |- bypass 변환: "Y".equalsIgnoreCase(bypass) → boolean bypassFlag
  |- new ContentPageSearchCriteria(contentId, bypassFlag)
  |- GetContentPageDetailUseCase.execute(ContentPageSearchCriteria)       [Port-In]
  |   +-- GetContentPageDetailService.execute(criteria)                   [Impl]
  |       +-- ContentPageDetailReadFacade.getContentPageDetail(criteria)  [ReadFacade]
  |           |
  |           +--(1) ContentPageQueryManager.fetchContentPage(criteria)   [@Transactional readOnly]
  |           |   +-- ContentPageQueryPort.fetchContentPage(criteria)     [Port-Out]
  |           |   |   +-- LegacyWebContentCompositeQueryAdapter.fetchContentPage()  [Impl]
  |           |   |       +-- repository.fetchContent(contentPageId)
  |           |   |           → Optional<LegacyWebContentQueryDto>
  |           |   |           → LegacyWebContentMapper.toContentPage(dto)
  |           |   +-- orElseThrow(ContentPageNotFoundException)
  |           |   +-- -> ContentPage
  |           |
  |           +--(2) DisplayComponentReadManager.fetchDisplayComponents(criteria) [@Transactional readOnly]
  |           |   +-- DisplayComponentQueryPort.fetchDisplayComponents()   [Port-Out]
  |           |       +-- LegacyWebDisplayComponentQueryAdapter.fetchDisplayComponents()  [Impl]
  |           |           |- repository.fetchComponentsByContentId(condition)
  |           |           |   → List<LegacyWebComponentQueryDto> (deduplicateByDisplayOrder 적용)
  |           |           |- viewExtensionIds 추출
  |           |           |- repository.fetchViewExtensionsByIds(viewExtensionIds)
  |           |           |   → Map<Long, LegacyWebViewExtensionQueryDto>
  |           |           |- buildComponentSpecMap(components)
  |           |           |   TITLE  → repository.fetchTitleComponents(ids)
  |           |           |   TEXT   → repository.fetchTextComponents(ids)
  |           |           |   BLANK  → repository.fetchBlankComponents(ids)
  |           |           |   IMAGE  → repository.fetchImageComponents(ids)
  |           |           |             + repository.fetchImageComponentItems(ids)
  |           |           |   PRODUCT → repository.fetchProductComponents(ids)
  |           |           |              + repository.fetchComponentProducts(ids)
  |           |           |   BRAND  → repository.fetchBrandComponents(ids)
  |           |           |             + repository.fetchBrandComponentItems(ids)
  |           |           |             + repository.fetchComponentProducts(ids)
  |           |           |   CATEGORY→ repository.fetchCategoryComponents(ids)
  |           |           |              + repository.fetchComponentProducts(ids)
  |           |           |   TAB    → repository.fetchTabComponents(ids)
  |           |           |             + repository.fetchTabs(tabComponentIds)
  |           |           |             + repository.fetchComponentProducts(ids)
  |           |           +-- LegacyWebContentMapper.toDisplayComponents(...)
  |           |           +-- -> List<DisplayComponent>
  |           |
  |           +--(3) ComponentProductReadFacade.fetchComponentProducts(components) [@Transactional readOnly]
  |               |- ProductComponentGroup.from(components)
  |               |
  |               +--(3a) 비-TAB 컴포넌트 FIXED 상품
  |               |   FixedProductReadManager.fetchFixedProducts(nonTabComponentIds)
  |               |   → ComponentFixedProductQueryPort.fetchFixedProducts()
  |               |     → LegacyWebFixedProductQueryAdapter
  |               |       → repository.fetchComponentProducts(componentIds)
  |               |         WHERE tabId=0 → componentId별 그룹핑
  |               |
  |               +--(3b) 비-TAB 컴포넌트 AUTO 상품
  |               |   AutoProductReadManager.fetchAutoProducts(AutoProductCriteria)
  |               |   |- CategoryQueryPort.findDescendantIds(categoryId)   [카테고리 하위 확장]
  |               |   → ComponentAutoProductQueryPort.fetchAutoProducts()
  |               |     → LegacyWebAutoProductQueryAdapter
  |               |       → repository.fetchAutoProducts(categoryIds, brandIds, limit)
  |               |         FROM product_group WHERE categoryId IN / brandId IN
  |               |
  |               +--(3c) TAB 컴포넌트 FIXED 상품
  |               |   FixedProductReadManager.fetchFixedProductsByTab(tabComponentIds)
  |               |   → repository.fetchComponentProducts(ids) WHERE tabId != 0
  |               |
  |               +--(3d) TAB 컴포넌트 AUTO 상품
  |               |   AutoProductReadManager.fetchAutoProducts(AutoProductCriteria) per tab
  |               |
  |               |- ProductComponentGroup.mergeComponentProducts(fixedByComponent, autoByComponent)
  |               |- ProductComponentGroup.mergeTabProducts(fixedByTab, autoByTab)
  |               +-- -> ComponentProductBundle(componentProducts, tabProducts)
  |
  |           +-- new ContentPageDetailResult(page, components, productBundle)
  |
  |- ContentV1ApiMapper.toContentResponse(page, components, productBundle)
  |   |- page 메타 → ContentV1ApiResponse 상위 필드
  |   |- components.stream().map(c → toComponentDetail(c, productBundle))
  |   |   |- buildComponentInner(component, productBundle)
  |   |   |   타입별 분기: TitleSpec, TextSpec, BlankSpec, ImageSpec,
  |   |   |                CategorySpec, ProductSpec, BrandSpec, TabSpec
  |   |   +-- -> ComponentDetailV1ApiResponse
  |   +-- -> ContentV1ApiResponse
  +-- -> ResponseEntity<V1ApiResponse<ContentV1ApiResponse>>
```

---

## Layer별 상세

### Adapter-In Layer

| 항목 | 상세 |
|------|------|
| Controller | `ContentQueryV1Controller` |
| Mapping | `@GetMapping("/{contentId}")` |
| Request | `@PathVariable long contentId`, `@RequestParam(required=false) String bypass` |
| bypass 변환 | `"Y".equalsIgnoreCase(bypass)` → boolean |
| Criteria 생성 | `new ContentPageSearchCriteria(contentId, bypassFlag)` |
| Mapper | `ContentV1ApiMapper.toContentResponse(ContentPage, List<DisplayComponent>, ComponentProductBundle)` |
| 컴포넌트 타입 분기 | TITLE / TEXT / BLANK / IMAGE / CATEGORY / PRODUCT / BRAND / TAB |
| Wrapper | `V1ApiResponse.success(response)` |

**ContentV1ApiResponse 필드**

| 필드 | 타입 | 설명 |
|------|------|------|
| contentId | long | 콘텐츠 ID |
| displayPeriod | DisplayPeriodResponse | 전시 시작/종료일 (yyyy-MM-dd HH:mm:ss) |
| title | String | 콘텐츠 제목 |
| memo | String | 메모 |
| imageUrl | String | 이미지 URL |
| componentDetails | List\<ComponentDetailV1ApiResponse\> | 컴포넌트 목록 (@JsonInclude NON_NULL) |

**ComponentDetailV1ApiResponse 필드**

| 필드 | 타입 | 설명 |
|------|------|------|
| componentId | long | 컴포넌트 ID |
| componentName | String | 컴포넌트 이름 |
| componentType | String | TEXT / TITLE / IMAGE / BLANK / TAB / BRAND / CATEGORY / PRODUCT |
| listType | String | ONE_STEP / TWO_STEP / MULTI / COLUMN / ROW / NONE |
| orderType | String | NONE / RECOMMEND / REVIEW / RECENT / ... |
| badgeType | String | LIKE / RANKING / ALL / NONE |
| filterYn | String | Y / N |
| displayYn | String | Y / N |
| component | Object | 타입별 내부 데이터 Map |
| viewExtensionDetails | Object | ViewExtension 정보 Map (nullable) |

### Application Layer

| 항목 | 상세 |
|------|------|
| UseCase (Port-In) | `GetContentPageDetailUseCase.execute(ContentPageSearchCriteria)` |
| Service | `GetContentPageDetailService implements GetContentPageDetailUseCase` |
| ReadFacade | `ContentPageDetailReadFacade.getContentPageDetail(criteria)` |
| 결과 DTO | `ContentPageDetailResult(ContentPage, List<DisplayComponent>, ComponentProductBundle)` |

**ContentPageDetailReadFacade 내부 Manager 호출 순서**

| 순서 | Manager | 역할 |
|------|---------|------|
| 1 | ContentPageQueryManager | 콘텐츠 메타 조회 + 미존재 예외 처리 |
| 2 | DisplayComponentReadManager | 컴포넌트 목록 + 타입별 Spec 조회 |
| 3 | ComponentProductReadFacade | FIXED / AUTO 상품 조합 (비-TAB + TAB) |

**AutoProductReadManager 특이사항**

- `AutoProductCriteria`의 `categoryIds`가 단일 ID인 경우 `CategoryQueryPort.findDescendantIds()` 호출로 하위 카테고리까지 확장
- 다중 categoryId이면 확장 없이 그대로 사용

**트랜잭션 경계**

| Manager | 트랜잭션 |
|---------|---------|
| ContentPageQueryManager | @Transactional(readOnly = true) |
| DisplayComponentReadManager | @Transactional(readOnly = true) |
| ComponentProductReadFacade (FixedProductReadManager) | @Transactional(readOnly = true) |
| ComponentProductReadFacade (AutoProductReadManager) | @Transactional(readOnly = true) |

### Domain Layer

| 항목 | 상세 |
|------|------|
| Port-Out 1 | `ContentPageQueryPort.fetchContentPage(ContentPageSearchCriteria)` → `Optional<ContentPage>` |
| Port-Out 2 | `DisplayComponentQueryPort.fetchDisplayComponents(ContentPageSearchCriteria)` → `List<DisplayComponent>` |
| Port-Out 3 | `ComponentFixedProductQueryPort.fetchFixedProducts(List<Long>)` → `Map<Long, List<ProductThumbnailSnapshot>>` |
| Port-Out 4 | `ComponentFixedProductQueryPort.fetchFixedProductsByTab(List<Long>)` → `Map<Long, List<ProductThumbnailSnapshot>>` |
| Port-Out 5 | `ComponentAutoProductQueryPort.fetchAutoProducts(AutoProductCriteria)` → `List<ProductThumbnailSnapshot>` |
| Domain 객체 | `ContentPage`, `DisplayComponent`, `ComponentProductBundle`, `ComponentSpec` (sealed), `ViewExtension` |
| Domain VO | `ContentPageSearchCriteria(long contentPageId, boolean bypass)` |

**ComponentSpec 타입 (sealed hierarchy)**

| 타입 | 테이블 |
|------|--------|
| TitleSpec | title_component |
| TextSpec | text_component |
| BlankSpec | blank_component |
| ImageSpec | image_component + image_component_item |
| CategorySpec | category_component |
| ProductSpec | product_component + component_target + component_item |
| BrandSpec | brand_component + brand_component_item + component_target + component_item |
| TabSpec | tab_component + tab + component_target + component_item |

### Adapter-Out Layer

| Adapter | 구현 Port | 조건 |
|---------|-----------|------|
| LegacyWebContentCompositeQueryAdapter | ContentPageQueryPort | legacy.content.enabled=true |
| LegacyWebDisplayComponentQueryAdapter | DisplayComponentQueryPort | legacy.content.enabled=true |
| LegacyWebFixedProductQueryAdapter | ComponentFixedProductQueryPort | legacy.content.enabled=true |
| LegacyWebAutoProductQueryAdapter | ComponentAutoProductQueryPort | legacy.content.enabled=true |

모든 Adapter가 동일한 `LegacyWebContentCompositeQueryDslRepository`와 `LegacyWebContentMapper`를 공유한다.

---

## Database Query 분석 — content detail

### 콘텐츠 메타 조회

| 항목 | 상세 |
|------|------|
| 대상 테이블 | `content` |
| WHERE | `content_id = ?` + `delete_yn = 'N'` |

### 컴포넌트 목록 조회

| 항목 | 상세 |
|------|------|
| 대상 테이블 | `component` |
| WHERE | `content_id = ?` + `onDisplayComponent(bypass)` — bypass=false면 전시 기간 + display_yn 체크 |
| ORDER BY | `display_order ASC`, `component_id ASC` |
| 중복 제거 | Adapter 내 `deduplicateByDisplayOrder()` — 동일 (displayOrder, componentType) 시 첫 등록 유지 |

### 타입별 Spec 조회 테이블

| 테이블 | 조건 |
|--------|------|
| title_component | component_id IN (...) AND delete_yn = 'N' |
| text_component | component_id IN (...) AND delete_yn = 'N' |
| blank_component | component_id IN (...) AND delete_yn = 'N' |
| image_component | component_id IN (...) AND delete_yn = 'N' |
| image_component_item | image_component_id IN (...) AND delete_yn = 'N', ORDER BY display_order ASC |
| product_component | component_id IN (...) |
| brand_component | component_id IN (...) |
| brand_component_item | brand_component_id IN (...) AND delete_yn = 'N', LEFT JOIN brand |
| category_component | component_id IN (...) |
| tab_component | component_id IN (...) |
| tab | tab_component_id IN (...) AND delete_yn = 'N', ORDER BY display_order ASC |
| view_extension | id IN (...) |

### FIXED 상품 조회

| 항목 | 상세 |
|------|------|
| 대상 테이블 | component_target, component_item, product_group, product_group_image, brand, product_rating_stats (LEFT), product_score (LEFT) |
| WHERE | `component_target.component_id IN (?)` + `component_target.delete_yn = 'N'` + `product_group.display_yn = 'Y'` + `product_group.sold_out_yn = 'N'` |
| JOIN | component_target → component_item → product_group → product_group_image(MAIN) → brand, LEFT JOIN rating_stats, score |
| ORDER BY | `component_item.display_order ASC` |
| TAB 분기 | `tabId = 0` → 컴포넌트 상품, `tabId != 0` → 탭 상품 |

### AUTO 상품 조회

| 항목 | 상세 |
|------|------|
| 대상 테이블 | product_group, product_group_image, brand, product_rating_stats (LEFT), product_score (LEFT) |
| WHERE | `display_yn = 'Y'` + `sold_out_yn = 'N'` + `category_id IN (?)` 또는 `brand_id IN (?)` |
| LIMIT | `AutoProductCriteria.limit()` 적용 |
| 카테고리 확장 | AutoProductReadManager에서 `CategoryQueryPort.findDescendantIds()` 호출 후 하위 ID 포함 |

**관련 JPA 엔티티 목록**

| 엔티티 | 테이블 |
|--------|--------|
| LegacyContentEntity | content |
| LegacyComponentEntity | component |
| LegacyViewExtensionEntity | view_extension |
| LegacyTitleComponentEntity | title_component |
| LegacyTextComponentEntity | text_component |
| LegacyBlankComponentEntity | blank_component |
| LegacyImageComponentEntity | image_component |
| LegacyImageComponentItemEntity | image_component_item |
| LegacyProductComponentEntity | product_component |
| LegacyBrandComponentEntity | brand_component |
| LegacyBrandComponentItemEntity | brand_component_item |
| LegacyCategoryComponentEntity | category_component |
| LegacyTabComponentEntity | tab_component |
| LegacyTabEntity | tab |
| LegacyComponentTargetEntity | component_target |
| LegacyComponentItemEntity | component_item |

---

---

# 아키텍처 특이사항 요약

## 1. 레거시 Adapter-Out 패턴

Banner, Navigation, ContentPage 모두 `persistence-mysql-legacy` 모듈의 Legacy Adapter를 사용한다.
각 Adapter는 `@ConditionalOnProperty`로 활성화되며, Application/Domain 레이어는 레거시 DB 존재를 모른다.

```
property: persistence.legacy.banner.enabled=true
property: persistence.legacy.navigation.enabled=true
property: persistence.legacy.content.enabled=true
```

## 2. Service → Manager 위임 패턴

Service는 UseCase를 구현하되 실질적인 로직 없이 Manager(또는 Facade)에 위임한다.
트랜잭션 경계는 항상 Manager 레벨에서 `@Transactional(readOnly = true)`로 선언한다.

```
Service (implements UseCase) → Manager (@Transactional) → Port-Out → Adapter
```

## 3. ContentPage Q3의 복합 Facade 패턴

Q3(fetchContent)는 단일 Service가 단일 UseCase를 구현하되, 내부에서 `ContentPageDetailReadFacade`가 3개의 Manager를 순차 호출하여 결과를 조합한다.

```
Service → ReadFacade → [ContentPageQueryManager, DisplayComponentReadManager, ComponentProductReadFacade]
                         ComponentProductReadFacade → [FixedProductReadManager, AutoProductReadManager]
```

## 4. ContentPage Q2 vs Q3 차이

| 항목 | Q2 (meta) | Q3 (detail) |
|------|-----------|-------------|
| 컴포넌트 조회 | 없음 | 있음 (타입별 N+1 쿼리) |
| 상품 조회 | 없음 | FIXED + AUTO |
| bypass 지원 | 없음 | 있음 (전시 기간 우회) |
| componentDetails | 항상 빈 리스트 | 실제 컴포넌트 목록 |
| Criteria | contentId(long) | ContentPageSearchCriteria(id, bypass) |

## 5. Banner 컨트롤러 직접 변환

Banner Controller는 `BannerType.valueOf(bannerType)` 을 컨트롤러에서 직접 호출한다.
잘못된 문자열 입력 시 `IllegalArgumentException` 발생 → 에러 매퍼(`BannerErrorMapper`)가 처리해야 한다.

## 6. Navigation 응답 필드 축소

`NavigationMenu` Domain 객체의 `displayOrder`, `displayPeriod`는 Redis TTL 산출 및 정렬 전용 필드로,
응답 DTO(`NavigationMenuV1ApiResponse`)에서 의도적으로 제외된다.

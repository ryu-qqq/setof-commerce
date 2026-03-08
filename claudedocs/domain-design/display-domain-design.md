# Display 도메인 설계 문서

## 1. 개요

레거시 `Content/Display` 모듈을 신규 헥사고날 아키텍처로 마이그레이션하기 위한 도메인 설계.
Strangler Fig Migration Plan의 **Phase 8: Display (전시)** 에 해당한다.

### 대상 레거시 엔드포인트 (6개, 전부 Query)

| # | 엔드포인트 | 레거시 엔티티 |
|---|---|---|
| 1 | `GET /api/v1/content/on-display` | Content |
| 2 | `GET /api/v1/content/meta/{contentId}` | Content + Component |
| 3 | `GET /api/v1/content/{contentId}` | Content + Component + SubComponent + ProductGroup |
| 4 | `GET /api/v1/content/component/{componentId}/products` | Component + ProductGroupThumbnail |
| 5 | `GET /api/v1/content/banner` | Banner + BannerItem |
| 6 | `GET /api/v1/content/gnbs` | Gnb |

> Banner(#5)는 `domain/banner/`, GNB(#6)는 `domain/navigation/`에 이미 도메인이 존재한다.
> 이 문서는 **Content 계열(#1~#4)** 의 신규 도메인 설계를 다룬다.

---

## 2. Aggregate 분리 전략

### 2.1 분리 대상

| Aggregate | 관심사 | 변경 빈도 |
|---|---|---|
| **ContentPage** | 콘텐츠 페이지 메타정보, 컴포넌트 구성 순서 | 낮음 (제목, 기간 변경) |
| **DisplayComponent** | 개별 컴포넌트의 레이아웃, 타입별 본문, 상품 큐레이션 | 높음 (상품 교체, 순서 변경) |

### 2.2 분리 근거

**일관성 경계(Consistency Boundary) 분석:**

1. **독립적 생명주기**: ContentPage의 제목/기간 변경과 DisplayComponent의 상품 교체는 서로 독립적
2. **트랜잭션 범위 최소화**: 상품 큐레이션 변경(빈번)이 ContentPage 트랜잭션에 영향을 주지 않음
3. **크기 제어**: 레거시에서 Content 1개에 Component 수십 개 + 각 Component에 상품 수백 개가 연결됨.
   단일 Aggregate로 묶으면 메모리/성능 문제 발생
4. **독립 조회 패턴**: 엔드포인트 #4(`/component/{id}/products`)는 Component를 직접 조회하므로,
   ContentPage를 거치지 않는 독립 진입점이 존재

**연결 방식:**
- ContentPage → DisplayComponent: ID 참조만 (`ContentPageId`로 소속 식별)
- DisplayComponent는 자신이 속한 ContentPage를 `contentPageId` 필드로 참조

---

## 3. 레거시 → 신규 매핑 상세

### 3.1 엔티티/클래스 매핑

| 레거시 (bootstrap-legacy-web-api) | 신규 도메인 | 분류 | 변경 사유 |
|---|---|---|---|
| `Content` | `ContentPage` | Aggregate | "Content"는 너무 범용적. 페이지 단위임을 명시 |
| `Component` | `DisplayComponent` | Aggregate | Aggregate Root로 승격. "Display" 접두어로 전시 맥락 명시 |
| `ComponentDetails` (Embedded) | `ComponentLayout` (VO) | VO | "Details"는 모호. 레이아웃 설정임을 명시 |
| `ViewExtension` + `ViewExtensionDetails` | `ViewExtension` (Entity) + `AfterMaxAction` (VO) | Entity+VO | 후속 액션(afterMax~)을 별도 VO로 분리하여 응집도 향상 |
| `SubComponent` 인터페이스 (8종 구현체) | `ComponentBody` sealed interface (8종 record) | VO | 다형 DTO → 타입 안전한 sealed interface. 불변 record 기반 |
| `ImageComponentItem` | `ImageSlide` (VO) | VO | "Item"은 모호. 이미지 슬라이드임을 명시 |
| `BrandComponentItem` | `BrandTarget` (VO) | VO | "Item"은 모호. 브랜드 타겟(조건)임을 명시 |
| `Tab` (Entity) | `TabItem` (VO, TabBody 내부) | VO | 독립 엔티티 → VO로 경량화. Tab은 컴포넌트 내부 개념 |
| `ComponentTarget` | `ProductCuration` (Entity) | Entity | "Target"은 모호. 상품 큐레이션(편성)임을 명시 |
| `ComponentItem` | `CuratedProduct` (Entity) | Entity | "Item"은 모호. 큐레이팅된 상품임을 명시 |

### 3.2 필드 레벨 변경

| 레거시 필드 | 신규 필드 | 변경 내용 |
|---|---|---|
| `Yn.Y / Yn.N` 전반 | `boolean` | enum 기반 Y/N → 원시 boolean. 도메인에서 Yn enum은 불필요 |
| `displayYn` | `active` | 의미 명확화. "표시 여부" → "활성 상태" |
| `deleteYn` + 별도 시간 | `DeletionStatus` (VO) | 삭제 여부+시간을 VO로 묶어 동기화 보장 (프로젝트 공통 패턴) |
| `LocalDateTime` 시간 필드 | `Instant` | 프로젝트 규칙 DOM-AGG-003 준수. 타임존 독립적 |
| `DisplayPeriod` (Embedded, LocalDateTime) | `DisplayPeriod` (VO, Instant) | 공통 VO 재사용 (domain/common/vo) |
| `ListType.ONE_STEP` | `ListType.ONE_COLUMN` | 의미 명확화. "Step"은 단계 느낌, 실제로는 컬럼 수 |
| `ListType.TWO_STEP` | `ListType.TWO_COLUMN` | 동일 사유 |
| `SortType` | `CurationSortType` | 범용 이름 충돌 방지. 큐레이션 맥락 명시 |
| `ImageType` | `ImageLayout` | "Type" 남발 방지. 레이아웃 설정임을 명시 |
| `lineYn` (BlankComponent) | `showLine: boolean` | Yn→boolean + 의미 명확화 |
| `stickyYn` (TabComponent) | `stickyEnabled: boolean` | 동일 |
| `filterYn` (ComponentDetails) | `filterEnabled: boolean` | 동일 |

### 3.3 구조적 변경

| 변경 항목 | 레거시 | 신규 | 근거 |
|---|---|---|---|
| SubComponent 다형성 | `@JsonSubTypes` + 인터페이스 | `sealed interface` + `permits` | 컴파일 타임 타입 안전성 보장. 새 타입 추가 시 누락 방지 |
| ViewExtension 후속 액션 | `afterMaxActionType` + `afterMaxActionLinkUrl` (2개 평면 필드) | `AfterMaxAction` VO (record) | 관련 필드를 VO로 묶어 응집도 향상 |
| Tab 독립 엔티티 | `Tab` JPA Entity (PK, FK) | `TabItem` VO (TabBody 내부) | Tab은 TabComponent 없이 독립 존재 불가 → VO로 경량화 |
| ComponentTarget + ComponentItem | 2개 엔티티, Target이 Item 목록 보유 | `ProductCuration` + `CuratedProduct` | 이름만으로 역할 파악 가능하도록 리네이밍 |
| 다형 서비스 전략 패턴 | `ProductComponentFetchStrategy` + `NonProductComponentFetchStrategy` | `ComponentType.isProductRelated()` + body 기반 분기 | 전략 패턴 과도한 추상화 제거. sealed interface 패턴 매칭으로 충분 |

---

## 4. DB 테이블 매핑 (Legacy DB 참조)

마이그레이션 중에는 같은 레거시 DB를 참조하므로, persistence-mysql-legacy 모듈에서 다음 테이블을 사용한다.

| 신규 도메인 | 레거시 테이블 | 비고 |
|---|---|---|
| `ContentPage` | `content` | 1:1 |
| `DisplayComponent` | `component` | 1:1 |
| `ViewExtension` | `view_extension` | component.view_extension_id FK |
| `TextBody` | `text_component` | component_id FK |
| `TitleBody` | `title_component` | component_id FK |
| `BlankBody` | `blank_component` | component_id FK |
| `ImageBody` | `image_component` | component_id FK |
| `ImageSlide` | `image_component_item` | image_component_id FK |
| `ProductBody` | `product_component` | component_id FK |
| `CategoryBody` | `category_component` | component_id FK |
| `BrandBody` | `brand_component` | component_id FK |
| `BrandTarget` | `brand_component_item` | brand_component_id FK |
| `TabBody` | `tab_component` | component_id FK |
| `TabItem` | `tab` | tab_component_id FK |
| `ProductCuration` | `component_target` | component_id FK + tab_id FK |
| `CuratedProduct` | `component_item` | component_target_id FK |

---

## 5. 전체 관계도

```
ContentPage ──(1:N, ID 참조)──> DisplayComponent
                                      │
                                      ├── ComponentLayout (VO)
                                      │   ├── componentType: ComponentType
                                      │   ├── listType: ListType
                                      │   ├── orderType: OrderType
                                      │   ├── badgeType: BadgeType
                                      │   └── filterEnabled: boolean
                                      │
                                      ├── DisplayPeriod (VO, 공통)
                                      │
                                      ├── ViewExtension (Entity, 0..1)
                                      │   ├── extensionType: ViewExtensionType
                                      │   ├── linkUrl, buttonName
                                      │   ├── productCountPerClick, maxClickCount
                                      │   └── afterMaxAction: AfterMaxAction (VO)
                                      │       ├── actionType: ViewExtensionType
                                      │       └── linkUrl: String
                                      │
                                      ├── ComponentBody (sealed interface, 택1)
                                      │   │
                                      │   ├─ Non-Product ─────────────────────
                                      │   │  ├── TextBody(content)
                                      │   │  ├── TitleBody(title1, title2, subTitle1, subTitle2)
                                      │   │  ├── BlankBody(height, showLine)
                                      │   │  └── ImageBody(imageLayout, slides: List<ImageSlide>)
                                      │   │      └── ImageSlide(imageUrl, linkUrl, displayOrder)
                                      │   │
                                      │   └─ Product-Related ─────────────────
                                      │      ├── ProductBody()
                                      │      ├── CategoryBody(categoryId)
                                      │      ├── BrandBody(targets: List<BrandTarget>)
                                      │      │   └── BrandTarget(brandId, categoryId)
                                      │      └── TabBody(stickyEnabled, tabMovingType, tabs: List<TabItem>)
                                      │          └── TabItem(tabId, tabName, displayOrder)
                                      │
                                      └── List<ProductCuration> (Entity, 상품 관련 타입만)
                                          ├── curationSortType: CurationSortType
                                          ├── tabId: Long (nullable, Tab용)
                                          └── items: List<CuratedProduct> (Entity)
                                              ├── productGroupId: long
                                              ├── displayName: String (nullable)
                                              ├── displayImage: String (nullable)
                                              └── displayOrder: int
```

---

## 6. 패키지 구조

```
domain/src/main/java/com/ryuqq/setof/domain/
├── contentpage/
│   ├── aggregate/
│   │   ├── ContentPage.java
│   │   └── ContentPageUpdateData.java
│   ├── id/
│   │   └── ContentPageId.java
│   └── exception/
│       ├── ContentPageErrorCode.java
│       ├── ContentPageException.java
│       └── ContentPageNotFoundException.java
│
└── displaycomponent/
    ├── aggregate/
    │   ├── DisplayComponent.java
    │   └── DisplayComponentUpdateData.java
    ├── entity/
    │   ├── ViewExtension.java
    │   ├── ProductCuration.java
    │   └── CuratedProduct.java
    ├── id/
    │   ├── DisplayComponentId.java
    │   ├── ViewExtensionId.java
    │   ├── ProductCurationId.java
    │   └── CuratedProductId.java
    ├── vo/
    │   ├── ComponentLayout.java
    │   ├── AfterMaxAction.java
    │   ├── ImageSlide.java
    │   ├── BrandTarget.java
    │   ├── TabItem.java
    │   ├── ComponentType.java
    │   ├── ListType.java
    │   ├── OrderType.java
    │   ├── BadgeType.java
    │   ├── ImageLayout.java
    │   ├── TabMovingType.java
    │   ├── CurationSortType.java
    │   ├── ViewExtensionType.java
    │   └── body/
    │       ├── ComponentBody.java        (sealed interface)
    │       ├── TextBody.java
    │       ├── TitleBody.java
    │       ├── BlankBody.java
    │       ├── ImageBody.java
    │       ├── ProductBody.java
    │       ├── CategoryBody.java
    │       ├── BrandBody.java
    │       └── TabBody.java
    └── exception/
        ├── DisplayComponentErrorCode.java
        ├── DisplayComponentException.java
        └── DisplayComponentNotFoundException.java
```

---

## 7. 기존 도메인과의 관계

| 기존 도메인 | 관계 | 설명 |
|---|---|---|
| `domain/banner/` | 독립 | Banner는 별도 Aggregate. 엔드포인트 #5에서 사용 |
| `domain/navigation/` | 독립 | NavigationMenu는 별도 Aggregate. 엔드포인트 #6에서 사용 |
| `domain/productgroup/` | CuratedProduct에서 productGroupId로 참조 | 상품 정보는 조회 시 JOIN으로 획득 |
| `domain/brand/` | BrandTarget에서 brandId로 참조 | 브랜드 컴포넌트 조건 |
| `domain/category/` | CategoryBody에서 categoryId로 참조 | 카테고리 컴포넌트 조건 |

---

## 8. 마이그레이션 시 주의사항

1. **같은 DB 참조**: persistence-mysql-legacy 모듈로 레거시 테이블 직접 접근
2. **Redis 캐싱**: 레거시에서 Banner/GNB는 Redis 캐싱 사용 중. 신규에서도 동일하게 적용 필요
3. **다형 응답**: 레거시 API는 `@JsonSubTypes`로 다형 JSON 응답. 신규에서도 호환 응답 필요
4. **ComponentTarget.tabId**: Tab 컴포넌트에서 탭별 상품을 구분하는 핵심 연결고리
5. **ViewExtension nullable**: 모든 컴포넌트가 ViewExtension을 가지는 것은 아님
6. **ProductCuration**: Non-Product 타입(TEXT, TITLE, IMAGE, BLANK)에서는 productCurations가 빈 리스트

---

## 9. 향후 스키마 분리 시 참고

DB Schema Separation (Phase Final) 시, 위 레거시 테이블들을 신규 스키마로 이관한다.
이때 이 문서의 "레거시 → 신규 매핑" 섹션을 참고하여:

1. 테이블명/컬럼명을 신규 도메인 네이밍으로 변경
2. `Yn` 컬럼 → `boolean` 변환
3. `LocalDateTime` → `timestamp` (UTC)
4. Embedded 필드들을 별도 테이블 또는 JSON 컬럼으로 정리
5. SubComponent 타입별 테이블을 유지하거나 단일 테이블 전략(STI)으로 통합 검토

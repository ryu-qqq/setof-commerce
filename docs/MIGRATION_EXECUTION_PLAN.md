# SetOf Commerce 마이그레이션 실행 계획

> 상위 문서: [Strangler Fig 마이그레이션 계획](./STRANGLER_FIG_MIGRATION_PLAN.md)
> 작성일: 2026-02-12
> 상태: Draft

---

## 1. 배경 및 목표

### 비즈니스 변경사항

세토프(SetOf)가 명품 이커머스 허브에서 **단순 자사몰**로 전환됩니다.

```
AS-IS: 세토프 = 상품 허브 (외부 OMS → 세토프 → 외부 사이트)
TO-BE: MarketPlace = 새 허브 서버, 세토프 = MarketPlace의 외부몰 중 하나
```

- **MarketPlace** 서버가 허브 역할 대체 (상품/주문/셀러 관리)
- 프론트 어드민 → MarketPlace 어드민 사용 (세토프 어드민은 CMS/Discount만 내부 관리용)
- 외부 OMS → Gateway → MarketPlace 프록시 (레거시 응답 포맷 유지)
- `rest-api-admin` 대규모 개발 불필요, **`rest-api`(웹) 마이그레이션이 핵심**

### 마이그레이션 전략

```
Phase 1: Read 마이그레이션 (레거시 DB) ─┐
Phase 2: v2 내부 API (MarketPlace 연동) ─┤ 병렬 진행
                                         │
Phase 3: Dual Write (새 DB + 레거시 DB) ─┘ Phase 1,2 완료 후
Phase 4: Read 전환 (새 DB)
Phase 5: 레거시 제거
Phase 6: 내부 SDK (MarketPlace용)
```

---

## 2. 현재 상태 매트릭스

### 도메인별 레이어 구현 현황

| 도메인 | v1 Controller | Application UseCase | Legacy Composite Adapter | 새 DB Adapter |
|--------|:---:|:---:|:---:|:---:|
| **productgroup** | **완료** (6 endpoints) | **완료** (7 UseCases) | **완료** (Thumbnail + Detail) | 미구현 |
| **brand** | **완료** (2 endpoints) | **완료** | **완료** | **완료** |
| **category** | **완료** (1 endpoint) | **완료** | **완료** | **완료** |
| **seller** | **완료** (1 endpoint) | **완료** | **완료** (Customer + Admin) | **완료** |
| **board** | **완료** (1 endpoint) | **완료** | **완료** | - |
| **faq** | **완료** (1 endpoint) | **완료** | **완료** | - |
| content/banner/gnb | DTO만 존재 | DTO만 존재 | **완료** (3개 Adapter) | - |
| cart | DTO만 존재 | DTO만 존재 | **완료** | - |
| user | DTO만 존재 | DTO만 존재 | **완료** (4개 Adapter) | - |
| order | DTO만 존재 | DTO만 존재 | **완료** | - |
| payment | DTO만 존재 | DTO만 존재 | **완료** | - |
| review | DTO만 존재 | DTO만 존재 | **완료** | - |
| qna | DTO만 존재 | DTO만 존재 | **완료** | - |
| mileage | DTO만 존재 | DTO만 존재 | **완료** | - |

**핵심 인사이트**: 나머지 8개 도메인은 Legacy Composite Adapter가 **이미 존재**합니다.
Application Port + UseCase + Service + Controller만 추가하면 새 서버(bootstrap-web-api)에서 서비스 가능합니다.

### 아키텍처 참조 패턴 (productgroup - 이미 완성)

```
[Adapter-In]                    [Application]                     [Adapter-Out]
Controller                      UseCase (Port-In)                 LegacyCompositeAdapter
  │                               │                                  │
  ├── Endpoints.java              ├── port/in/query/UseCase.java     ├── adapter/Adapter.java
  ├── controller/Controller.java  ├── port/out/query/Port.java       ├── repository/Repository.java
  ├── mapper/Mapper.java          ├── service/query/Service.java     ├── mapper/Mapper.java
  ├── dto/request/                ├── dto/query/                     ├── dto/
  ├── dto/response/               └── dto/response/                  └── condition/
  └── error/ErrorMapper.java
```

---

## 3. Phase 1: rest-api(웹) Read 마이그레이션

> **목표**: 레거시 서버(bootstrap-legacy-web-api)를 완전 대체하여 새 서버(bootstrap-web-api)에서 모든 v1 Read API 서비스
> **DB**: 레거시 DB 사용 (기존 Legacy Adapter 활용)

### 각 도메인별 생성 파일 구조 (공통)

```
application/src/main/java/com/ryuqq/setof/application/legacy/{domain}/
  port/in/query/{UseCase}.java          ← UseCase 인터페이스
  port/out/query/{Port}.java            ← Port-Out 인터페이스
  service/query/{Service}.java          ← @Service UseCase 구현

adapter-in/rest-api/src/main/java/com/ryuqq/setof/adapter/in/rest/v1/{domain}/
  {Domain}V1Endpoints.java              ← URL 상수 정의
  controller/{Domain}QueryV1Controller.java  ← @RestController
  mapper/{Domain}V1ApiMapper.java       ← @Component 변환
  error/{Domain}ErrorMapper.java        ← 에러 매핑
```

**수정 파일**: 각 Legacy Adapter에 신규 Port `implements` 추가

---

### Task 1-1: Content 도메인 (배너, GNB, 컴포넌트)

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | 없음 (독립) |

**현재 존재하는 것**:
- Adapter-In DTO: `GetContentV1ApiRequest`, `SearchBannersV1ApiRequest`, `SearchComponentProductsV1ApiRequest`, Response 6개
- Legacy Adapter: `LegacyWebContentCompositeQueryAdapter`, `LegacyWebBannerCompositeQueryAdapter`, `LegacyWebGnbCompositeQueryAdapter`
- Application DTO: `legacy/content/`, `legacy/banner/`, `legacy/gnb/`

**생성할 것**:
- Application UseCases: `GetContentUseCase`, `GetBannersUseCase`, `GetGnbUseCase`, `GetComponentProductsUseCase`
- Application Ports: `ContentCompositionQueryPort`, `BannerCompositionQueryPort`, `GnbCompositionQueryPort`
- Application Services: 4개
- Controller: `ContentQueryV1Controller` (또는 배너/GNB 분리)
- Mapper, Endpoints, ErrorMapper

---

### Task 1-2: Cart 도메인

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | 인증 필요 (userId) |

**현재 존재하는 것**:
- Adapter-In DTO: `SearchCartsCursorV1ApiRequest`, `CartCountV1ApiResponse`, `CartV1ApiResponse`
- Legacy Adapter: `LegacyWebCartCompositeQueryAdapter`
- Application DTO: `LegacyCartResult`, `LegacyCartCountResult`, `LegacyCartOptionResult`, `LegacyCartPriceResult`

**생성할 것**:
- Application UseCases: `GetCartsByUserUseCase`, `GetCartCountUseCase`
- Application Port: `CartCompositionQueryPort`
- Application Services: 2개
- Controller, Mapper, Endpoints, ErrorMapper

---

### Task 1-3: User 도메인

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | 없음 (독립) |

**현재 존재하는 것**:
- Adapter-In DTO: `CheckUserExistsV1ApiRequest`, `SearchMyFavoritesCursorV1ApiRequest` 등, Response 6개
- Legacy Adapter: `LegacyWebUserCompositeQueryAdapter`, `LegacyWebUserFavoriteQueryAdapter`, `LegacyWebRefundAccountQueryAdapter`, `LegacyWebShippingAddressQueryAdapter` (4개)
- Application DTO: 7개

**생성할 것**:
- Application UseCases: `GetMyPageUseCase`, `GetUserInfoUseCase`, `CheckUserExistsUseCase`, `GetShippingAddressesUseCase`, `GetRefundAccountUseCase`, `GetUserFavoritesUseCase`
- Application Ports: `UserCompositionQueryPort`, `UserFavoriteQueryPort`, `ShippingAddressQueryPort`, `RefundAccountQueryPort`
- Application Services: 6개
- Controller, Mapper, Endpoints, ErrorMapper

---

### Task 1-4: Order 도메인

| 항목 | 내용 |
|------|------|
| **크기** | L (가장 복잡) |
| **의존성** | User (userId 필요) |

**현재 존재하는 것**:
- Adapter-In DTO: `SearchOrderCountsV1ApiRequest`, `SearchOrdersCursorV1ApiRequest`, Response 4개
- Legacy Adapter: `LegacyWebOrderCompositeQueryAdapter`
- Application DTO: 11개 (가장 많음 - 주문상태, 배송, 반품, 클레임 등)

**생성할 것**:
- Application UseCases: `GetOrderHistoryUseCase`, `GetOrderDetailUseCase`, `GetOrderCountsUseCase`
- Application Port: `OrderCompositionQueryPort`
- Application Services: 3개
- Controller, Mapper, Endpoints, ErrorMapper

---

### Task 1-5: Payment 도메인

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | Order |

**현재 존재하는 것**:
- Adapter-In DTO: `SearchPaymentsCursorV1ApiRequest`, Response 6개
- Legacy Adapter: `LegacyWebPaymentCompositeQueryAdapter`
- Application DTO: `LegacyPaymentResult`

**생성할 것**:
- Application UseCases: `GetPaymentDetailUseCase`, `GetPayMethodsUseCase`, `GetBanksUseCase`
- Application Port: `PaymentCompositionQueryPort`
- Application Services: 3개
- Controller, Mapper, Endpoints, ErrorMapper

---

### Task 1-6: Review 도메인

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | User, ProductGroup |

**현재 존재하는 것**:
- Adapter-In DTO: Request 3개, Response 3개
- Legacy Adapter: `LegacyWebReviewCompositeQueryAdapter`
- Application DTO: 4개

**생성할 것**:
- Application UseCases: `GetProductReviewsUseCase`, `GetMyReviewsUseCase`, `GetAvailableReviewsUseCase`
- Application Port: `ReviewCompositionQueryPort`
- Application Services: 3개
- Controller, Mapper, Endpoints, ErrorMapper

---

### Task 1-7: QnA 도메인

| 항목 | 내용 |
|------|------|
| **크기** | S |
| **의존성** | User, ProductGroup |

**현재 존재하는 것**:
- Adapter-In DTO: Request 2개, Response 3개
- Legacy Adapter: `LegacyWebQnaCompositeQueryAdapter`
- Application DTO: `LegacyQnaResult`

**생성할 것**: Application Port/Service + Controller/Endpoints/Mapper/ErrorMapper

---

### Task 1-8: Mileage 도메인

| 항목 | 내용 |
|------|------|
| **크기** | S |
| **의존성** | User |

**현재 존재하는 것**:
- Adapter-In DTO: Request 1개, Response 3개
- Legacy Adapter: `LegacyWebMileageCompositeQueryAdapter`
- Application DTO: 3개

**생성할 것**: Application Port/Service + Controller/Endpoints/Mapper/ErrorMapper

---

### Task 1-9: Bootstrap 연결 및 통합 검증

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | Task 1-1 ~ 1-8 전체 |

**작업**:
- `bootstrap-web-api` ComponentScan 확인 (신규 컨트롤러 등록)
- Swagger UI에서 전체 v1 엔드포인트 노출 확인
- 레거시 서버(`bootstrap-legacy-web-api`) 응답과 비교 테스트
- Gateway 라우팅 전환 준비

**검증**: `./gradlew :bootstrap:bootstrap-web-api:bootRun` → Swagger UI 전체 확인 + RestDocs

---

## 4. Phase 2: v2 내부 API (MarketPlace → 세토프 연동)

> **목표**: MarketPlace 서버가 세토프의 상품/주문/셀러/회원을 관리할 수 있는 내부 API 제공
> **위치**: `adapter-in/rest-api` 모듈의 `v2/internal/` 패키지
> **인증**: API Key 기반 (MarketPlace 전용)

### Task 2-1: v2 내부 API 인프라

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | 없음 (독립, Phase 1과 병렬 가능) |

**생성 파일**:
```
adapter-in/rest-api/src/main/java/com/ryuqq/setof/adapter/in/rest/v2/
  common/
    dto/V2ApiResponse.java                    ← 표준 응답 래퍼
    controller/V2GlobalExceptionHandler.java  ← v2 예외 처리
  internal/
    config/InternalApiAuthConfig.java         ← MarketPlace 인증 설정
    interceptor/InternalAuthInterceptor.java  ← API Key 인증 인터셉터
```

---

### Task 2-2: v2 ProductGroup 내부 API

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | Task 2-1 |

**엔드포인트 설계**:

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/api/v2/internal/product-groups` | 오프셋 페이징 목록 |
| `GET` | `/api/v2/internal/product-groups/{id}` | 상세 조회 |
| `GET` | `/api/v2/internal/product-groups/{id}/products` | SKU 목록 |
| `POST` | `/api/v2/internal/product-groups` | 생성 |
| `PUT` | `/api/v2/internal/product-groups/{id}` | 수정 |
| `PATCH` | `/api/v2/internal/product-groups/{id}/status` | 상태 변경 (진열/품절 등) |
| `PATCH` | `/api/v2/internal/products/{id}/stock` | 재고 변경 |

**생성 파일**:
```
adapter-in/rest-api/src/main/java/.../v2/internal/productgroup/
  InternalProductGroupV2Endpoints.java
  controller/InternalProductGroupQueryV2Controller.java
  controller/InternalProductGroupCommandV2Controller.java
  dto/request/CreateProductGroupV2Request.java
  dto/request/UpdateProductGroupV2Request.java
  dto/request/UpdateProductGroupStatusV2Request.java
  dto/request/UpdateProductStockV2Request.java
  dto/response/ProductGroupV2Response.java
  dto/response/ProductGroupDetailV2Response.java
  mapper/InternalProductGroupV2ApiMapper.java
  error/InternalProductGroupErrorMapper.java
```

**Application 생성**: Command UseCase + CommandPort + Service (Read는 기존 재사용)

---

### Task 2-3: v2 Seller 내부 API

| 항목 | 내용 |
|------|------|
| **크기** | S |
| **의존성** | Task 2-1 (Seller Application 레이어는 이미 완료) |

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/api/v2/internal/sellers` | 셀러 목록 |
| `GET` | `/api/v2/internal/sellers/{id}` | 셀러 상세 |
| `POST` | `/api/v2/internal/sellers` | 셀러 등록 |
| `PUT` | `/api/v2/internal/sellers/{id}` | 셀러 수정 |

---

### Task 2-4: v2 Order 내부 API

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | Task 2-1 |

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/api/v2/internal/orders` | 주문 목록 |
| `GET` | `/api/v2/internal/orders/{id}` | 주문 상세 |
| `POST` | `/api/v2/internal/orders` | 주문 생성 |
| `PUT` | `/api/v2/internal/orders/{id}/status` | 상태 변경 |
| `POST` | `/api/v2/internal/orders/{id}/cancel` | 주문 취소 |
| `POST` | `/api/v2/internal/orders/{id}/return` | 반품 요청 |

---

### Task 2-5: v2 User/Member 내부 API

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | Task 2-1 (Member Application 레이어 이미 존재) |

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/api/v2/internal/members/{id}` | 회원 조회 |
| `POST` | `/api/v2/internal/members/sync` | 회원 동기화 |
| `PATCH` | `/api/v2/internal/members/{id}/status` | 상태 변경 |

---

### Task 2-6: v2 Payment 내부 API

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | Task 2-1, Task 2-4 |

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/api/v2/internal/payments/{orderId}` | 결제 상세 |
| `POST` | `/api/v2/internal/payments/confirm` | 결제 확인 |
| `POST` | `/api/v2/internal/payments/{id}/cancel` | 결제 취소 |

---

## 5. Phase 3: Dual Write (새 DB + 레거시 DB 동시 쓰기)

> **목표**: Command 작업 시 새 DB와 레거시 DB에 동시 쓰기하여 새 DB에 데이터 축적
> **전제조건**: Phase 1, 2 안정화 완료

### Task 3-1: ProductGroup 새 DB Entity + Adapter 구현

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | Phase 1 완료 |
| **현재 상태** | `persistence-mysql`에 ProductGroup 관련 구현 없음 |

**생성 파일**:
```
adapter-out/persistence-mysql/src/main/java/.../productgroup/
  entity/ProductGroupJpaEntity.java
  entity/ProductJpaEntity.java
  entity/ProductStockJpaEntity.java
  entity/ProductImageJpaEntity.java
  repository/ProductGroupJpaRepository.java
  repository/ProductGroupQueryDslRepository.java
  adapter/ProductGroupCommandAdapter.java     ← 새 DB Write
  adapter/ProductGroupQueryAdapter.java       ← 새 DB Read
  mapper/ProductGroupPersistenceMapper.java
  condition/ProductGroupConditionBuilder.java
```

**Flyway**: 새 DB 테이블 DDL 마이그레이션 스크립트 추가

---

### Task 3-2: Dual Write 메커니즘 구현

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | Task 3-1 |

**설계**: Application Layer의 Coordinator가 두 Port를 조합

```java
@Service
public class ProductGroupDualWriteCoordinator {
    private final ProductGroupCommandPort newDbPort;
    private final LegacyProductGroupCommandPort legacyPort;

    @Transactional
    public void create(ProductGroupCommand command) {
        legacyPort.save(command);     // 레거시 먼저 (안전 - 실패 시 롤백)
        newDbPort.save(command);      // 새 DB
    }
}
```

**생성 파일**:
- `application/.../productgroup/port/out/command/ProductGroupCommandPort.java`
- `application/.../productgroup/port/out/command/LegacyProductGroupCommandPort.java`
- `application/.../productgroup/internal/ProductGroupDualWriteCoordinator.java`
- Legacy Adapter에 `LegacyProductGroupCommandPort` 구현 추가

---

### Task 3-3: Order/Payment 새 DB + Dual Write

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | Task 3-2 패턴 확립 후 |

동일 Coordinator 패턴 적용

---

### Task 3-4: User/Member Dual Write

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | Task 3-2 |

동일 패턴 적용

---

## 6. Phase 4: Read를 새 DB로 전환

> **목표**: Port 구현체를 프로퍼티 기반으로 전환하여 Read를 새 DB에서 수행
> **전제조건**: Phase 3 완료 + 새 DB에 데이터 충분히 축적

### Task 4-1: ConditionalOnProperty 기반 Read 소스 전환

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | Phase 3 완료 |

**설계**: 도메인별 프로퍼티로 Bean 선택

```java
// 레거시 (기본값 - matchIfMissing = true)
@Component
@ConditionalOnProperty(name = "setof.read.productgroup.source", havingValue = "legacy", matchIfMissing = true)
public class LegacyWebProductGroupCompositeQueryAdapter implements ProductGroupCompositionQueryPort { ... }

// 새 DB
@Component
@ConditionalOnProperty(name = "setof.read.productgroup.source", havingValue = "new")
public class NewProductGroupCompositeQueryAdapter implements ProductGroupCompositionQueryPort { ... }
```

```yaml
# application.yml - 도메인별 독립 전환 가능
setof:
  read:
    productgroup:
      source: legacy   # legacy | new
    brand:
      source: legacy
    order:
      source: legacy
```

**수정 파일**: 모든 Legacy Composite Adapter에 `@ConditionalOnProperty` 추가

**생성 파일**:
```
adapter-out/persistence-mysql/src/main/java/.../composite/productgroup/
  adapter/NewProductGroupCompositeQueryAdapter.java
  repository/ProductGroupCompositeQueryDslRepository.java
```

---

### Task 4-2: Shadow Read (비교 검증)

| 항목 | 내용 |
|------|------|
| **크기** | M |
| **의존성** | Task 4-1 |

**설계**: 전환 전에 양쪽 DB를 동시 조회하여 결과 비교, 불일치 시 로그/메트릭

**생성 파일**:
- `application/.../common/shadow/ShadowReadComparator.java`
- `application/.../common/shadow/ShadowReadProperties.java`

**기준**: 불일치율 < 0.1% 달성 후 전환

---

### Task 4-3: 나머지 도메인 Read 전환

| 항목 | 내용 |
|------|------|
| **크기** | L |
| **의존성** | Task 4-2 안정화 |

ProductGroup 패턴 검증 후 Brand → Category → Seller → Order → Payment 순차 적용

---

## 7. Phase 5: 레거시 제거

### Task 5-1: Read default를 새 DB로 전환 [S]

`application.yml`에서 모든 도메인 `setof.read.*.source=new` 기본값 변경

### Task 5-2: Dual Write에서 레거시 쓰기 제거 [M]

Coordinator에서 legacy Port 호출 제거 → 단일 쓰기 (새 DB only)

### Task 5-3: persistence-mysql-legacy 모듈 제거 [M]

- Composite Adapter, Entity, Repository 삭제
- `build.gradle.kts` 의존성 제거
- Bootstrap 모듈에서 레거시 Persistence 스캔 제거

---

## 8. Phase 6: 내부 SDK (MarketPlace용)

### Task 6-1: setof-client-sdk 모듈 설계 [L]

**설계안**: 별도 라이브러리 또는 `adapter-out/client/` 하위 모듈

```
setof-client-sdk/
  src/main/java/com/ryuqq/setof/sdk/
    SetofClient.java                    ← 메인 클라이언트 (Builder 패턴)
    product/SetofProductClient.java     ← 상품 API 클라이언트
    order/SetofOrderClient.java         ← 주문 API 클라이언트
    seller/SetofSellerClient.java       ← 셀러 API 클라이언트
    member/SetofMemberClient.java       ← 회원 API 클라이언트
    dto/                                ← SDK DTO (v2 API 응답 기반)
    config/SetofClientProperties.java   ← 설정 (URL, API Key, timeout)
```

MarketPlace에서 의존성 추가 후 바로 사용 가능

### Task 6-2: SDK 테스트 및 문서 [M]

단위 테스트 + 통합 테스트 + API 문서 자동 생성

---

## 9. 실행 타임라인

```
Month 1-2 (병렬 진행):
┌─ Phase 1: Read 마이그레이션 ────────────────────────┐
│  1-1 Content [M]  ─┐                                │
│  1-2 Cart [M]      │                                │
│  1-3 User [L]      ├─ 1-9 Bootstrap 통합 [M]       │
│  1-4 Order [L]     │                                │
│  1-5 Payment [M]   │                                │
│  1-6 Review [M]    │                                │
│  1-7 QnA [S]       │                                │
│  1-8 Mileage [S]  ─┘                                │
└──────────────────────────────────────────────────────┘
┌─ Phase 2: v2 내부 API (병렬) ────────────────────────┐
│  2-1 인프라 [M] ─┬─ 2-2 ProductGroup [L]            │
│                  ├─ 2-3 Seller [S]                   │
│                  ├─ 2-4 Order [L]                    │
│                  ├─ 2-5 Member [M]                   │
│                  └─ 2-6 Payment [M]                  │
└──────────────────────────────────────────────────────┘

Month 3-4:
  Phase 3: Dual Write (3-1 → 3-2 → 3-3 → 3-4)

Month 4-5:
  Phase 4: Read 전환 (4-1 → 4-2 → 4-3)

Month 5-6:
  Phase 5: 레거시 제거 (5-1 → 5-2 → 5-3)
  Phase 6: SDK (6-1 → 6-2)
```

---

## 10. 핵심 참조 파일

| 용도 | 파일 경로 |
|------|-----------|
| **Controller 패턴** | `adapter-in/rest-api/src/main/java/com/ryuqq/setof/adapter/in/rest/v1/productgroup/controller/ProductGroupQueryV1Controller.java` |
| **Endpoints 상수** | `adapter-in/rest-api/src/main/java/com/ryuqq/setof/adapter/in/rest/v1/productgroup/ProductGroupV1Endpoints.java` |
| **API Mapper** | `adapter-in/rest-api/src/main/java/com/ryuqq/setof/adapter/in/rest/v1/productgroup/mapper/ProductGroupV1ApiMapper.java` |
| **UseCase 인터페이스** | `application/src/main/java/com/ryuqq/setof/application/productgroup/port/in/query/GetProductGroupDetailUseCase.java` |
| **Port-Out 인터페이스** | `application/src/main/java/com/ryuqq/setof/application/productgroup/port/out/query/ProductGroupCompositionQueryPort.java` |
| **Service 구현체** | `application/src/main/java/com/ryuqq/setof/application/productgroup/service/query/GetProductGroupDetailService.java` |
| **Legacy Adapter** | `adapter-out/persistence-mysql-legacy/src/main/java/com/ryuqq/setof/storage/legacy/composite/web/productgroup/adapter/LegacyWebProductGroupCompositeQueryAdapter.java` |
| **V1 응답 래퍼** | `adapter-in/rest-api/src/main/java/com/ryuqq/setof/adapter/in/rest/v1/common/dto/V1ApiResponse.java` |
| **Legacy DTO** | `application/src/main/java/com/ryuqq/setof/application/legacy/product/dto/response/LegacyProductGroupDetailResult.java` |
| **Strangler Fig 계획** | `docs/STRANGLER_FIG_MIGRATION_PLAN.md` |

---

## 11. 검증 방법

| Phase | 검증 방법 |
|-------|-----------|
| **Phase 1** | `./gradlew :bootstrap:bootstrap-web-api:bootRun` → Swagger UI 전체 엔드포인트 확인 + RestDocs 테스트 + 레거시 서버 응답 비교 |
| **Phase 2** | RestDocs 테스트 + API Key 인증 테스트 + MarketPlace 연동 시뮬레이션 |
| **Phase 3** | 트랜잭션 일관성 테스트 + 양쪽 DB 데이터 정합성 비교 스크립트 |
| **Phase 4** | Shadow Read 비교 (불일치율 < 0.1%) + 프로퍼티 전환 테스트 + 성능 벤치마크 |
| **Phase 5** | 전체 통합 테스트 + 프로덕션 모니터링 (에러율 < 0.1%, P99 < 250ms) |
| **Phase 6** | SDK 단위 테스트 + MarketPlace 통합 테스트 |

---

## 12. 작업량 요약

| Phase | 설명 | Task 수 | S | M | L |
|-------|------|---------|---|---|---|
| 1 | 웹 Read 마이그레이션 | 9 | 2 | 4 | 3 |
| 2 | v2 내부 API | 6 | 1 | 3 | 2 |
| 3 | Dual Write | 4 | 0 | 1 | 3 |
| 4 | Read 전환 | 3 | 0 | 1 | 2 |
| 5 | 레거시 제거 | 3 | 1 | 2 | 0 |
| 6 | SDK | 2 | 0 | 1 | 1 |
| **총합** | | **27** | **4** | **12** | **11** |

---

## 13. 리스크 및 대응

| 리스크 | 영향 | 대응 방안 |
|--------|------|-----------|
| Legacy Adapter가 Port를 구현하지 않는 경우 | Phase 1 지연 | Adapter 코드 확인 후 Port 인터페이스 설계 |
| v2 내부 API 인증 체계 불명확 | MarketPlace 연동 불가 | API Key + IP Whitelist 이중 인증 |
| Dual Write 트랜잭션 실패 | 데이터 불일치 | 레거시 먼저 쓰기 + 새 DB 실패 시 보상 트랜잭션 |
| Shadow Read 불일치율 높음 | Read 전환 지연 | 불일치 원인별 분류 + 매핑 로직 수정 |
| 레거시 테이블 스키마 변경 | Adapter 깨짐 | 레거시 DB DDL 변경 금지 정책 + 모니터링 |

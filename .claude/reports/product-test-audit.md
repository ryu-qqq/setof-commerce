# Product / ProductGroup 테스트 감사 리포트

> 생성일: 2026-03-13
> 대상: domain/application/adapter-out/adapter-in — product, productgroup 관련 전 레이어

---

## 커버리지 요약

### Domain Layer

| 역할 | 소스 클래스 | 테스트 있음 | 테스트 없음 | 커버리지 |
|------|-----------|-----------|-----------|---------|
| Aggregate (product) | 2 | 2 | 0 | 100% |
| Aggregate (productgroup) | 2 | 2 | 0 | 100% |
| Aggregate (productgroupimage) | 1 | 1 | 0 | 100% |
| Aggregate (productnotice) | 2 | 2 | 0 | 100% |
| Aggregate (productdescription) | 2 | 2 | 0 | 100% |
| VO (product) | 6 | 6 | 0 | 100% |
| VO (productgroup) | 12 | 8 | **4** | 67% |
| VO (productgroupimage) | 3 | 3 | 0 | 100% |
| VO (productnotice) | 2 | 2 | 0 | 100% |
| ID (product) | 2 | 2 | 0 | 100% |
| ID (productgroup) | 3 | 3 | 0 | 100% |
| ID (productgroupimage) | 1 | 1 | 0 | 100% |
| ID (productnotice) | 3 | 2 | **1** | 67% |
| Query (productgroup) | 4 | 3 | **1** | 75% |
| Exception | 13 | 13 | 0 | 100% |
| **합계** | **58** | **52** | **6** | **90%** |

### Application Layer

| 역할 | 소스 클래스 | 테스트 있음 | 테스트 없음 | 커버리지 |
|------|-----------|-----------|-----------|---------|
| Service - productgroup (query) | 8 | 0 | **8** | 0% |
| Service - productgroup (command) | 3 | 0 | **3** | 0% |
| Service - product (command) | 3 | 0 | **3** | 0% |
| Manager - productgroup | 3 | 0 | **3** | 0% |
| Manager - product | 3 | 0 | **3** | 0% |
| Factory - productgroup | 3 | 0 | **3** | 0% |
| Factory - product | 1 | 0 | **1** | 0% |
| Assembler - productgroup | 1 | 0 | **1** | 0% |
| Internal / Coordinator - productgroup | 5 | 0 | **5** | 0% |
| Internal / Coordinator - product | 1 | 0 | **1** | 0% |
| DTO (composite/response) | 13 | 7 | **6** | 54% |
| **합계** | **44** | **7** | **37** | **16%** |

### Adapter-Out (persistence-mysql)

| 역할 | 소스 클래스 | 테스트 있음 | 테스트 없음 | 커버리지 |
|------|-----------|-----------|-----------|---------|
| Adapter - product | 3 | 1 | **2** | 33% |
| Adapter - productgroup | 5 | 1 | **4** | 20% |
| Adapter - productgroupimage | 2 | 0 | **2** | 0% |
| Adapter - productnotice | 3 | 0 | **3** | 0% |
| Adapter - productdescription | 3 | 0 | **3** | 0% |
| Mapper - product | 1 | 1 | 0 | 100% |
| Mapper - productgroup | 1 | 1 | 0 | 100% |
| Mapper - productgroupimage | 1 | 1 | 0 | 100% |
| Mapper - productnotice | 1 | 1 | 0 | 100% |
| Mapper - productdescription | 1 | 1 | 0 | 100% |
| ConditionBuilder - product | 1 | 1 | 0 | 100% |
| ConditionBuilder - productgroup | 1 | 1 | 0 | 100% |
| ConditionBuilder (composite) | 2 | 2 | 0 | 100% |
| Adapter (composite) | 1 | 1 | 0 | 100% |
| **합계** | **26** | **12** | **14** | **46%** |

### Adapter-In (rest-api-admin v2)

| 역할 | 소스 클래스 | 테스트 있음 | 테스트 없음 | 커버리지 |
|------|-----------|-----------|-----------|---------|
| Controller - product (command) | 1 | 1 | 0 | 100% |
| Controller - productgroup (command) | 1 | 1 | 0 | 100% |
| Controller - productgroup (query) | 1 | 0 | **1** | 0% |
| Controller - productgroupimage (command) | 1 | 1 | 0 | 100% |
| Controller - productgroupdescription (command) | 1 | 1 | 0 | 100% |
| Controller - productnotice (command) | 1 | 1 | 0 | 100% |
| Mapper - product (command) | 1 | 1 | 0 | 100% |
| Mapper - productgroup (command) | 1 | 1 | 0 | 100% |
| Mapper - productgroup (query) | 1 | 0 | **1** | 0% |
| Mapper - productgroupimage (command) | 1 | 1 | 0 | 100% |
| Mapper - productgroupdescription (command) | 1 | 1 | 0 | 100% |
| Mapper - productnotice (command) | 1 | 1 | 0 | 100% |
| ErrorMapper | 5 | 0 | **5** | 0% |
| **합계** | **17** | **10** | **7** | **59%** |

---

## Fixtures 현황

| 레이어 | 패키지 | Fixtures 파일 | 상태 |
|--------|--------|-------------|------|
| Domain | product | ProductFixtures.java | 존재 |
| Domain | productgroup | ProductGroupFixtures.java | 존재 |
| Domain | productgroupimage | ProductGroupImageFixtures.java | 존재 |
| Domain | productnotice | ProductNoticeFixtures.java | 존재 |
| Domain | productdescription | ProductDescriptionFixtures.java | 존재 |
| Application | product | ProductQueryFixtures.java | 존재 |
| Application | productgroup | ProductGroupCompositeQueryFixtures.java | 존재 |
| Application | productgroupimage | 없음 | 없음 |
| Application | productnotice | 없음 | 없음 |
| Application | productdescription | 없음 | 없음 |
| Adapter-Out | product | 없음 | 없음 |
| Adapter-Out | productgroup | 없음 | 없음 |
| Adapter-Out | productgroupimage | 없음 | 없음 |
| Adapter-Out | productnotice | 없음 | 없음 |
| Adapter-Out | productdescription | 없음 | 없음 |
| Adapter-Out | composite/productgroup | ProductGroupCompositeDtoFixtures.java | 존재 |
| Adapter-In | product | ProductApiFixtures.java | 존재 |
| Adapter-In | productgroup | ProductGroupApiFixtures.java | 존재 |
| Adapter-In | productgroupimage | ProductGroupImageApiFixtures.java | 존재 |
| Adapter-In | productgroupdescription | ProductGroupDescriptionApiFixtures.java | 존재 |
| Adapter-In | productnotice | ProductNoticeApiFixtures.java | 존재 |

---

## 미테스트 클래스 (MISSING_TEST)

### Application Layer

| 클래스 | 역할 | public 메서드 수 | 우선순위 |
|--------|------|----------------|---------|
| ProductGroupAssembler | Assembler | 5 (toSliceResult, toDetailResult, toWebDetailResult, toProductDetailResults + private 4) | 🔴 HIGH |
| ProductGroupReadFacade | Facade/Coordinator | 6 (getDetailBundle, getProductGroupDetailBundle, getListBundle, getListBundleByIds, getListBundleByBrand, getListBundleBySeller, getSearchBundle) | 🔴 HIGH |
| ProductGroupCommandManager | Manager | 1 (persist) | 🟡 MEDIUM |
| ProductGroupReadManager | Manager | 3 (getById, findByProductGroupId, getByIds) | 🔴 HIGH |
| ProductGroupCompositionReadManager | Manager | 여러 fetch 메서드 | 🔴 HIGH |
| ProductGroupCommandFactory | Factory | 1 (createUpdateData) | 🟡 MEDIUM |
| ProductGroupBundleFactory | Factory | bundle 생성 | 🟡 MEDIUM |
| ProductGroupQueryFactory | Factory | query 생성 | 🟡 MEDIUM |
| FullProductGroupRegistrationCoordinator | Coordinator | 1 (register) | 🔴 HIGH |
| FullProductGroupUpdateCoordinator | Coordinator | 1 (update) | 🔴 HIGH |
| ProductGroupCommandCoordinator | Coordinator | 여러 메서드 | 🔴 HIGH |
| ProductGroupPersistFacade | Facade | 여러 메서드 | 🔴 HIGH |
| RegisterProductGroupFullService | Service | 1 (execute) | 🔴 HIGH |
| UpdateProductGroupBasicInfoService | Service | 1 (execute) | 🔴 HIGH |
| UpdateProductGroupFullService | Service | 1 (execute) | 🔴 HIGH |
| GetAdminProductGroupService | Service | 1 (execute) | 🔴 HIGH |
| GetProductGroupDetailService | Service | 1 (execute) | 🔴 HIGH |
| GetProductGroupsByBrandService | Service | 1 (execute) | 🟡 MEDIUM |
| GetProductGroupsByIdsService | Service | 1 (execute) | 🟡 MEDIUM |
| GetProductGroupsBySellerService | Service | 1 (execute) | 🟡 MEDIUM |
| GetProductGroupsService | Service | 1 (execute) | 🟡 MEDIUM |
| GetWebProductGroupDetailService | Service | 1 (execute) | 🟡 MEDIUM |
| SearchProductGroupsService | Service | 1 (execute) | 🟡 MEDIUM |
| ProductReadManager | Manager | 3 (getById, findByProductGroupId, getByIds) | 🔴 HIGH |
| ProductCommandManager | Manager | persist, update 등 | 🔴 HIGH |
| ProductOptionMappingCommandManager | Manager | persist | 🟡 MEDIUM |
| ProductCommandFactory | Factory | toOptionCommand, toEntries, toUpdateData 등 | 🔴 HIGH |
| ProductCommandCoordinator | Coordinator | update | 🔴 HIGH |
| UpdateProductPriceService | Service | 1 (execute) | 🔴 HIGH |
| UpdateProductStockService | Service | 1 (execute) | 🔴 HIGH |
| UpdateProductsService | Service | 1 (execute) | 🔴 HIGH |

### Adapter-Out Layer

| 클래스 | 역할 | public 메서드 수 | 우선순위 |
|--------|------|----------------|---------|
| ProductQueryAdapter | Adapter | 3 (findById, findByProductGroupId, findByIds) | 🔴 HIGH |
| ProductCommandAdapter | Adapter | persist, update | 🔴 HIGH |
| ProductOptionMappingCommandAdapter | Adapter | persist | 🟡 MEDIUM |
| ProductJpaEntityMapper | Mapper | toDomain, toEntity 계열 | 🔴 HIGH |
| ProductConditionBuilder | ConditionBuilder | buildCondition 계열 | 🟡 MEDIUM |
| ProductGroupQueryAdapter | Adapter | findById, findByIds 등 | 🔴 HIGH |
| ProductGroupCommandAdapter | Adapter | persist | 🔴 HIGH |
| SellerOptionGroupQueryAdapter | Adapter | findByProductGroupId 등 | 🔴 HIGH |
| SellerOptionGroupCommandAdapter | Adapter | persist, delete 등 | 🔴 HIGH |
| SellerOptionValueCommandAdapter | Adapter | persist, delete 등 | 🟡 MEDIUM |
| ProductGroupJpaEntityMapper | Mapper | toDomain, toEntity 계열 | 🔴 HIGH |
| ProductGroupConditionBuilder | ConditionBuilder | buildCondition 계열 | 🟡 MEDIUM |
| ProductGroupImageCommandAdapter | Adapter | persist, delete 등 | 🟡 MEDIUM |
| ProductGroupImageQueryAdapter | Adapter | findByProductGroupId | 🟡 MEDIUM |
| ProductGroupImageJpaEntityMapper | Mapper | toDomain, toEntity 계열 | 🟡 MEDIUM |
| ProductGroupDescriptionCommandAdapter | Adapter | persist, update | 🟡 MEDIUM |
| ProductGroupDescriptionQueryAdapter | Adapter | findByProductGroupId | 🟡 MEDIUM |
| ProductGroupDescriptionJpaEntityMapper | Mapper | toDomain, toEntity 계열 | 🟡 MEDIUM |
| ProductNoticeCommandAdapter | Adapter | persist, update | 🟡 MEDIUM |
| ProductNoticeEntryCommandAdapter | Adapter | persist, delete | 🟡 MEDIUM |
| ProductNoticeQueryAdapter | Adapter | findByProductGroupId | 🟡 MEDIUM |
| ProductNoticeJpaEntityMapper | Mapper | toDomain, toEntity 계열 | 🟡 MEDIUM |
| DescriptionImageCommandAdapter | Adapter | persist, delete | 🟡 MEDIUM |

### Adapter-In Layer

| 클래스 | 역할 | public 메서드 수 | 우선순위 |
|--------|------|----------------|---------|
| ProductGroupQueryController | Controller | 1 (getById) | 🔴 HIGH |
| ProductGroupQueryApiMapper | Mapper | 1 public (toDetailResponse) + private 8 | 🔴 HIGH |
| ProductGroupV1ApiMapper (rest-api) | Mapper | toCommand/toResponse 계열 | 🟡 MEDIUM |
| ProductGroupErrorMapper | ErrorMapper | exception → HTTP status 변환 | 🟢 LOW |
| ProductErrorMapper | ErrorMapper | exception → HTTP status 변환 | 🟢 LOW |
| ProductGroupImageErrorMapper | ErrorMapper | exception → HTTP status 변환 | 🟢 LOW |
| ProductGroupDescriptionErrorMapper | ErrorMapper | exception → HTTP status 변환 | 🟢 LOW |
| ProductNoticeErrorMapper | ErrorMapper | exception → HTTP status 변환 | 🟢 LOW |

---

## 메서드 갭 (MISSING_METHOD)

### Domain Layer - 테스트 있지만 미커버 메서드

| 테스트 파일 | 누락 메서드 | 우선순위 |
|------------|-----------|---------|
| ProductGroupTest | `replaceImages()` - 이미지 교체 후 썸네일 포함 검증 누락 | 🟡 MEDIUM |
| ProductGroupTest | `replaceSellerOptionGroups()` - 옵션 구조 검증 실패 케이스 (옵션 수 불일치) 누락 | 🔴 HIGH |
| SellerOptionGroupTest | `forNew()` - 빈 optionValues 리스트 입력 케이스 누락 | 🟡 MEDIUM |
| ProductTest | `changeStatus()` - `default` 분기 (지원하지 않는 상태) 케이스 누락 | 🟡 MEDIUM |

---

## 시나리오 갭 (MISSING_EDGE_CASE / MISSING_STATE_TRANSITION)

### Domain Layer

| 테스트 파일 | 누락 시나리오 | 유형 | 우선순위 |
|------------|-------------|------|---------|
| ProductGroupTest | `replaceSellerOptionGroups()` — SINGLE optionType인데 그룹 2개 전달 시 `ProductGroupInvalidOptionStructureException` 발생 검증 | MISSING_STATE_TRANSITION | 🔴 HIGH |
| ProductGroupTest | `replaceSellerOptionGroups()` — COMBINATION optionType인데 그룹 1개만 전달 시 예외 발생 검증 | MISSING_STATE_TRANSITION | 🔴 HIGH |
| ProductGroupTest | `replaceSellerOptionGroups()` — NONE optionType인데 그룹 존재 시 예외 발생 검증 | MISSING_STATE_TRANSITION | 🔴 HIGH |
| ProductGroupTest | `forNew()` (지정 ID 버전) — 레거시 ID 동기화 시나리오 테스트 없음 | MISSING_EDGE_CASE | 🟡 MEDIUM |
| ProductTest | `isOnSale()` — salePrice가 null인 경우 검증 누락 | MISSING_EDGE_CASE | 🟡 MEDIUM |
| ProductTest | `effectivePrice()` — discountRate > 0이지만 salePrice == null인 경우 | MISSING_EDGE_CASE | 🟡 MEDIUM |
| ProductGroupTest | `discountRate()` — salePrice == currentPrice (세일 아님) 경계 케이스 | MISSING_EDGE_CASE | 🟢 LOW |

### Application Layer (테스트 존재하는 DTO 클래스 기준)

| 테스트 파일 | 누락 시나리오 | 유형 | 우선순위 |
|------------|-------------|------|---------|
| ProductGroupAssembler (MISSING_TEST) | 빈 번들 입력 → `toSliceResult()` empty 결과 검증 | MISSING_EDGE_CASE | 🔴 HIGH |
| ProductGroupAssembler (MISSING_TEST) | `pageSize + 1`개 결과 → hasNext=true, cursor 계산 검증 | MISSING_EDGE_CASE | 🔴 HIGH |
| ProductGroupAssembler (MISSING_TEST) | `orderType=null` → cursor에 lastDomainId만 포함 검증 | MISSING_EDGE_CASE | 🔴 HIGH |
| ProductGroupReadFacade (MISSING_TEST) | 존재하지 않는 productGroupId → `ProductGroupNotFoundException` 발생 | MISSING_EDGE_CASE | 🔴 HIGH |
| ProductGroupReadFacade (MISSING_TEST) | 빈 ID 목록 → 빈 결과 번들 반환 | MISSING_EDGE_CASE | 🟡 MEDIUM |
| ProductReadManager (MISSING_TEST) | 존재하지 않는 productId → `ProductNotFoundException` 발생 | MISSING_EDGE_CASE | 🔴 HIGH |
| UpdateProductsService (MISSING_TEST) | 옵션 변경 + 상품 diff 정합성 검증 (Coordinator 협력 순서) | MISSING_STATE_TRANSITION | 🔴 HIGH |

---

## 컨벤션 위반 (PATTERN_VIOLATION)

현재 존재하는 테스트 파일들을 기준으로 검토한 결과:

| 테스트 파일 | 위반 항목 | 기대값 | 현재값 |
|------------|----------|--------|--------|
| ProductGroupTest | 테스트 대상 변수명 | `sut` | `productGroup` (직접 변수명 사용) |
| ProductTest | 테스트 대상 변수명 | `sut` | `product` (직접 변수명 사용) |
| SellerOptionGroupTest | 테스트 대상 변수명 | `sut` | `group` (직접 변수명 사용) |
| ProductGroupCommandApiMapperTest | 테스트 대상 변수명 | `sut` | `mapper` 사용 (허용 가능) |

> 참고: `sut` 네이밍 규칙은 프로젝트 컨벤션에 따라 판단 필요. 현재 도메인 테스트에서는 의미 있는 변수명 사용이 우세하므로 실제 위반 여부는 컨벤션 DB 기준으로 최종 확인 권장.

---

## 권장 조치

### 🔴 HIGH

| # | 유형 | 대상 | 조치 |
|---|------|------|------|
| 1 | MISSING_TEST | ProductGroupAssembler | `toSliceResult()`, `toDetailResult()`, `toWebDetailResult()`, `toProductDetailResults()` 단위 테스트 작성. 빈 입력, hasNext 판별, cursor 계산, description/notice null 처리 포함 |
| 2 | MISSING_TEST | ProductGroupReadFacade | 각 public 메서드별 단위 테스트. 존재하지 않는 ID 조회 → NotFound 예외, 정상 번들 반환 검증 |
| 3 | MISSING_TEST | ProductGroupReadManager | `getById()` not-found, `findByProductGroupId()` 빈 결과, `getByIds()` 빈 ID 리스트 케이스 |
| 4 | MISSING_TEST | ProductReadManager | `getById()` not-found 예외, `findByProductGroupId()` 빈 결과 |
| 5 | MISSING_TEST | ProductCommandFactory | option → product 변환 정합성, null 입력 처리 |
| 6 | MISSING_TEST | ProductCommandCoordinator | product diff update 협력 순서 검증 |
| 7 | MISSING_TEST | UpdateProductPriceService | execute() 정상 + 비즈니스 규칙 위반(가격 역전) 케이스 |
| 8 | MISSING_TEST | UpdateProductStockService | execute() 정상 + 음수 재고 케이스 |
| 9 | MISSING_TEST | UpdateProductsService | execute() - 옵션 수정 + product diff 협력 검증 |
| 10 | MISSING_TEST | RegisterProductGroupFullService | execute() 정상 등록, 트랜잭션 내 모든 협력 객체 호출 검증 |
| 11 | MISSING_TEST | UpdateProductGroupFullService | execute() 정상, 옵션 구조 변경 케이스 |
| 12 | MISSING_TEST | GetAdminProductGroupService | execute() 정상, not-found 예외 전파 |
| 13 | MISSING_TEST | GetProductGroupDetailService | execute() 정상, not-found 예외 전파 |
| 14 | MISSING_TEST | FullProductGroupRegistrationCoordinator | `register()` - persistFacade 위임 검증 |
| 15 | MISSING_TEST | FullProductGroupUpdateCoordinator | `update()` - persistFacade 위임 검증 |
| 16 | MISSING_TEST | ProductGroupQueryAdapter | `findById()` 없음 → Optional.empty(), `findByIds()` 빈 결과 |
| 17 | MISSING_TEST | ProductGroupCommandAdapter | `persist()` 정상 저장 검증 |
| 18 | MISSING_TEST | ProductQueryAdapter | `findById()` not-found, `findByProductGroupId()` 빈 결과 |
| 19 | MISSING_TEST | ProductCommandAdapter | `persist()`, `update()` 정상 검증 |
| 20 | MISSING_TEST | ProductJpaEntityMapper | Domain ↔ JpaEntity 변환 정합성, null 필드 처리 |
| 21 | MISSING_TEST | ProductGroupJpaEntityMapper | Domain ↔ JpaEntity 변환 정합성 |
| 22 | MISSING_TEST | ProductGroupQueryController | `getById()` RestDocs 테스트 - 200 정상, 404 not-found |
| 23 | MISSING_TEST | ProductGroupQueryApiMapper | `toDetailResponse()` - shippingPolicy/refundPolicy/description/notice null 처리 각각 검증 |
| 24 | MISSING_EDGE_CASE | ProductGroupTest | `replaceSellerOptionGroups()` — SINGLE/COMBINATION/NONE optionType 별 구조 검증 실패 케이스 |
| 25 | MISSING_FIXTURES | application/productgroupimage | `ProductGroupImageApplicationFixtures.java` 생성 |
| 26 | MISSING_FIXTURES | application/productnotice | `ProductNoticeApplicationFixtures.java` 생성 |
| 27 | MISSING_FIXTURES | adapter-out/product | `ProductPersistenceFixtures.java` 생성 |
| 28 | MISSING_FIXTURES | adapter-out/productgroup | `ProductGroupPersistenceFixtures.java` 생성 |

### 🟡 MEDIUM

| # | 유형 | 대상 | 조치 |
|---|------|------|------|
| 29 | MISSING_TEST | ProductGroupCommandManager | `persist()` - Port 호출 위임 검증 |
| 30 | MISSING_TEST | ProductGroupCommandFactory | `createUpdateData()` - OptionType 유지 검증 |
| 31 | MISSING_TEST | ProductGroupBundleFactory | bundle 생성 정합성 검증 |
| 32 | MISSING_TEST | ProductGroupQueryFactory | query 파라미터 변환 검증 |
| 33 | MISSING_TEST | UpdateProductGroupBasicInfoService | execute() 정상, not-found 예외 |
| 34 | MISSING_TEST | GetProductGroupsByBrandService | execute() 정상, 빈 결과 |
| 35 | MISSING_TEST | GetProductGroupsByIdsService | execute() 정상, 빈 ID 목록 |
| 36 | MISSING_TEST | GetProductGroupsBySellerService | execute() 정상, 빈 결과 |
| 37 | MISSING_TEST | GetProductGroupsService | execute() 정상 |
| 38 | MISSING_TEST | GetWebProductGroupDetailService | execute() 정상, not-found |
| 39 | MISSING_TEST | SearchProductGroupsService | execute() 정상, 빈 검색어 |
| 40 | MISSING_TEST | ProductOptionMappingCommandManager | persist 위임 검증 |
| 41 | MISSING_TEST | SellerOptionGroupQueryAdapter | findByProductGroupId 빈 결과, 다건 결과 |
| 42 | MISSING_TEST | SellerOptionGroupCommandAdapter | persist, delete 검증 |
| 43 | MISSING_TEST | SellerOptionValueCommandAdapter | persist, delete 검증 |
| 44 | MISSING_TEST | ProductGroupConditionBuilder | 조건 조합 검증 (null 조건 전체, 부분 조건) |
| 45 | MISSING_TEST | ProductConditionBuilder | 조건 조합 검증 |
| 46 | MISSING_TEST | ProductGroupImageCommandAdapter | persist, delete 검증 |
| 47 | MISSING_TEST | ProductGroupImageQueryAdapter | findByProductGroupId 빈 결과 |
| 48 | MISSING_TEST | ProductGroupImageJpaEntityMapper | Domain ↔ JpaEntity 변환 정합성 |
| 49 | MISSING_TEST | ProductGroupDescriptionCommandAdapter | persist, update 검증 |
| 50 | MISSING_TEST | ProductGroupDescriptionQueryAdapter | findByProductGroupId Optional.empty() 검증 |
| 51 | MISSING_TEST | ProductGroupDescriptionJpaEntityMapper | Domain ↔ JpaEntity 변환 정합성 |
| 52 | MISSING_TEST | ProductNoticeCommandAdapter | persist, update 검증 |
| 53 | MISSING_TEST | ProductNoticeQueryAdapter | findByProductGroupId Optional.empty() 검증 |
| 54 | MISSING_TEST | ProductNoticeJpaEntityMapper | Domain ↔ JpaEntity 변환 정합성 |
| 55 | MISSING_TEST | DescriptionImageCommandAdapter | persist, delete 검증 |
| 56 | MISSING_TEST | ProductGroupV1ApiMapper | toCommand / toResponse 변환 정합성, null 필드 처리 |
| 57 | MISSING_EDGE_CASE | ProductGroupTest | `forNew()` 지정 ID 버전 - null ID 입력 시 신규 ID 생성 검증 |
| 58 | MISSING_EDGE_CASE | ProductTest | `isOnSale()` — salePrice null 케이스 |
| 59 | MISSING_EDGE_CASE | SellerOptionGroupTest | `forNew()` — 빈 optionValues 리스트 생성 검증 |
| 60 | MISSING_EDGE_CASE | ProductTest | `changeStatus()` — 지원하지 않는 상태 IllegalArgumentException 발생 |

### 🟢 LOW

| # | 유형 | 대상 | 조치 |
|---|------|------|------|
| 61 | MISSING_TEST | ProductGroupErrorMapper | HTTP 상태 매핑 검증 |
| 62 | MISSING_TEST | ProductErrorMapper | HTTP 상태 매핑 검증 |
| 63 | MISSING_TEST | ProductGroupImageErrorMapper | HTTP 상태 매핑 검증 |
| 64 | MISSING_TEST | ProductGroupDescriptionErrorMapper | HTTP 상태 매핑 검증 |
| 65 | MISSING_TEST | ProductNoticeErrorMapper | HTTP 상태 매핑 검증 |
| 66 | MISSING_EDGE_CASE | ProductGroupTest | `discountRate()` — salePrice == currentPrice 경계 케이스 (세일 아님) 명시적 검증 |
| 67 | MISSING_TEST | ProductNoticeEntryIdTest | `ProductNoticeEntryIdTest.java` 미존재 (ID 클래스 단순 케이스) |

---

## 통계

| 항목 | 수치 |
|------|------|
| 총 갭 수 (전 레이어) | 67 |
| HIGH | 28 |
| MEDIUM | 32 |
| LOW | 7 |
| 소스 클래스 합계 (4개 레이어) | 145 |
| 테스트 존재 클래스 | 72 |
| 전체 커버리지 | 약 50% |
| 예상 보완 테스트 파일 수 | 약 50 |
| 예상 보완 테스트 메서드 수 | 약 180~220 |

---

## 분석 메모

### Domain Layer: 잘 된 점

Domain 레이어는 전반적으로 커버리지가 매우 높습니다. `ProductTest`, `ProductGroupTest`, `SellerOptionGroupTest` 모두 상태 전이 테스트(정상 + 예외), 생성/복원, accessor, 비즈니스 메서드가 체계적으로 작성되어 있습니다. `@Tag("unit")`, `@DisplayName` (한글), `@Nested` 그룹핑, testFixtures 활용도 모두 컨벤션에 부합합니다.

주요 갭은 `ProductGroup.replaceSellerOptionGroups()` 호출 시 `validateOptionStructure()` 내 `ProductGroupInvalidOptionStructureException`이 발생하는 케이스가 미테스트인 점입니다. 이 메서드는 SINGLE/COMBINATION/NONE 별로 그룹 수 제약이 다르므로 각 경우의 실패 케이스가 반드시 필요합니다.

### Application Layer: 중대한 공백

Application 레이어 product/productgroup 관련 Service, Manager, Factory, Assembler, Coordinator 클래스 전체가 테스트 미작성 상태입니다. 다른 도메인(brand, cart, seller 등)에는 패턴이 잘 갖춰진 것과 대조적으로, product/productgroup 은 DTO 테스트 일부(7개)만 존재합니다.

특히 `ProductGroupAssembler`는 커서 페이징 계산, Detail 조립, 옵션-상품 매트릭스 조립 등 분기 로직이 많고 복잡도가 높아 HIGH 우선순위입니다. `ProductGroupReadFacade`는 여러 포트를 조합하는 오케스트레이션 클래스로, not-found 예외 전파 등의 시나리오가 무조건 필요합니다.

### Adapter-Out Layer: 광범위한 공백

Adapter-Out 레이어는 composite 패키지(3개 테스트 존재)를 제외하면 product/productgroup 관련 Adapter/Mapper 전체가 테스트 미작성입니다. Mapper 클래스들은 Domain ↔ JpaEntity 변환 정합성을 보증하는 중요한 역할이므로 단위 테스트가 필요합니다. ConditionBuilder 클래스들은 QueryDSL 조건 조합 로직을 갖고 있어 null 조건 / 단일 조건 / 복합 조건 케이스 검증이 필요합니다. Adapter 클래스들은 통합 테스트(`integration-test` 모듈)로 커버 가능하지만, Repository 통합 테스트가 `ProductGroupCompositeQueryDslRepositoryTest` 1개만 존재하므로 product/productgroup 기본 Repository 통합 테스트 추가도 검토가 필요합니다.

### Adapter-In Layer: Query 쪽 공백

Command 계열 Controller와 Mapper는 대부분 테스트가 갖춰져 있지만, 신규 추가된 `ProductGroupQueryController`와 `ProductGroupQueryApiMapper`가 테스트 없이 배포된 상태입니다. `ProductGroupQueryApiMapper.toDetailResponse()`는 shippingPolicy/refundPolicy/description/notice가 모두 null일 수 있는 필드들의 처리 로직이 포함되어 있어 누락 시 NPE 리스크가 있습니다.

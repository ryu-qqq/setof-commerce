# Plan: Banner/Navigation Command 어댑터 구현

## 메타 정보
- **유형**: 기능 확장 (기존 Query 구조에 Command side 추가)
- **관련 도메인**: banner, navigation
- **영향 레이어**: Application(Command side 신규), Adapter-Out(Command Adapter 신규), Adapter-In(Admin REST API 신규)
- **Domain**: 변경 없음 (BannerGroup, BannerSlide, NavigationMenu 이미 완성)

---

## 비즈니스 규칙

### Banner (BannerGroup + BannerSlide)

| 규칙 ID | 내용 |
|--------|------|
| BR-BNR-001 | BannerGroup 등록: title, bannerType, displayPeriod, active, slides 목록 |
| BR-BNR-002 | BannerGroup 수정: diff 패턴으로 슬라이드 업데이트 (replaceSlides). UpdateContext 사용 |
| BR-BNR-003 | BannerGroup 상태 변경: changeDisplayStatus(active, now). StatusChangeContext 사용 |
| BR-BNR-004 | BannerGroup 삭제: remove(now) - 소프트 삭제. StatusChangeContext 사용 |
| BR-BNR-005 | 슬라이드 전체 교체: BannerGroup.replaceSlides() 호출. 기존 슬라이드 삭제 후 신규 저장 |
| BR-BNR-006 | BannerSlide는 BannerGroup에 종속. bannerGroupId를 통해 관리 |

### Navigation (NavigationMenu)

| 규칙 ID | 내용 |
|--------|------|
| BR-NAV-001 | NavigationMenu 등록: title, linkUrl, displayOrder, displayPeriod, active |
| BR-NAV-002 | NavigationMenu 수정: UpdateContext 사용. NavigationMenuUpdateData로 전달 |
| BR-NAV-003 | NavigationMenu 삭제: remove(now) - 소프트 삭제. StatusChangeContext 사용 |

---

## 영향도 분석

### 현재 구현 상태

| 레이어 | Banner | Navigation |
|--------|--------|-----------|
| Domain | 완료 (BannerGroup, BannerSlide, BannerGroupUpdateData) | 완료 (NavigationMenu, NavigationMenuUpdateData) |
| Application (Query) | 완료 (BannerSlideQueryUseCase, BannerSlideQueryManager, BannerSlideQueryService) | 완료 (NavigationMenuQueryUseCase, NavigationMenuQueryManager, NavigationMenuQueryService) |
| Application (Command) | 없음 - 신규 생성 필요 | 없음 - 신규 생성 필요 |
| Adapter-Out Query | 완료 (BannerSlideQueryAdapter, BannerSlideQueryDslRepository) | 완료 (NavigationMenuQueryAdapter, NavigationMenuQueryDslRepository) |
| Adapter-Out Command | 없음 - 신규 생성 필요 | 없음 - 신규 생성 필요 |
| Adapter-In Admin | 없음 - 신규 생성 필요 | 없음 - 신규 생성 필요 |

### 기존 파일 수정 없음

- BannerGroup, BannerSlide, NavigationMenu: 변경 없음
- BannerGroupUpdateData, NavigationMenuUpdateData: 변경 없음
- 기존 Query 어댑터 모두 변경 없음

### 신규 생성 파일 목록

**Application - Banner Command (11개)**
- `dto/command/RegisterBannerGroupCommand.java`
- `dto/command/RegisterBannerSlideCommand.java`
- `dto/command/UpdateBannerGroupCommand.java`
- `dto/command/UpdateBannerSlideCommand.java`
- `dto/command/ChangeBannerGroupStatusCommand.java`
- `dto/command/RemoveBannerGroupCommand.java`
- `factory/BannerGroupCommandFactory.java`
- `manager/BannerGroupCommandManager.java`
- `port/in/command/RegisterBannerGroupUseCase.java`
- `port/in/command/UpdateBannerGroupUseCase.java`
- `port/in/command/ChangeBannerGroupStatusUseCase.java`
- `port/in/command/RemoveBannerGroupUseCase.java`
- `port/out/command/BannerGroupCommandPort.java`
- `service/command/RegisterBannerGroupService.java`
- `service/command/UpdateBannerGroupService.java`
- `service/command/ChangeBannerGroupStatusService.java`
- `service/command/RemoveBannerGroupService.java`
- `validator/BannerGroupValidator.java`

**Application - Navigation Command (10개)**
- `dto/command/RegisterNavigationMenuCommand.java`
- `dto/command/UpdateNavigationMenuCommand.java`
- `dto/command/RemoveNavigationMenuCommand.java`
- `factory/NavigationMenuCommandFactory.java`
- `manager/NavigationMenuCommandManager.java`
- `port/in/command/RegisterNavigationMenuUseCase.java`
- `port/in/command/UpdateNavigationMenuUseCase.java`
- `port/in/command/RemoveNavigationMenuUseCase.java`
- `port/out/command/NavigationMenuCommandPort.java`
- `service/command/RegisterNavigationMenuService.java`
- `service/command/UpdateNavigationMenuService.java`
- `service/command/RemoveNavigationMenuService.java`
- `validator/NavigationMenuValidator.java`

**Adapter-Out - Banner Command**
- `adapter/BannerGroupCommandAdapter.java`
- `repository/BannerGroupQueryDslRepository.java` (findById 추가 필요)

**Adapter-Out - Navigation Command**
- `adapter/NavigationMenuCommandAdapter.java`
- `repository/NavigationMenuQueryDslRepository.java` (findById 추가 - 기존 파일 수정)

**Adapter-In - Admin REST API**
- `v2/banner/BannerGroupAdminEndpoints.java`
- `v2/banner/controller/BannerGroupCommandController.java`
- `v2/banner/dto/command/RegisterBannerGroupApiRequest.java`
- `v2/banner/dto/command/RegisterBannerSlideApiRequest.java`
- `v2/banner/dto/command/UpdateBannerGroupApiRequest.java`
- `v2/banner/dto/command/UpdateBannerSlideApiRequest.java`
- `v2/banner/dto/command/ChangeBannerGroupStatusApiRequest.java`
- `v2/banner/mapper/BannerGroupCommandApiMapper.java`
- `v2/navigation/NavigationMenuAdminEndpoints.java`
- `v2/navigation/controller/NavigationMenuCommandController.java`
- `v2/navigation/dto/command/RegisterNavigationMenuApiRequest.java`
- `v2/navigation/dto/command/UpdateNavigationMenuApiRequest.java`
- `v2/navigation/mapper/NavigationMenuCommandApiMapper.java`

---

## 구현 계획

### Task 분해 (Domain -> Application -> Adapter-Out -> Adapter-In 순서)

---

### TASK-001: Banner Application Command Layer 구현

**레이어**: Application
**유형**: 신규 생성
**예상 크기**: ~8K tokens
**완료 조건**: 모든 Command UseCase 구현, Factory/Manager/Service/Validator 완성

**생성 파일** (`application/src/main/java/com/ryuqq/setof/application/banner/`):

```
dto/command/
  RegisterBannerGroupCommand.java     # record: title, bannerType, displayStartAt, displayEndAt, active, slides
  RegisterBannerSlideCommand.java     # record: title, imageUrl, linkUrl, displayOrder, displayStartAt, displayEndAt, active
  UpdateBannerGroupCommand.java       # record: id, title, bannerType, displayStartAt, displayEndAt, active, slides
  UpdateBannerSlideCommand.java       # record: title, imageUrl, linkUrl, displayOrder, displayStartAt, displayEndAt, active
  ChangeBannerGroupStatusCommand.java # record: id, active
  RemoveBannerGroupCommand.java       # record: id

factory/
  BannerGroupCommandFactory.java
    - create(RegisterBannerGroupCommand) -> BannerGroup  [BannerSlide.forNew() 포함]
    - createUpdateContext(UpdateBannerGroupCommand) -> UpdateContext<BannerGroupId, BannerGroupUpdateData>
    - createStatusChangeContext(ChangeBannerGroupStatusCommand) -> StatusChangeContext<BannerGroupId>
    - createRemoveContext(RemoveBannerGroupCommand) -> StatusChangeContext<BannerGroupId>

manager/
  BannerGroupCommandManager.java
    - @Transactional persist(BannerGroup) -> Long
    - @Transactional persistWithSlides(BannerGroup) -> Long  [BannerGroup + slides 저장]

port/in/command/
  RegisterBannerGroupUseCase.java     # Long execute(RegisterBannerGroupCommand)
  UpdateBannerGroupUseCase.java       # void execute(UpdateBannerGroupCommand)
  ChangeBannerGroupStatusUseCase.java # void execute(ChangeBannerGroupStatusCommand)
  RemoveBannerGroupUseCase.java       # void execute(RemoveBannerGroupCommand)

port/out/command/
  BannerGroupCommandPort.java
    - Long persist(BannerGroup)
    - void persistSlides(long bannerGroupId, List<BannerSlide>)
    - void deleteSlidesByGroupId(long bannerGroupId)

service/command/
  RegisterBannerGroupService.java
    - factory.create() -> BannerGroup
    - commandManager.persistWithSlides(bannerGroup)

  UpdateBannerGroupService.java  [diff 패턴]
    - factory.createUpdateContext() -> UpdateContext
    - validator.findExistingOrThrow(context.id())
    - bannerGroup.update(context.updateData(), context.changedAt())
    - commandManager.persistWithSlides(bannerGroup)  [slides 교체: delete old + insert new]

  ChangeBannerGroupStatusService.java
    - factory.createStatusChangeContext() -> StatusChangeContext
    - validator.findExistingOrThrow(context.id())
    - bannerGroup.changeDisplayStatus(command.active(), context.changedAt())
    - commandManager.persist(bannerGroup)

  RemoveBannerGroupService.java
    - factory.createRemoveContext() -> StatusChangeContext
    - validator.findExistingOrThrow(context.id())
    - bannerGroup.remove(context.changedAt())
    - commandManager.persist(bannerGroup)

validator/
  BannerGroupValidator.java
    - BannerGroup findExistingOrThrow(BannerGroupId id)  [APP-VAL-001]
```

**주요 설계 포인트**:
- `BannerGroupCommandPort.persistSlides()` + `deleteSlidesByGroupId()`: Update 시 기존 슬라이드 전부 삭제 후 신규 저장 (diff 패턴의 persistence 구현)
- `BannerGroup.update(BannerGroupUpdateData)` 내부에서 `replaceSlides()`가 호출되어 도메인 상태는 교체. Adapter-Out이 실제 DB 교체 수행
- Factory에서 `BannerSlide.forNew()` 호출로 슬라이드 도메인 객체 생성

---

### TASK-002: Navigation Application Command Layer 구현

**레이어**: Application
**유형**: 신규 생성
**예상 크기**: ~5K tokens
**완료 조건**: 모든 Command UseCase 구현 완성

**생성 파일** (`application/src/main/java/com/ryuqq/setof/application/navigation/`):

```
dto/command/
  RegisterNavigationMenuCommand.java  # record: title, linkUrl, displayOrder, displayStartAt, displayEndAt, active
  UpdateNavigationMenuCommand.java    # record: id, title, linkUrl, displayOrder, displayStartAt, displayEndAt, active
  RemoveNavigationMenuCommand.java    # record: id

factory/
  NavigationMenuCommandFactory.java
    - create(RegisterNavigationMenuCommand) -> NavigationMenu
    - createUpdateContext(UpdateNavigationMenuCommand) -> UpdateContext<NavigationMenuId, NavigationMenuUpdateData>
    - createRemoveContext(RemoveNavigationMenuCommand) -> StatusChangeContext<NavigationMenuId>

manager/
  NavigationMenuCommandManager.java
    - @Transactional persist(NavigationMenu) -> Long
    - @Transactional persistAll(List<NavigationMenu>)

port/in/command/
  RegisterNavigationMenuUseCase.java  # Long execute(RegisterNavigationMenuCommand)
  UpdateNavigationMenuUseCase.java    # void execute(UpdateNavigationMenuCommand)
  RemoveNavigationMenuUseCase.java    # void execute(RemoveNavigationMenuCommand)

port/out/command/
  NavigationMenuCommandPort.java
    - Long persist(NavigationMenu)

service/command/
  RegisterNavigationMenuService.java
  UpdateNavigationMenuService.java
  RemoveNavigationMenuService.java

validator/
  NavigationMenuValidator.java
    - NavigationMenu findExistingOrThrow(NavigationMenuId id)
```

---

### TASK-003: Banner Adapter-Out Command 구현

**레이어**: Adapter-Out (persistence-mysql)
**유형**: 신규 생성 + 기존 파일 수정
**예상 크기**: ~5K tokens
**완료 조건**: BannerGroupCommandAdapter 구현 완료, QueryDslRepository에 findById 추가

**생성 파일** (`adapter-out/persistence-mysql/.../banner/`):

```
adapter/
  BannerGroupCommandAdapter.java  [BannerGroupCommandPort 구현]
    - Long persist(BannerGroup): BannerGroupJpaEntity 저장 (mapper.toGroupEntity)
    - void persistSlides(long bannerGroupId, List<BannerSlide>): saveAll
    - void deleteSlidesByGroupId(long bannerGroupId): BannerSlideJpaRepository.deleteByBannerGroupId()
```

**수정 파일**:
```
repository/
  BannerGroupJpaRepository.java  [기존 수정]
    + void deleteByBannerGroupId(long bannerGroupId) 추가 (JPA derived delete)

  BannerSlideJpaRepository.java  [기존 수정]
    + void deleteAllByBannerGroupId(long bannerGroupId) 추가
```

**BannerGroupJpaEntityMapper 추가** (`mapper/`):
```
mapper/
  BannerGroupJpaEntityMapper.java  [신규 생성]
    - BannerGroupJpaEntity toEntity(BannerGroup): BannerGroup -> BannerGroupJpaEntity
    - BannerGroup toDomain(BannerGroupJpaEntity): BannerGroupJpaEntity -> BannerGroup (slides 없이)
```

**QueryDslRepository 수정** (기존 파일에 findById 추가):
```
repository/
  BannerSlideQueryDslRepository.java  [기존 수정]
    + List<BannerSlideJpaEntity> findByGroupId(long bannerGroupId) 추가
    + Optional<BannerGroupJpaEntity> findBannerGroupById(Long id) 추가
```

**BannerGroupValidator용 QueryPort 추가**:
```
application/banner/port/out/query/
  BannerGroupQueryPort.java  [신규]
    - Optional<BannerGroup> findById(BannerGroupId id)
```

---

### TASK-004: Navigation Adapter-Out Command 구현

**레이어**: Adapter-Out (persistence-mysql)
**유형**: 신규 생성 + 기존 파일 수정
**예상 크기**: ~3K tokens
**완료 조건**: NavigationMenuCommandAdapter 구현 완료

**생성 파일** (`adapter-out/persistence-mysql/.../navigation/`):

```
adapter/
  NavigationMenuCommandAdapter.java  [NavigationMenuCommandPort 구현]
    - Long persist(NavigationMenu): NavigationMenuJpaEntity 저장

mapper/
  NavigationMenuJpaEntityMapper.java  [기존 파일에 toEntity 이미 있음 - 확인 필요]
```

**QueryDslRepository 수정**:
```
repository/
  NavigationMenuQueryDslRepository.java  [기존 수정]
    + Optional<NavigationMenuJpaEntity> findById(Long id) 추가
```

**NavigationMenuQueryPort 수정**:
```
application/navigation/port/out/
  NavigationMenuQueryPort.java  [기존 수정]
    + Optional<NavigationMenu> findById(NavigationMenuId id) 추가
```

---

### TASK-005: Banner Admin REST API Adapter-In 구현

**레이어**: Adapter-In (rest-api-admin v2)
**유형**: 신규 생성
**예상 크기**: ~8K tokens
**완료 조건**: BannerGroupCommandController 구현, 모든 API 동작 확인

**생성 파일** (`adapter-in/rest-api-admin/.../v2/banner/`):

```
BannerGroupAdminEndpoints.java
  - BASE: /api/v2/admin/banner-groups
  - ID: /{bannerGroupId}
  - ACTIVE_STATUS: /active-status
  - REMOVE: /remove

controller/
  BannerGroupCommandController.java
    - POST /api/v2/admin/banner-groups -> register (201)
    - PUT  /api/v2/admin/banner-groups/{id} -> update (200)  [diff 패턴 - slides 전체 교체]
    - PATCH /api/v2/admin/banner-groups/{id}/active-status -> changeStatus (200)
    - PATCH /api/v2/admin/banner-groups/{id}/remove -> remove (200)

dto/command/
  RegisterBannerGroupApiRequest.java
    - @NotBlank String title
    - @NotBlank String bannerType
    - @NotNull Instant displayStartAt
    - @NotNull Instant displayEndAt
    - boolean active
    - @NotNull @Valid List<RegisterBannerSlideApiRequest> slides

  RegisterBannerSlideApiRequest.java
    - @NotBlank String title
    - @NotBlank String imageUrl
    - @NotBlank String linkUrl
    - int displayOrder
    - @NotNull Instant displayStartAt
    - @NotNull Instant displayEndAt
    - boolean active

  UpdateBannerGroupApiRequest.java  [ID는 PathVariable로]
    - @NotBlank String title
    - @NotBlank String bannerType
    - @NotNull Instant displayStartAt
    - @NotNull Instant displayEndAt
    - boolean active
    - @NotNull @Valid List<UpdateBannerSlideApiRequest> slides

  UpdateBannerSlideApiRequest.java
    - @NotBlank String title
    - @NotBlank String imageUrl
    - @NotBlank String linkUrl
    - int displayOrder
    - @NotNull Instant displayStartAt
    - @NotNull Instant displayEndAt
    - boolean active

  ChangeBannerGroupStatusApiRequest.java
    - boolean active

mapper/
  BannerGroupCommandApiMapper.java
    - RegisterBannerGroupCommand toCommand(RegisterBannerGroupApiRequest)
    - UpdateBannerGroupCommand toCommand(Long id, UpdateBannerGroupApiRequest)
    - ChangeBannerGroupStatusCommand toCommand(Long id, ChangeBannerGroupStatusApiRequest)
    - RemoveBannerGroupCommand toCommand(Long id)
```

---

### TASK-006: Navigation Admin REST API Adapter-In 구현

**레이어**: Adapter-In (rest-api-admin v2)
**유형**: 신규 생성
**예상 크기**: ~5K tokens
**완료 조건**: NavigationMenuCommandController 구현, 모든 API 동작 확인

**생성 파일** (`adapter-in/rest-api-admin/.../v2/navigation/`):

```
NavigationMenuAdminEndpoints.java
  - BASE: /api/v2/admin/navigation-menus
  - ID: /{navigationMenuId}
  - REMOVE: /remove

controller/
  NavigationMenuCommandController.java
    - POST /api/v2/admin/navigation-menus -> register (201)
    - PUT  /api/v2/admin/navigation-menus/{id} -> update (200)
    - PATCH /api/v2/admin/navigation-menus/{id}/remove -> remove (200)

dto/command/
  RegisterNavigationMenuApiRequest.java
    - @NotBlank String title
    - @NotBlank String linkUrl
    - int displayOrder
    - @NotNull Instant displayStartAt
    - @NotNull Instant displayEndAt
    - boolean active

  UpdateNavigationMenuApiRequest.java  [ID는 PathVariable로]
    - @NotBlank String title
    - @NotBlank String linkUrl
    - int displayOrder
    - @NotNull Instant displayStartAt
    - @NotNull Instant displayEndAt
    - boolean active

mapper/
  NavigationMenuCommandApiMapper.java
    - RegisterNavigationMenuCommand toCommand(RegisterNavigationMenuApiRequest)
    - UpdateNavigationMenuCommand toCommand(Long id, UpdateNavigationMenuApiRequest)
    - RemoveNavigationMenuCommand toCommand(Long id)
```

---

## 의존성 그래프

```
TASK-001 (Banner Application)
    |
    v
TASK-003 (Banner Adapter-Out)   <-- TASK-001의 Port 구현
    |
    v
TASK-005 (Banner Adapter-In)    <-- TASK-001의 UseCase 사용

TASK-002 (Navigation Application)
    |
    v
TASK-004 (Navigation Adapter-Out)  <-- TASK-002의 Port 구현
    |
    v
TASK-006 (Navigation Adapter-In)   <-- TASK-002의 UseCase 사용
```

Banner 작업 (TASK-001 -> 003 -> 005)과 Navigation 작업 (TASK-002 -> 004 -> 006)은 **병렬 진행 가능**.

---

## 컨벤션 체크리스트

- [x] Domain: 변경 없음 (이미 완성)
- [ ] Application Factory: TimeProvider.now()는 Factory에서만 호출 (APP-TIM-001)
- [ ] Application Factory: createUpdateContext()로 ID+UpdateData+changedAt 한 번에 (FAC-008)
- [ ] Application Service: TimeProvider 직접 사용 금지
- [ ] Application Validator: findExistingOrThrow() 패턴 (APP-VAL-001)
- [ ] Adapter-Out CommandAdapter: @Transactional 금지 (PER-ADP-002)
- [ ] Adapter-Out CommandAdapter: JpaRepository만 사용 (PER-ADP-001)
- [ ] Adapter-Out Mapper: toEntity() + toDomain() 제공 (PER-MAP-002)
- [ ] Adapter-In Controller: @Valid 필수 (API-CTR-009)
- [ ] Adapter-In Controller: CQRS Controller 분리 (API-CTR-010)
- [ ] Adapter-In Controller: DELETE 메서드 금지, 소프트 삭제는 PATCH (API-CTR-002)
- [ ] Adapter-In Endpoints: final class, static final 상수 (API-END-001, API-END-002)
- [ ] Adapter-In Mapper: @Component (API-MAP-001)

## 진행 상태

- [ ] TASK-001: Banner Application Command Layer
- [ ] TASK-002: Navigation Application Command Layer
- [ ] TASK-003: Banner Adapter-Out Command
- [ ] TASK-004: Navigation Adapter-Out Command
- [ ] TASK-005: Banner Admin REST API Adapter-In
- [ ] TASK-006: Navigation Admin REST API Adapter-In

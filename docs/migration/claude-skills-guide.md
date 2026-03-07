# Claude Skills Guide - Legacy Migration

레거시 마이그레이션에 사용하는 Claude 스킬/에이전트 사용법 가이드.

---

## 전체 파이프라인

```
/legacy-endpoints       엔드포인트 분석 (Query/Command 분류)
       |
/legacy-flow            API 흐름 분석 (Controller -> Service -> Repository -> DB)
       |
       +-- /legacy-convert      DTO 변환 (Request/Response record)
       |
       +-- /legacy-query        Persistence Layer (Entity, Repository, Adapter)
       |
       +-- /shadow-test         Shadow Traffic 테스트 케이스 YAML
       |
/legacy-service         Application Layer (Port, Service, Manager, Assembler)
       |
/legacy-controller      Controller + ApiMapper
       |
/shadow-verify          로컬 Docker 검증 (Legacy vs New 응답 비교)
```

`/legacy-migrate`를 사용하면 위 전체 파이프라인을 자동 실행합니다 (Step 3,4,7 병렬).

---

## 개별 스킬 사용법

### 1. /legacy-endpoints - 엔드포인트 분석

레거시 모듈의 컨트롤러를 스캔하여 Query/Command로 분류합니다.

```bash
/legacy-endpoints web:user        # Web API user 모듈
/legacy-endpoints admin:brand     # Admin API brand 모듈
```

- 출력: `claudedocs/legacy-endpoints/{prefix}/{module}_endpoints.md`
- 마이그레이션 계획 수립의 첫 단계

---

### 2. /legacy-flow - API 흐름 분석

특정 엔드포인트의 전체 호출 체인을 추적합니다.

```bash
/legacy-flow web:UserController.fetchAddressBook
/legacy-flow admin:BrandController.fetchBrands
```

- 출력: `claudedocs/legacy-flows/{prefix}/{Controller}_{method}.md`
- Controller -> Service -> Repository -> DB 테이블까지 추적
- 이후 단계(DTO, Persistence, Application, Controller)의 입력으로 사용

---

### 3. /legacy-convert - DTO 변환

legacy-flow 분석 결과를 기반으로 record 기반 Request/Response DTO를 생성합니다.

```bash
/legacy-convert web:UserController.fetchAddressBook
/legacy-convert admin:BrandController.fetchBrands
```

- 출력: `claudedocs/legacy-converts/{prefix}/{Controller}_{method}/request/`, `response/`
- MarketPlace의 `SearchSellersApiRequest`, `SellerApiResponse` 패턴을 따름
- @Schema, @Parameter 어노테이션 포함

---

### 4. /legacy-query - Persistence Layer 생성

legacy-flow 분석 결과를 기반으로 Entity, QueryDSL Repository, Adapter를 생성합니다.

```bash
/legacy-query web:UserController.fetchAddressBook
/legacy-query admin:BrandController.fetchBrands
```

- 출력: `adapter-out/persistence-mysql-legacy/` 하위
- 생성 파일: Entity, ConditionBuilder, QueryDto, Repository, Mapper, Adapter
- `@Qualifier("legacyJpaQueryFactory")`, `Projections.constructor()` 패턴 적용

---

### 5. /legacy-service - Application Layer 생성

Persistence Layer를 기반으로 UseCase, Service, Manager, Assembler를 생성합니다.

```bash
/legacy-service web:UserController.fetchAddressBook
/legacy-service admin:BrandController.fetchBrands
```

- 출력: `application/src/main/java/.../legacy/{prefix}/{domain}/`
- 생성 파일: UseCase(Port), QueryPort, Service, Manager, Assembler, CompositeResult, QueryCommand
- Service는 Manager + Assembler에 의존 (Port 직접 호출 금지)
- Manager에 `@Transactional(readOnly=true)` 메서드 레벨 적용

---

### 6. /legacy-controller - Controller + ApiMapper 생성

DTO와 UseCase를 기반으로 Controller와 ApiMapper를 생성합니다.

```bash
/legacy-controller web:UserController.fetchAddressBook
/legacy-controller admin:BrandController.fetchBrands
```

- 출력: `adapter-in/rest-api-{web|admin}/` 하위
- 생성 파일: Controller, ApiMapper, Request DTO (복사), Response DTO (복사)
- Controller는 UseCase + ApiMapper만 의존
- `ResponseEntity<ApiResponse<T>>` 리턴, Swagger 어노테이션 필수

---

### 7. /shadow-test - Shadow Traffic 테스트 케이스 생성

legacy-flow 분석 결과를 기반으로 테스트 케이스 YAML을 자동 생성합니다.

```bash
/shadow-test web:UserController.fetchAddressBook
/shadow-test admin:BrandController.fetchBrands
```

- 출력: `tools/shadow-traffic/test-cases/{domain}.yml`
- 시나리오: 정상 조회, 404, 인증 실패, 경계값, Command 등
- compare_mode: GET=full, POST/PUT=structure_only, DELETE=status_only

---

### 8. /shadow-verify - 로컬 Docker 검증

Legacy + New 서버를 동시에 Docker로 띄워 실제 응답을 비교합니다.

```bash
/shadow-verify shipping-address
/shadow-verify brand
```

실행 순서:
1. SSM 포트포워딩 확인 (Stage RDS, port 13308)
2. Docker 빌드 + 실행 (legacy: 48082, new: 48080)
3. 헬스체크
4. Shadow Traffic 실행 (`--dry-run` 모드)
5. 결과 분석 + 리포트

사전 조건:
- `aws ssm start-session` 으로 Stage RDS 포트포워딩 활성화
- Docker Desktop 실행 중

---

### 9. /legacy-migrate - 전체 마이그레이션 (자동 오케스트레이션)

위 모든 스킬을 자동으로 순서대로 실행합니다.

```bash
/legacy-migrate web:user                # Web API user 모듈 전체
/legacy-migrate admin:brand             # Admin API brand 모듈 전체
/legacy-migrate web:user --step 3       # 3단계부터 재개
/legacy-migrate web:user --verify-only  # 로컬 검증만
```

실행 흐름 (7단계):
```
Step 1: 엔드포인트 분석 (순차)
Step 2: API 흐름 분석 (순차)
Step 3: DTO 변환 ────────────┐
Step 4: Persistence Layer ───┼── 병렬 실행
Step 7: Shadow Test 케이스 ──┘
Step 5: Application Layer (Step 4 완료 대기)
Step 6: Controller (Step 3+5 완료 대기)
(선택) Shadow Verify
```

UserController처럼 여러 도메인이 섞인 경우 자동으로 도메인별 분리 처리:
```bash
/legacy-migrate web:user
# -> shipping-address -> refund-account -> wishlist -> mypage 순차 마이그레이션
```

---

## --ref 옵션 (MarketPlace 레퍼런스 도메인)

모든 코드 생성 스킬에서 `--ref {domain}` 옵션으로 MarketPlace 레퍼런스 도메인을 변경할 수 있습니다.

```bash
# 기본: seller 도메인의 코드를 읽고 패턴 학습
/legacy-query web:UserController.fetchAddressBook

# product 도메인의 코드를 레퍼런스로 사용
/legacy-query web:UserController.fetchAddressBook --ref product

# brand 도메인의 코드를 레퍼런스로 사용
/legacy-convert admin:BrandController.fetchBrands --ref brand

# /legacy-migrate에서도 사용 가능 (하위 에이전트 전체에 전달)
/legacy-migrate web:user --ref product
```

에이전트가 Phase 0에서 MarketPlace의 해당 도메인 디렉토리를 Glob으로 탐색하여 Controller, Service, Repository, DTO 등의 실제 코드를 읽고 동일한 패턴으로 생성합니다.

사용 가능한 MarketPlace 도메인: `seller`(기본), `brand`, `product`, `productgroup`, `category`, `order`, `notice` 등

---

## 접두사 규칙

모든 스킬에서 `web:` 또는 `admin:` 접두사로 대상 모듈을 구분합니다.

| 접두사 | 대상 | Controller 경로 | Adapter-In 경로 |
|--------|------|----------------|-----------------|
| `web:` (기본) | Web API | `bootstrap-legacy-web-api` | `rest-api-web` |
| `admin:` | Admin API | `bootstrap-legacy-admin-api` | `rest-api-admin` |

---

## 마이그레이션 완료 후 다음 단계

```bash
# 1. 로컬 검증
/shadow-verify {domain}

# 2. 테스트 작성
/test-repository    # Persistence Layer 테스트
/test-application   # Application Layer 테스트
/test-api           # Controller + RestDocs 테스트

# 3. 코드 리뷰
/review

# 4. Stage 배포 후 Shadow Traffic 자동 검증 (EventBridge 5분 주기)

# 5. Gateway 라우팅 전환
```

---

## 관련 문서

- [Strangler Fig Migration Plan](./strangler-fig-migration-plan.md)
- [Shadow Traffic Test Cases](../../tools/shadow-traffic/test-cases/)
- [Docker Compose (AWS)](../../local-dev/docker-compose.aws.yml)

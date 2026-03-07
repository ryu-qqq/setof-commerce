---
name: legacy-migrate
description: 레거시 API 전체 마이그레이션 워크플로우. 7단계 병렬 파이프라인 + 로컬 검증.
allowed-tools: Agent, Bash, Read, Glob, Grep
---

# /legacy-migrate

레거시 API를 새 아키텍처로 **완전 마이그레이션**하는 자동화 워크플로우입니다.
**Agent 도구 기반 병렬 실행**으로 최적화되어 있습니다.

## 사용법

```bash
/legacy-migrate web:user                      # Web API user 모듈 전체 마이그레이션
/legacy-migrate web:cart                      # Web API cart 모듈 전체 마이그레이션
/legacy-migrate admin:brand                   # Admin API brand 모듈 전체 마이그레이션
/legacy-migrate web:user --step 3             # 3단계부터 재개
/legacy-migrate web:user --verify-only        # 로컬 검증만 실행
/legacy-migrate web:user --ref product        # MarketPlace product 도메인을 레퍼런스로 사용
```

## 입력

- `$ARGUMENTS[0]`: 대상 모듈 (예: `web:user`, `admin:brand`)
- `$ARGUMENTS[1]`: (선택) `--step N` 또는 `--verify-only` 또는 `--ref {domain}`
- `--ref`: MarketPlace 레퍼런스 도메인 지정 (기본: `seller`). 하위 에이전트의 Phase 0에서 해당 도메인의 코드를 읽고 패턴을 학습함

---

## 워크플로우 (7단계, 병렬 최적화)

```
Step 1: 엔드포인트 분석 (순차)
  |
Step 2: API 흐름 분석 (엔드포인트별 병렬 가능)
  |
  +-- Step 3: DTO 변환 ────────────────┐
  |                                     |
  +-- Step 4: Persistence Layer ──┐     |
  |                               |     |
  +-- Step 7: Shadow Test 케이스   |     |   <-- 병렬 실행 구간
                                  |     |
                            Step 5: Application
                                  |     |
                                  +-----+
                                  |
                            Step 6: Controller
                                  |
                            Shadow Verify (선택)
```

---

### Step 1: 엔드포인트 분석 (순차)

```python
Agent(
    subagent_type="legacy-endpoints-analyzer",
    prompt=f"{prefix}:{module} 모듈의 엔드포인트를 분석하여 Query/Command로 분류해주세요.",
    description="엔드포인트 분석"
)
```

출력: `claudedocs/legacy-endpoints/{prefix}/{module}_endpoints.md`

결과에서 **Query 엔드포인트 목록**을 추출하여 Step 2에 전달.

---

### Step 2: API 흐름 분석 (엔드포인트별 순차)

Step 1에서 추출한 각 엔드포인트에 대해:

```python
for endpoint in query_endpoints:
    Agent(
        subagent_type="legacy-flow-analyzer",
        prompt=f"{prefix}:{endpoint} 엔드포인트의 전체 호출 흐름을 분석해주세요.",
        description=f"흐름 분석: {endpoint}"
    )
```

출력: `claudedocs/legacy-flows/{prefix}/{Controller}_{method}.md`

---

### Step 3, 4, 7: 병렬 실행 구간

Step 2 완료 후 **3개를 동시에** Agent로 실행:

```python
# 동시에 3개 Agent 실행
Agent(
    subagent_type="legacy-dto-converter",
    prompt=f"{prefix}:{Controller}.{method}의 legacy-flow 분석 결과를 기반으로 record 기반 Request/Response DTO를 생성해주세요.",
    description="DTO 변환",
    run_in_background=True
)

Agent(
    subagent_type="legacy-query-generator",
    prompt=f"{prefix}:{Controller}.{method}의 legacy-flow 분석 결과를 기반으로 Persistence Layer (Entity, Repository, Adapter)를 생성해주세요.",
    description="Persistence 생성",
    run_in_background=True
)

Agent(
    subagent_type="shadow-test-generator",
    prompt=f"{prefix}:{Controller}.{method}의 legacy-flow 분석 결과를 기반으로 Shadow Traffic 테스트 케이스 YAML을 생성해주세요.",
    description="Shadow 테스트 생성",
    run_in_background=True
)
```

---

### Step 5: Application Layer 생성 (Step 4 완료 대기)

Step 4 (Persistence Layer) 완료 후:

```python
Agent(
    subagent_type="legacy-service-generator",
    prompt=f"{prefix}:{Controller}.{method}의 Persistence Layer를 기반으로 Application Layer (Port, Service, Manager, Assembler)를 생성해주세요.",
    description="Application Layer 생성"
)
```

---

### Step 6: Controller 생성 (Step 3 + 5 완료 대기)

Step 3 (DTO) + Step 5 (Application) 모두 완료 후:

```python
Agent(
    subagent_type="legacy-controller-generator",
    prompt=f"{prefix}:{Controller}.{method}의 DTO와 UseCase를 기반으로 Controller + ApiMapper를 생성해주세요.",
    description="Controller 생성"
)
```

---

### (선택) Shadow Verify: 로컬 검증

모든 Step 완료 후 사용자에게 확인:

```
마이그레이션 코드 생성이 완료되었습니다.
로컬 Docker 환경에서 Shadow Traffic 검증을 실행할까요? [Y/n]
```

Y인 경우 `/shadow-verify {domain}` 실행.

---

## 실행 지침

1. **의존성 순서 엄수**: Step 1 → 2 → (3,4,7 병렬) → 5 → 6
2. **병렬 실행**: Step 3, 4, 7은 반드시 동시에 Agent 호출 (run_in_background=True)
3. **에러 처리**: 실패 시 해당 단계에서 중단하고 사용자에게 보고
4. **진행 상황 보고**: 각 단계 완료 시 결과 요약 출력

## 진행 상황 보고 형식

```
Step 1/7: 엔드포인트 분석 완료
   - Query: 5개, Command: 3개
   - 출력: claudedocs/legacy-endpoints/web/user_endpoints.md

Step 2/7: API 흐름 분석 완료
   - 분석 완료: UserController.fetchAddressBook, fetchRefundAccount, ...

Step 3,4,7/7: 병렬 실행 중...
   - DTO 변환: 진행 중
   - Persistence: 진행 중
   - Shadow Test: 진행 중

Step 3/7: DTO 변환 완료
   - Request: 2개, Response: 2개

Step 4/7: Persistence Layer 완료
   - Entity: 1개, Repository: 1개, Adapter: 1개

Step 7/7: Shadow Test 케이스 완료
   - 테스트 케이스: 8개 (shipping-address.yml)

Step 5/7: Application Layer 완료
   - UseCase: 1개, Service: 1개, Manager: 1개

Step 6/7: Controller 완료
   - Controller: 1개, ApiMapper: 1개
```

## 마이그레이션 완료 후

```
마이그레이션 완료: web:user (shipping-address)

생성된 파일:
- DTO: 2개 (Request + Response)
- Persistence: 5개 (Entity, ConditionBuilder, QueryDto, Repository, Mapper, Adapter)
- Application: 6개 (UseCase, Port, Service, Manager, Assembler, CompositeResult)
- Controller: 2개 (Controller, ApiMapper)
- Shadow Test: 1개 (shipping-address.yml, 8 케이스)

다음 단계:
- 로컬 검증: /shadow-verify shipping-address
- 테스트 작성: /test-repository, /test-api
- 코드 리뷰: /review
```

---

## 복수 엔드포인트 처리

UserController처럼 여러 도메인이 섞여있는 경우:
1. Step 1에서 도메인별로 엔드포인트를 그룹핑
2. 각 도메인 그룹에 대해 Step 2~6를 반복
3. 도메인 순서: shipping-address → refund-account → wishlist → mypage

```bash
# 자동으로 도메인별로 분리하여 순차 마이그레이션
/legacy-migrate web:user
# → shipping-address 마이그레이션 완료
# → refund-account 마이그레이션 완료
# → wishlist 마이그레이션 완료
# → mypage 마이그레이션 완료
```

# Spring Standards Project - Claude Code Configuration

이 프로젝트는 **Spring Boot 3.5.x + Java 21** 기반의 헥사고날 아키텍처 엔터프라이즈 표준 프로젝트입니다.

---

## 🚀 핵심 개발 워크플로우

### Epic 중심 워크플로우

```
📋 Epic = 1 Branch = 1 PR

/epic "결제 기능"
        ↓
/jira-create                    # Epic + Tasks 등록 (본인 자동 할당)
        ↓
/work EPIC-123                  # Epic 브랜치 생성
        ↓                         feature/EPIC-123-payment
┌───────────────────────────────────────────────────┐
│  Task 1 작업 → 커밋                               │
│       ↓                                           │
│  /next                        # 다음 Task로 이동  │
│       ↓                                           │
│  Task 2 작업 → 커밋                               │
│       ↓                                           │
│  /next → ... → Task N 완료                        │
└───────────────────────────────────────────────────┘
        ↓
/review → /check → /ship        # Epic 전체 → 1 PR
```

### 전체 워크플로우

```bash
# 1️⃣ 기획 단계
/epic "결제 기능"              # Epic 문서 작성 + Task 분해
/jira-create                   # Jira에 Epic/Task 등록 (본인 자동 할당)

# 2️⃣ Epic 작업 시작
/work EPIC-123                 # Epic 브랜치 생성 + Task 큐 로드

# 3️⃣ Task 순차 진행
# Task 1 작업...
/next                          # Task 1 완료 → Task 2 시작
# Task 2 작업...
/next                          # Task 2 완료 → Task 3 시작
# ... 모든 Task 완료

# 4️⃣ 품질 검증
/review                        # Claude 직접 리뷰 (Knowledge Base 401개 규칙)
/check                         # 정적 분석 + ArchUnit 테스트

# 5️⃣ Epic 배포
/ship                          # 모든 커밋 Squash → 1 PR → Jira Epic 완료
```

---

## 📋 Phase 1: Epic/Task 관리

### /epic - Epic 기획

```bash
/epic "결제 기능 구현"
```

**수행 작업**:
1. 작업 유형 분류 (🆕신규/➕확장/🔄리팩토링/🐛버그/🔌연동)
2. 유형별 맞춤 분석 수행
3. Task 분해 (컨텍스트 크기 ~15K tokens 기준)
4. Serena Memory에 `epic-{feature}` 저장

### /jira-create - Jira 등록 (메타데이터 포함)

```bash
/jira-create                  # 본인에게 자동 할당
/jira-create --assignee user  # 다른 담당자 지정
/jira-create --dry-run        # 미리보기
```

**수행 작업**:
- **본인 자동 할당**: `atlassianUserInfo()`로 조회 후 Epic/Task에 할당
- Epic memory 기반 Jira Epic/Sub-task 생성
- **Components**: 레이어 기반 자동 추출
- **Story Points**: 복잡도 기반 계산
- **Priority**: 비즈니스 영향도 기반
- **Labels**: 작업 유형 + 도메인
- **브랜치명**: `feature/EPIC-{key}-{desc}`

### /jira-fetch - 작업 가져오기

```bash
/jira-fetch EPIC-123
```

**수행 작업**:
- Jira에서 Epic + Sub-tasks 정보 조회
- Serena Memory에 `jira-epic-{key}` 저장
- Task 큐 구성

### /resume - 작업 재개

```bash
/resume
```

**수행 작업**:
- Serena Memory에서 진행 중인 Epic 검색
- Git 상태 복구
- Task 큐 복원

---

## 🔧 Phase 2: 작업 수행

### /work - Epic 작업 시작

```bash
/work EPIC-123              # Epic으로 작업 시작 (권장)
```

**수행 작업**:
1. **Epic 브랜치 생성** (`feature/EPIC-123-payment`)
2. Task 큐 구성 (순서대로)
3. Auto-commit 활성화 (15분 간격)
4. 첫 번째 Task 시작

**Task 큐 표시**:
```
📋 Task 큐:
   🔄 1/4 TASK-124 [Domain] PaymentAggregate  ◀ 현재
   ⬜ 2/4 TASK-125 [Application] UseCase
   ⬜ 3/4 TASK-126 [Persistence] Entity
   ⬜ 4/4 TASK-127 [REST API] Controller
```

### /next - 다음 Task로 이동 ⭐ NEW

```bash
/next                   # 현재 Task 완료 → 다음 Task 시작
/next --skip            # 현재 Task 스킵
/next --status          # 진행 상황 확인
```

**수행 작업**:
1. 현재 Task 커밋 생성
2. Jira Task 상태 → Done
3. Task 큐 업데이트
4. 다음 Task 컨텍스트 로드

### /plan - 상세 기획

```bash
/plan "주문 취소 기능"
```

**수행 작업**:
1. 요구사항 분석 (requirements-analyst)
2. 영향도 분석 (Serena MCP 검색)
3. 구현 전략 결정 (신규 vs 수정)

---

## 📦 Phase 3: 품질 검증 & 배포

### /review - 코드 리뷰 (Claude 직접)

```bash
/review                     # 현재 변경사항 전체 리뷰
/review --staged            # staged 파일만 리뷰
/review --fix               # 자동 수정 가능 항목 즉시 수정
```

**수행 작업**:
1. 변경된 파일 수집 및 레이어별 분류
2. **Zero-Tolerance 규칙 체크** (212개 필수)
3. Layer별 규칙 체크 (401개)
4. 코드 품질 분석
5. 🔴필수 / 🟡권장 / 🟢통과 분류

### /check - 정적 분석

```bash
/check
```

**수행 작업**:
1. `./gradlew spotlessCheck` - 포맷팅
2. `./gradlew checkstyleMain` - 코드 스타일
3. `./gradlew pmdMain` - 정적 분석
4. `./gradlew spotbugsMain` - 버그 탐지
5. `./gradlew test --tests "*ArchTest"` - 아키텍처 규칙

### /ship - Epic 배포 (1 PR)

```bash
/ship                       # Epic 전체 ship
/ship --no-squash           # 커밋 히스토리 보존
/ship --draft               # Draft PR 생성
```

**수행 작업**:
1. 모든 Task 완료 여부 확인
2. 전체 테스트 실행
3. **WIP 커밋 Squash** (모든 Task 커밋 → 1개)
4. 원격 푸시 + **1개 PR 생성**
5. 모든 Sub-task → Done
6. **Epic → In Review/Done**

---

## 🔄 Hook 시스템

### Auto-commit Hook

`/work` 실행 시 활성화:

```
.claude/state/
├── work-mode              # active | paused | inactive
├── current-epic           # EPIC-123
├── current-task           # TASK-125
├── task-queue             # JSON: Task 목록 + 상태
└── last-auto-commit       # 마지막 커밋 시간
```

**커밋 메시지 형식**:
```
WIP: EPIC-123 [2/4] ProcessPaymentUseCase 구현 중
```

### Rule Checker Hook

Java 파일 수정 시 Zero-Tolerance 규칙 자동 검사:

```bash
# 검사 항목
- AGG-001: Lombok 금지
- AGG-014: Law of Demeter
- ENT-002: Long FK 전략
- CTR-005: Controller @Transactional 금지
```

---

## 📚 Knowledge Base 시스템

401개 규칙, 67개 템플릿, 184개 예제가 마크다운으로 인덱싱:

```
.claude/knowledge/
├── rules/                    # 401개 규칙
│   ├── zero-tolerance.md     # 🚨 Zero-Tolerance (212개)
│   ├── domain-rules.md       # DOMAIN (214개)
│   ├── application-rules.md  # APPLICATION (103개)
│   ├── persistence-rules.md  # PERSISTENCE (18개)
│   └── rest-api-rules.md     # REST_API (58개)
├── templates/                # 67개 템플릿
└── examples/                 # 184개 GOOD/BAD 예제
```

---

## 🔌 개발 도구

### 운영 DB 포트포워딩

운영 DB에 직접 접속이 필요할 때 사용:

```bash
# 1. AWS SSM 포트포워딩 시작
./local-dev/scripts/aws-port-forward.sh

# 2. DB 비밀번호 조회 (Secrets Manager)
aws secretsmanager get-secret-value \
  --secret-id "prod-shared-mysql-master-password" \
  --query "SecretString" --output text | jq -r '.password'

# 3. MySQL 접속 (user: admin, db: common)
mysql -h 127.0.0.1 -P 13307 -u admin -p common
# 조회한 비밀번호 입력
```

**주의**: 운영 DB 직접 수정 시 반드시 백업 확인 후 진행

---

## 🎯 Command 요약

### 핵심 워크플로우 (9개)

| Command | 용도 | Skill |
|---------|------|-------|
| `/epic` | Epic 기획 + Task 분해 (유형별) | planner |
| `/jira-create` | Jira Epic/Task 등록 (본인 자동 할당) | shipper |
| `/jira-fetch` | Jira Epic → Memory 로드 | shipper |
| `/work` | **Epic 브랜치** 생성 + Task 큐 | implementer |
| `/next` | **Task 전환** (완료 → 다음) ⭐ | implementer |
| `/review` | Claude 직접 리뷰 (Knowledge Base 기반) | reviewer |
| `/check` | 정적 분석 + ArchUnit | tester |
| `/ship` | Squash + **Epic PR** 생성 | shipper |
| `/resume` | 중단 작업 재개 | - |

### 기타 지원

| Command | 용도 |
|---------|------|
| `/plan` | 요구사항 분석 + 구현 전략 |
| `/design` | 설계 + 체크리스트 생성 |
| `/create-prd` | PRD 문서 생성 |

---

## 🛠️ Skill 요약

### 핵심 (5개)

| Skill | 역할 |
|-------|------|
| `planner` | Epic 기획, Task 분해, 영향도 분석 |
| `implementer` | 모든 레이어 구현, Zero-Tolerance 준수 |
| `tester` | ArchUnit, 단위/통합 테스트 |
| `reviewer` | Claude 직접 리뷰, Knowledge Base 컨벤션 검증 |
| `shipper` | Git, Jira, PR 관리 |

---

## 🚨 Zero-Tolerance 규칙

### Domain Layer
- ❌ Lombok 금지
- ❌ Getter 체이닝 금지 (Law of Demeter)
- ✅ Tell, Don't Ask

### Application Layer
- ❌ `@Transactional` 내 외부 API 금지
- ✅ CQRS 분리
- ✅ DTO는 Record

### Persistence Layer
- ❌ JPA 관계 어노테이션 금지
- ✅ Long FK 전략
- ✅ QueryDSL DTO Projection

### REST API Layer
- ❌ MockMvc 금지
- ✅ TestRestTemplate 사용
- ✅ @Valid 필수

---

## ⚡ 빠른 시작

```bash
# 새 기능 개발
/epic "회원 가입"            # 기획 (유형별 분석)
/jira-create                 # Jira 등록 (본인 자동 할당)

# Epic 작업 시작
/work EPIC-123               # Epic 브랜치 생성 + Task 1 시작

# Task 순차 진행
# Task 1 구현...
/next                        # Task 1 완료 → Task 2
# Task 2 구현...
/next                        # Task 2 완료 → Task 3
# ... 반복

# 품질 & Epic 배포
/review                      # Claude 직접 리뷰
/check                       # 정적 분석
/ship                        # Epic 전체 → 1 PR
```

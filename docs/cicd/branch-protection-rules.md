# GitHub 브랜치 보호 규칙

SetOf Commerce 프로젝트의 3-Tier CI/CD 파이프라인을 위한 브랜치 보호 규칙 문서입니다.

## 브랜치 전략 개요

```
┌─────────────────────────────────────────────────────────────────┐
│                        Branch Strategy                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   feature/*  ──┬──▶  develop  ──▶  stage  ──▶  main (prod)     │
│   bugfix/*   ──┤                                                │
│   hotfix/*   ──┴──────────────────────────────▶  main          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 브랜치 역할

| 브랜치 | 환경 | 역할 | 배포 트리거 |
|--------|------|------|-------------|
| `main` | Production | 운영 환경, 안정적인 릴리스만 포함 | Push 시 자동 배포 |
| `stage` | Staging | Production 미러 환경, 최종 검증 | Push 시 자동 배포 |
| `develop` | Development | 개발 통합 브랜치, CI 검증 | PR merge 시 CI 실행 |
| `feature/*` | - | 기능 개발 브랜치 | PR 생성 시 CI 실행 |
| `bugfix/*` | - | 버그 수정 브랜치 | PR 생성 시 CI 실행 |
| `hotfix/*` | - | 긴급 수정 브랜치 | PR 생성 시 CI 실행 |

## 브랜치 보호 규칙

### 1. main 브랜치 (Production)

```yaml
branch: main
protection_rules:
  # PR 필수
  require_pull_request:
    enabled: true
    required_approving_review_count: 1
    dismiss_stale_reviews: true
    require_code_owner_reviews: false
    require_last_push_approval: true

  # 상태 체크 필수
  required_status_checks:
    strict: true  # 브랜치가 최신 상태여야 함
    contexts:
      - "Run Tests (Legacy Only)"
      - "Build legacy-api"
      - "Build legacy-api-admin"
      - "Build legacy-batch"

  # 강제 푸시 금지
  allow_force_pushes: false

  # 삭제 금지
  allow_deletions: false

  # 선형 히스토리 권장 (Squash merge)
  require_linear_history: true

  # 관리자도 규칙 적용
  enforce_admins: true

  # 서명된 커밋 권장
  require_signed_commits: false

  # 대화 해결 필수
  require_conversation_resolution: true
```

### 2. stage 브랜치 (Staging)

```yaml
branch: stage
protection_rules:
  # PR 필수
  require_pull_request:
    enabled: true
    required_approving_review_count: 1
    dismiss_stale_reviews: true
    require_last_push_approval: false

  # 상태 체크 필수
  required_status_checks:
    strict: true
    contexts:
      - "Run Tests (Stage)"
      - "Build legacy-api (Stage)"
      - "Build legacy-api-admin (Stage)"
      - "Build legacy-batch (Stage)"

  # 강제 푸시 금지
  allow_force_pushes: false

  # 삭제 금지
  allow_deletions: false

  # 선형 히스토리
  require_linear_history: true
```

### 3. develop 브랜치 (Development)

```yaml
branch: develop
protection_rules:
  # PR 필수
  require_pull_request:
    enabled: true
    required_approving_review_count: 1
    dismiss_stale_reviews: false
    require_last_push_approval: false

  # 상태 체크 필수
  required_status_checks:
    strict: false  # 최신 상태 강제 안함 (개발 편의성)
    contexts:
      - "lint"
      - "test"
      - "build"

  # 강제 푸시 금지
  allow_force_pushes: false

  # 삭제 금지
  allow_deletions: false
```

## GitHub UI 설정 가이드

### Settings → Branches → Branch protection rules

1. **main 브랜치 보호 규칙 추가**
   ```
   Settings → Branches → Add branch protection rule

   Branch name pattern: main

   ☑ Require a pull request before merging
     ☑ Require approvals: 1
     ☑ Dismiss stale pull request approvals when new commits are pushed
     ☑ Require approval of the most recent reviewable push

   ☑ Require status checks to pass before merging
     ☑ Require branches to be up to date before merging
     Status checks that are required:
       - Run Tests (Legacy Only)
       - Build legacy-api
       - Build legacy-api-admin
       - Build legacy-batch

   ☑ Require conversation resolution before merging
   ☑ Require linear history
   ☑ Do not allow bypassing the above settings
   ☐ Allow force pushes
   ☐ Allow deletions
   ```

2. **stage 브랜치 보호 규칙 추가**
   ```
   Branch name pattern: stage

   ☑ Require a pull request before merging
     ☑ Require approvals: 1
     ☑ Dismiss stale pull request approvals when new commits are pushed

   ☑ Require status checks to pass before merging
     ☑ Require branches to be up to date before merging
     Status checks that are required:
       - Run Tests (Stage)
       - Build legacy-api (Stage)
       - Build legacy-api-admin (Stage)
       - Build legacy-batch (Stage)

   ☑ Require linear history
   ☐ Allow force pushes
   ☐ Allow deletions
   ```

3. **develop 브랜치 보호 규칙 추가**
   ```
   Branch name pattern: develop

   ☑ Require a pull request before merging
     ☑ Require approvals: 1

   ☑ Require status checks to pass before merging
     ☐ Require branches to be up to date before merging
     Status checks that are required:
       - lint
       - test
       - build

   ☐ Allow force pushes
   ☐ Allow deletions
   ```

## Merge 전략

### 권장 Merge 방법

| Source → Target | Merge 방법 | 이유 |
|-----------------|------------|------|
| `feature/*` → `develop` | **Squash and merge** | 깔끔한 커밋 히스토리 |
| `develop` → `stage` | **Merge commit** | 변경사항 추적 용이 |
| `stage` → `main` | **Merge commit** | 릴리스 추적 용이 |
| `hotfix/*` → `main` | **Squash and merge** | 긴급 수정 명확화 |

### Merge 시 주의사항

1. **PR 제목 컨벤션**
   ```
   [TYPE] 간단한 설명

   예시:
   [FEAT] 결제 기능 추가
   [FIX] 주문 취소 버그 수정
   [REFACTOR] 인증 모듈 리팩토링
   [HOTFIX] 프로덕션 긴급 수정
   ```

2. **PR 체크리스트**
   - [ ] 테스트 통과 확인
   - [ ] 코드 리뷰 승인
   - [ ] 충돌 해결
   - [ ] 관련 이슈 링크

## 환경별 배포 흐름

### 일반 개발 흐름

```
1. feature/EPIC-123 브랜치 생성
   git checkout -b feature/EPIC-123 develop

2. 개발 및 커밋
   git commit -m "feat: 결제 기능 구현"

3. develop으로 PR 생성 및 Merge
   → CI 실행 (lint, test, build)

4. stage로 PR 생성 및 Merge
   → Stage 환경 배포
   → E2E 테스트 실행

5. main으로 PR 생성 및 Merge
   → Production 환경 배포
```

### 긴급 수정 흐름 (Hotfix)

```
1. hotfix/critical-fix 브랜치 생성
   git checkout -b hotfix/critical-fix main

2. 수정 및 커밋
   git commit -m "hotfix: 결제 오류 긴급 수정"

3. main으로 직접 PR 생성
   → 빠른 리뷰 후 Merge
   → Production 즉시 배포

4. develop으로 역병합
   → 수정 사항 동기화
```

## 자동화된 상태 체크

### CI/CD 워크플로우와 연동

| 워크플로우 | 트리거 | 상태 체크 이름 |
|------------|--------|----------------|
| `ci.yml` | develop, feature/* PR | lint, test, build |
| `stage-build-deploy.yml` | stage push | Run Tests (Stage) |
| `build-and-deploy.yml` | main push | Run Tests (Legacy Only) |

### CODEOWNERS 설정 (선택사항)

```
# .github/CODEOWNERS

# 전체 코드베이스
* @ryu-qqq

# 인프라 변경
/terraform/ @ryu-qqq
/.github/workflows/ @ryu-qqq

# 도메인 레이어
/domain/ @ryu-qqq

# 배포 설정
/bootstrap/ @ryu-qqq
```

## 참고 사항

- 브랜치 보호 규칙은 GitHub Repository Settings에서 설정
- 상태 체크 이름은 워크플로우 job 이름과 일치해야 함
- 규칙 변경 시 팀 전체에 공지 필요

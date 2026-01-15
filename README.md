# SetOf Commerce

레거시 시스템에서 새로운 아키텍처로 점진적 마이그레이션을 진행하는 이커머스 플랫폼입니다.

## 아키텍처

**Hexagonal Architecture (Port & Adapter)** 기반의 멀티모듈 구조

```
setof-commerce/
├── core/
│   ├── core-domain/              # 도메인 모델, 비즈니스 규칙
│   └── core-application/         # UseCase, Port 정의
├── adapter-in/
│   ├── rest-api/                 # 신규 REST API
│   └── rest-api-admin/           # 신규 Admin API
├── adapter-out/
│   ├── persistence-mysql/        # JPA, QueryDSL
│   └── persistence-redis/        # Cache, Lock
├── bootstrap/
│   ├── bootstrap-web-api/        # 신규 API 서버
│   ├── bootstrap-web-api-admin/  # 신규 Admin 서버
│   ├── bootstrap-legacy-web-api/ # 레거시 API 서버
│   ├── bootstrap-legacy-web-api-admin/
│   ├── bootstrap-batch/          # 배치 서버
│   └── bootstrap-migration/      # 마이그레이션 서버
└── terraform/                    # 인프라 코드
```

---

## 브랜치 전략

### Git Flow (3-Tier)

```
feature/* ─────► dev ─────► stage ─────► main (prod)
                 │           │            │
                 ▼           ▼            ▼
              Dev 환경    Stage 환경    Prod 환경
```

### 브랜치 설명

| 브랜치 | 용도 | 배포 환경 | 보호 규칙 |
|--------|------|-----------|-----------|
| `main` | 프로덕션 코드 | Production | PR 필수, 승인 2명 |
| `stage` | QA/통합 테스트 | Stage (운영 동일 환경) | PR 필수, 승인 1명 |
| `dev` | 개발 통합, 프론트 협업 | Dev (경량) | PR 필수 |
| `feature/*` | 기능 개발 | 로컬 | - |
| `hotfix/*` | 긴급 수정 | - | main에서 분기 |

### 워크플로우

```bash
# 1. 기능 개발
git checkout dev
git pull origin dev
git checkout -b feature/SC-123-new-feature

# 2. 개발 완료 후 PR
git push -u origin feature/SC-123-new-feature
# GitHub에서 PR: feature/* → dev

# 3. Dev 테스트 후 Stage 배포
# PR: dev → stage

# 4. QA 완료 후 Production 배포
# PR: stage → main
```

### CI/CD 파이프라인

| 이벤트 | 액션 |
|--------|------|
| `feature/*` push | Build + Test |
| `dev` push | Build + Test + Deploy to Dev |
| `stage` push | Build + Test + Deploy to Stage |
| `main` push | Build + Test + Deploy to Prod |
| PR 생성 | Build + Test + CodeRabbit 리뷰 |

---

## 로컬 개발 환경

### 사전 준비

```bash
# AWS SSO 로그인
aws sso login

# Java 21 + Gradle 확인
java -version   # 21+
./gradlew -v
```

### 빠른 시작

```bash
cd local-dev

# 1. 인프라 시작 (Redis + Stage RDS 포트포워딩)
./scripts/local-start.sh

# 2. IDE에서 애플리케이션 실행
# VM Options: -Dspring.profiles.active=local
# 환경변수: DB_HOST=127.0.0.1, DB_PORT=13308
```

상세 가이드: [local-dev/README.md](local-dev/README.md)

---

## 기술 스택

### Backend
- **Java 21** + **Spring Boot 3.5.x**
- **JPA** + **QueryDSL** (DTO Projection)
- **Redis** (Cache, Distributed Lock)

### Infrastructure
- **AWS ECS Fargate** + **ALB**
- **AWS RDS MySQL 8.0**
- **AWS ElastiCache Redis**
- **Terraform** (IaC)

### CI/CD
- **GitHub Actions**
- **AWS ECR** (Container Registry)
- **CodeRabbit** (AI Code Review)

---

## 문서

| 문서 | 설명 |
|------|------|
| [local-dev/README.md](local-dev/README.md) | 로컬 개발 환경 가이드 |
| [docs/cicd/](docs/cicd/) | CI/CD 파이프라인 문서 |
| [docs/coding_convention/](docs/coding_convention/) | 코딩 컨벤션 |
| [claudedocs/](claudedocs/) | Claude AI 분석 문서 |

---

## 주요 명령어

```bash
# 빌드
./gradlew build

# 테스트
./gradlew test

# 특정 모듈 실행
./gradlew :bootstrap:bootstrap-web-api:bootRun

# 코드 포맷팅
./gradlew spotlessApply

# 정적 분석
./gradlew checkstyleMain pmdMain spotbugsMain
```

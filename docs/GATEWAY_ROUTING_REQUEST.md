# connectly-gateway 라우팅 변경 요청서

## 1. 목적

Strangler Fig 마이그레이션을 위해 **카테고리·브랜드·셀러 조회** API를 새 서버로 라우팅해 주세요.

## 2. 배경

- **현재**: `commerce.set-of.com` / `stage.set-of.com` 트래픽이 모두 레거시 서버로만 전달됨
- **목표**: 일부 경로만 새 서버로 전환하여 점진적 마이그레이션 진행
- **선택 도메인**: Brand, Category, Seller 조회 API만 신규 서버로 라우팅

## 3. 요청 사항

### 3-1. 라우팅 규칙 추가

아래 경로에 대한 요청을 **새 서버**로 라우팅해 주세요.

| 경로 패턴 | 설명 |
| --------- | ---- |
| `/api/v1/brands/**` | 브랜드 조회 (목록, 상세 등) |
| `/api/v1/categories/**` | 카테고리 조회 (목록, 트리 등) |
| `/api/v1/sellers/**` | 셀러 조회 (목록, 상세, 사업자 검증 등) |

### 3-2. 라우팅 우선순위

- 위 세 경로는 **기존 catch-all (`/**`)** 보다 **먼저** 매칭되어야 합니다.
- 매칭되지 않는 경로는 기존처럼 레거시로 전달됩니다.

### 3-3. 대상 서비스 URI

| 환경 | 새 서버 URI | 레거시 URI |
| ---- | ----------- | ---------- |
| Stage | `http://setof-commerce-web-api-stage.connectly.local:8080` | `http://setof-commerce-legacy-api-stage.connectly.local:8088` |
| Prod | `http://setof-commerce-web-api-prod.connectly.local:8080` | `http://setof-commerce-legacy-api-prod.connectly.local:8088` |

> Service Discovery FQDN은 인프라 배포 시점에 맞춰 확인 부탁드립니다.

### 3-4. Host 매칭

- **Stage**: `stage.set-of.com`
- **Prod**: `commerce.set-of.com` (또는 실제 사용 중인 도메인)

## 4. YAML 설정 예시

gateway 설정이 YAML 기반이라면 다음 형태로 추가해 주세요:

```yaml
# 1. 마이그레이션된 조회 API (구체 경로 먼저)
- id: commerce-new
  uri: http://setof-commerce-web-api-stage.connectly.local:8080  # 환경별로 변경
  paths:
    - /api/v1/brands/**
    - /api/v1/categories/**
    - /api/v1/sellers/**
  hosts:
    - stage.set-of.com
    # - commerce.set-of.com  # Prod

# 2. 레거시 catch-all (기존 규칙, 나머지 전부)
- id: legacy-commerce
  uri: http://setof-commerce-legacy-api-stage.connectly.local:8088
  paths:
    - /**
  hosts:
    - stage.set-of.com
```

## 5. 롤백 방법

문제 발생 시 `commerce-new` 라우트를 제거 또는 비활성화하면 해당 경로는 자동으로 레거시 catch-all으로 폴백됩니다. 별도 데이터 변경 없이 설정만으로 즉시 롤백 가능합니다.

## 6. 검증 방법

### 6-1. Stage 검증

```bash
# 브랜드 목록
curl -s "https://stage.set-of.com/api/v1/brands?page=0&size=10"

# 카테고리 트리
curl -s "https://stage.set-of.com/api/v1/categories/tree"

# 셀러 목록
curl -s "https://stage.set-of.com/api/v1/sellers?page=0&size=10"
```

응답이 200이고 기존과 동일한 형식인지 확인해 주세요.

### 6-2. 라우팅 확인

- 응답 헤더나 로그에 새 서버로 라우팅된 요청인지 식별할 수 있으면 좋습니다.

## 7. 추가 옵션 (선택)

### 7-1. 조회(GET)만 새 서버로 라우팅

Brand/Category/Seller에 POST/PUT/DELETE도 있을 수 있습니다. 아직 새 서버에서는 **조회(GET)만** 구현되어 있으므로, Path + Method 기반 라우팅이 가능하다면:

- `GET /api/v1/brands/**` → 새 서버
- `POST /api/v1/brands` 등 → 레거시

로 분리하는 것도 고려해 주시면 됩니다. 현재는 Path만으로 라우팅해도 무방합니다.

### 7-2. Circuit Breaker

새 서버 장애 시 자동으로 레거시로 폴백하는 Circuit Breaker 적용을 권장합니다. (참고: [STRANGLER_FIG_MIGRATION_PLAN.md](./STRANGLER_FIG_MIGRATION_PLAN.md) 267~284라인)

## 8. 문의

- setof-commerce 팀
- 상세 마이그레이션 계획: [STRANGLER_FIG_MIGRATION_PLAN.md](./STRANGLER_FIG_MIGRATION_PLAN.md)

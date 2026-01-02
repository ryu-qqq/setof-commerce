---
description: 레거시 vs 신규 서버 엔드포인트 비교 분석. 응답 구조, 권한, 미구현 엔드포인트 파악.
tags: [project]
---

# /migrate:analyze - Legacy vs New Server Analysis

레거시 서버와 신규 서버의 특정 도메인 엔드포인트를 비교 분석합니다.

## 입력

```bash
/migrate:analyze {domain}

# 예시
/migrate:analyze product
/migrate:analyze brand
/migrate:analyze order
```

## 분석 프로세스

```
/migrate:analyze {domain}
        ↓
┌─────────────────────────────────────────────────┐
│ 1️⃣ Serena Memory 읽기                           │
│    - read_memory("migration-strategy")          │
│    - read_memory("v1-api-pattern")              │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 2️⃣ 레거시 엔드포인트 검색                        │
│    - bootstrap-legacy-web-api                   │
│    - bootstrap-legacy-web-api-admin             │
│    - Controller, Response DTO 파악              │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 3️⃣ 신규 V1 엔드포인트 검색                       │
│    - adapter-in/rest-api (Customer)             │
│    - adapter-in/rest-api-admin (Admin)          │
│    - V1Controller, V1Response 파악              │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 4️⃣ 비교 분석                                    │
│    - 엔드포인트 매핑 (Legacy ↔ New)              │
│    - 응답 구조 비교                              │
│    - 권한 설정 비교 (V1 vs V2)                   │
│    - 미구현 엔드포인트 식별                       │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 5️⃣ 분석 결과 출력 + migration-status 업데이트    │
└─────────────────────────────────────────────────┘
```

## 검색 대상

### 레거시 서버

```
bootstrap/
├── bootstrap-legacy-web-api/
│   └── src/main/java/.../module/{domain}/
│       ├── controller/
│       ├── dto/
│       └── service/
│
└── bootstrap-legacy-web-api-admin/
    └── src/main/java/.../module/{domain}/
        ├── controller/
        ├── dto/
        └── service/
```

### 신규 서버

```
adapter-in/
├── rest-api/
│   └── src/main/java/.../v1/{domain}/
│       ├── controller/{Domain}V1Controller.java
│       ├── dto/response/{Domain}V1ApiResponse.java
│       └── mapper/{Domain}V1ApiMapper.java
│
└── rest-api-admin/
    └── src/main/java/.../v1/{domain}/
        ├── controller/{Domain}V1Controller.java
        ├── dto/response/{Domain}V1ApiResponse.java
        └── mapper/{Domain}V1ApiMapper.java
```

## 출력 형식

```markdown
## 🔍 {Domain} Migration Analysis

### 📊 엔드포인트 매핑

#### Customer API (rest-api)

| Legacy Endpoint | New V1 Endpoint | 상태 | 비고 |
|-----------------|-----------------|------|------|
| GET /api/v1/xxx | GET /api/v1/xxx | ✅ 구현 | |
| POST /api/v1/yyy | - | ❌ 미구현 | |
| GET /api/v1/zzz | GET /api/v1/zzz | ⚠️ UnsupportedOp | 구현 필요 |

#### Admin API (rest-api-admin)

| Legacy Endpoint | New V1 Endpoint | 상태 | 비고 |
|-----------------|-----------------|------|------|
| ... | ... | ... | ... |

### 📝 응답 구조 비교

| 항목 | Legacy | New V1 | 일치 |
|------|--------|--------|------|
| 래퍼 | ApiResponse | V1ApiResponse | ⚠️ 확인 필요 |
| status 타입 | "SUCCESS" | 200 | ❌ 다름 |
| 필드명 | {field} | {field} | ✅ 동일 |

### 🔐 권한 구조 비교

| 버전 | 권한 적용 방식 |
|------|---------------|
| V1 | @PreAuthorize 클래스 레벨 |
| V2 | @PreAuthorize 메서드 레벨 + sellerAccess |

### 🎯 다음 단계

| 우선순위 | 작업 | 명령어 |
|----------|------|--------|
| 🔴 High | 미구현 엔드포인트 구현 | `/migrate:v1 {domain}` |
| 🟡 Medium | V1 문서화 | `/migrate:docs {domain}` |
| 🟢 Low | 응답 구조 통일 | 수동 작업 |
```

## Serena 검색 명령

```python
# 레거시 Controller 검색
mcp__serena__search_for_pattern(
    substring_pattern="class.*{Domain}Controller",
    relative_path="bootstrap/bootstrap-legacy-web-api"
)

# 신규 V1 Controller 검색
mcp__serena__search_for_pattern(
    substring_pattern="class.*{Domain}V1Controller",
    relative_path="adapter-in/rest-api"
)

# 응답 DTO 검색
mcp__serena__find_symbol(
    name_path_pattern="{Domain}*Response"
)
```

## 관련 커맨드

- `/migrate:v1 {domain}` - V1 엔드포인트 구현
- `/migrate:docs {domain}` - V1 문서화
- `/migrate:status` - 전체 마이그레이션 현황

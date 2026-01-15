# Epic: MCP Convention API Server

## 개요

- **목적**: FastMCP 서버 + Admin UI가 호출할 REST API 서버 개발 (코딩 컨벤션 데이터 CRUD)
- **범위**:
  - ✅ 포함: 계층적 데이터 조회 (depth 제어, 트리 조회)
  - ✅ 포함: FastMCP 특화 Aggregated APIs
  - ✅ 포함: Admin UI용 CRUD + Sub-resource 패턴
  - ✅ 포함: Soft Delete 처리
  - ❌ 제외: 인증/인가, MCP 서버 자체, 캐싱
- **예상 Task 수**: 8개 (기존 18개에서 통합)
- **API 소비자**: FastMCP (Python), Admin UI (Web)

---

## API 소비자별 Use Case

### FastMCP (Python MCP 서버)
**목적**: AI 에이전트에게 코딩 컨벤션 데이터 제공

| Use Case | 필요 API | 우선순위 |
|----------|----------|----------|
| Convention 전체 규칙 조회 | `GET /conventions/{id}/tree` | 🔴 Critical |
| Zero-Tolerance 규칙 추출 | `GET /conventions/{id}/zero-tolerance` | 🔴 Critical |
| 클래스 타입별 템플릿 조회 | `GET /class-templates?type=CONTROLLER` | 🟡 High |
| 패키지 구조 조회 | `GET /modules/{id}?expand=packageStructures` | 🟡 High |
| 규칙 코드로 검색 | `GET /coding-rules/code/{code}` | 🟢 Medium |

### Admin UI (Web)
**목적**: 관리자가 컨벤션 데이터 CRUD

| Use Case | 필요 API | 우선순위 |
|----------|----------|----------|
| 트리 네비게이션 | `GET /tech-stacks/{id}?depth=1` | 🔴 Critical |
| 엔티티 CRUD | `POST/PATCH /conventions` 등 | 🔴 Critical |
| 규칙 검색/필터 | `GET /coding-rules?category=X&severity=Y` | 🟡 High |
| 예제/체크리스트 편집 | `POST /coding-rules/{id}/examples` | 🟡 High |
| Module 트리 편집 | `PATCH /modules/{id}` (parentModuleId) | 🟢 Medium |

---

## 엔티티 계층 구조

```
TechStack (ROOT)
└── Architecture
    ├── Module (self-ref: parentModuleId) ──────────────┐
    │   └── PackageStructure ─────────────────┐         │
    ├── LayerDependencyRule                    │         │ moduleTypeId (FK)
    └── Convention                             │         │
        ├── CodingRule ─── structureId (참조) ─┤         │
        │   ├── RuleExample                    │         │
        │   ├── ChecklistItem                  │         │
        │   └── ZeroToleranceRule (1:1)        │         │
        ├── ClassTemplate ─ structureId (참조) ┤         │
        └── ArchUnitTest ─ structureId (참조) ─┘         │
                                               │         │
참조 데이터 (Reference Data)                   │         │
├── ModuleType ◄───────────────────────────────┼─────────┘
│     └── DOMAIN, APPLICATION, ADAPTER_IN, ... │
└── PackagePurpose ◄───────────────────────────┘
      └── moduleTypeId (FK → ModuleType)
```

### 🚨 도메인 구조 변경 사항

#### 1. ModuleType 테이블 신규 생성
```sql
-- Enum → 테이블로 변경하여 FK 무결성 보장
CREATE TABLE module_type (
    id BIGINT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,  -- DOMAIN, APPLICATION, ...
    name VARCHAR(100) NOT NULL,
    description TEXT
);
```

#### 2. ZeroToleranceRule 구조 변경
```
Before: conventionId (FK) + ruleId (FK)
After:  ruleId (FK - 소유 관계만)
        → CodingRule의 Sub-resource (1:1)
        → Convention은 CodingRule.conventionId로 접근
```

#### 3. PackagePurpose/Module의 moduleType 변경
```
Before: moduleType = Enum (하드코딩)
After:  moduleTypeId = FK → ModuleType
        → 데이터 무결성 보장
        → PackageStructure 생성 시 Validation 가능
```

### FK 관계 유형 정의

| 유형 | 설명 | API 처리 |
|------|------|----------|
| **소유 관계** | 부모-자식 관계, 생명주기 공유 | Sub-resource 패턴 |
| **참조 관계** | 독립 엔티티 참조, 템플릿/설정 선택 | 응답에 ID 포함 + expand 지원 |

### 엔티티별 FK 상세

| Entity | FK 필드 | 관계 유형 | 상대 Entity |
|--------|---------|----------|-------------|
| **Architecture** | techStackId | 소유 | TechStack |
| **Module** | architectureId | 소유 | Architecture |
| **Module** | parentModuleId | 자기참조 | Module |
| **Module** | moduleTypeId | 참조 | ModuleType ⭐ |
| **Convention** | architectureId | 소유 | Architecture |
| **LayerDependencyRule** | architectureId | 소유 | Architecture |
| **CodingRule** | conventionId | 소유 | Convention |
| **CodingRule** | structureId | 참조 (nullable) | PackageStructure |
| **RuleExample** | ruleId | 소유 | CodingRule |
| **ChecklistItem** | ruleId | 소유 | CodingRule |
| **ZeroToleranceRule** | ruleId | 소유 (1:1) | CodingRule ⭐ |
| **ClassTemplate** | conventionId | 소유 | Convention |
| **ClassTemplate** | structureId | 참조 (nullable) | PackageStructure |
| **ArchUnitTest** | conventionId | 소유 | Convention |
| **ArchUnitTest** | structureId | 참조 (nullable) | PackageStructure |
| **PackageStructure** | moduleId | 소유 | Module |
| **PackageStructure** | purposeId | 참조 | PackagePurpose |
| **PackagePurpose** | moduleTypeId | 참조 | ModuleType ⭐ |

> ⭐ **변경된 FK**: Enum 하드코딩 → 테이블 FK 참조로 변경

---

## 엔티티 Tier 분류

### Tier 1: Root Aggregate (독립 CRUD 필수)

| 엔티티 | API 전략 | 이유 |
|--------|----------|------|
| **TechStack** | 전체 CRUD + depth 조회 | 최상위 루트, 진입점 |
| **Architecture** | 전체 CRUD + expand 조회 | TechStack 하위 핵심 구조 |
| **Convention** | 전체 CRUD + 트리 조회 | FastMCP 핵심 데이터 |

**API 패턴**:
```
GET    /{entity}                    # 목록 (pagination)
GET    /{entity}/{id}?depth=0|1|full  # 단건 + 깊이 제어
POST   /{entity}                    # 생성
PATCH  /{entity}/{id}               # 수정/삭제
```

### Tier 2: Sub-Aggregate (개별 CRUD + 검색)

| 엔티티 | API 전략 | 이유 |
|--------|----------|------|
| **Module** | CRUD + 트리 조회 | self-ref 트리 구조 복잡 |
| **CodingRule** | CRUD + 상세 조회 + 검색 | 핵심 규칙, 검색 필요 |
| **ClassTemplate** | CRUD + 타입별 조회 | classType 검색 필요 |
| **ArchUnitTest** | CRUD + 필터 | 독립 검색 필요 |

**API 패턴**:
```
GET    /{entity}?parentId=X&filter=Y  # 필터 검색
GET    /{entity}/{id}/details         # 상세 (하위 포함)
POST   /{entity}                       # 생성
PATCH  /{entity}/{id}                  # 수정/삭제
```

### Tier 3: Value Object / Sub-resource

| 엔티티 | 상위 엔티티 | FK 관계 | API 전략 |
|--------|-------------|---------|----------|
| **PackageStructure** | Module | moduleId (소유) | Sub-resource |
| **LayerDependencyRule** | Architecture | architectureId (소유) | Sub-resource |
| **RuleExample** | CodingRule | ruleId (소유) | Sub-resource |
| **ChecklistItem** | CodingRule | ruleId (소유) | Sub-resource |
| **ZeroToleranceRule** | CodingRule | ruleId (소유, 1:1) | Sub-resource ⭐ |

> **⭐ ZeroToleranceRule 변경**: `ruleId`가 소유 FK (1:1 관계)
> - `conventionId` 제거 (CodingRule.conventionId로 Convention 접근)
> - API: `/api/v1/coding-rules/{ruleId}/zero-tolerance-rule`

**Sub-resource API 패턴**:
```
GET    /{parent}/{parentId}/{children}           # 목록
POST   /{parent}/{parentId}/{children}           # 생성
PATCH  /{parent}/{parentId}/{children}/{id}      # 수정
DELETE /{parent}/{parentId}/{children}/{id}      # 삭제 (실제 삭제)
```

### 참조 데이터 (Reference Data)

| 엔티티 | FK 관계 | API 전략 |
|--------|---------|----------|
| **ModuleType** | - (최상위 코드 테이블) | 읽기 전용 조회 |
| **PackagePurpose** | moduleTypeId (FK → ModuleType) | 참조 데이터 API + 필터 |

**참조 데이터 API 패턴**:
```
# ModuleType (읽기 전용)
GET    /api/ref/module-types                          # 전체 목록

# PackagePurpose (moduleTypeId로 필터)
GET    /api/ref/package-purposes?moduleTypeId=1       # 필터 조회
GET    /api/ref/package-purposes/{id}                 # 단건 조회
POST   /api/ref/package-purposes                      # 생성 (Admin)
PATCH  /api/ref/package-purposes/{id}                 # 수정 (Admin)
```

**PackageStructure Validation 규칙**:
```java
// PackageStructure 생성 시 검증
Module.moduleTypeId == PackagePurpose.moduleTypeId
→ 불일치 시 DomainException("INVALID_PURPOSE_FOR_MODULE_TYPE")
```

---

## API 설계 전략

### 1. 응답 깊이 제어 (depth 파라미터)

```http
# 기본 (depth=0): 해당 엔티티만
GET /api/v1/tech-stacks/1?depth=0
→ { "id": 1, "name": "Spring Boot", "version": "3.5.x" }

# 1단계 (depth=1): 직계 자식 포함
GET /api/v1/tech-stacks/1?depth=1
→ {
    "id": 1, "name": "Spring Boot",
    "architectures": [{ "id": 10, "name": "Hexagonal" }]
  }

# 전체 (depth=full): 모든 하위 트리
GET /api/v1/tech-stacks/1?depth=full
→ { 전체 트리 JSON }
```

### 2. 확장 필드 선택 (expand 파라미터)

```http
# 특정 관계만 확장
GET /api/v1/conventions/5?expand=codingRules
GET /api/v1/conventions/5?expand=codingRules,classTemplates
GET /api/v1/modules/3?expand=packageStructures,childModules
```

### 3. Sub-resource 패턴

```http
# RuleExample은 CodingRule의 하위 리소스
GET    /api/v1/coding-rules/10/examples
POST   /api/v1/coding-rules/10/examples
PATCH  /api/v1/coding-rules/10/examples/1
DELETE /api/v1/coding-rules/10/examples/1

# ChecklistItem도 동일
GET    /api/v1/coding-rules/10/checklist-items
POST   /api/v1/coding-rules/10/checklist-items
```

### 4. 트리 조회 전용 엔드포인트

```http
# Convention 전체 트리 (FastMCP 핵심)
GET /api/v1/conventions/{id}/tree
→ Convention + CodingRules + Examples + Checklists + Templates + Tests

# Module 하위 트리
GET /api/v1/modules/{id}/tree
→ Module + ChildModules (재귀) + PackageStructures

# Zero-Tolerance 규칙만 추출
GET /api/v1/conventions/{id}/zero-tolerance
→ isZeroTolerance=true인 CodingRule + ZeroToleranceRule
```

---

## 비즈니스 규칙

### Core Rules (핵심 규칙)

| ID | 규칙 | 설명 |
|----|------|------|
| BR-001 | 계층적 데이터 무결성 | 상위 엔티티 삭제 시 하위 엔티티 존재 여부 검증 필요 |
| BR-002 | Soft Delete 적용 | 모든 삭제는 deletedAt 필드 설정으로 처리 |
| BR-003 | 다중 Convention 지원 | Convention 기준 데이터 격리 및 조회 |

### CRUD Rules (생성/수정/삭제 규칙)

| ID | 규칙 | 적용 대상 |
|----|------|----------|
| BR-010 | TechStack 삭제 시 Architecture 존재 확인 | TechStack |
| BR-011 | Architecture 삭제 시 하위 존재 확인 | Module, Convention, LayerDependencyRule |
| BR-012 | Module 삭제 시 자식 Module 및 PackageStructure 확인 | Module |
| BR-013 | Convention 삭제 시 하위 Rule/Template 확인 | CodingRule, ClassTemplate, ArchUnitTest |
| BR-014 | CodingRule 삭제 시 Example/Checklist 확인 | RuleExample, ChecklistItem, ZeroToleranceRule |

### Validation Rules (검증 규칙)

| ID | 규칙 | 설명 |
|----|------|------|
| BR-030 | 코드 유일성 검증 | CodingRule.code, Convention.name 등 유니크 필드 중복 검사 |
| BR-031 | FK 참조 유효성 | 생성/수정 시 참조하는 엔티티 존재 여부 확인 |
| BR-032 | 순환 참조 방지 | Module의 parentModuleId 순환 참조 검증 |

---

## Task 목록 (재구성)

### Phase 1: Infrastructure (기반 구축)

#### TASK-1: API 공통 모듈 설정

- **Jira Key**: AESA-77
- **레이어**: REST API + Application
- **예상 크기**: ~10K tokens
- **설명**: CRUD 공통 패턴 + 응답 깊이 제어 기반 설정
- **산출물**:
  - `ResponseEntity<ApiResponse<T>>` 패턴 정립
  - GlobalExceptionHandler에서 DomainException 처리
  - `depth`, `expand` 파라미터 처리 공통 로직
  - Soft Delete 처리 공통 로직
  - 계층적 삭제 검증 서비스 (DeletionValidator)

---

### Phase 2: Core Aggregates (Tier 1 CRUD)

#### TASK-2: TechStack + Architecture CRUD

- **레이어**: Application + Persistence + REST API
- **예상 크기**: ~12K tokens
- **의존성**: TASK-1
- **설명**: 최상위 엔티티 CRUD + depth 조회
- **API Endpoints**:
  - `POST /api/v1/tech-stacks`
  - `GET /api/v1/tech-stacks?depth=0|1`
  - `GET /api/v1/tech-stacks/{id}?depth=0|1|full`
  - `PATCH /api/v1/tech-stacks/{id}`
  - `POST /api/v1/architectures`
  - `GET /api/v1/architectures?techStackId=X`
  - `GET /api/v1/architectures/{id}?expand=modules,conventions`
  - `PATCH /api/v1/architectures/{id}`

#### TASK-3: Convention CRUD + 트리 조회

- **레이어**: Application + Persistence + REST API
- **예상 크기**: ~12K tokens (핵심 기능)
- **의존성**: TASK-2
- **설명**: Convention CRUD + 전체 트리 조회 (FastMCP 핵심)
- **API Endpoints**:
  - `POST /api/v1/conventions`
  - `GET /api/v1/conventions?architectureId=X`
  - `GET /api/v1/conventions/{id}?expand=codingRules,classTemplates`
  - `GET /api/v1/conventions/{id}/tree` ⭐ FastMCP 핵심
  - `GET /api/v1/conventions/{id}/zero-tolerance` ⭐ FastMCP 핵심 (CodingRule + ZeroToleranceRule 조인)
  - `PATCH /api/v1/conventions/{id}`

> **Note**: ZeroToleranceRule은 CodingRule의 Sub-resource (1:1)로 TASK-5에서 처리

---

### Phase 3: Sub-Aggregates (Tier 2 CRUD)

#### TASK-4: Module CRUD + 트리 구조

- **레이어**: Application + Persistence + REST API
- **예상 크기**: ~12K tokens
- **의존성**: TASK-2
- **설명**: Module CRUD + 계층적 트리 + 순환 참조 검증
- **API Endpoints**:
  - `POST /api/v1/modules`
  - `GET /api/v1/modules?architectureId=X&parentModuleId=Y`
  - `GET /api/v1/modules/{id}?expand=packageStructures,childModules`
  - `GET /api/v1/modules/{id}/tree`
  - `PATCH /api/v1/modules/{id}`
- **Sub-resources**:
  - `GET /api/v1/modules/{id}/package-structures`
  - `POST /api/v1/modules/{id}/package-structures`
  - `PATCH /api/v1/modules/{id}/package-structures/{psId}`
  - `DELETE /api/v1/modules/{id}/package-structures/{psId}`

#### TASK-5: CodingRule CRUD + 상세 조회 + ZeroToleranceRule

- **레이어**: Application + Persistence + REST API
- **예상 크기**: ~14K tokens
- **의존성**: TASK-3
- **설명**: CodingRule CRUD + 검색 필터 + 예제/체크리스트/ZeroToleranceRule Sub-resource
- **API Endpoints**:
  - `POST /api/v1/coding-rules`
  - `GET /api/v1/coding-rules?conventionId=X&category=Y&severity=Z`
  - `GET /api/v1/coding-rules/{id}`
  - `GET /api/v1/coding-rules/{id}/details` (예제+체크리스트+ZT규칙 포함)
  - `GET /api/v1/coding-rules/code/{code}`
  - `PATCH /api/v1/coding-rules/{id}`
- **Sub-resources** (RuleExample, ChecklistItem, ZeroToleranceRule - ruleId가 소유 FK):
  - `GET/POST /api/v1/coding-rules/{id}/examples`
  - `PATCH/DELETE /api/v1/coding-rules/{id}/examples/{exId}`
  - `GET/POST /api/v1/coding-rules/{id}/checklist-items`
  - `PATCH/DELETE /api/v1/coding-rules/{id}/checklist-items/{ciId}`
  - `GET /api/v1/coding-rules/{id}/zero-tolerance-rule` ⭐ 1:1 관계
  - `PUT /api/v1/coding-rules/{id}/zero-tolerance-rule` ⭐ 생성/수정 (Upsert)
  - `DELETE /api/v1/coding-rules/{id}/zero-tolerance-rule` ⭐ 삭제

> **Note**: ZeroToleranceRule은 CodingRule과 1:1 관계 (ruleId가 소유 FK, conventionId 제거됨)

#### TASK-6: ClassTemplate + ArchUnitTest CRUD

- **레이어**: Application + Persistence + REST API
- **예상 크기**: ~12K tokens
- **의존성**: TASK-3
- **설명**: 템플릿/테스트 CRUD + 타입별 조회
- **API Endpoints**:
  - `POST /api/v1/class-templates`
  - `GET /api/v1/class-templates?conventionId=X&classType=Y`
  - `GET /api/v1/class-templates/{id}`
  - `GET /api/v1/class-templates/type/{classType}` (타입별 목록)
  - `PATCH /api/v1/class-templates/{id}`
  - `POST /api/v1/arch-unit-tests`
  - `GET /api/v1/arch-unit-tests?conventionId=X&structureId=Y`
  - `GET /api/v1/arch-unit-tests/{id}`
  - `PATCH /api/v1/arch-unit-tests/{id}`

---

### Phase 4: Support Entities (Tier 3 + 코드 테이블)

#### TASK-7: 참조 데이터 (ModuleType, PackagePurpose) + LayerDependencyRule

- **레이어**: Application + Persistence + REST API
- **예상 크기**: ~10K tokens
- **의존성**: TASK-1
- **설명**: 참조 데이터 API (ModuleType, PackagePurpose) + Architecture Sub-resource
- **API Endpoints** (ModuleType - 읽기 전용 코드 테이블):
  - `GET /api/ref/module-types` ⭐ 전체 목록 (DOMAIN, APPLICATION, ADAPTER_IN, ...)
  - `GET /api/ref/module-types/{id}` 단건 조회
- **API Endpoints** (PackagePurpose - moduleTypeId FK 참조):
  - `GET /api/ref/package-purposes?moduleTypeId=1` ⭐ moduleTypeId 필터 필수
  - `GET /api/ref/package-purposes/{id}`
  - `POST /api/ref/package-purposes` (Admin)
  - `PATCH /api/ref/package-purposes/{id}` (Admin)
- **API Endpoints** (LayerDependencyRule - Architecture Sub-resource):
  - `GET /api/v1/architectures/{id}/layer-dependency-rules`
  - `POST /api/v1/architectures/{id}/layer-dependency-rules`
  - `PATCH /api/v1/architectures/{id}/layer-dependency-rules/{ldrId}`
  - `DELETE /api/v1/architectures/{id}/layer-dependency-rules/{ldrId}`

> **구조 변경**: `moduleType` Enum → `moduleTypeId` FK (ModuleType 테이블 참조)
> - ModuleType: 코드 테이블 (DOMAIN, APPLICATION, ADAPTER_IN, ADAPTER_OUT, COMMON, INFRASTRUCTURE)
> - PackagePurpose.moduleTypeId: ModuleType FK 참조
> - Module.moduleTypeId: ModuleType FK 참조

---

### Phase 5: FastMCP Specialized APIs

#### TASK-8: MCP 특화 Aggregated APIs

- **레이어**: Application + REST API
- **예상 크기**: ~10K tokens
- **의존성**: Phase 2-4 완료 후
- **설명**: FastMCP 최적화된 읽기 전용 API
- **API Endpoints**:
  - `GET /api/v1/mcp/convention-tree/{conventionId}` (최적화된 전체 트리)
  - `GET /api/v1/mcp/zero-tolerance-rules?conventionId=X` (모든 ZT 규칙)
  - `GET /api/v1/mcp/class-templates?type=CONTROLLER&conventionId=X`
  - `GET /api/v1/mcp/coding-rules?category=DOMAIN&conventionId=X`
  - `GET /api/v1/mcp/search?q=transaction&conventionId=X` (통합 검색)

---

## 의존성 그래프

```
Phase 1: Infrastructure
┌─────────────────────────────────────┐
│  TASK-1 (공통 모듈 + depth 전략)   │
└─────────────────────────────────────┘
                ↓
Phase 2: Core Aggregates (Tier 1)
┌─────────────────────────────────────┐
│  TASK-2 (TechStack + Architecture) │
│               ↓                     │
│  TASK-3 (Convention + 트리) ⭐     │
└─────────────────────────────────────┘
                ↓
Phase 3: Sub-Aggregates (Tier 2)
┌─────────────────────────────────────┐
│  TASK-4 (Module + 트리)            │
│               ↓                     │
│  TASK-5 (CodingRule + Sub-res)     │
│               ↓                     │
│  TASK-6 (Templates + Tests)        │
└─────────────────────────────────────┘
                ↓
Phase 4: Support (Tier 3)
┌─────────────────────────────────────┐
│  TASK-7 (PackagePurpose + LDR)     │
└─────────────────────────────────────┘
                ↓
Phase 5: FastMCP Specialized
┌─────────────────────────────────────┐
│  TASK-8 (MCP Aggregated APIs) ⭐   │
└─────────────────────────────────────┘
```

---

## REST API 명세 (전체 요약)

### Tier 1: Root Aggregates

| Method | Endpoint | Params | 설명 |
|--------|----------|--------|------|
| POST | `/api/v1/tech-stacks` | - | TechStack 생성 |
| GET | `/api/v1/tech-stacks` | `depth=0\|1` | TechStack 목록 |
| GET | `/api/v1/tech-stacks/{id}` | `depth=0\|1\|full` | TechStack 단건 |
| PATCH | `/api/v1/tech-stacks/{id}` | - | TechStack 수정/삭제 |
| POST | `/api/v1/architectures` | - | Architecture 생성 |
| GET | `/api/v1/architectures` | `techStackId`, `depth` | Architecture 목록 |
| GET | `/api/v1/architectures/{id}` | `expand=modules,conventions` | Architecture 단건 |
| PATCH | `/api/v1/architectures/{id}` | - | Architecture 수정/삭제 |
| POST | `/api/v1/conventions` | - | Convention 생성 |
| GET | `/api/v1/conventions` | `architectureId` | Convention 목록 |
| GET | `/api/v1/conventions/{id}` | `expand=codingRules,...` | Convention 단건 |
| GET | `/api/v1/conventions/{id}/tree` | - | ⭐ Convention 전체 트리 |
| GET | `/api/v1/conventions/{id}/zero-tolerance` | - | ⭐ Zero-Tolerance 규칙 |
| PATCH | `/api/v1/conventions/{id}` | - | Convention 수정/삭제 |

### Tier 2: Sub-Aggregates

| Method | Endpoint | Params | 설명 |
|--------|----------|--------|------|
| POST | `/api/v1/modules` | - | Module 생성 |
| GET | `/api/v1/modules` | `architectureId`, `parentModuleId` | Module 목록 |
| GET | `/api/v1/modules/{id}` | `expand=packageStructures,childModules` | Module 단건 |
| GET | `/api/v1/modules/{id}/tree` | - | Module 하위 트리 |
| PATCH | `/api/v1/modules/{id}` | - | Module 수정/삭제 |
| POST | `/api/v1/coding-rules` | - | CodingRule 생성 |
| GET | `/api/v1/coding-rules` | `conventionId`, `category`, `severity`, `isZeroTolerance` | 규칙 검색 |
| GET | `/api/v1/coding-rules/{id}` | - | CodingRule 단건 |
| GET | `/api/v1/coding-rules/{id}/details` | - | 상세 (예제+체크리스트) |
| GET | `/api/v1/coding-rules/code/{code}` | - | 코드로 조회 |
| PATCH | `/api/v1/coding-rules/{id}` | - | CodingRule 수정/삭제 |
| POST | `/api/v1/class-templates` | - | ClassTemplate 생성 |
| GET | `/api/v1/class-templates` | `conventionId`, `classType` | 템플릿 검색 |
| GET | `/api/v1/class-templates/{id}` | - | ClassTemplate 단건 |
| GET | `/api/v1/class-templates/type/{type}` | - | 타입별 목록 |
| PATCH | `/api/v1/class-templates/{id}` | - | ClassTemplate 수정/삭제 |
| POST | `/api/v1/arch-unit-tests` | - | ArchUnitTest 생성 |
| GET | `/api/v1/arch-unit-tests` | `conventionId`, `structureId` | 테스트 검색 |
| GET | `/api/v1/arch-unit-tests/{id}` | - | ArchUnitTest 단건 |
| PATCH | `/api/v1/arch-unit-tests/{id}` | - | ArchUnitTest 수정/삭제 |

### Tier 3: Sub-resources

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/v1/modules/{id}/package-structures` | PackageStructure 목록 |
| POST | `/api/v1/modules/{id}/package-structures` | PackageStructure 생성 |
| PATCH | `/api/v1/modules/{id}/package-structures/{psId}` | PackageStructure 수정 |
| DELETE | `/api/v1/modules/{id}/package-structures/{psId}` | PackageStructure 삭제 |
| GET | `/api/v1/architectures/{id}/layer-dependency-rules` | LayerDependencyRule 목록 |
| POST | `/api/v1/architectures/{id}/layer-dependency-rules` | LayerDependencyRule 생성 |
| PATCH | `/api/v1/architectures/{id}/layer-dependency-rules/{ldrId}` | LayerDependencyRule 수정 |
| DELETE | `/api/v1/architectures/{id}/layer-dependency-rules/{ldrId}` | LayerDependencyRule 삭제 |
| GET | `/api/v1/coding-rules/{id}/examples` | RuleExample 목록 |
| POST | `/api/v1/coding-rules/{id}/examples` | RuleExample 생성 |
| PATCH | `/api/v1/coding-rules/{id}/examples/{exId}` | RuleExample 수정 |
| DELETE | `/api/v1/coding-rules/{id}/examples/{exId}` | RuleExample 삭제 |
| GET | `/api/v1/coding-rules/{id}/checklist-items` | ChecklistItem 목록 |
| POST | `/api/v1/coding-rules/{id}/checklist-items` | ChecklistItem 생성 |
| PATCH | `/api/v1/coding-rules/{id}/checklist-items/{ciId}` | ChecklistItem 수정 |
| DELETE | `/api/v1/coding-rules/{id}/checklist-items/{ciId}` | ChecklistItem 삭제 |
| GET | `/api/v1/coding-rules/{id}/zero-tolerance-rule` | ⭐ ZeroToleranceRule 조회 (1:1) |
| PUT | `/api/v1/coding-rules/{id}/zero-tolerance-rule` | ⭐ ZeroToleranceRule 생성/수정 (Upsert) |
| DELETE | `/api/v1/coding-rules/{id}/zero-tolerance-rule` | ZeroToleranceRule 삭제 |

### 참조 데이터 API (Reference Data)

| Method | Endpoint | Params | 설명 |
|--------|----------|--------|------|
| GET | `/api/ref/module-types` | - | ⭐ ModuleType 전체 목록 (읽기 전용) |
| GET | `/api/ref/module-types/{id}` | - | ModuleType 단건 |
| GET | `/api/ref/package-purposes` | `moduleTypeId=1\|2\|...` | ⭐ PackagePurpose 필터 조회 |
| GET | `/api/ref/package-purposes/{id}` | - | PackagePurpose 단건 |
| POST | `/api/ref/package-purposes` | - | PackagePurpose 생성 (Admin) |
| PATCH | `/api/ref/package-purposes/{id}` | - | PackagePurpose 수정/삭제 (Admin) |

> **참조 데이터 특징**:
> - **ModuleType**: 코드 테이블 (DOMAIN, APPLICATION, ADAPTER_IN, ADAPTER_OUT, COMMON, INFRASTRUCTURE)
> - **PackagePurpose**: moduleTypeId (FK → ModuleType) 참조, PackageStructure.purposeId에서 참조
> - **Module**: moduleTypeId (FK → ModuleType) 참조
> - Admin UI에서만 CRUD, 일반 API는 조회 위주

### FastMCP Specialized (Read-Only)

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/v1/mcp/convention-tree/{conventionId}` | ⭐ 최적화된 전체 트리 |
| GET | `/api/v1/mcp/zero-tolerance-rules` | ⭐ 모든 ZT 규칙 (conventionId 필터) |
| GET | `/api/v1/mcp/class-templates` | 타입별 템플릿 (type, conventionId) |
| GET | `/api/v1/mcp/coding-rules` | 카테고리별 규칙 (category, conventionId) |
| GET | `/api/v1/mcp/search` | 통합 검색 (q, conventionId) |

---

## Jira 등록 정보

- **Epic**: **AESA-76** - [CONV] MCP Convention API Server 개발 (Full CRUD)
- **Epic 설명**: FastMCP 서버 + Admin UI가 호출할 코딩 컨벤션 REST API 서버 개발
- **Labels**: `mcp`, `api`, `convention`, `backend`, `crud`
- **Jira URL**: <https://ryuqqq.atlassian.net/browse/AESA-76>

### Sub-tasks

| Task ID | Jira Key | 제목 | 예상 크기 |
|---------|----------|------|----------|
| TASK-1 | AESA-77 | API 공통 모듈 설정 | ~10K |
| TASK-2 | - | TechStack + Architecture CRUD | ~12K |
| TASK-3 | - | Convention CRUD + 트리 조회 | ~12K |
| TASK-4 | - | Module CRUD + 트리 구조 | ~12K |
| TASK-5 | - | CodingRule CRUD + ZeroToleranceRule + Sub-resources | ~14K |
| TASK-6 | - | ClassTemplate + ArchUnitTest CRUD | ~12K |
| TASK-7 | - | 참조 데이터 (ModuleType, PackagePurpose) + LayerDependencyRule | ~10K |
| TASK-8 | - | MCP 특화 Aggregated APIs | ~10K |

---

## 기술 스택

- **Framework**: Spring Boot 3.5.x
- **Language**: Java 21
- **Database**: MySQL 8.x (via Spring Data JPA + QueryDSL)
- **Architecture**: Hexagonal (Ports & Adapters)
- **API Pattern**: RESTful, CQRS (Command/Query 분리)
- **Soft Delete**: deletedAt 필드 (DELETE 메서드 미사용, PATCH로 상태 변경)
- **Sub-resource DELETE**: Tier 3 엔티티는 실제 DELETE 허용 (부모 통해 관리)

---

## Response DTO 설계 가이드

### Depth 별 응답 구조

```java
// depth=0 (기본)
public record TechStackResponse(
    Long id,
    String name,
    String version,
    LocalDateTime createdAt
) {}

// depth=1 (직계 자식 포함)
public record TechStackWithArchitecturesResponse(
    Long id,
    String name,
    String version,
    List<ArchitectureSummary> architectures
) {}

// depth=full (전체 트리)
public record TechStackTreeResponse(
    Long id,
    String name,
    String version,
    List<ArchitectureTreeResponse> architectures
) {}
```

### Expand 처리 예시

```java
// expand=codingRules
public record ConventionWithRulesResponse(
    Long id,
    String name,
    List<CodingRuleSummary> codingRules
) {}

// expand=codingRules,classTemplates
public record ConventionExpandedResponse(
    Long id,
    String name,
    List<CodingRuleSummary> codingRules,
    List<ClassTemplateSummary> classTemplates
) {}
```

---

## 🚫 BAD EXAMPLES (Anti-Patterns)

### EXC-BAD-001: 도메인 예외 클래스 분리 금지

**규칙**: 도메인 예외는 오직 `DomainException` 하나만 사용한다. 상황별 예외 클래스를 만들지 않는다.

#### ❌ BAD: 상황별 예외 클래스 생성

```java
// 절대 이렇게 하지 마세요!
public class EntityNotFoundException extends RuntimeException { }
public class DeletionConstraintException extends RuntimeException { }
public class DuplicateEntityException extends RuntimeException { }
```

#### ✅ GOOD: DomainException 하나만 사용

```java
public class DomainException extends RuntimeException {
    private final String errorCode;

    public DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

// 사용 예시
throw new DomainException("ENTITY_NOT_FOUND", "Entity not found: " + id);
throw new DomainException("DELETION_CONSTRAINT", "Cannot delete: has children");
```

### EXC-BAD-002: 독립 Sub-resource 엔드포인트 금지

**규칙**: Tier 3 엔티티는 부모의 Sub-resource로만 접근한다.

#### ❌ BAD: 독립 엔드포인트

```java
// 절대 이렇게 하지 마세요!
GET /api/v1/rule-examples
GET /api/v1/rule-examples/{id}
POST /api/v1/rule-examples
```

#### ✅ GOOD: Sub-resource 패턴

```java
GET /api/v1/coding-rules/{ruleId}/examples
POST /api/v1/coding-rules/{ruleId}/examples
PATCH /api/v1/coding-rules/{ruleId}/examples/{exId}
```

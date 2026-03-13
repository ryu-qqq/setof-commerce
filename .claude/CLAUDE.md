# MarketPlace - Claude Code Configuration

이 프로젝트는 **SPRING_BOOT 3.5.x + JAVA 21** 기반의 **hexagonal-multimodule** 프로젝트입니다.

---

## 🏗️ 아키텍처 개요

```text
│  DOMAIN          │  Domain Layer                    │
│  APPLICATION     │  Application Layer               │
│  ADAPTER_OUT     │  Adapter-Out Layer               │
│  ADAPTER_IN      │  Adapter-In Layer                │
│  BOOTSTRAP       │  Bootstrap Layer                 │
```

**아키텍처 원칙**: DIP, SRP, OCP, ISP, CQRS, DDD

---

## 🔴 핵심 모듈 vs 레거시 모듈 (반드시 준수)

### 모듈 구분

| 구분 | 모듈 | 설명 |
|------|------|------|
| **핵심 (New)** | `domain`, `application` | 새로 설계한 고도화/리팩토링 모듈. **진실의 원천** |
| **레거시** | `rest-api v1`, `rest-api-admin v1`, `persistence-mysql-legacy` | 기존 운영 코드. 점진적 마이그레이션 대상 |
| **어댑터** | `rest-api v2`, `rest-api-admin v2`, `persistence-mysql` | 핵심과 외부를 연결하는 브릿지 |

### 의존성 방향 원칙

> **Domain과 Application은 독립적인 핵심이다. 절대로 레거시에 맞추지 않는다.**

```text
[레거시 요청] → Adapter-In(Mapper) → Application UseCase → Domain
                                                              ↓
[레거시 DB] ← Adapter-Out(Mapper) ← Domain 객체 반환 ← Domain
```

**인바운드 (외부 → 내부)**
- 레거시 v1 Controller나 외부 요청이 들어오면, **Adapter-In의 Mapper가 변환**하여 Application UseCase 형태에 맞춘다
- Application의 UseCase 시그니처를 레거시 요청 형태에 맞추는 것은 **금지**

**아웃바운드 (내부 → 외부)**
- Domain 객체를 그대로 반환하면, **Adapter-Out의 Mapper/Repository가 자기 DB 구조에 맞춰 저장**한다
- Domain 객체의 필드나 구조를 레거시 DB 스키마에 맞추는 것은 **금지**

### 위반 사례 (하지 말 것)

```java
// ❌ Domain 객체에 레거시 DB 컬럼명을 그대로 반영
public class Product {
    private Long old_product_seq;  // 레거시 PK 이름 그대로 사용
}

// ❌ Application UseCase가 레거시 DTO를 직접 받음
public interface RegisterProductUseCase {
    void execute(LegacyProductRequest request);  // 레거시 DTO 의존
}

// ✅ Adapter-In Mapper가 변환
// Controller(v1) → Mapper.toCommand(legacyRequest) → UseCase.execute(command)

// ✅ Adapter-Out Mapper가 변환
// Domain Product → PersistenceMapper.toEntity(product) → LegacyEntity 저장
```

---

## 🧰 MCP 도구 사용법

이 프로젝트의 코딩 컨벤션은 **Convention Hub DB**에서 관리됩니다.
코드 작성 시 반드시 MCP 도구를 사용하여 규칙을 조회하세요.

### 3-Phase 워크플로우

```text
┌─────────────────────────────────────────────────────────────┐
│  1️⃣ PLANNING PHASE                                          │
│     planning_context(layers=[...])                          │
│     → 레이어는 list_tech_stacks()로 먼저 조회                 │
├─────────────────────────────────────────────────────────────┤
│  2️⃣ EXECUTION PHASE                                         │
│     module_context(module_id=N, class_type="...")           │
│     → 템플릿 + 규칙 기반 코드 생성                            │
├─────────────────────────────────────────────────────────────┤
│  3️⃣ VALIDATION PHASE                                        │
│     validation_context(layers=[...])                        │
│     → Zero-Tolerance + Checklist 검증                       │
└─────────────────────────────────────────────────────────────┘
```

### 사용 예시

```python
# 0. 먼저 레이어 목록 조회 (하드코딩 금지!)
list_tech_stacks()
# → layers: ["DOMAIN", "APPLICATION", "ADAPTER_OUT", "ADAPTER_IN", "BOOTSTRAP"]

# 1. 개발 계획 수립
planning_context(layers=["DOMAIN", "APPLICATION"])

# 2. 코드 생성
module_context(module_id=1, class_type="AGGREGATE")

# 3. 코드 검증
validation_context(layers=["DOMAIN"])
```

---

## 🚨 Zero-Tolerance 규칙

> ⚠️ **중요**: 규칙은 DB에서 조회하세요.

```python
# Zero-Tolerance 규칙 조회 (레이어는 동적으로!)
validation_context(layers=["DOMAIN", "APPLICATION", "ADAPTER_OUT", "ADAPTER_IN", "BOOTSTRAP"])
```

### 주요 규칙 (요약)

> 상세 규칙은 MCP `validation_context()` 또는 `get_rule()` 로 조회

MCP를 통해 최신 규칙을 동적으로 조회하세요.
하드코딩된 규칙은 DB 변경 시 outdated 될 수 있습니다.

---

## 📚 MCP Tools 목록

| 분류 | Tool | 용도 |
|------|------|------|
| **워크플로우** | planning_context | 개발 계획 수립 |
|  | module_context | 코드 생성 (템플릿 + 규칙) |
|  | validation_context | 코드 검증 (Zero-Tolerance) |
| **컨텍스트** | get_context | 빠른 컨텍스트 조회 |
|  | get_rule | 규칙 상세 + 예시 |
| **계층** | list_tech_stacks | 기술 스택 + 레이어 목록 |
|  | get_architecture | 아키텍처 상세 |
|  | get_layer_detail | 레이어 상세 |

---

## 🔧 설계 원칙

MCP 서버는 **순수 정보 브릿지**로 설계되었습니다:

- MCP = 규칙/템플릿 전달 (Spring API → LLM)
- **LLM은 규칙을 반드시 준수**하며 코드 생성
- 규칙을 "판단"하지 않고 **100% 준수**

---

## ⚡ 빠른 시작

```python
# 1. 레이어 목록 조회
layers = list_tech_stacks()  # → ["DOMAIN", "APPLICATION", ...]

# 2. Aggregate 생성 시
planning_context(layers=["DOMAIN"])
module_context(module_id=1, class_type="AGGREGATE")
validation_context(layers=["DOMAIN"])

# 3. UseCase 생성 시
module_context(module_id=2, class_type="USE_CASE")
```

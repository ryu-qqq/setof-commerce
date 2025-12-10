# Persistence Layer Rules - 09. Admin Query Adapter

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/adapter/query/admin/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "ADMIN-ADAPTER-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 반드시 클래스여야 한다 (interface 금지)",
  "pattern": "public class {Bc}AdminQueryAdapter implements {Bc}AdminQueryPort",
  "antiPattern": "public interface {Bc}AdminQueryAdapter"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-002",
  "name": "@Component 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 반드시 @Component 어노테이션을 가져야 한다",
  "pattern": "@Component\npublic class {Bc}AdminQueryAdapter implements {Bc}AdminQueryPort",
  "antiPattern": "public class {Bc}AdminQueryAdapter // @Component 없음"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-003",
  "name": "*AdminQueryAdapter 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 반드시 *AdminQueryAdapter로 끝나야 한다",
  "pattern": "public class OrderAdminQueryAdapter",
  "antiPattern": "public class AdminOrderQueryAdapter, OrderAdminAdapter"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-004",
  "name": "Port 인터페이스 구현 필수",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 반드시 *AdminQueryPort 인터페이스를 구현해야 한다",
  "pattern": "public class {Bc}AdminQueryAdapter implements {Bc}AdminQueryPort",
  "antiPattern": "public class {Bc}AdminQueryAdapter // Port 구현 없음"
}
```

### 2. 필드 규칙 (1-2개)

```json
{
  "ruleId": "ADMIN-ADAPTER-005",
  "name": "AdminQueryDslRepository 필드 필수",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 반드시 AdminQueryDslRepository 필드를 가져야 한다",
  "pattern": "private final {Bc}AdminQueryDslRepository repository;",
  "antiPattern": "// AdminQueryDslRepository 필드 없음"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-006",
  "name": "Mapper 필드 선택적",
  "severity": "INFO",
  "description": "Admin Query Adapter에서 Mapper는 선택적 (DTO Projection 직접 반환 시 불필요)",
  "pattern": "private final {Bc}AdminQueryDslRepository repository; // Mapper 없이도 가능",
  "antiPattern": "// Mapper가 반드시 필요하다고 생각하는 것"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-007",
  "name": "final 필드 (생성자 주입)",
  "severity": "CRITICAL",
  "description": "모든 필드는 final로 선언하여 생성자 주입",
  "pattern": "private final {Bc}AdminQueryDslRepository repository;",
  "antiPattern": "private {Bc}AdminQueryDslRepository repository; // final 아님"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-008",
  "name": "다른 Repository 주입 금지 (1:1 매핑)",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 하나의 AdminQueryDslRepository에만 1:1 매핑",
  "pattern": "// AdminQueryDslRepository만 의존 (1:1 매핑)",
  "antiPattern": "private final CustomerAdminQueryDslRepository customerRepository; // 다른 Repository 금지"
}
```

### 3. 메서드 규칙 (제한 없음)

```json
{
  "ruleId": "ADMIN-ADAPTER-009",
  "name": "메서드 개수 제한 없음",
  "severity": "INFO",
  "description": "Admin Query Adapter는 메서드 개수 제한 없음 (관리자 요구사항에 따라 자유롭게)",
  "pattern": "// 관리자 요구사항에 맞는 다양한 조회 메서드 정의 가능",
  "antiPattern": "// 4개 메서드만 정의 (일반 QueryAdapter와 혼동)"
}
```

### 4. 반환 타입 규칙

```json
{
  "ruleId": "ADMIN-ADAPTER-010",
  "name": "DTO Projection 반환",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 DTO를 반환해야 한다 (Repository에서 Projection된 DTO)",
  "pattern": "public List<{Bc}AdminDto> findForAdminList(...)",
  "antiPattern": "public List<{Bc}> findForAdminList(...) // Domain 반환 금지"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-011",
  "name": "Domain 반환 금지",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 Domain 반환 금지 (DTO만)",
  "pattern": "public List<{Bc}AdminDto> findForAdminList(...)",
  "antiPattern": "return repository.findForAdminList(...).stream().map(mapper::toDomain)..."
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-012",
  "name": "Entity 반환 금지",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 Entity 반환 금지",
  "pattern": "return repository.findForAdminList(criteria); // DTO 직접 반환",
  "antiPattern": "return repository.findForAdminList(...); // Entity 반환 금지"
}
```

### 5. 금지 규칙

```json
{
  "ruleId": "ADMIN-ADAPTER-013",
  "name": "@Transactional 절대 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer(UseCase)에서 관리",
  "pattern": "@Component\npublic class {Bc}AdminQueryAdapter",
  "antiPattern": "@Component\n@Transactional\npublic class {Bc}AdminQueryAdapter"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-014",
  "name": "Command 메서드 금지",
  "severity": "CRITICAL",
  "description": "저장/수정/삭제 메서드 금지 (CommandAdapter로 분리)",
  "pattern": "// 조회 메서드만 존재",
  "antiPattern": "public void save(...), persist(...), update(...), delete(...)"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-015",
  "name": "비즈니스 로직 금지",
  "severity": "CRITICAL",
  "description": "비즈니스 로직 수행 금지 (단순 위임만)",
  "pattern": "return repository.findForAdminList(criteria);",
  "antiPattern": "if (result.isEmpty()) throw new Exception(); // 비즈니스 로직 금지"
}
```

### 6. 구현 패턴 규칙

```json
{
  "ruleId": "ADMIN-ADAPTER-016",
  "name": "Repository 위임만",
  "severity": "CRITICAL",
  "description": "Admin Query Adapter는 Repository에 단순 위임만 수행",
  "pattern": "return repository.findForAdminList(criteria);",
  "antiPattern": "// Adapter에서 추가 가공 로직"
}
```

```json
{
  "ruleId": "ADMIN-ADAPTER-017",
  "name": "DTO Projection은 Repository에서",
  "severity": "CRITICAL",
  "description": "DTO 변환/Projection은 AdminQueryDslRepository에서 처리",
  "pattern": "// Repository가 DTO를 직접 반환, Adapter는 위임만",
  "antiPattern": "// Adapter에서 Entity → DTO 변환"
}
```

---

## 템플릿 코드

```java
@Component
public class {Bc}AdminQueryAdapter implements {Bc}AdminQueryPort {

    private final {Bc}AdminQueryDslRepository repository;

    public {Bc}AdminQueryAdapter({Bc}AdminQueryDslRepository repository) {
        this.repository = repository;
    }

    /**
     * 관리자용 목록 조회 (Join 포함, DTO 반환)
     */
    @Override
    public List<{Bc}AdminDto> findForAdminList({Bc}AdminSearchCriteria criteria) {
        return repository.findForAdminList(criteria);
    }

    /**
     * 관리자용 개수 조회
     */
    @Override
    public long countForAdminList({Bc}AdminSearchCriteria criteria) {
        return repository.countForAdminList(criteria);
    }

    /**
     * 관리자용 통계 조회
     */
    @Override
    public {Bc}StatisticsDto findStatistics({Bc}StatisticsCriteria criteria) {
        return repository.findStatistics(criteria);
    }
}
```

---

## 일반 QueryAdapter vs Admin QueryAdapter 비교

| 항목 | QueryAdapter | AdminQueryAdapter |
|------|-------------|-------------------|
| **메서드 수** | 4개 고정 | 제한 없음 |
| **반환 타입** | Domain | DTO Projection |
| **Mapper** | 필수 | 선택적 (DTO 직접 반환) |
| **Repository** | QueryDslRepository | AdminQueryDslRepository |
| **용도** | 일반 사용자 조회 | 관리자 조회 |
| **네이밍** | *QueryAdapter | *AdminQueryAdapter |

---

## 체크리스트

Admin Query Adapter 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Component 어노테이션
- [ ] *AdminQueryAdapter 네이밍
- [ ] *AdminQueryPort 인터페이스 구현
- [ ] AdminQueryDslRepository 필드 존재
- [ ] 다른 Repository 의존 안 함 (1:1 매핑)
- [ ] DTO 반환 (Domain/Entity 아님)
- [ ] @Transactional 어노테이션 없음
- [ ] Command 메서드 없음
- [ ] 단순 위임만 (비즈니스 로직 없음)

---

**총 규칙 수**: 17개
**작성일**: 2025-12-08

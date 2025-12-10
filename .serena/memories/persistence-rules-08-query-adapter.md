# Persistence Layer Rules - 08. Query Adapter

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/adapter/query/general/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "QRY-ADAPTER-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "Query Adapter는 반드시 클래스여야 한다 (interface 금지)",
  "pattern": "public class {Bc}QueryAdapter implements {Bc}QueryPort",
  "antiPattern": "public interface {Bc}QueryAdapter"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-002",
  "name": "@Component 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Query Adapter는 반드시 @Component 어노테이션을 가져야 한다",
  "pattern": "@Component\npublic class {Bc}QueryAdapter implements {Bc}QueryPort",
  "antiPattern": "public class {Bc}QueryAdapter // @Component 없음"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-003",
  "name": "*QueryAdapter 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 반드시 *QueryAdapter로 끝나야 한다",
  "pattern": "public class OrderQueryAdapter",
  "antiPattern": "public class OrderReadAdapter, OrderSearchAdapter"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-004",
  "name": "Port 인터페이스 구현 필수",
  "severity": "CRITICAL",
  "description": "Query Adapter는 반드시 *QueryPort 인터페이스를 구현해야 한다",
  "pattern": "public class {Bc}QueryAdapter implements {Bc}QueryPort",
  "antiPattern": "public class {Bc}QueryAdapter // Port 구현 없음"
}
```

### 2. 필드 규칙 (정확히 2개)

```json
{
  "ruleId": "QRY-ADAPTER-005",
  "name": "필드 개수: 정확히 2개",
  "severity": "CRITICAL",
  "description": "Query Adapter는 정확히 2개의 필드만 가져야 한다 (QueryDslRepository + Mapper)",
  "pattern": "private final {Bc}QueryDslRepository repository;\nprivate final {Bc}JpaEntityMapper mapper;",
  "antiPattern": "private final OtherRepository otherRepository; // 3개 이상 필드 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-006",
  "name": "QueryDslRepository 필드 필수",
  "severity": "CRITICAL",
  "description": "Query Adapter는 반드시 QueryDslRepository 필드를 가져야 한다",
  "pattern": "private final {Bc}QueryDslRepository repository;",
  "antiPattern": "// QueryDslRepository 필드 없음"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-007",
  "name": "Mapper 필드 필수",
  "severity": "CRITICAL",
  "description": "Query Adapter는 반드시 Mapper 필드를 가져야 한다",
  "pattern": "private final {Bc}JpaEntityMapper mapper;",
  "antiPattern": "// Mapper 필드 없음"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-008",
  "name": "final 필드 (생성자 주입)",
  "severity": "CRITICAL",
  "description": "모든 필드는 final로 선언하여 생성자 주입",
  "pattern": "private final {Bc}QueryDslRepository repository;",
  "antiPattern": "private {Bc}QueryDslRepository repository; // final 아님"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-009",
  "name": "다른 Repository 주입 금지 (1:1 매핑)",
  "severity": "CRITICAL",
  "description": "Query Adapter는 하나의 QueryDslRepository에만 1:1 매핑",
  "pattern": "// 2개 필드만: QueryDslRepository + Mapper",
  "antiPattern": "private final CustomerQueryDslRepository customerRepository; // 다른 Repository 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-010",
  "name": "JPAQueryFactory 직접 의존 금지",
  "severity": "CRITICAL",
  "description": "JPAQueryFactory는 Repository에서만 사용, Adapter에서 직접 사용 금지",
  "pattern": "// QueryDslRepository만 의존",
  "antiPattern": "private final JPAQueryFactory queryFactory; // 금지"
}
```

### 3. 메서드 규칙 (정확히 4개)

```json
{
  "ruleId": "QRY-ADAPTER-011",
  "name": "public 메서드: 정확히 4개",
  "severity": "CRITICAL",
  "description": "Query Adapter는 정확히 4개의 public 메서드만 가져야 한다",
  "pattern": "findById, existsById, findByCriteria, countByCriteria",
  "antiPattern": "findByName, findByStatus 등 추가 메서드 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-012",
  "name": "findById() 메서드 필수",
  "severity": "CRITICAL",
  "description": "ID로 단건 조회하는 findById() 메서드 필수",
  "pattern": "public Optional<{Bc}> findById({Bc}Id id)",
  "antiPattern": "// findById 메서드 없음"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-013",
  "name": "existsById() 메서드 필수",
  "severity": "CRITICAL",
  "description": "ID로 존재 여부 확인하는 existsById() 메서드 필수",
  "pattern": "public boolean existsById({Bc}Id id)",
  "antiPattern": "// existsById 메서드 없음"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-014",
  "name": "findByCriteria() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Criteria로 목록 조회하는 findByCriteria() 메서드 필수",
  "pattern": "public List<{Bc}> findByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "// findByCriteria 메서드 없음"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-015",
  "name": "countByCriteria() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Criteria로 개수 조회하는 countByCriteria() 메서드 필수",
  "pattern": "public long countByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "// countByCriteria 메서드 없음"
}
```

### 4. 반환 타입 규칙

```json
{
  "ruleId": "QRY-ADAPTER-016",
  "name": "Domain 반환 (DTO 금지)",
  "severity": "CRITICAL",
  "description": "Query Adapter는 Domain 객체를 반환해야 한다 (DTO 금지)",
  "pattern": "public Optional<{Bc}> findById({Bc}Id id)",
  "antiPattern": "public Optional<{Bc}Dto> findById(...) // DTO 반환 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-017",
  "name": "Entity 반환 금지",
  "severity": "CRITICAL",
  "description": "Query Adapter는 Entity 직접 반환 금지 (Mapper로 Domain 변환)",
  "pattern": "return repository.findById(id).map(mapper::toDomain);",
  "antiPattern": "return repository.findById(id); // Entity 반환 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-018",
  "name": "findById 반환: Optional<Domain>",
  "severity": "CRITICAL",
  "description": "findById는 Optional<Domain> 반환",
  "pattern": "public Optional<{Bc}> findById({Bc}Id id)",
  "antiPattern": "public {Bc} findById({Bc}Id id) // null 가능성"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-019",
  "name": "existsById 반환: boolean",
  "severity": "CRITICAL",
  "description": "existsById는 boolean (primitive) 반환",
  "pattern": "public boolean existsById({Bc}Id id)",
  "antiPattern": "public Boolean existsById(...) // 박싱 타입 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-020",
  "name": "findByCriteria 반환: List<Domain>",
  "severity": "CRITICAL",
  "description": "findByCriteria는 List<Domain> 반환",
  "pattern": "public List<{Bc}> findByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "public Page<{Bc}> findByCriteria(...) // Page 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-021",
  "name": "countByCriteria 반환: long",
  "severity": "CRITICAL",
  "description": "countByCriteria는 long (primitive) 반환",
  "pattern": "public long countByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "public Long countByCriteria(...) // 박싱 타입 금지"
}
```

### 5. 금지 규칙

```json
{
  "ruleId": "QRY-ADAPTER-022",
  "name": "@Transactional 절대 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer(UseCase)에서 관리",
  "pattern": "@Component\npublic class {Bc}QueryAdapter",
  "antiPattern": "@Component\n@Transactional\npublic class {Bc}QueryAdapter"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-023",
  "name": "Command 메서드 금지",
  "severity": "CRITICAL",
  "description": "저장/수정/삭제 메서드 금지 (CommandAdapter로 분리)",
  "pattern": "// 4개 조회 메서드만 존재",
  "antiPattern": "public void save(...), persist(...), update(...), delete(...)"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-024",
  "name": "비즈니스 로직 금지",
  "severity": "CRITICAL",
  "description": "비즈니스 로직 수행 금지 (단순 위임 + 변환만)",
  "pattern": "return repository.findById(id.getValue()).map(mapper::toDomain);",
  "antiPattern": "if (domain.getStatus() == ACTIVE) { ... } // 비즈니스 로직 금지"
}
```

### 6. 구현 패턴 규칙

```json
{
  "ruleId": "QRY-ADAPTER-025",
  "name": "Value Object → primitive 변환",
  "severity": "CRITICAL",
  "description": "Repository 호출 시 VO를 primitive로 변환",
  "pattern": "repository.findById(id.getValue())",
  "antiPattern": "repository.findById(id) // VO 그대로 전달 금지"
}
```

```json
{
  "ruleId": "QRY-ADAPTER-026",
  "name": "mapper::toDomain 사용",
  "severity": "CRITICAL",
  "description": "Entity → Domain 변환 시 mapper.toDomain() 사용",
  "pattern": ".map(mapper::toDomain)",
  "antiPattern": ".map(entity -> new {Bc}(...)) // 직접 생성 금지"
}
```

---

## 템플릿 코드

```java
@Component
public class {Bc}QueryAdapter implements {Bc}QueryPort {

    private final {Bc}QueryDslRepository repository;
    private final {Bc}JpaEntityMapper mapper;

    public {Bc}QueryAdapter(
        {Bc}QueryDslRepository repository,
        {Bc}JpaEntityMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<{Bc}> findById({Bc}Id id) {
        return repository.findById(id.getValue())
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsById({Bc}Id id) {
        return repository.existsById(id.getValue());
    }

    @Override
    public List<{Bc}> findByCriteria({Bc}SearchCriteria criteria) {
        return repository.findByCriteria(criteria).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public long countByCriteria({Bc}SearchCriteria criteria) {
        return repository.countByCriteria(criteria);
    }
}
```

---

## 체크리스트

Query Adapter 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Component 어노테이션
- [ ] *QueryAdapter 네이밍
- [ ] *QueryPort 인터페이스 구현
- [ ] 정확히 2개 필드 (QueryDslRepository + Mapper)
- [ ] 정확히 4개 public 메서드
- [ ] Domain 반환 (DTO/Entity 아님)
- [ ] @Transactional 어노테이션 없음
- [ ] Command 메서드 없음
- [ ] JPAQueryFactory 직접 의존 안 함

---

**총 규칙 수**: 26개
**작성일**: 2025-12-08

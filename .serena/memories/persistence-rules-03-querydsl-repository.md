# Persistence Layer Rules - 03. QueryDSL Repository

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/repository/querydsl/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "QDSL-REPO-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "QueryDSL Repository는 반드시 클래스여야 한다 (interface 금지)",
  "pattern": "public class {Bc}QueryDslRepository",
  "antiPattern": "public interface {Bc}QueryDslRepository"
}
```

```json
{
  "ruleId": "QDSL-REPO-002",
  "name": "@Repository 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "QueryDSL Repository는 반드시 @Repository 어노테이션을 가져야 한다",
  "pattern": "@Repository\npublic class {Bc}QueryDslRepository",
  "antiPattern": "public class {Bc}QueryDslRepository // @Repository 없음"
}
```

```json
{
  "ruleId": "QDSL-REPO-003",
  "name": "*QueryDslRepository 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 반드시 *QueryDslRepository로 끝나야 한다",
  "pattern": "public class OrderQueryDslRepository",
  "antiPattern": "public class OrderQueryRepository, OrderDslRepository"
}
```

### 2. 필드 규칙

```json
{
  "ruleId": "QDSL-REPO-004",
  "name": "JPAQueryFactory 필드 필수",
  "severity": "CRITICAL",
  "description": "QueryDSL Repository는 반드시 JPAQueryFactory 필드를 가져야 한다",
  "pattern": "private final JPAQueryFactory queryFactory;",
  "antiPattern": "// JPAQueryFactory 없이 EntityManager만 사용"
}
```

```json
{
  "ruleId": "QDSL-REPO-005",
  "name": "QType static final 필드 필수",
  "severity": "CRITICAL",
  "description": "Q-Type 필드는 반드시 private static final로 선언",
  "pattern": "private static final Q{Bc}JpaEntity {bc} = Q{Bc}JpaEntity.{bc}JpaEntity;",
  "antiPattern": "private Q{Bc}JpaEntity {bc} = ...; // static final 아님"
}
```

```json
{
  "ruleId": "QDSL-REPO-006",
  "name": "final 필드 (생성자 주입)",
  "severity": "CRITICAL",
  "description": "모든 필드는 final로 선언하여 생성자 주입",
  "pattern": "private final JPAQueryFactory queryFactory;",
  "antiPattern": "private JPAQueryFactory queryFactory; // final 아님"
}
```

### 3. 메서드 규칙 (4개 고정)

```json
{
  "ruleId": "QDSL-REPO-007",
  "name": "메서드 4개 고정",
  "severity": "CRITICAL",
  "description": "QueryDSL Repository는 정확히 4개의 public 메서드만 허용",
  "pattern": "findById, existsById, findByCriteria, countByCriteria",
  "antiPattern": "findByName, findByStatus, countByName 등 추가 메서드"
}
```

```json
{
  "ruleId": "QDSL-REPO-008",
  "name": "findById() 메서드 필수",
  "severity": "CRITICAL",
  "description": "ID로 단건 조회하는 findById() 메서드 필수",
  "pattern": "public Optional<{Bc}JpaEntity> findById(Long id)",
  "antiPattern": "// findById 메서드 없음"
}
```

```json
{
  "ruleId": "QDSL-REPO-009",
  "name": "existsById() 메서드 필수",
  "severity": "CRITICAL",
  "description": "ID로 존재 여부 확인하는 existsById() 메서드 필수",
  "pattern": "public boolean existsById(Long id)",
  "antiPattern": "// existsById 메서드 없음"
}
```

```json
{
  "ruleId": "QDSL-REPO-010",
  "name": "findByCriteria() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Criteria로 목록 조회하는 findByCriteria() 메서드 필수",
  "pattern": "public List<{Bc}JpaEntity> findByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "// findByCriteria 메서드 없음"
}
```

```json
{
  "ruleId": "QDSL-REPO-011",
  "name": "countByCriteria() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Criteria로 개수 조회하는 countByCriteria() 메서드 필수",
  "pattern": "public long countByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "// countByCriteria 메서드 없음"
}
```

### 4. 금지 규칙 - Join

```json
{
  "ruleId": "QDSL-REPO-012",
  "name": "Join 절대 금지",
  "severity": "CRITICAL",
  "description": "QueryDSL Repository에서 Join 절대 금지 (AdminQueryDslRepository로)",
  "pattern": "queryFactory.selectFrom({bc}).where(...)",
  "antiPattern": "queryFactory.selectFrom({bc}).join({bc}.customer, customer)"
}
```

```json
{
  "ruleId": "QDSL-REPO-013",
  "name": "leftJoin 금지",
  "severity": "CRITICAL",
  "description": "leftJoin 절대 금지",
  "pattern": "// Join 없이 단일 Entity 조회",
  "antiPattern": ".leftJoin({bc}.items, item)"
}
```

```json
{
  "ruleId": "QDSL-REPO-014",
  "name": "rightJoin 금지",
  "severity": "CRITICAL",
  "description": "rightJoin 절대 금지",
  "pattern": "// Join 없이 단일 Entity 조회",
  "antiPattern": ".rightJoin(...)"
}
```

```json
{
  "ruleId": "QDSL-REPO-015",
  "name": "innerJoin 금지",
  "severity": "CRITICAL",
  "description": "innerJoin 절대 금지",
  "pattern": "// Join 없이 단일 Entity 조회",
  "antiPattern": ".innerJoin(...)"
}
```

```json
{
  "ruleId": "QDSL-REPO-016",
  "name": "fetchJoin 금지",
  "severity": "CRITICAL",
  "description": "fetchJoin 절대 금지",
  "pattern": "// Join 없이 단일 Entity 조회",
  "antiPattern": ".fetchJoin()"
}
```

### 5. 금지 규칙 - 기타

```json
{
  "ruleId": "QDSL-REPO-017",
  "name": "@Transactional 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer에서 관리",
  "pattern": "@Repository\npublic class {Bc}QueryDslRepository",
  "antiPattern": "@Repository\n@Transactional\npublic class {Bc}QueryDslRepository"
}
```

```json
{
  "ruleId": "QDSL-REPO-018",
  "name": "Mapper 의존성 금지",
  "severity": "CRITICAL",
  "description": "Repository에서 Mapper 의존 금지 (Adapter에서 처리)",
  "pattern": "// JPAQueryFactory만 의존",
  "antiPattern": "private final {Bc}JpaEntityMapper mapper; // 금지"
}
```

```json
{
  "ruleId": "QDSL-REPO-019",
  "name": "Domain 반환 금지",
  "severity": "CRITICAL",
  "description": "Repository는 Entity 반환, Domain 변환은 Adapter에서",
  "pattern": "public Optional<{Bc}JpaEntity> findById(Long id)",
  "antiPattern": "public Optional<{Bc}> findById(Long id) // Domain 반환 금지"
}
```

### 6. 반환 타입 규칙

```json
{
  "ruleId": "QDSL-REPO-020",
  "name": "findById 반환: Optional<Entity>",
  "severity": "CRITICAL",
  "description": "findById는 Optional<Entity> 반환",
  "pattern": "public Optional<{Bc}JpaEntity> findById(Long id)",
  "antiPattern": "public {Bc}JpaEntity findById(Long id) // null 가능성"
}
```

```json
{
  "ruleId": "QDSL-REPO-021",
  "name": "existsById 반환: boolean",
  "severity": "CRITICAL",
  "description": "existsById는 boolean 반환",
  "pattern": "public boolean existsById(Long id)",
  "antiPattern": "public Boolean existsById(Long id) // 박싱 타입 금지"
}
```

```json
{
  "ruleId": "QDSL-REPO-022",
  "name": "findByCriteria 반환: List<Entity>",
  "severity": "CRITICAL",
  "description": "findByCriteria는 List<Entity> 반환",
  "pattern": "public List<{Bc}JpaEntity> findByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "public Page<{Bc}JpaEntity> findByCriteria(...) // Page 금지"
}
```

```json
{
  "ruleId": "QDSL-REPO-023",
  "name": "countByCriteria 반환: long",
  "severity": "CRITICAL",
  "description": "countByCriteria는 long (primitive) 반환",
  "pattern": "public long countByCriteria({Bc}SearchCriteria criteria)",
  "antiPattern": "public Long countByCriteria(...) // 박싱 타입 금지"
}
```

### 7. BooleanBuilder 패턴 규칙

```json
{
  "ruleId": "QDSL-REPO-024",
  "name": "BooleanBuilder 동적 조건 패턴",
  "severity": "RECOMMENDED",
  "description": "동적 조건은 BooleanBuilder 또는 BooleanExpression 사용",
  "pattern": "private BooleanBuilder buildCondition({Bc}SearchCriteria criteria) { ... }",
  "antiPattern": "// if-else로 직접 쿼리 분기"
}
```

---

## 템플릿 코드

```java
@Repository
public class {Bc}QueryDslRepository {

    private static final Q{Bc}JpaEntity {bc} = Q{Bc}JpaEntity.{bc}JpaEntity;

    private final JPAQueryFactory queryFactory;

    public {Bc}QueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Optional<{Bc}JpaEntity> findById(Long id) {
        return Optional.ofNullable(
            queryFactory.selectFrom({bc})
                .where({bc}.id.eq(id))
                .fetchOne()
        );
    }

    public boolean existsById(Long id) {
        return queryFactory.selectOne()
            .from({bc})
            .where({bc}.id.eq(id))
            .fetchFirst() != null;
    }

    public List<{Bc}JpaEntity> findByCriteria({Bc}SearchCriteria criteria) {
        return queryFactory.selectFrom({bc})
            .where(buildCondition(criteria))
            .offset(criteria.offset())
            .limit(criteria.limit())
            .orderBy({bc}.id.desc())
            .fetch();
    }

    public long countByCriteria({Bc}SearchCriteria criteria) {
        return Optional.ofNullable(
            queryFactory.select({bc}.count())
                .from({bc})
                .where(buildCondition(criteria))
                .fetchOne()
        ).orElse(0L);
    }

    private BooleanBuilder buildCondition({Bc}SearchCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();
        // 동적 조건 추가
        return builder;
    }
}
```

---

## 체크리스트

QueryDSL Repository 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Repository 어노테이션
- [ ] *QueryDslRepository 네이밍
- [ ] JPAQueryFactory 필드 존재
- [ ] QType static final 필드
- [ ] 정확히 4개 메서드만 (findById, existsById, findByCriteria, countByCriteria)
- [ ] Join 사용 안 함 (join, leftJoin, fetchJoin 등)
- [ ] @Transactional 어노테이션 없음
- [ ] Mapper 의존성 없음
- [ ] Entity 반환 (Domain 아님)

---

**총 규칙 수**: 24개
**작성일**: 2025-12-08

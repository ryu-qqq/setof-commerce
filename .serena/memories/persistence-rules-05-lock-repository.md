# Persistence Layer Rules - 05. Lock Repository

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/repository/lock/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "LOCK-REPO-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "Lock Repository는 반드시 클래스여야 한다 (interface 금지)",
  "pattern": "public class {Bc}LockRepository",
  "antiPattern": "public interface {Bc}LockRepository"
}
```

```json
{
  "ruleId": "LOCK-REPO-002",
  "name": "@Repository 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Lock Repository는 반드시 @Repository 어노테이션을 가져야 한다",
  "pattern": "@Repository\npublic class {Bc}LockRepository",
  "antiPattern": "public class {Bc}LockRepository // @Repository 없음"
}
```

```json
{
  "ruleId": "LOCK-REPO-003",
  "name": "*LockRepository 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 반드시 *LockRepository로 끝나야 한다",
  "pattern": "public class OrderLockRepository",
  "antiPattern": "public class LockOrderRepository, OrderLockRepo"
}
```

### 2. 필드 규칙

```json
{
  "ruleId": "LOCK-REPO-004",
  "name": "JPAQueryFactory 또는 EntityManager 필드 필수",
  "severity": "CRITICAL",
  "description": "Lock Repository는 JPAQueryFactory 또는 EntityManager 필드를 가져야 한다",
  "pattern": "private final JPAQueryFactory queryFactory;\n// 또는\nprivate final EntityManager entityManager;",
  "antiPattern": "// JPAQueryFactory/EntityManager 없이 구현"
}
```

```json
{
  "ruleId": "LOCK-REPO-005",
  "name": "final 필드 (생성자 주입)",
  "severity": "CRITICAL",
  "description": "모든 필드는 final로 선언하여 생성자 주입",
  "pattern": "private final JPAQueryFactory queryFactory;",
  "antiPattern": "private JPAQueryFactory queryFactory; // final 아님"
}
```

### 3. 메서드 규칙 (Lock 메서드만)

```json
{
  "ruleId": "LOCK-REPO-006",
  "name": "Lock 메서드만 허용",
  "severity": "CRITICAL",
  "description": "Lock Repository는 Lock 관련 메서드만 허용 (일반 조회/Command 금지)",
  "pattern": "findByIdForUpdate, findByIdsForUpdate, findByIdForShare 등",
  "antiPattern": "findById, save, delete, findByCriteria 등 일반 메서드"
}
```

```json
{
  "ruleId": "LOCK-REPO-007",
  "name": "findByIdForUpdate() 메서드",
  "severity": "CRITICAL",
  "description": "Pessimistic Write Lock 단건 조회 메서드 (FOR UPDATE)",
  "pattern": "public Optional<{Bc}JpaEntity> findByIdForUpdate(Long id)",
  "antiPattern": "public {Bc}JpaEntity findByIdForUpdate(Long id) // Optional 필수"
}
```

```json
{
  "ruleId": "LOCK-REPO-008",
  "name": "findByIdsForUpdate() 메서드",
  "severity": "CRITICAL",
  "description": "Pessimistic Write Lock 복수건 조회 메서드 (FOR UPDATE)",
  "pattern": "public List<{Bc}JpaEntity> findByIdsForUpdate(List<Long> ids)",
  "antiPattern": "// 복수건 Lock 조회 없음"
}
```

```json
{
  "ruleId": "LOCK-REPO-009",
  "name": "findByIdForShare() 메서드",
  "severity": "CRITICAL",
  "description": "Pessimistic Read Lock 단건 조회 메서드 (FOR SHARE)",
  "pattern": "public Optional<{Bc}JpaEntity> findByIdForShare(Long id)",
  "antiPattern": "// FOR SHARE Lock 메서드 없음"
}
```

### 4. Lock 구현 규칙

```json
{
  "ruleId": "LOCK-REPO-010",
  "name": "setLockMode(PESSIMISTIC_WRITE) 사용",
  "severity": "CRITICAL",
  "description": "FOR UPDATE는 PESSIMISTIC_WRITE LockModeType 사용",
  "pattern": "queryFactory.selectFrom({bc}).where(...).setLockMode(LockModeType.PESSIMISTIC_WRITE)",
  "antiPattern": "// LockMode 설정 없이 일반 조회"
}
```

```json
{
  "ruleId": "LOCK-REPO-011",
  "name": "setLockMode(PESSIMISTIC_READ) 사용",
  "severity": "CRITICAL",
  "description": "FOR SHARE는 PESSIMISTIC_READ LockModeType 사용",
  "pattern": "queryFactory.selectFrom({bc}).where(...).setLockMode(LockModeType.PESSIMISTIC_READ)",
  "antiPattern": "// LockMode 설정 없이 일반 조회"
}
```

```json
{
  "ruleId": "LOCK-REPO-012",
  "name": "Deadlock 방지: ID 정렬",
  "severity": "CRITICAL",
  "description": "복수건 Lock 시 ID 오름차순 정렬로 Deadlock 방지",
  "pattern": "List<Long> sortedIds = ids.stream().sorted().toList();\nqueryFactory.selectFrom({bc}).where({bc}.id.in(sortedIds)).orderBy({bc}.id.asc())",
  "antiPattern": "// 정렬 없이 Lock (Deadlock 위험)"
}
```

### 5. 금지 규칙

```json
{
  "ruleId": "LOCK-REPO-013",
  "name": "@Transactional 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer에서 관리",
  "pattern": "@Repository\npublic class {Bc}LockRepository",
  "antiPattern": "@Repository\n@Transactional\npublic class {Bc}LockRepository"
}
```

```json
{
  "ruleId": "LOCK-REPO-014",
  "name": "일반 조회 메서드 금지",
  "severity": "CRITICAL",
  "description": "Lock 없는 일반 조회는 QueryDslRepository에서 처리",
  "pattern": "// Lock 메서드만 정의",
  "antiPattern": "public Optional<{Bc}JpaEntity> findById(Long id) // Lock 없는 조회 금지"
}
```

```json
{
  "ruleId": "LOCK-REPO-015",
  "name": "Command 메서드 금지",
  "severity": "CRITICAL",
  "description": "저장/수정/삭제는 JpaRepository/CommandAdapter에서 처리",
  "pattern": "// Lock 조회 메서드만 정의",
  "antiPattern": "public void save({Bc}JpaEntity entity) // Command 메서드 금지"
}
```

```json
{
  "ruleId": "LOCK-REPO-016",
  "name": "Mapper 의존성 금지",
  "severity": "CRITICAL",
  "description": "Repository에서 Mapper 의존 금지 (Adapter에서 처리)",
  "pattern": "// JPAQueryFactory만 의존",
  "antiPattern": "private final {Bc}JpaEntityMapper mapper; // 금지"
}
```

### 6. 반환 타입 규칙

```json
{
  "ruleId": "LOCK-REPO-017",
  "name": "Entity 반환",
  "severity": "CRITICAL",
  "description": "Lock Repository는 Entity 반환, Domain 변환은 Adapter에서",
  "pattern": "public Optional<{Bc}JpaEntity> findByIdForUpdate(Long id)",
  "antiPattern": "public Optional<{Bc}> findByIdForUpdate(Long id) // Domain 반환 금지"
}
```

```json
{
  "ruleId": "LOCK-REPO-018",
  "name": "단건 조회: Optional<Entity>",
  "severity": "CRITICAL",
  "description": "단건 Lock 조회는 Optional<Entity> 반환",
  "pattern": "public Optional<{Bc}JpaEntity> findByIdForUpdate(Long id)",
  "antiPattern": "public {Bc}JpaEntity findByIdForUpdate(Long id) // null 가능성"
}
```

```json
{
  "ruleId": "LOCK-REPO-019",
  "name": "복수건 조회: List<Entity>",
  "severity": "CRITICAL",
  "description": "복수건 Lock 조회는 List<Entity> 반환",
  "pattern": "public List<{Bc}JpaEntity> findByIdsForUpdate(List<Long> ids)",
  "antiPattern": "public Set<{Bc}JpaEntity> findByIdsForUpdate(...) // List 사용"
}
```

---

## 템플릿 코드

```java
@Repository
public class {Bc}LockRepository {

    private static final Q{Bc}JpaEntity {bc} = Q{Bc}JpaEntity.{bc}JpaEntity;

    private final JPAQueryFactory queryFactory;

    public {Bc}LockRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * Pessimistic Write Lock (FOR UPDATE) - 단건
     */
    public Optional<{Bc}JpaEntity> findByIdForUpdate(Long id) {
        return Optional.ofNullable(
            queryFactory.selectFrom({bc})
                .where({bc}.id.eq(id))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne()
        );
    }

    /**
     * Pessimistic Write Lock (FOR UPDATE) - 복수건
     * 
     * <p>Deadlock 방지를 위해 ID 오름차순 정렬
     */
    public List<{Bc}JpaEntity> findByIdsForUpdate(List<Long> ids) {
        List<Long> sortedIds = ids.stream().sorted().toList();
        return queryFactory.selectFrom({bc})
            .where({bc}.id.in(sortedIds))
            .orderBy({bc}.id.asc())  // Deadlock 방지
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetch();
    }

    /**
     * Pessimistic Read Lock (FOR SHARE) - 단건
     */
    public Optional<{Bc}JpaEntity> findByIdForShare(Long id) {
        return Optional.ofNullable(
            queryFactory.selectFrom({bc})
                .where({bc}.id.eq(id))
                .setLockMode(LockModeType.PESSIMISTIC_READ)
                .fetchOne()
        );
    }
}
```

---

## 체크리스트

Lock Repository 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Repository 어노테이션
- [ ] *LockRepository 네이밍
- [ ] JPAQueryFactory 또는 EntityManager 필드 존재
- [ ] Lock 메서드만 정의 (ForUpdate, ForShare)
- [ ] setLockMode() 사용
- [ ] 복수건 Lock 시 ID 정렬 (Deadlock 방지)
- [ ] @Transactional 어노테이션 없음
- [ ] 일반 조회/Command 메서드 없음
- [ ] Entity 반환 (Domain 아님)

---

**총 규칙 수**: 19개
**작성일**: 2025-12-08

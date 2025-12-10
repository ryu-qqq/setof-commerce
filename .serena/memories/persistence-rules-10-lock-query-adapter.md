# Persistence Layer Rules - 10. Lock Query Adapter

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/adapter/query/lock/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "LOCK-ADAPTER-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 반드시 클래스여야 한다 (interface 금지)",
  "pattern": "public class {Bc}LockQueryAdapter implements {Bc}LockQueryPort",
  "antiPattern": "public interface {Bc}LockQueryAdapter"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-002",
  "name": "@Component 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 반드시 @Component 어노테이션을 가져야 한다",
  "pattern": "@Component\npublic class {Bc}LockQueryAdapter implements {Bc}LockQueryPort",
  "antiPattern": "public class {Bc}LockQueryAdapter // @Component 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-003",
  "name": "*LockQueryAdapter 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 반드시 *LockQueryAdapter로 끝나야 한다",
  "pattern": "public class OrderLockQueryAdapter",
  "antiPattern": "public class LockOrderQueryAdapter, OrderLockAdapter"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-004",
  "name": "Port 인터페이스 구현 필수",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 반드시 *LockQueryPort 인터페이스를 구현해야 한다",
  "pattern": "public class {Bc}LockQueryAdapter implements {Bc}LockQueryPort",
  "antiPattern": "public class {Bc}LockQueryAdapter // Port 구현 없음"
}
```

### 2. 필드 규칙 (정확히 2개)

```json
{
  "ruleId": "LOCK-ADAPTER-005",
  "name": "필드 개수: 정확히 2개",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 정확히 2개의 필드만 가져야 한다 (LockRepository + Mapper)",
  "pattern": "private final {Bc}LockRepository lockRepository;\nprivate final {Bc}JpaEntityMapper mapper;",
  "antiPattern": "private final OtherRepository otherRepository; // 3개 이상 필드 금지"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-006",
  "name": "LockRepository 필드 필수",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 반드시 LockRepository 필드를 가져야 한다",
  "pattern": "private final {Bc}LockRepository lockRepository;",
  "antiPattern": "// LockRepository 필드 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-007",
  "name": "Mapper 필드 필수",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 반드시 Mapper 필드를 가져야 한다",
  "pattern": "private final {Bc}JpaEntityMapper mapper;",
  "antiPattern": "// Mapper 필드 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-008",
  "name": "final 필드 (생성자 주입)",
  "severity": "CRITICAL",
  "description": "모든 필드는 final로 선언하여 생성자 주입",
  "pattern": "private final {Bc}LockRepository lockRepository;",
  "antiPattern": "private {Bc}LockRepository lockRepository; // final 아님"
}
```

### 3. 메서드 규칙 (정확히 6개)

```json
{
  "ruleId": "LOCK-ADAPTER-009",
  "name": "public 메서드: 정확히 6개",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 정확히 6개의 public 메서드만 가져야 한다",
  "pattern": "findByIdForUpdate, findByCriteriaForUpdate, findByIdForShare, findByCriteriaForShare, findByIdWithOptimisticLock, findByCriteriaWithOptimisticLock",
  "antiPattern": "findById, existsById 등 일반 조회 메서드"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-010",
  "name": "findByIdForUpdate() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Pessimistic Write Lock 단건 조회 메서드 필수",
  "pattern": "public Optional<{Bc}> findByIdForUpdate({Bc}Id id)",
  "antiPattern": "// findByIdForUpdate 메서드 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-011",
  "name": "findByCriteriaForUpdate() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Pessimistic Write Lock 목록 조회 메서드 필수",
  "pattern": "public List<{Bc}> findByCriteriaForUpdate({Bc}SearchCriteria criteria)",
  "antiPattern": "// findByCriteriaForUpdate 메서드 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-012",
  "name": "findByIdForShare() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Pessimistic Read Lock 단건 조회 메서드 필수",
  "pattern": "public Optional<{Bc}> findByIdForShare({Bc}Id id)",
  "antiPattern": "// findByIdForShare 메서드 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-013",
  "name": "findByCriteriaForShare() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Pessimistic Read Lock 목록 조회 메서드 필수",
  "pattern": "public List<{Bc}> findByCriteriaForShare({Bc}SearchCriteria criteria)",
  "antiPattern": "// findByCriteriaForShare 메서드 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-014",
  "name": "findByIdWithOptimisticLock() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Optimistic Lock 단건 조회 메서드 필수",
  "pattern": "public Optional<{Bc}> findByIdWithOptimisticLock({Bc}Id id)",
  "antiPattern": "// findByIdWithOptimisticLock 메서드 없음"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-015",
  "name": "findByCriteriaWithOptimisticLock() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Optimistic Lock 목록 조회 메서드 필수",
  "pattern": "public List<{Bc}> findByCriteriaWithOptimisticLock({Bc}SearchCriteria criteria)",
  "antiPattern": "// findByCriteriaWithOptimisticLock 메서드 없음"
}
```

### 4. 반환 타입 규칙

```json
{
  "ruleId": "LOCK-ADAPTER-016",
  "name": "Domain 반환",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 Domain 객체를 반환해야 한다",
  "pattern": "public Optional<{Bc}> findByIdForUpdate({Bc}Id id)",
  "antiPattern": "public Optional<{Bc}Dto> findByIdForUpdate(...) // DTO 반환 금지"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-017",
  "name": "Entity 반환 금지",
  "severity": "CRITICAL",
  "description": "Lock Query Adapter는 Entity 직접 반환 금지 (Mapper로 Domain 변환)",
  "pattern": "return lockRepository.findByIdForUpdate(id.getValue()).map(mapper::toDomain);",
  "antiPattern": "return lockRepository.findByIdForUpdate(id.getValue()); // Entity 반환 금지"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-018",
  "name": "단건 조회: Optional<Domain>",
  "severity": "CRITICAL",
  "description": "단건 Lock 조회는 Optional<Domain> 반환",
  "pattern": "public Optional<{Bc}> findByIdForUpdate({Bc}Id id)",
  "antiPattern": "public {Bc} findByIdForUpdate({Bc}Id id) // null 가능성"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-019",
  "name": "목록 조회: List<Domain>",
  "severity": "CRITICAL",
  "description": "목록 Lock 조회는 List<Domain> 반환",
  "pattern": "public List<{Bc}> findByCriteriaForUpdate({Bc}SearchCriteria criteria)",
  "antiPattern": "public Set<{Bc}> findByCriteriaForUpdate(...) // List 사용"
}
```

### 5. 금지 규칙

```json
{
  "ruleId": "LOCK-ADAPTER-020",
  "name": "@Transactional 절대 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer(UseCase)에서 관리",
  "pattern": "@Component\npublic class {Bc}LockQueryAdapter",
  "antiPattern": "@Component\n@Transactional\npublic class {Bc}LockQueryAdapter"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-021",
  "name": "Command 메서드 금지",
  "severity": "CRITICAL",
  "description": "저장/수정/삭제 메서드 금지 (CommandAdapter로 분리)",
  "pattern": "// 6개 Lock 조회 메서드만 존재",
  "antiPattern": "public void save(...), persist(...), update(...), delete(...)"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-022",
  "name": "일반 조회 메서드 금지",
  "severity": "CRITICAL",
  "description": "Lock 없는 일반 조회 메서드 금지 (QueryAdapter로 분리)",
  "pattern": "// Lock 메서드만 존재",
  "antiPattern": "public Optional<{Bc}> findById({Bc}Id id) // Lock 없는 조회 금지"
}
```

```json
{
  "ruleId": "LOCK-ADAPTER-023",
  "name": "try-catch 금지",
  "severity": "CRITICAL",
  "description": "Lock 예외 처리는 Application Layer에서, Adapter에서 catch 금지",
  "pattern": "return lockRepository.findByIdForUpdate(...).map(mapper::toDomain); // 예외 그대로 던짐",
  "antiPattern": "try { ... } catch (PessimisticLockException e) { ... } // catch 금지"
}
```

### 6. JavaDoc 규칙

```json
{
  "ruleId": "LOCK-ADAPTER-024",
  "name": "@throws 명시",
  "severity": "RECOMMENDED",
  "description": "Lock 메서드는 JavaDoc에 @throws로 예외 명시 (처리는 안 함)",
  "pattern": "/**\n * @throws PessimisticLockException Lock 획득 실패 시\n * @throws LockTimeoutException Lock 타임아웃 시\n */",
  "antiPattern": "// @throws 없이 메서드 정의"
}
```

---

## 템플릿 코드

```java
@Component
public class {Bc}LockQueryAdapter implements {Bc}LockQueryPort {

    private final {Bc}LockRepository lockRepository;
    private final {Bc}JpaEntityMapper mapper;

    public {Bc}LockQueryAdapter(
        {Bc}LockRepository lockRepository,
        {Bc}JpaEntityMapper mapper
    ) {
        this.lockRepository = lockRepository;
        this.mapper = mapper;
    }

    // ========== Pessimistic Write Lock (FOR UPDATE) ==========

    /**
     * @throws PessimisticLockException Lock 획득 실패 시
     * @throws LockTimeoutException Lock 타임아웃 시
     */
    @Override
    public Optional<{Bc}> findByIdForUpdate({Bc}Id id) {
        return lockRepository.findByIdForUpdate(id.getValue())
            .map(mapper::toDomain);
    }

    @Override
    public List<{Bc}> findByCriteriaForUpdate({Bc}SearchCriteria criteria) {
        return lockRepository.findByCriteriaForUpdate(criteria).stream()
            .map(mapper::toDomain)
            .toList();
    }

    // ========== Pessimistic Read Lock (FOR SHARE) ==========

    @Override
    public Optional<{Bc}> findByIdForShare({Bc}Id id) {
        return lockRepository.findByIdForShare(id.getValue())
            .map(mapper::toDomain);
    }

    @Override
    public List<{Bc}> findByCriteriaForShare({Bc}SearchCriteria criteria) {
        return lockRepository.findByCriteriaForShare(criteria).stream()
            .map(mapper::toDomain)
            .toList();
    }

    // ========== Optimistic Lock (@Version) ==========

    @Override
    public Optional<{Bc}> findByIdWithOptimisticLock({Bc}Id id) {
        return lockRepository.findByIdWithOptimisticLock(id.getValue())
            .map(mapper::toDomain);
    }

    @Override
    public List<{Bc}> findByCriteriaWithOptimisticLock({Bc}SearchCriteria criteria) {
        return lockRepository.findByCriteriaWithOptimisticLock(criteria).stream()
            .map(mapper::toDomain)
            .toList();
    }
}
```

---

## 체크리스트

Lock Query Adapter 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Component 어노테이션
- [ ] *LockQueryAdapter 네이밍
- [ ] *LockQueryPort 인터페이스 구현
- [ ] 정확히 2개 필드 (LockRepository + Mapper)
- [ ] 정확히 6개 public 메서드
- [ ] Domain 반환 (DTO/Entity 아님)
- [ ] @Transactional 어노테이션 없음
- [ ] Command/일반조회 메서드 없음
- [ ] try-catch 금지 (예외 그대로 던짐)
- [ ] JavaDoc @throws 명시

---

**총 규칙 수**: 24개
**작성일**: 2025-12-08

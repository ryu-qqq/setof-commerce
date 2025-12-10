# Persistence Layer Rules - 02. JPA Repository

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/repository/jpa/`

---

## 규칙 목록

### 1. 인터페이스 구조 규칙

```json
{
  "ruleId": "JPA-REPO-001",
  "name": "인터페이스 타입 필수",
  "severity": "CRITICAL",
  "description": "JPA Repository는 반드시 인터페이스여야 한다 (class 금지)",
  "pattern": "public interface {Bc}Repository extends JpaRepository<{Bc}JpaEntity, Long>",
  "antiPattern": "public class {Bc}Repository"
}
```

```json
{
  "ruleId": "JPA-REPO-002",
  "name": "JpaRepository<Entity, Long> 상속 필수",
  "severity": "CRITICAL",
  "description": "반드시 JpaRepository<Entity, Long>만 상속해야 한다",
  "pattern": "extends JpaRepository<{Bc}JpaEntity, Long>",
  "antiPattern": "extends JpaRepository<{Bc}JpaEntity, String> // Long만 허용"
}
```

```json
{
  "ruleId": "JPA-REPO-003",
  "name": "*Repository 네이밍",
  "severity": "CRITICAL",
  "description": "Repository 인터페이스명은 반드시 *Repository로 끝나야 한다",
  "pattern": "public interface OrderRepository",
  "antiPattern": "public interface OrderRepo, OrderJpaRepository"
}
```

### 2. 금지 규칙 - 상속

```json
{
  "ruleId": "JPA-REPO-004",
  "name": "QuerydslPredicateExecutor 상속 금지",
  "severity": "CRITICAL",
  "description": "QuerydslPredicateExecutor 상속 금지 (QueryDslRepository로 분리)",
  "pattern": "extends JpaRepository<{Bc}JpaEntity, Long> // 단일 상속",
  "antiPattern": "extends JpaRepository<...>, QuerydslPredicateExecutor<...>"
}
```

```json
{
  "ruleId": "JPA-REPO-005",
  "name": "JpaSpecificationExecutor 상속 금지",
  "severity": "CRITICAL",
  "description": "JpaSpecificationExecutor 상속 금지",
  "pattern": "extends JpaRepository<{Bc}JpaEntity, Long> // 단일 상속",
  "antiPattern": "extends JpaRepository<...>, JpaSpecificationExecutor<...>"
}
```

### 3. 금지 규칙 - 메서드

```json
{
  "ruleId": "JPA-REPO-006",
  "name": "Query 메서드 금지 (findBy*)",
  "severity": "CRITICAL",
  "description": "Spring Data JPA Query 메서드 정의 금지 (QueryDslRepository 사용)",
  "pattern": "// 빈 인터페이스, JpaRepository 기본 메서드만 사용",
  "antiPattern": "List<{Bc}JpaEntity> findByName(String name);"
}
```

```json
{
  "ruleId": "JPA-REPO-007",
  "name": "Query 메서드 금지 (countBy*)",
  "severity": "CRITICAL",
  "description": "countBy* 쿼리 메서드 정의 금지",
  "pattern": "// QueryDslRepository에서 처리",
  "antiPattern": "long countByStatus(String status);"
}
```

```json
{
  "ruleId": "JPA-REPO-008",
  "name": "Query 메서드 금지 (existsBy*)",
  "severity": "CRITICAL",
  "description": "existsBy* 쿼리 메서드 정의 금지",
  "pattern": "// QueryDslRepository에서 처리",
  "antiPattern": "boolean existsByEmail(String email);"
}
```

```json
{
  "ruleId": "JPA-REPO-009",
  "name": "Query 메서드 금지 (deleteBy*)",
  "severity": "CRITICAL",
  "description": "deleteBy* 쿼리 메서드 정의 금지",
  "pattern": "// JpaRepository.delete() 사용",
  "antiPattern": "void deleteByStatus(String status);"
}
```

```json
{
  "ruleId": "JPA-REPO-010",
  "name": "@Query 어노테이션 금지",
  "severity": "CRITICAL",
  "description": "@Query 어노테이션 사용 금지 (QueryDslRepository 사용)",
  "pattern": "// QueryDslRepository에서 QueryDSL로 구현",
  "antiPattern": "@Query(\"SELECT o FROM Order o WHERE o.status = :status\")"
}
```

```json
{
  "ruleId": "JPA-REPO-011",
  "name": "@Modifying 어노테이션 금지",
  "severity": "CRITICAL",
  "description": "@Modifying 어노테이션 사용 금지",
  "pattern": "// JPA dirty checking 사용",
  "antiPattern": "@Modifying\n@Query(\"UPDATE Order o SET o.status = :status\")"
}
```

```json
{
  "ruleId": "JPA-REPO-012",
  "name": "Custom Repository 구현 금지",
  "severity": "CRITICAL",
  "description": "Custom Repository 상속/구현 금지 (QueryDslRepository로 분리)",
  "pattern": "public interface {Bc}Repository extends JpaRepository<{Bc}JpaEntity, Long>",
  "antiPattern": "public interface {Bc}Repository extends JpaRepository<...>, {Bc}RepositoryCustom"
}
```

### 4. 금지 규칙 - 어노테이션

```json
{
  "ruleId": "JPA-REPO-013",
  "name": "@Repository 어노테이션 금지",
  "severity": "CRITICAL",
  "description": "JPA Repository 인터페이스에 @Repository 어노테이션 불필요",
  "pattern": "public interface {Bc}Repository extends JpaRepository<...>",
  "antiPattern": "@Repository\npublic interface {Bc}Repository"
}
```

```json
{
  "ruleId": "JPA-REPO-014",
  "name": "@Transactional 어노테이션 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer에서 관리",
  "pattern": "// 어노테이션 없는 순수 인터페이스",
  "antiPattern": "@Transactional\npublic interface {Bc}Repository"
}
```

### 5. 역할 제한 규칙

```json
{
  "ruleId": "JPA-REPO-015",
  "name": "Command 전용 역할",
  "severity": "CRITICAL",
  "description": "JPA Repository는 Command(CUD) 전용, Query는 QueryDslRepository로",
  "pattern": "// save(), delete(), deleteById() 만 사용",
  "antiPattern": "// JPA Repository에서 복잡한 조회 쿼리 작성"
}
```

```json
{
  "ruleId": "JPA-REPO-016",
  "name": "빈 인터페이스 유지",
  "severity": "CRITICAL",
  "description": "추가 메서드 정의 없이 JpaRepository 기본 메서드만 사용",
  "pattern": "public interface {Bc}Repository extends JpaRepository<{Bc}JpaEntity, Long> {\n    // 빈 인터페이스\n}",
  "antiPattern": "public interface {Bc}Repository extends JpaRepository<...> {\n    List<...> findByXxx(...);\n}"
}
```

---

## 템플릿 코드

```java
/**
 * {Bc} JPA Repository
 *
 * <p>Command(CUD) 전용 Repository입니다.
 * <p>Query는 {Bc}QueryDslRepository를 사용하세요.
 */
public interface {Bc}Repository extends JpaRepository<{Bc}JpaEntity, Long> {
    // JpaRepository 기본 메서드만 사용
    // - save(entity)
    // - delete(entity)
    // - deleteById(id)
    // - findById(id) - 단순 ID 조회만 허용
}
```

---

## 체크리스트

JPA Repository 구현 시:
- [ ] 인터페이스 타입
- [ ] JpaRepository<Entity, Long> 단일 상속
- [ ] *Repository 네이밍
- [ ] QuerydslPredicateExecutor 상속 안 함
- [ ] Query 메서드 정의 안 함 (findBy*, countBy*, existsBy*, deleteBy*)
- [ ] @Query 어노테이션 없음
- [ ] @Repository 어노테이션 없음
- [ ] @Transactional 어노테이션 없음
- [ ] 빈 인터페이스 유지

---

**총 규칙 수**: 16개
**작성일**: 2025-12-08

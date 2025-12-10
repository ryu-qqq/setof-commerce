# Persistence Layer Rules - 07. Command Adapter

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/adapter/command/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "CMD-ADAPTER-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "Command Adapter는 반드시 클래스여야 한다 (interface 금지)",
  "pattern": "public class {Bc}CommandAdapter implements {Bc}CommandPort",
  "antiPattern": "public interface {Bc}CommandAdapter"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-002",
  "name": "@Component 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Command Adapter는 반드시 @Component 어노테이션을 가져야 한다",
  "pattern": "@Component\npublic class {Bc}CommandAdapter implements {Bc}CommandPort",
  "antiPattern": "public class {Bc}CommandAdapter // @Component 없음"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-003",
  "name": "*CommandAdapter 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 반드시 *CommandAdapter로 끝나야 한다",
  "pattern": "public class OrderCommandAdapter",
  "antiPattern": "public class OrderAdapter, OrderRepositoryAdapter"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-004",
  "name": "Port 인터페이스 구현 필수",
  "severity": "CRITICAL",
  "description": "Command Adapter는 반드시 *CommandPort 인터페이스를 구현해야 한다",
  "pattern": "public class {Bc}CommandAdapter implements {Bc}CommandPort",
  "antiPattern": "public class {Bc}CommandAdapter // Port 구현 없음"
}
```

### 2. 필드 규칙 (정확히 2개)

```json
{
  "ruleId": "CMD-ADAPTER-005",
  "name": "필드 개수: 정확히 2개",
  "severity": "CRITICAL",
  "description": "Command Adapter는 정확히 2개의 필드만 가져야 한다 (JpaRepository + Mapper)",
  "pattern": "private final {Bc}Repository repository;\nprivate final {Bc}JpaEntityMapper mapper;",
  "antiPattern": "private final OtherRepository otherRepository; // 3개 이상 필드 금지"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-006",
  "name": "JpaRepository 필드 필수",
  "severity": "CRITICAL",
  "description": "Command Adapter는 반드시 JpaRepository 필드를 가져야 한다",
  "pattern": "private final {Bc}Repository repository;",
  "antiPattern": "// JpaRepository 필드 없음"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-007",
  "name": "Mapper 필드 필수",
  "severity": "CRITICAL",
  "description": "Command Adapter는 반드시 Mapper 필드를 가져야 한다",
  "pattern": "private final {Bc}JpaEntityMapper mapper;",
  "antiPattern": "// Mapper 필드 없음"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-008",
  "name": "final 필드 (생성자 주입)",
  "severity": "CRITICAL",
  "description": "모든 필드는 final로 선언하여 생성자 주입",
  "pattern": "private final {Bc}Repository repository;",
  "antiPattern": "private {Bc}Repository repository; // final 아님"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-009",
  "name": "다른 Repository 주입 금지 (1:1 매핑)",
  "severity": "CRITICAL",
  "description": "Command Adapter는 하나의 JpaRepository에만 1:1 매핑",
  "pattern": "// 2개 필드만: Repository + Mapper",
  "antiPattern": "private final CustomerRepository customerRepository; // 다른 Repository 금지"
}
```

### 3. 메서드 규칙 (정확히 1개)

```json
{
  "ruleId": "CMD-ADAPTER-010",
  "name": "public 메서드: 정확히 1개 (persist)",
  "severity": "CRITICAL",
  "description": "Command Adapter는 정확히 1개의 public 메서드만 가져야 한다 (persist)",
  "pattern": "public {Bc}Id persist({Bc} domain)",
  "antiPattern": "public void update(...), public void delete(...) // 추가 메서드 금지"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-011",
  "name": "persist() 메서드 필수",
  "severity": "CRITICAL",
  "description": "저장 메서드는 반드시 persist()로 네이밍",
  "pattern": "public {Bc}Id persist({Bc} domain)",
  "antiPattern": "public {Bc}Id save(...), create(...), insert(...)"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-012",
  "name": "persist() 반환 타입: *Id",
  "severity": "CRITICAL",
  "description": "persist() 메서드는 *Id (Value Object) 반환",
  "pattern": "public OrderId persist(Order order)",
  "antiPattern": "public Long persist(...), public Order persist(...)"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-013",
  "name": "persist() 파라미터: Domain",
  "severity": "CRITICAL",
  "description": "persist() 메서드 파라미터는 Domain 객체",
  "pattern": "public {Bc}Id persist({Bc} domain)",
  "antiPattern": "public {Bc}Id persist({Bc}JpaEntity entity) // Entity 금지"
}
```

### 4. 금지 규칙 - 메서드

```json
{
  "ruleId": "CMD-ADAPTER-014",
  "name": "update() 메서드 금지",
  "severity": "CRITICAL",
  "description": "update() 메서드 금지 (JPA dirty checking 사용)",
  "pattern": "// persist()만 사용, dirty checking으로 자동 업데이트",
  "antiPattern": "public void update({Bc} domain)"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-015",
  "name": "delete() 메서드 금지",
  "severity": "CRITICAL",
  "description": "delete() 메서드 금지 (soft delete는 상태 변경으로)",
  "pattern": "// soft delete는 Domain.markAsDeleted() + persist()",
  "antiPattern": "public void delete(Long id)"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-016",
  "name": "Query 메서드 금지",
  "severity": "CRITICAL",
  "description": "조회 메서드 금지 (QueryAdapter로 분리)",
  "pattern": "// persist()만 존재",
  "antiPattern": "public Optional<{Bc}> findById(Long id)"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-017",
  "name": "비즈니스 메서드 금지",
  "severity": "CRITICAL",
  "description": "비즈니스 로직 수행 메서드 금지 (단순 위임만)",
  "pattern": "// persist()만 존재, 비즈니스 로직은 Domain에서",
  "antiPattern": "public void processOrder(...), calculateTotal(...)"
}
```

### 5. 금지 규칙 - 어노테이션

```json
{
  "ruleId": "CMD-ADAPTER-018",
  "name": "@Transactional 절대 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer(UseCase)에서 관리",
  "pattern": "@Component\npublic class {Bc}CommandAdapter",
  "antiPattern": "@Component\n@Transactional\npublic class {Bc}CommandAdapter"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-019",
  "name": "메서드 레벨 @Transactional 금지",
  "severity": "CRITICAL",
  "description": "메서드 레벨에서도 @Transactional 금지",
  "pattern": "public {Bc}Id persist({Bc} domain) { ... }",
  "antiPattern": "@Transactional\npublic {Bc}Id persist({Bc} domain)"
}
```

### 6. 구현 패턴 규칙

```json
{
  "ruleId": "CMD-ADAPTER-020",
  "name": "Mapper.toEntity() 사용",
  "severity": "CRITICAL",
  "description": "Domain → Entity 변환 시 Mapper.toEntity() 사용",
  "pattern": "{Bc}JpaEntity entity = mapper.toEntity(domain);",
  "antiPattern": "{Bc}JpaEntity entity = new {Bc}JpaEntity(...); // 직접 생성 금지"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-021",
  "name": "repository.save() 호출",
  "severity": "CRITICAL",
  "description": "JpaRepository.save() 호출하여 저장",
  "pattern": "{Bc}JpaEntity saved = repository.save(entity);",
  "antiPattern": "// EntityManager 직접 사용 금지"
}
```

```json
{
  "ruleId": "CMD-ADAPTER-022",
  "name": "*Id.of() 반환",
  "severity": "CRITICAL",
  "description": "저장된 Entity의 ID를 Value Object로 변환하여 반환",
  "pattern": "return {Bc}Id.of(saved.getId());",
  "antiPattern": "return saved.getId(); // primitive 반환 금지"
}
```

---

## 템플릿 코드

```java
@Component
public class {Bc}CommandAdapter implements {Bc}CommandPort {

    private final {Bc}Repository repository;
    private final {Bc}JpaEntityMapper mapper;

    public {Bc}CommandAdapter(
        {Bc}Repository repository,
        {Bc}JpaEntityMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Domain 저장 (신규 생성 및 수정)
     *
     * <p>JPA dirty checking을 활용하여 insert/update 자동 결정
     *
     * @param domain Domain 객체
     * @return 저장된 Entity의 ID (Value Object)
     */
    @Override
    public {Bc}Id persist({Bc} domain) {
        {Bc}JpaEntity entity = mapper.toEntity(domain);
        {Bc}JpaEntity saved = repository.save(entity);
        return {Bc}Id.of(saved.getId());
    }
}
```

---

## JPA Dirty Checking 활용

```java
// ✅ 올바른 수정 패턴 (Application Layer UseCase)
@Transactional
public void updateOrder(UpdateOrderCommand command) {
    // 1. 조회 (QueryPort)
    Order order = orderQueryPort.findById(command.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

    // 2. Domain 비즈니스 로직 수행
    order.updateStatus(command.getNewStatus());

    // 3. 저장 (CommandPort) - dirty checking으로 UPDATE
    orderCommandPort.persist(order);
}
```

---

## 체크리스트

Command Adapter 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Component 어노테이션
- [ ] *CommandAdapter 네이밍
- [ ] *CommandPort 인터페이스 구현
- [ ] 정확히 2개 필드 (JpaRepository + Mapper)
- [ ] 정확히 1개 public 메서드 (persist)
- [ ] persist() 반환 타입: *Id (Value Object)
- [ ] @Transactional 어노테이션 없음
- [ ] update(), delete() 메서드 없음
- [ ] Query 메서드 없음

---

**총 규칙 수**: 22개
**작성일**: 2025-12-08

# Persistence Layer Rules - 01. Entity

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/entity/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "ENTITY-001",
  "name": "클래스 타입",
  "severity": "CRITICAL",
  "description": "Entity는 반드시 클래스 타입이어야 한다 (interface/enum 금지)",
  "pattern": "public class {Bc}JpaEntity",
  "antiPattern": "public interface/enum {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-002",
  "name": "@Entity 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "JPA Entity는 반드시 @Entity 어노테이션을 가져야 한다",
  "pattern": "@Entity\npublic class {Bc}JpaEntity",
  "antiPattern": "public class {Bc}JpaEntity // @Entity 없음"
}
```

```json
{
  "ruleId": "ENTITY-003",
  "name": "@Table 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Entity는 반드시 @Table 어노테이션으로 테이블명을 명시해야 한다",
  "pattern": "@Table(name = \"bc_table\")\npublic class {Bc}JpaEntity",
  "antiPattern": "public class {Bc}JpaEntity // @Table 없음"
}
```

```json
{
  "ruleId": "ENTITY-004",
  "name": "*JpaEntity 네이밍",
  "severity": "CRITICAL",
  "description": "Entity 클래스명은 반드시 *JpaEntity로 끝나야 한다",
  "pattern": "public class OrderJpaEntity",
  "antiPattern": "public class Order, OrderEntity, OrderJpa"
}
```

### 2. 생성자 규칙

```json
{
  "ruleId": "ENTITY-005",
  "name": "protected 기본 생성자 필수",
  "severity": "CRITICAL",
  "description": "JPA를 위해 protected 접근자의 기본 생성자가 필수이다",
  "pattern": "protected {Bc}JpaEntity() {}",
  "antiPattern": "public/private {Bc}JpaEntity() {}"
}
```

```json
{
  "ruleId": "ENTITY-006",
  "name": "private 전체 필드 생성자 필수",
  "severity": "CRITICAL",
  "description": "외부 직접 생성 방지를 위해 private all-args 생성자 필수",
  "pattern": "private {Bc}JpaEntity(Long id, String name, ...) { ... }",
  "antiPattern": "public {Bc}JpaEntity(Long id, String name, ...)"
}
```

```json
{
  "ruleId": "ENTITY-007",
  "name": "public static of() 팩토리 메서드 필수",
  "severity": "CRITICAL",
  "description": "객체 생성은 반드시 public static of() 팩토리 메서드를 통해서만 가능",
  "pattern": "public static {Bc}JpaEntity of(Long id, String name) { return new {Bc}JpaEntity(id, name); }",
  "antiPattern": "new {Bc}JpaEntity(id, name) // 직접 생성"
}
```

### 3. 필드 규칙

```json
{
  "ruleId": "ENTITY-008",
  "name": "@Id 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Entity는 반드시 @Id가 붙은 PK 필드를 가져야 한다",
  "pattern": "@Id\n@GeneratedValue(strategy = GenerationType.IDENTITY)\nprivate Long id;",
  "antiPattern": "private Long id; // @Id 없음"
}
```

```json
{
  "ruleId": "ENTITY-009",
  "name": "private 필드 접근자",
  "severity": "CRITICAL",
  "description": "Entity 필드는 반드시 private 접근자여야 한다",
  "pattern": "private Long customerId;",
  "antiPattern": "public/protected Long customerId;"
}
```

```json
{
  "ruleId": "ENTITY-010",
  "name": "Getter 메서드만 허용",
  "severity": "CRITICAL",
  "description": "Entity는 Getter만 제공하고 Setter는 절대 금지",
  "pattern": "public Long getId() { return id; }",
  "antiPattern": "public void setId(Long id) { this.id = id; }"
}
```

### 4. 금지 규칙 - Lombok

```json
{
  "ruleId": "ENTITY-011",
  "name": "@Data 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Data 어노테이션 절대 금지",
  "pattern": "// Lombok 없이 Plain Java로 구현",
  "antiPattern": "@Data\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-012",
  "name": "@Getter 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Getter 어노테이션 절대 금지 (직접 작성)",
  "pattern": "public Long getId() { return id; }",
  "antiPattern": "@Getter\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-013",
  "name": "@Setter 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Setter 어노테이션 절대 금지",
  "pattern": "// Setter 메서드 자체가 없어야 함",
  "antiPattern": "@Setter\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-014",
  "name": "@Builder 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Builder 어노테이션 절대 금지 (static of() 사용)",
  "pattern": "public static {Bc}JpaEntity of(...)",
  "antiPattern": "@Builder\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-015",
  "name": "@Value 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Value 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@Value\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-016",
  "name": "@AllArgsConstructor 금지",
  "severity": "CRITICAL",
  "description": "Lombok @AllArgsConstructor 어노테이션 절대 금지",
  "pattern": "private {Bc}JpaEntity(Long id, ...) { ... }",
  "antiPattern": "@AllArgsConstructor\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-017",
  "name": "@NoArgsConstructor 금지",
  "severity": "CRITICAL",
  "description": "Lombok @NoArgsConstructor 어노테이션 절대 금지 (직접 작성)",
  "pattern": "protected {Bc}JpaEntity() {}",
  "antiPattern": "@NoArgsConstructor\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-018",
  "name": "@RequiredArgsConstructor 금지",
  "severity": "CRITICAL",
  "description": "Lombok @RequiredArgsConstructor 어노테이션 절대 금지",
  "pattern": "// 직접 생성자 작성",
  "antiPattern": "@RequiredArgsConstructor\npublic class {Bc}JpaEntity"
}
```

```json
{
  "ruleId": "ENTITY-019",
  "name": "@UtilityClass 금지",
  "severity": "CRITICAL",
  "description": "Lombok @UtilityClass 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@UtilityClass\npublic class {Bc}JpaEntity"
}
```

### 5. 금지 규칙 - JPA 관계

```json
{
  "ruleId": "ENTITY-020",
  "name": "@ManyToOne 금지 (Long FK 전략)",
  "severity": "CRITICAL",
  "description": "JPA 관계 어노테이션 금지, Long FK 전략 사용",
  "pattern": "private Long customerId; // Long FK",
  "antiPattern": "@ManyToOne\nprivate Customer customer;"
}
```

```json
{
  "ruleId": "ENTITY-021",
  "name": "@OneToMany 금지 (Long FK 전략)",
  "severity": "CRITICAL",
  "description": "JPA 관계 어노테이션 금지, Long FK 전략 사용",
  "pattern": "private Long orderId; // Long FK",
  "antiPattern": "@OneToMany\nprivate List<OrderItem> items;"
}
```

```json
{
  "ruleId": "ENTITY-022",
  "name": "@OneToOne 금지 (Long FK 전략)",
  "severity": "CRITICAL",
  "description": "JPA 관계 어노테이션 금지, Long FK 전략 사용",
  "pattern": "private Long profileId; // Long FK",
  "antiPattern": "@OneToOne\nprivate Profile profile;"
}
```

```json
{
  "ruleId": "ENTITY-023",
  "name": "@ManyToMany 금지 (Long FK 전략)",
  "severity": "CRITICAL",
  "description": "JPA 관계 어노테이션 금지, 중간 테이블 Entity로 분리",
  "pattern": "// OrderProductJpaEntity 중간 테이블 Entity 생성",
  "antiPattern": "@ManyToMany\nprivate List<Product> products;"
}
```

### 6. 상속 규칙

```json
{
  "ruleId": "ENTITY-024",
  "name": "BaseAuditEntity 상속 권장",
  "severity": "RECOMMENDED",
  "description": "생성일시/수정일시 감사 필드를 위해 BaseAuditEntity 상속 권장",
  "pattern": "public class {Bc}JpaEntity extends BaseAuditEntity",
  "antiPattern": "// 감사 필드 직접 정의"
}
```

```json
{
  "ruleId": "ENTITY-025",
  "name": "SoftDeletableEntity 상속 (삭제 시)",
  "severity": "RECOMMENDED",
  "description": "소프트 삭제가 필요한 경우 SoftDeletableEntity 상속",
  "pattern": "public class {Bc}JpaEntity extends SoftDeletableEntity",
  "antiPattern": "private boolean deleted; // 직접 정의"
}
```

### 7. Domain Enum 참조 규칙

```json
{
  "ruleId": "ENTITY-026",
  "name": "Domain Layer Enum만 참조 가능",
  "severity": "CRITICAL",
  "description": "Entity에서 Enum 사용 시 Domain Layer의 Enum만 참조 가능",
  "pattern": "@Enumerated(EnumType.STRING)\nprivate OrderStatus status; // Domain Enum",
  "antiPattern": "// Persistence Layer에 Enum 정의 금지"
}
```

```json
{
  "ruleId": "ENTITY-027",
  "name": "@Enumerated(EnumType.STRING) 필수",
  "severity": "CRITICAL",
  "description": "Enum 필드는 반드시 STRING 타입으로 매핑",
  "pattern": "@Enumerated(EnumType.STRING)\nprivate OrderStatus status;",
  "antiPattern": "@Enumerated(EnumType.ORDINAL) // ORDINAL 금지"
}
```

---

## 체크리스트

Entity 구현 시:
- [ ] 클래스명 *JpaEntity 형식
- [ ] @Entity, @Table 어노테이션 존재
- [ ] protected 기본 생성자 존재
- [ ] private 전체 필드 생성자 존재
- [ ] public static of() 팩토리 메서드 존재
- [ ] Lombok 어노테이션 없음 (9개 금지)
- [ ] JPA 관계 어노테이션 없음 (4개 금지)
- [ ] Setter 메서드 없음
- [ ] Long FK 전략 적용
- [ ] BaseAuditEntity/SoftDeletableEntity 상속 검토

---

**총 규칙 수**: 27개
**작성일**: 2025-12-08

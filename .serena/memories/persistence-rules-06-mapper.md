# Persistence Layer Rules - 06. Mapper

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/mapper/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "MAPPER-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "Mapper는 반드시 클래스여야 한다 (interface 금지)",
  "pattern": "public class {Bc}JpaEntityMapper",
  "antiPattern": "public interface {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-002",
  "name": "@Component 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Mapper는 반드시 @Component 어노테이션을 가져야 한다",
  "pattern": "@Component\npublic class {Bc}JpaEntityMapper",
  "antiPattern": "public class {Bc}JpaEntityMapper // @Component 없음"
}
```

```json
{
  "ruleId": "MAPPER-003",
  "name": "*JpaEntityMapper 또는 *Mapper 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 *JpaEntityMapper 또는 *Mapper로 끝나야 한다",
  "pattern": "public class OrderJpaEntityMapper",
  "antiPattern": "public class OrderConverter, OrderTransformer"
}
```

### 2. 메서드 규칙

```json
{
  "ruleId": "MAPPER-004",
  "name": "toEntity() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Domain → Entity 변환 메서드 필수",
  "pattern": "public {Bc}JpaEntity toEntity({Bc} domain)",
  "antiPattern": "// toEntity 메서드 없음"
}
```

```json
{
  "ruleId": "MAPPER-005",
  "name": "toDomain() 메서드 필수",
  "severity": "CRITICAL",
  "description": "Entity → Domain 변환 메서드 필수",
  "pattern": "public {Bc} toDomain({Bc}JpaEntity entity)",
  "antiPattern": "// toDomain 메서드 없음"
}
```

```json
{
  "ruleId": "MAPPER-006",
  "name": "Entity.of() 팩토리 메서드 사용",
  "severity": "CRITICAL",
  "description": "Entity 생성 시 반드시 Entity.of() 팩토리 메서드 사용",
  "pattern": "return {Bc}JpaEntity.of(domain.getId(), domain.getName(), ...);",
  "antiPattern": "return new {Bc}JpaEntity(...); // 직접 생성자 호출 금지"
}
```

```json
{
  "ruleId": "MAPPER-007",
  "name": "Domain 팩토리 메서드 사용",
  "severity": "CRITICAL",
  "description": "Domain 생성 시 Domain의 팩토리 메서드/생성자 사용",
  "pattern": "return {Bc}.of(entity.getId(), entity.getName(), ...);",
  "antiPattern": "// Builder 패턴 등 다른 방식 사용"
}
```

### 3. 금지 규칙 - Lombok

```json
{
  "ruleId": "MAPPER-008",
  "name": "@Data 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Data 어노테이션 절대 금지",
  "pattern": "// Lombok 없이 Plain Java로 구현",
  "antiPattern": "@Data\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-009",
  "name": "@Getter 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Getter 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@Getter\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-010",
  "name": "@Setter 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Setter 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@Setter\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-011",
  "name": "@Builder 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Builder 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@Builder\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-012",
  "name": "@Value 금지",
  "severity": "CRITICAL",
  "description": "Lombok @Value 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@Value\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-013",
  "name": "@AllArgsConstructor 금지",
  "severity": "CRITICAL",
  "description": "Lombok @AllArgsConstructor 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@AllArgsConstructor\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-014",
  "name": "@NoArgsConstructor 금지",
  "severity": "CRITICAL",
  "description": "Lombok @NoArgsConstructor 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@NoArgsConstructor\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-015",
  "name": "@RequiredArgsConstructor 금지",
  "severity": "CRITICAL",
  "description": "Lombok @RequiredArgsConstructor 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@RequiredArgsConstructor\npublic class {Bc}JpaEntityMapper"
}
```

```json
{
  "ruleId": "MAPPER-016",
  "name": "@UtilityClass 금지",
  "severity": "CRITICAL",
  "description": "Lombok @UtilityClass 어노테이션 절대 금지",
  "pattern": "// Plain Java 구현",
  "antiPattern": "@UtilityClass\npublic class {Bc}JpaEntityMapper"
}
```

### 4. 금지 규칙 - 메서드

```json
{
  "ruleId": "MAPPER-017",
  "name": "static 메서드 금지",
  "severity": "CRITICAL",
  "description": "Mapper 메서드는 static이면 안 됨 (인스턴스 메서드만)",
  "pattern": "public {Bc}JpaEntity toEntity({Bc} domain)",
  "antiPattern": "public static {Bc}JpaEntity toEntity({Bc} domain)"
}
```

```json
{
  "ruleId": "MAPPER-018",
  "name": "비즈니스 로직 금지",
  "severity": "CRITICAL",
  "description": "Mapper에서 비즈니스 로직 수행 금지 (단순 변환만)",
  "pattern": "// 필드 매핑만 수행",
  "antiPattern": "if (domain.getStatus() == ACTIVE) { ... } // 비즈니스 로직"
}
```

```json
{
  "ruleId": "MAPPER-019",
  "name": "시간 필드 직접 전달",
  "severity": "CRITICAL",
  "description": "시간 필드는 그대로 전달, LocalDateTime.now() 사용 금지",
  "pattern": "entity.getCreatedAt() // 그대로 전달",
  "antiPattern": "LocalDateTime.now() // Mapper에서 시간 생성 금지"
}
```

### 5. 의존성 규칙

```json
{
  "ruleId": "MAPPER-020",
  "name": "다른 Mapper 의존 금지",
  "severity": "CRITICAL",
  "description": "Mapper는 다른 Mapper를 의존하면 안 됨 (순환 의존 방지)",
  "pattern": "@Component\npublic class {Bc}JpaEntityMapper { }",
  "antiPattern": "private final OtherMapper otherMapper; // 다른 Mapper 의존 금지"
}
```

```json
{
  "ruleId": "MAPPER-021",
  "name": "Repository 의존 금지",
  "severity": "CRITICAL",
  "description": "Mapper는 Repository를 의존하면 안 됨",
  "pattern": "@Component\npublic class {Bc}JpaEntityMapper { }",
  "antiPattern": "private final {Bc}Repository repository; // Repository 의존 금지"
}
```

```json
{
  "ruleId": "MAPPER-022",
  "name": "Service 의존 금지",
  "severity": "CRITICAL",
  "description": "Mapper는 Service를 의존하면 안 됨",
  "pattern": "@Component\npublic class {Bc}JpaEntityMapper { }",
  "antiPattern": "private final {Bc}Service service; // Service 의존 금지"
}
```

### 6. Value Object 변환 규칙

```json
{
  "ruleId": "MAPPER-023",
  "name": "VO → primitive 변환",
  "severity": "CRITICAL",
  "description": "Entity 저장 시 Value Object를 primitive로 변환",
  "pattern": "domain.getOrderId().getValue() // VO → Long",
  "antiPattern": "domain.getOrderId() // VO 그대로 전달"
}
```

```json
{
  "ruleId": "MAPPER-024",
  "name": "primitive → VO 변환",
  "severity": "CRITICAL",
  "description": "Domain 생성 시 primitive를 Value Object로 변환",
  "pattern": "OrderId.of(entity.getId()) // Long → VO",
  "antiPattern": "entity.getId() // primitive 그대로 전달"
}
```

---

## 템플릿 코드

```java
@Component
public class {Bc}JpaEntityMapper {

    /**
     * Domain → Entity 변환
     *
     * @param domain Domain 객체
     * @return JPA Entity
     */
    public {Bc}JpaEntity toEntity({Bc} domain) {
        return {Bc}JpaEntity.of(
            domain.getId().getValue(),      // VO → primitive
            domain.getName(),
            domain.getStatus(),             // Domain Enum 그대로
            domain.getCreatedAt()           // 시간 필드 그대로 전달
        );
    }

    /**
     * Entity → Domain 변환
     *
     * @param entity JPA Entity
     * @return Domain 객체
     */
    public {Bc} toDomain({Bc}JpaEntity entity) {
        return {Bc}.of(
            {Bc}Id.of(entity.getId()),      // primitive → VO
            entity.getName(),
            entity.getStatus(),             // Domain Enum 그대로
            entity.getCreatedAt()           // 시간 필드 그대로 전달
        );
    }
}
```

---

## 체크리스트

Mapper 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Component 어노테이션
- [ ] *JpaEntityMapper 또는 *Mapper 네이밍
- [ ] toEntity() 메서드 존재
- [ ] toDomain() 메서드 존재
- [ ] Entity.of() 팩토리 메서드 사용
- [ ] Lombok 어노테이션 없음 (9개 금지)
- [ ] static 메서드 없음
- [ ] 비즈니스 로직 없음
- [ ] LocalDateTime.now() 사용 안 함
- [ ] 다른 Mapper/Repository/Service 의존 안 함

---

**총 규칙 수**: 24개
**작성일**: 2025-12-08

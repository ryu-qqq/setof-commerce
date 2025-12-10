# Domain Layer - Value Object Rules

## VALUE_OBJECT (9 rules)

일반 Value Object 규칙

```json
{
  "category": "VALUE_OBJECT",
  "rules": [
    {
      "ruleId": "VO-001",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "Value Object는 Record 타입 필수 (Enum/Interface/Abstract 제외)",
      "pattern": "public\\s+record\\s+\\w+\\(",
      "antiPattern": "public\\s+class\\s+\\w+(Id|Email|Money|Address|Phone)",
      "archUnitTest": "VOArchTest.vo_MustBeRecord"
    },
    {
      "ruleId": "VO-002",
      "name": "of() 정적 팩토리 메서드 필수",
      "severity": "ERROR",
      "description": "Value Object는 of() 정적 팩토리 메서드 필수",
      "pattern": "public\\s+static\\s+\\w+\\s+of\\(",
      "archUnitTest": "VOArchTest.vo_MustHaveOfMethod"
    },
    {
      "ruleId": "VO-003",
      "name": "ID VO forNew() 메서드 필수",
      "severity": "ERROR",
      "description": "ID Value Object는 forNew() 메서드 필수 (신규 ID 생성용)",
      "pattern": "public\\s+static\\s+\\w+Id\\s+forNew\\(",
      "archUnitTest": "VOArchTest.idVo_MustHaveForNewMethod"
    },
    {
      "ruleId": "VO-004",
      "name": "Long ID VO isNew() 필수",
      "severity": "ERROR",
      "description": "Long 기반 ID VO는 isNew() 메서드 필수 (UUID 제외)",
      "pattern": "public\\s+boolean\\s+isNew\\(\\)",
      "archUnitTest": "VOArchTest.longIdVo_MustHaveIsNewMethod"
    },
    {
      "ruleId": "VO-005",
      "name": "Enum VO displayName() 필수",
      "severity": "ERROR",
      "description": "Enum Value Object는 displayName() 메서드 필수",
      "pattern": "public\\s+String\\s+displayName\\(\\)",
      "archUnitTest": "VOArchTest.enumVo_MustHaveDisplayNameMethod"
    },
    {
      "ruleId": "VO-006",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Value Object에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor|AllArgsConstructor|NoArgsConstructor|Value)",
      "archUnitTest": "VOArchTest.vo_MustNotUseLombok"
    },
    {
      "ruleId": "VO-007",
      "name": "JPA 금지",
      "severity": "ERROR",
      "description": "Value Object에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column|Embeddable|Embedded)",
      "archUnitTest": "VOArchTest.vo_MustNotUseJpa"
    },
    {
      "ruleId": "VO-008",
      "name": "Spring 금지",
      "severity": "ERROR",
      "description": "Value Object에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository|Transactional|Autowired)",
      "archUnitTest": "VOArchTest.vo_MustNotUseSpring"
    },
    {
      "ruleId": "VO-009",
      "name": "create*() 메서드 금지",
      "severity": "ERROR",
      "description": "create*() 메서드 금지, of() 또는 forNew() 사용",
      "antiPattern": "public\\s+static\\s+\\w+\\s+create\\w*\\(",
      "archUnitTest": "VOArchTest.vo_MustNotHaveCreateMethod"
    }
  ]
}
```

---

## LOCK_KEY (10 rules)

LockKey Value Object 규칙

```json
{
  "category": "LOCK_KEY",
  "rules": [
    {
      "ruleId": "LK-001",
      "name": "LockKey 인터페이스 구현 필수",
      "severity": "ERROR",
      "description": "LockKey VO는 LockKey 인터페이스를 구현해야 함",
      "pattern": "implements\\s+LockKey",
      "archUnitTest": "LockKeyArchTest.lockKey_MustImplementInterface"
    },
    {
      "ruleId": "LK-002",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "LockKey VO는 Record 타입 필수",
      "pattern": "public\\s+record\\s+\\w+LockKey\\(",
      "archUnitTest": "LockKeyArchTest.lockKey_MustBeRecord"
    },
    {
      "ruleId": "LK-003",
      "name": "value() 메서드 필수",
      "severity": "ERROR",
      "description": "LockKey VO는 value() 메서드 필수 (LockKey 인터페이스)",
      "pattern": "String\\s+value\\(\\)",
      "archUnitTest": "LockKeyArchTest.lockKey_MustHaveValueMethod"
    },
    {
      "ruleId": "LK-004",
      "name": "*LockKey 네이밍 필수",
      "severity": "ERROR",
      "description": "LockKey VO는 *LockKey 네이밍 규칙 필수",
      "pattern": "\\w+LockKey",
      "archUnitTest": "LockKeyArchTest.lockKey_MustHaveCorrectNaming"
    },
    {
      "ruleId": "LK-005",
      "name": "vo 패키지에 위치",
      "severity": "ERROR",
      "description": "LockKey VO는 vo 패키지에 위치해야 함",
      "pattern": "domain\\.\\w+\\.vo\\.\\w+LockKey",
      "archUnitTest": "LockKeyArchTest.lockKey_MustBeInVoPackage"
    },
    {
      "ruleId": "LK-006",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "LockKey VO에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor)",
      "archUnitTest": "LockKeyArchTest.lockKey_MustNotUseLombok"
    },
    {
      "ruleId": "LK-007",
      "name": "JPA 금지",
      "severity": "ERROR",
      "description": "LockKey VO에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column)",
      "archUnitTest": "LockKeyArchTest.lockKey_MustNotUseJpa"
    },
    {
      "ruleId": "LK-008",
      "name": "Spring 금지",
      "severity": "ERROR",
      "description": "LockKey VO에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository)",
      "archUnitTest": "LockKeyArchTest.lockKey_MustNotUseSpring"
    },
    {
      "ruleId": "LK-009",
      "name": "of() 메서드 권장",
      "severity": "WARNING",
      "description": "LockKey VO는 of() 팩토리 메서드 권장",
      "pattern": "public\\s+static\\s+\\w+LockKey\\s+of\\("
    },
    {
      "ruleId": "LK-010",
      "name": "불변 필드 필수",
      "severity": "ERROR",
      "description": "LockKey VO 필드는 불변 (record로 자동 보장)",
      "archUnitTest": "LockKeyArchTest.lockKey_MustBeImmutable"
    }
  ]
}
```

---

## CACHE_KEY (10 rules)

CacheKey Value Object 규칙

```json
{
  "category": "CACHE_KEY",
  "rules": [
    {
      "ruleId": "CK-001",
      "name": "CacheKey 인터페이스 구현 필수",
      "severity": "ERROR",
      "description": "CacheKey VO는 CacheKey 인터페이스를 구현해야 함",
      "pattern": "implements\\s+CacheKey",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustImplementInterface"
    },
    {
      "ruleId": "CK-002",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "CacheKey VO는 Record 타입 필수",
      "pattern": "public\\s+record\\s+\\w+CacheKey\\(",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustBeRecord"
    },
    {
      "ruleId": "CK-003",
      "name": "value() 메서드 필수",
      "severity": "ERROR",
      "description": "CacheKey VO는 value() 메서드 필수 (CacheKey 인터페이스)",
      "pattern": "String\\s+value\\(\\)",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustHaveValueMethod"
    },
    {
      "ruleId": "CK-004",
      "name": "*CacheKey 네이밍 필수",
      "severity": "ERROR",
      "description": "CacheKey VO는 *CacheKey 네이밍 규칙 필수",
      "pattern": "\\w+CacheKey",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustHaveCorrectNaming"
    },
    {
      "ruleId": "CK-005",
      "name": "vo 패키지에 위치",
      "severity": "ERROR",
      "description": "CacheKey VO는 vo 패키지에 위치해야 함",
      "pattern": "domain\\.\\w+\\.vo\\.\\w+CacheKey",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustBeInVoPackage"
    },
    {
      "ruleId": "CK-006",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "CacheKey VO에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor)",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustNotUseLombok"
    },
    {
      "ruleId": "CK-007",
      "name": "JPA 금지",
      "severity": "ERROR",
      "description": "CacheKey VO에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column)",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustNotUseJpa"
    },
    {
      "ruleId": "CK-008",
      "name": "Spring 금지",
      "severity": "ERROR",
      "description": "CacheKey VO에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository)",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustNotUseSpring"
    },
    {
      "ruleId": "CK-009",
      "name": "of() 메서드 권장",
      "severity": "WARNING",
      "description": "CacheKey VO는 of() 팩토리 메서드 권장",
      "pattern": "public\\s+static\\s+\\w+CacheKey\\s+of\\("
    },
    {
      "ruleId": "CK-010",
      "name": "불변 필드 필수",
      "severity": "ERROR",
      "description": "CacheKey VO 필드는 불변 (record로 자동 보장)",
      "archUnitTest": "CacheKeyArchTest.cacheKey_MustBeImmutable"
    }
  ]
}
```

---

## SORT_KEY (5 rules)

SortKey Value Object 규칙

```json
{
  "category": "SORT_KEY",
  "rules": [
    {
      "ruleId": "SK-001",
      "name": "SortKey 인터페이스 구현 필수",
      "severity": "ERROR",
      "description": "SortKey VO는 SortKey 인터페이스를 구현해야 함",
      "pattern": "implements\\s+SortKey",
      "archUnitTest": "SortKeyArchTest.sortKey_MustImplementInterface"
    },
    {
      "ruleId": "SK-002",
      "name": "Enum 타입 필수",
      "severity": "ERROR",
      "description": "SortKey VO는 Enum 타입 필수",
      "pattern": "public\\s+enum\\s+\\w+SortKey",
      "archUnitTest": "SortKeyArchTest.sortKey_MustBeEnum"
    },
    {
      "ruleId": "SK-003",
      "name": "fieldName() 메서드 필수",
      "severity": "ERROR",
      "description": "SortKey VO는 fieldName() 메서드 필수 (SortKey 인터페이스)",
      "pattern": "String\\s+fieldName\\(\\)",
      "archUnitTest": "SortKeyArchTest.sortKey_MustHaveFieldNameMethod"
    },
    {
      "ruleId": "SK-004",
      "name": "*SortKey 네이밍 필수",
      "severity": "ERROR",
      "description": "SortKey VO는 *SortKey 네이밍 규칙 필수",
      "pattern": "\\w+SortKey",
      "archUnitTest": "SortKeyArchTest.sortKey_MustHaveCorrectNaming"
    },
    {
      "ruleId": "SK-005",
      "name": "vo 패키지에 위치",
      "severity": "ERROR",
      "description": "SortKey VO는 vo 패키지에 위치해야 함",
      "pattern": "domain\\.\\w+\\.vo\\.\\w+SortKey",
      "archUnitTest": "SortKeyArchTest.sortKey_MustBeInVoPackage"
    }
  ]
}
```

---

## 코드 예시

### ID VO (Long 기반 - Auto Increment)

```java
public record OrderId(Long value) {
    public static OrderId of(Long value) {
        return new OrderId(value);
    }
    
    public static OrderId forNew() {
        return new OrderId(null);
    }
    
    public boolean isNew() {
        return value == null;
    }
}
```

### ID VO (UUID 기반)

```java
public record TransactionId(String value) {
    public static TransactionId of(String value) {
        return new TransactionId(value);
    }
    
    public static TransactionId forNew() {
        return new TransactionId(UuidCreator.getTimeOrderedEpoch().toString());
    }
    // isNew() 불필요 - UUID는 항상 값 존재
}
```

### Enum VO

```java
public enum OrderStatus {
    PENDING("대기중"),
    CONFIRMED("확정"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    CANCELLED("취소됨");

    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String displayName() {
        return this.displayName;
    }
}
```

### LockKey VO

```java
public record OrderLockKey(Long orderId) implements LockKey {
    private static final String PREFIX = "lock:order:";
    
    public static OrderLockKey of(Long orderId) {
        return new OrderLockKey(orderId);
    }
    
    @Override
    public String value() {
        return PREFIX + orderId;
    }
}
```

### CacheKey VO

```java
public record ProductCacheKey(Long productId) implements CacheKey {
    private static final String PREFIX = "cache:product:";
    
    public ProductCacheKey {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
    }
    
    public static ProductCacheKey of(Long productId) {
        return new ProductCacheKey(productId);
    }
    
    @Override
    public String value() {
        return PREFIX + productId;
    }
}
```

### SortKey VO

```java
public enum OrderSortKey implements SortKey {
    ORDER_DATE("orderDate"),
    TOTAL_AMOUNT("totalAmount"),
    MEMBER_NAME("memberName");

    private final String fieldName;

    OrderSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
```

---

## 단건 조회용 VO 재사용

```java
// ✅ 올바른 사용 - 기존 VO 재사용
public interface UserQueryPort {
    Optional<User> findByEmail(Email email);
    Optional<User> findById(UserId userId);
}

// ❌ 잘못된 사용 - 별도 조회용 VO 생성 불필요
public interface UserQueryPort {
    Optional<User> findByEmail(EmailSearchCriteria criteria); // X
}
```

---

## 관련 문서

- [VO Guide](docs/coding_convention/02-domain-layer/vo/vo-guide.md)
- [VO ArchUnit](docs/coding_convention/02-domain-layer/vo/vo-archunit.md)
- [Query VO Guide](docs/coding_convention/02-domain-layer/vo/query-vo-guide.md)
- [LockKey ArchUnit](docs/coding_convention/02-domain-layer/vo/lockkey-archunit.md)

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09

# Domain Layer - Common Rules

## COMMON_ANNOTATION (3 rules)

Domain Layer 공통 어노테이션 금지 규칙

```json
{
  "category": "COMMON_ANNOTATION",
  "rules": [
    {
      "ruleId": "CMN-001",
      "name": "Lombok 전면 금지",
      "severity": "ERROR",
      "description": "Domain Layer 전체에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor|AllArgsConstructor|NoArgsConstructor|Value|ToString|EqualsAndHashCode)",
      "archUnitTest": "DomainPurityArchTest.domain_MustNotUseLombok"
    },
    {
      "ruleId": "CMN-002",
      "name": "JPA 전면 금지",
      "severity": "ERROR",
      "description": "Domain Layer 전체에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column|ManyToOne|OneToMany|ManyToMany|JoinColumn|Embeddable|Embedded)",
      "archUnitTest": "DomainPurityArchTest.domain_MustNotUseJpa"
    },
    {
      "ruleId": "CMN-003",
      "name": "Spring 전면 금지",
      "severity": "ERROR",
      "description": "Domain Layer 전체에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository|Transactional|Autowired|EventListener|Configuration|Bean)",
      "archUnitTest": "DomainPurityArchTest.domain_MustNotUseSpring"
    }
  ]
}
```

---

## COMMON_TIME (2 rules)

Domain Layer 시간 처리 규칙

```json
{
  "category": "COMMON_TIME",
  "rules": [
    {
      "ruleId": "CMN-004",
      "name": "Instant.now() 직접 호출 금지",
      "severity": "ERROR",
      "description": "테스트 가능성을 위해 Instant.now() 직접 호출 금지, 파라미터로 시간 전달",
      "antiPattern": "Instant\\.now\\(\\)",
      "archUnitTest": "DomainPurityArchTest.domain_MustNotCallInstantNow"
    },
    {
      "ruleId": "CMN-005",
      "name": "LocalDateTime 사용 금지",
      "severity": "ERROR",
      "description": "시간대 불안전한 LocalDateTime 금지, Instant 사용",
      "antiPattern": "LocalDateTime",
      "archUnitTest": "DomainPurityArchTest.domain_MustNotUseLocalDateTime"
    }
  ]
}
```

---

## COMMON_DEPENDENCY (3 rules)

Domain Layer 의존성 규칙

```json
{
  "category": "COMMON_DEPENDENCY",
  "rules": [
    {
      "ruleId": "CMN-006",
      "name": "FK는 VO ID 타입 사용",
      "severity": "ERROR",
      "description": "외래키는 Long 대신 VO ID 타입 사용 (같은 BC 내)",
      "pattern": "private\\s+(final\\s+)?\\w+Id\\s+\\w+Id;",
      "antiPattern": "private\\s+(final\\s+)?Long\\s+\\w+Id;",
      "archUnitTest": "DomainPurityArchTest.domain_FkMustUseVoIdType"
    },
    {
      "ruleId": "CMN-007",
      "name": "외부 레이어 의존 금지",
      "severity": "ERROR",
      "description": "Domain Layer에서 Application/Adapter/Bootstrap 레이어 의존 금지",
      "antiPattern": "import\\s+.*\\.(application|adapter|bootstrap)\\.",
      "archUnitTest": "DomainPurityArchTest.domain_MustNotDependOnOuterLayer"
    },
    {
      "ruleId": "CMN-008",
      "name": "허용 외부 의존성: Java 표준 + jakarta.annotation만",
      "severity": "WARNING",
      "description": "Domain Layer 허용 외부 의존성: java.*, jakarta.annotation.* 만",
      "pattern": "import\\s+(java\\.|jakarta\\.annotation\\.)",
      "antiPattern": "import\\s+(org\\.springframework\\.|jakarta\\.persistence\\.)"
    }
  ]
}
```

---

## COMMON_PACKAGE (2 rules)

Domain Layer 패키지 구조 규칙

```json
{
  "category": "COMMON_PACKAGE",
  "rules": [
    {
      "ruleId": "CMN-009",
      "name": "BC 간 순환 의존 금지",
      "severity": "ERROR",
      "description": "Bounded Context 간 순환 의존 금지",
      "antiPattern": "순환 참조 감지",
      "archUnitTest": "PackageStructureArchTest.boundedContexts_MustNotHaveCycle"
    },
    {
      "ruleId": "CMN-010",
      "name": "common 패키지는 모든 BC에서 접근 가능",
      "severity": "INFO",
      "description": "domain.common 패키지는 모든 Bounded Context에서 접근 가능",
      "pattern": "import\\s+.*\\.domain\\.common\\."
    }
  ]
}
```

---

## 코드 예시

### CMN-001: Lombok 금지

```java
// ❌ 금지
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Value
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Order { ... }

// ✅ Pure Java 사용
public record Money(BigDecimal amount) {
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
}
```

### CMN-002: JPA 금지

```java
// ❌ 금지
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private Long id;
    
    @Column(name = "status")
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

// ✅ Domain은 순수 Java
// JPA 어노테이션은 Persistence Layer의 Entity에서만
public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private OrderStatus status;
}
```

### CMN-003: Spring 금지

```java
// ❌ 금지
@Component
@Service
@Repository
@Transactional
@Autowired
@EventListener
@Configuration
@Bean

// ✅ Domain은 프레임워크 독립적
// Spring 어노테이션은 Application/Adapter Layer에서만
```

### CMN-004: Instant.now() 금지

```java
// ❌ 테스트 불가능
public class Order {
    public void place() {
        this.placedAt = Instant.now();  // X - 테스트 시 시간 제어 불가
    }
}

// ✅ 파라미터로 시간 전달
public class Order {
    public void place(Instant now) {
        this.placedAt = now;  // O - 테스트 시 시간 제어 가능
    }
}
```

### CMN-005: LocalDateTime 금지

```java
// ❌ 시간대 불안전
private LocalDateTime createdAt;

// ✅ 시간대 안전
private Instant createdAt;  // UTC 기준, 시간대 변환은 Presentation에서
```

### CMN-006: VO FK 사용

```java
// ❌ Primitive FK
public class Order {
    private Long customerId;  // X - 타입 안전성 없음
}

// ✅ VO ID FK (같은 BC 내)
public class Order {
    private CustomerId customerId;  // O - 타입 안전, 의미 명확
}

// ✅ Long FK (다른 BC 간 - 순환 의존 방지)
public class Order {
    private Long externalUserId;  // O - 다른 BC의 ID
}
```

### CMN-007: 외부 레이어 의존 금지

```java
// ❌ Domain이 외부 레이어 의존
import com.ryuqq.application.OrderService;    // X
import com.ryuqq.adapter.OrderController;     // X
import com.ryuqq.persistence.OrderEntity;     // X
import com.ryuqq.bootstrap.OrderConfig;       // X

// ✅ 허용된 의존성만
import com.ryuqq.domain.common.vo.DateRange;  // O
import com.ryuqq.domain.order.vo.OrderId;     // O
import java.time.Instant;                      // O
import jakarta.annotation.Nullable;            // O (어노테이션만)
```

### CMN-009: BC 순환 의존 금지

```
// ❌ 순환 의존
domain.order → domain.customer
domain.customer → domain.order

// ✅ Long FK 전략으로 의존 차단
domain.order {
    private Long customerId;  // CustomerId VO 대신 Long 사용
}

// ✅ Domain Event로 느슨한 결합
domain.order → OrderPlacedEvent
domain.customer ← @EventListener(OrderPlacedEvent.class)
```

---

## 허용된 외부 의존성

| 패키지 | 용도 | 비고 |
|--------|------|------|
| `java.*` | Java 표준 라이브러리 | 모든 Java 패키지 |
| `jakarta.annotation.*` | @Nullable 등 | 어노테이션만 |

---

## 패키지 구조

```
domain/
├── common/                    # 공통 인터페이스/추상화
│   ├── event/
│   │   └── DomainEvent.java   # Event 인터페이스
│   ├── exception/
│   │   ├── DomainException.java  # Base Exception
│   │   └── ErrorCode.java        # ErrorCode 인터페이스
│   ├── util/
│   │   └── ClockHolder.java   # 시간 추상화 인터페이스
│   └── vo/
│       ├── DateRange.java     # 날짜 범위
│       ├── PageRequest.java   # 오프셋 페이징
│       ├── CursorPageRequest.java # 커서 페이징
│       ├── SortDirection.java # 정렬 방향
│       ├── SortKey.java       # 정렬 키 인터페이스
│       ├── LockKey.java       # Lock 키 인터페이스
│       └── CacheKey.java      # Cache 키 인터페이스
│
└── {boundedContext}/          # 각 Bounded Context
    ├── aggregate/
    │   └── {name}/
    │       └── {Aggregate}.java  # Aggregate Root
    ├── vo/
    │   ├── {Bc}Id.java        # ID VO
    │   ├── {Other}Vo.java     # 일반 VO
    │   ├── {Bc}Status.java    # Enum VO
    │   ├── {Bc}LockKey.java   # Lock 키
    │   ├── {Bc}CacheKey.java  # Cache 키
    │   └── {Bc}SortKey.java   # Sort 키
    ├── event/
    │   └── {Bc}{Action}Event.java  # Domain Event
    ├── exception/
    │   ├── {Bc}ErrorCode.java      # ErrorCode Enum
    │   └── {Bc}XxxException.java   # Concrete Exception
    └── query/
        └── criteria/
            └── {Bc}SearchCriteria.java  # 복합 검색 조건
```

---

## 관련 문서

- [Domain Guide](docs/coding_convention/02-domain-layer/domain-guide.md)
- [convention-domain-layer-validation-rules](memory: convention-domain-layer-validation-rules)

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09

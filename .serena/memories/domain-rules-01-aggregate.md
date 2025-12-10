# Domain Layer - Aggregate Rules

## AGGREGATE_STRUCTURE (9 rules)

Aggregate Root 구조 규칙

```json
{
  "category": "AGGREGATE_STRUCTURE",
  "rules": [
    {
      "ruleId": "AGG-001",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Aggregate Root에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor|AllArgsConstructor|NoArgsConstructor|Value|ToString|EqualsAndHashCode)",
      "archUnitTest": "AggregateArchTest.aggregate_MustNotUseLombok"
    },
    {
      "ruleId": "AGG-002",
      "name": "JPA 금지",
      "severity": "ERROR",
      "description": "Aggregate Root에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column|ManyToOne|OneToMany|ManyToMany|JoinColumn|Embeddable|Embedded)",
      "archUnitTest": "AggregateArchTest.aggregate_MustNotUseJpa"
    },
    {
      "ruleId": "AGG-003",
      "name": "Spring 금지",
      "severity": "ERROR",
      "description": "Aggregate Root에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository|Transactional|Autowired|EventListener|Configuration|Bean)",
      "archUnitTest": "AggregateArchTest.aggregate_MustNotUseSpring"
    },
    {
      "ruleId": "AGG-004",
      "name": "Setter 금지",
      "severity": "ERROR",
      "description": "Aggregate Root에서 Setter 메서드 금지",
      "antiPattern": "public\\s+void\\s+set[A-Z]",
      "archUnitTest": "AggregateArchTest.aggregate_MustNotHaveSetter"
    },
    {
      "ruleId": "AGG-005",
      "name": "private 생성자 필수",
      "severity": "ERROR",
      "description": "Aggregate Root는 private 생성자 필수 (팩토리 메서드 강제)",
      "pattern": "private\\s+[A-Z]\\w+\\(",
      "archUnitTest": "AggregateArchTest.aggregate_MustHavePrivateConstructor"
    },
    {
      "ruleId": "AGG-006",
      "name": "forNew() 팩토리 메서드 필수",
      "severity": "ERROR",
      "description": "신규 생성용 forNew() 정적 팩토리 메서드 필수",
      "pattern": "public\\s+static\\s+\\w+\\s+forNew\\(",
      "archUnitTest": "AggregateArchTest.aggregate_MustHaveForNewMethod"
    },
    {
      "ruleId": "AGG-007",
      "name": "of() 팩토리 메서드 필수",
      "severity": "ERROR",
      "description": "기존 데이터 래핑용 of() 정적 팩토리 메서드 필수",
      "pattern": "public\\s+static\\s+\\w+\\s+of\\(",
      "archUnitTest": "AggregateArchTest.aggregate_MustHaveOfMethod"
    },
    {
      "ruleId": "AGG-008",
      "name": "reconstitute() 팩토리 메서드 필수",
      "severity": "ERROR",
      "description": "DB 복원용 reconstitute() 정적 팩토리 메서드 필수",
      "pattern": "public\\s+static\\s+\\w+\\s+reconstitute\\(",
      "archUnitTest": "AggregateArchTest.aggregate_MustHaveReconstituteMethod"
    },
    {
      "ruleId": "AGG-009",
      "name": "ID 필드 final 필수",
      "severity": "ERROR",
      "description": "Aggregate Root ID 필드는 final 필수",
      "pattern": "private\\s+final\\s+\\w+Id\\s+\\w+;",
      "archUnitTest": "AggregateArchTest.aggregate_IdFieldMustBeFinal"
    }
  ]
}
```

---

## AGGREGATE_TIME (5 rules)

Aggregate Root 시간 처리 규칙

```json
{
  "category": "AGGREGATE_TIME",
  "rules": [
    {
      "ruleId": "AGG-010",
      "name": "Instant 필드 필수",
      "severity": "ERROR",
      "description": "Aggregate Root는 시간 관리를 위한 Instant 타입 필드 필수",
      "pattern": "private\\s+(final\\s+)?Instant\\s+\\w+;",
      "archUnitTest": "AggregateArchTest.aggregate_MustHaveInstantField"
    },
    {
      "ruleId": "AGG-011",
      "name": "createdAt Instant + final 필수",
      "severity": "ERROR",
      "description": "createdAt 필드는 Instant 타입이고 final 필수",
      "pattern": "private\\s+final\\s+Instant\\s+createdAt;",
      "archUnitTest": "AggregateArchTest.aggregate_CreatedAtMustBeInstantFinal"
    },
    {
      "ruleId": "AGG-012",
      "name": "updatedAt Instant (non-final)",
      "severity": "ERROR",
      "description": "updatedAt 필드는 Instant 타입 (변경 가능하므로 non-final)",
      "pattern": "private\\s+Instant\\s+updatedAt;",
      "archUnitTest": "AggregateArchTest.aggregate_UpdatedAtMustBeInstant"
    },
    {
      "ruleId": "AGG-013",
      "name": "Instant.now() 직접 호출 금지",
      "severity": "ERROR",
      "description": "테스트 가능성을 위해 Instant.now() 직접 호출 금지, 파라미터로 시간 전달",
      "antiPattern": "Instant\\.now\\(\\)",
      "archUnitTest": "AggregateArchTest.aggregate_MustNotCallInstantNow"
    },
    {
      "ruleId": "AGG-014",
      "name": "LocalDateTime 사용 금지",
      "severity": "ERROR",
      "description": "시간대 불안전한 LocalDateTime 금지, Instant 사용",
      "antiPattern": "LocalDateTime",
      "archUnitTest": "AggregateArchTest.aggregate_MustNotUseLocalDateTime"
    }
  ]
}
```

---

## AGGREGATE_PACKAGE (2 rules)

Aggregate Root 패키지/네이밍 규칙

```json
{
  "category": "AGGREGATE_PACKAGE",
  "rules": [
    {
      "ruleId": "AGG-015",
      "name": "패키지 위치 준수",
      "severity": "ERROR",
      "description": "Aggregate Root는 domain.[bc].aggregate.[name] 패키지에 위치",
      "pattern": "domain\\.\\w+\\.aggregate\\.\\w+",
      "archUnitTest": "AggregateArchTest.aggregate_MustBeInCorrectPackage"
    },
    {
      "ruleId": "AGG-016",
      "name": "FK는 VO ID 타입 사용",
      "severity": "ERROR",
      "description": "외래키는 Long 대신 VO ID 타입 사용 (같은 BC 내)",
      "pattern": "private\\s+(final\\s+)?\\w+Id\\s+\\w+Id;",
      "antiPattern": "private\\s+(final\\s+)?Long\\s+\\w+Id;",
      "archUnitTest": "AggregateArchTest.aggregate_FkMustUseVoIdType"
    }
  ]
}
```

---

## AGGREGATE_BUSINESS (2 rules)

Aggregate Root 비즈니스 메서드 규칙

```json
{
  "category": "AGGREGATE_BUSINESS",
  "rules": [
    {
      "ruleId": "AGG-017",
      "name": "비즈니스 메서드 네이밍 권장",
      "severity": "WARNING",
      "description": "비즈니스 의미를 담은 메서드명 사용 권장 (place, cancel, approve 등)",
      "pattern": "(place|cancel|approve|reject|complete|ship|activate|deactivate|archive|restore|update|change|add|remove|assign|unassign|publish|unpublish|expire|renew|verify|confirm|process|execute|apply|revoke|suspend|resume|close|reopen|lock|unlock)\\(",
      "antiPattern": "set[A-Z]\\w+\\(|changeStatus\\("
    },
    {
      "ruleId": "AGG-018",
      "name": "비즈니스 메서드 패턴 가이드",
      "severity": "INFO",
      "description": "Aggregate Root 비즈니스 메서드는 행위 중심 네이밍, 파라미터로 시간 전달"
    }
  ]
}
```

---

## AGGREGATE_EVENT (2 rules)

Aggregate Root Domain Event 규칙

```json
{
  "category": "AGGREGATE_EVENT",
  "rules": [
    {
      "ruleId": "AGG-019",
      "name": "Domain Events 관리 필드 권장",
      "severity": "WARNING",
      "description": "Aggregate Root에서 Domain Events 관리용 List 필드 권장",
      "pattern": "private\\s+final\\s+List<DomainEvent>\\s+domainEvents"
    },
    {
      "ruleId": "AGG-020",
      "name": "pullDomainEvents() 메서드 권장",
      "severity": "WARNING",
      "description": "Domain Events 조회 및 클리어용 pullDomainEvents() 메서드 권장",
      "pattern": "public\\s+List<DomainEvent>\\s+pullDomainEvents\\("
    }
  ]
}
```

---

## AGGREGATE_EXCEPTION (2 rules)

Aggregate Root 예외 처리 규칙

```json
{
  "category": "AGGREGATE_EXCEPTION",
  "rules": [
    {
      "ruleId": "AGG-021",
      "name": "불변조건 위반 → IllegalArgumentException",
      "severity": "ERROR",
      "description": "생성자/팩토리에서 불변조건 위반 시 IllegalArgumentException 던지기",
      "pattern": "throw\\s+new\\s+IllegalArgumentException\\(",
      "rationale": "개발자 버그에 해당하는 불변조건 위반"
    },
    {
      "ruleId": "AGG-022",
      "name": "비즈니스 룰 위반 → DomainException",
      "severity": "ERROR",
      "description": "비즈니스 메서드에서 비즈니스 룰 위반 시 DomainException 던지기",
      "pattern": "throw\\s+new\\s+\\w+Exception\\(",
      "antiPattern": "throw\\s+new\\s+RuntimeException\\(",
      "rationale": "정상적인 비즈니스 예외 상황"
    }
  ]
}
```

---

## 코드 예시

### 올바른 Aggregate Root

```java
public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private OrderStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // ✅ private 생성자
    private Order(OrderId id, CustomerId customerId, OrderStatus status, 
                  Instant createdAt, Instant updatedAt) {
        if (id == null) {
            throw new IllegalArgumentException("OrderId is required");
        }
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ✅ forNew() - 신규 생성
    public static Order forNew(OrderId id, CustomerId customerId, Instant now) {
        Order order = new Order(id, customerId, OrderStatus.PENDING, now, now);
        order.domainEvents.add(OrderPlacedEvent.from(order, now));
        return order;
    }

    // ✅ of() - 기존 데이터 래핑
    public static Order of(OrderId id, CustomerId customerId, OrderStatus status,
                           Instant createdAt, Instant updatedAt) {
        return new Order(id, customerId, status, createdAt, updatedAt);
    }

    // ✅ reconstitute() - DB 복원
    public static Order reconstitute(OrderId id, CustomerId customerId, 
                                      OrderStatus status, Instant createdAt, 
                                      Instant updatedAt) {
        return new Order(id, customerId, status, createdAt, updatedAt);
    }

    // ✅ 비즈니스 메서드 - 시간 파라미터로 전달
    public void cancel(String reason, Instant now) {
        if (this.status == OrderStatus.SHIPPED) {
            throw new OrderCannotCancelException(this.id, "이미 배송됨");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = now;
        this.domainEvents.add(OrderCancelledEvent.from(this, reason, now));
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }
}
```

---

## 관련 문서

- [Aggregate Guide](docs/coding_convention/02-domain-layer/aggregate/aggregate-guide.md)
- [Aggregate ArchUnit](docs/coding_convention/02-domain-layer/aggregate/aggregate-archunit.md)
- [Aggregate Test Guide](docs/coding_convention/02-domain-layer/aggregate/aggregate-test-guide.md)

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09

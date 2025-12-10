# Domain Layer - Criteria Rules

## CRITERIA_LOCATION (3 rules)

Criteria 위치/네이밍 규칙

```json
{
  "category": "CRITERIA_LOCATION",
  "rules": [
    {
      "ruleId": "CRI-001",
      "name": "domain.[bc].query.criteria 패키지 위치",
      "severity": "ERROR",
      "description": "Criteria는 domain.[bc].query.criteria 패키지에 위치",
      "pattern": "domain\\.\\w+\\.query\\.criteria\\.\\w+Criteria",
      "archUnitTest": "CriteriaArchTest.criteria_MustBeInCorrectPackage"
    },
    {
      "ruleId": "CRI-002",
      "name": "*Criteria 또는 *SearchCriteria 네이밍",
      "severity": "ERROR",
      "description": "Criteria는 *Criteria 또는 *SearchCriteria 네이밍 규칙 필수",
      "pattern": "\\w+(Search)?Criteria",
      "archUnitTest": "CriteriaArchTest.criteria_MustHaveCorrectNaming"
    },
    {
      "ruleId": "CRI-003",
      "name": "public 클래스 필수",
      "severity": "ERROR",
      "description": "Criteria는 public 클래스 필수",
      "pattern": "public\\s+record\\s+\\w+Criteria",
      "archUnitTest": "CriteriaArchTest.criteria_MustBePublic"
    }
  ]
}
```

---

## CRITERIA_STRUCTURE (2 rules)

Criteria 구조 규칙

```json
{
  "category": "CRITERIA_STRUCTURE",
  "rules": [
    {
      "ruleId": "CRI-004",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "Criteria는 Record 타입 필수 (불변성 보장)",
      "pattern": "public\\s+record\\s+\\w+Criteria\\(",
      "antiPattern": "public\\s+class\\s+\\w+Criteria",
      "archUnitTest": "CriteriaArchTest.criteria_MustBeRecord"
    },
    {
      "ruleId": "CRI-005",
      "name": "of() 정적 팩토리 메서드 필수",
      "severity": "ERROR",
      "description": "Criteria는 of() 정적 팩토리 메서드 필수 (기본값 적용용)",
      "pattern": "public\\s+static\\s+\\w+Criteria\\s+of\\(",
      "archUnitTest": "CriteriaArchTest.criteria_MustHaveOfMethod"
    }
  ]
}
```

---

## CRITERIA_PROHIBITION (3 rules)

Criteria 금지 규칙

```json
{
  "category": "CRITERIA_PROHIBITION",
  "rules": [
    {
      "ruleId": "CRI-006",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Criteria에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor|AllArgsConstructor|Value)",
      "archUnitTest": "CriteriaArchTest.criteria_MustNotUseLombok"
    },
    {
      "ruleId": "CRI-007",
      "name": "JPA 금지",
      "severity": "ERROR",
      "description": "Criteria에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column|Embeddable|Embedded)",
      "archUnitTest": "CriteriaArchTest.criteria_MustNotUseJpa"
    },
    {
      "ruleId": "CRI-008",
      "name": "Spring 금지",
      "severity": "ERROR",
      "description": "Criteria에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository|Transactional|Autowired)",
      "archUnitTest": "CriteriaArchTest.criteria_MustNotUseSpring"
    }
  ]
}
```

---

## CRITERIA_DEPENDENCY (2 rules)

Criteria 의존성 규칙

```json
{
  "category": "CRITERIA_DEPENDENCY",
  "rules": [
    {
      "ruleId": "CRI-009",
      "name": "외부 레이어 의존 금지",
      "severity": "ERROR",
      "description": "Criteria에서 Application/Adapter/Bootstrap 레이어 의존 금지",
      "antiPattern": "import\\s+.*\\.(application|adapter|bootstrap)\\.",
      "archUnitTest": "CriteriaArchTest.criteria_MustNotDependOnOuterLayer"
    },
    {
      "ruleId": "CRI-010",
      "name": "공통 VO 사용 권장",
      "severity": "WARNING",
      "description": "Criteria는 DateRange, SortDirection, PageRequest 등 공통 VO 사용 권장",
      "pattern": "(DateRange|SortDirection|PageRequest|CursorPageRequest|SortKey)"
    }
  ]
}
```

---

## 코드 예시

### Criteria 구현

```java
// domain/order/query/criteria/OrderSearchCriteria.java
public record OrderSearchCriteria(
    // 1. 필터 조건 (도메인 VO 또는 primitive)
    Long memberId,
    OrderStatus status,
    
    // 2. 날짜 범위 (공통 VO)
    DateRange orderDateRange,
    
    // 3. 정렬 (공통 VO + BC별 SortKey)
    OrderSortKey sortKey,
    SortDirection sortDirection,
    
    // 4. 페이징 (공통 VO)
    PageRequest page
) {
    // ✅ of() 팩토리 메서드로 기본값 적용
    public static OrderSearchCriteria of(
        Long memberId,
        OrderStatus status,
        DateRange orderDateRange,
        OrderSortKey sortKey,
        SortDirection sortDirection,
        PageRequest page
    ) {
        return new OrderSearchCriteria(
            memberId,
            status,
            orderDateRange,
            sortKey != null ? sortKey : OrderSortKey.ORDER_DATE,
            sortDirection != null ? sortDirection : SortDirection.DESC,
            page != null ? page : PageRequest.defaultPage()
        );
    }
}
```

### 공통 VO 예시

```java
// domain/common/vo/DateRange.java
public record DateRange(LocalDate startDate, LocalDate endDate) {
    public static DateRange of(LocalDate startDate, LocalDate endDate) {
        return new DateRange(startDate, endDate);
    }
    
    public static DateRange lastDays(int days) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days);
        return new DateRange(start, end);
    }
}

// domain/common/vo/SortDirection.java
public enum SortDirection {
    ASC("오름차순"),
    DESC("내림차순");
    
    private final String displayName;
    
    SortDirection(String displayName) {
        this.displayName = displayName;
    }
    
    public String displayName() {
        return displayName;
    }
    
    public static SortDirection fromString(String value) {
        return value != null && value.equalsIgnoreCase("ASC") ? ASC : DESC;
    }
}

// domain/common/vo/PageRequest.java
public record PageRequest(int page, int size) {
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }
    
    public static PageRequest defaultPage() {
        return new PageRequest(0, 20);
    }
    
    public int offset() {
        return page * size;
    }
}
```

### SortKey 예시

```java
// domain/order/vo/OrderSortKey.java
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

## Criteria vs VO 구분

| 용도 | 타입 | 위치 | 예시 |
|------|------|------|------|
| **단건 조회** | 기존 VO 재사용 | `domain/{bc}/vo/` | `findByEmail(Email email)` |
| **복합 검색** | Criteria | `domain/{bc}/query/criteria/` | `search(OrderSearchCriteria criteria)` |

### 올바른 사용

```java
// ✅ 단건 조회 → 기존 VO 재사용
public interface UserQueryPort {
    Optional<User> findByEmail(Email email);
    Optional<User> findById(UserId userId);
}

// ✅ 복합 검색 → Criteria 사용
public interface OrderQueryPort {
    Page<Order> search(OrderSearchCriteria criteria);
}
```

### 잘못된 사용

```java
// ❌ 단건 조회에 Criteria 불필요
public interface UserQueryPort {
    Optional<User> findByEmail(EmailSearchCriteria criteria); // X
}
```

---

## 사용 흐름

```
Controller (RequestDto)
    ↓
UseCase (Application Layer *Query)
    ↓  비즈니스 로직 적용 (권한별 기간 조정 등)
Domain Criteria ← DateRange, SortKey, PageRequest 조합
    ↓
QueryPort (out)
    ↓
Adapter (QueryDSL)
```

### Application Layer에서 변환

```java
// Application Layer - Query → Criteria 변환
public record OrderSearchQuery(
    Long memberId,
    OrderStatus status,
    LocalDate startDate,
    LocalDate endDate,
    String sortKey,
    String sortDirection,
    int page,
    int size
) {
    public OrderSearchCriteria toCriteria(UserRole userRole) {
        // 비즈니스 로직: 권한별 조회 기간 조정
        DateRange dateRange = adjustDateRangeByRole(
            DateRange.of(startDate, endDate),
            userRole
        );

        return OrderSearchCriteria.of(
            memberId,
            status,
            dateRange,
            parseSortKey(sortKey),
            SortDirection.fromString(sortDirection),
            PageRequest.of(page, size)
        );
    }
}
```

---

## 금지 패턴

### Criteria에 비즈니스 로직 금지

```java
// ❌ 잘못된 사용 - Criteria에 비즈니스 로직
public record OrderSearchCriteria(...) {
    public boolean isValidForMember(UserRole role) { ... }  // X
}

// ✅ 올바른 사용 - 비즈니스 로직은 Application Layer
public record OrderSearchQuery(...) {
    public OrderSearchCriteria toCriteria(UserRole role) { ... }  // O
}
```

---

## 관련 문서

- [Query VO Guide](docs/coding_convention/02-domain-layer/vo/query-vo-guide.md)
- [VO Guide](docs/coding_convention/02-domain-layer/vo/vo-guide.md)
- [Query Adapter Guide](docs/coding_convention/04-persistence-layer/mysql/adapter/query/general/query-adapter-guide.md)

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09

# Application Layer 서비스 패턴 분석

> 작성일: 2025-12-29
> 목적: 현재 Application 레이어 코드 패턴 분석 및 컨벤션 대비 Gap 분석

---

## 1. 참조 모델 (Good Pattern)

### 1.1 Product 모듈 - 모범 사례

`product` 모듈은 대부분의 컨벤션을 준수하고 있어 **참조 모델**로 사용합니다.

#### 구조
```
product/
├── dto/
│   ├── command/           # Command DTO (record)
│   ├── query/             # Query DTO - ProductGroupSearchQuery
│   ├── response/          # Response DTO
│   └── bundle/            # PersistBundle
├── port/
│   ├── in/command/        # Command UseCase
│   ├── in/query/          # Query UseCase
│   ├── out/command/       # PersistencePort
│   └── out/query/         # QueryPort
├── service/
│   ├── command/           # CommandService
│   └── query/             # QueryService
├── manager/
│   ├── command/           # TransactionManager
│   └── query/             # ReadManager
├── facade/                # Facade (2+ Manager 조율)
├── factory/
│   ├── command/           # CommandFactory
│   └── query/             # QueryFactory
├── assembler/             # Assembler
└── component/             # 도메인 특화 컴포넌트
```

#### 어드민 조회 조건 지원 (ProductGroupSearchQuery)

```java
public record ProductGroupSearchQuery(
    Long sellerId,                    // 필터: 셀러 ID
    Long categoryId,                  // 필터: 카테고리 ID
    Long brandId,                     // 필터: 브랜드 ID
    String name,                      // 검색: 상품그룹명
    String status,                    // 필터: 상태
    ProductSearchPeriod searchPeriod, // 기간: dateType + DateRange
    ProductSortBy sortBy,             // 정렬: 필드
    SortDirection sortDirection,      // 정렬: 방향
    int page,                         // 페이지네이션
    int size                          // 페이지네이션
) {}
```

**지원 기능:**
- 복합 필터 조건
- 키워드 검색 (name LIKE)
- 기간 조회 (dateType + startDate/endDate)
- 정렬 (sortBy + sortDirection)
- 페이지네이션 (page + size)

#### QueryPort - Criteria 패턴

```java
public interface ProductGroupQueryPort {
    Optional<ProductGroup> findById(ProductGroupId productGroupId);
    List<ProductGroup> findByCriteria(ProductGroupSearchCriteria criteria);
    long countByCriteria(ProductGroupSearchCriteria criteria);
    boolean existsById(ProductGroupId productGroupId);
    List<ProductGroup> findByIds(List<ProductGroupId> productGroupIds);
}
```

**컨벤션 준수:**
- `findById` - 단건 조회
- `findByCriteria` - 조건 조회 (Criteria 패턴)
- `countByCriteria` - 카운트 (Criteria 패턴)
- `existsById` - 존재 여부
- `findByIds` - 다건 ID 조회

#### QueryFactory - Query → Criteria 변환

```java
@Component
public class ProductGroupQueryFactory {
    public ProductGroupSearchCriteria create(ProductGroupSearchQuery query) {
        // Query DTO → Domain Criteria 변환
    }
}
```

---

## 2. 위반 패턴 분석 (Gap Analysis)

### 2.1 Seller 모듈 - 리팩토링 필요

#### 현재 상태

| 항목 | 현재 | 컨벤션 | 위반 룰 |
|------|------|--------|---------|
| QueryPort 메서드 | `findByConditions(name, status, offset, limit)` | `findByCriteria(Criteria)` | APP-POQ-002 |
| Criteria 패턴 | 미사용 (개별 파라미터) | Criteria VO 사용 | APP-POQ-004 |
| QueryFactory | 없음 | 필수 | APP-QYF-001 |
| 정렬 지원 | 없음 | sortBy, sortDirection | - |
| 기간 조회 | 없음 | dateType + DateRange | - |
| ReadManager @Transactional | 없음 | 메서드 레벨 readOnly=true | APP-RM-002 |

#### SellerQueryPort 현재 코드

```java
// 현재 (위반)
List<Seller> findByConditions(String sellerName, String approvalStatus, int offset, int limit);
long countByConditions(String sellerName, String approvalStatus);

// 리팩토링 후 (컨벤션)
List<Seller> findByCriteria(SellerSearchCriteria criteria);
long countByCriteria(SellerSearchCriteria criteria);
```

#### SellerSearchQuery 현재 코드

```java
// 현재 (정렬/기간 미지원)
public record SellerSearchQuery(
    String sellerName,
    String approvalStatus,
    int page,
    int size
) {}

// 리팩토링 후 (어드민 조회 조건 지원)
public record SellerSearchQuery(
    String sellerName,
    String approvalStatus,
    LocalDateTime registeredStartDate,
    LocalDateTime registeredEndDate,
    SellerSortBy sortBy,
    SortDirection sortDirection,
    int page,
    int size
) {}
```

---

## 3. 공통 위반 패턴 예상

### 3.1 QueryPort 메서드 네이밍

많은 모듈에서 `findByConditions`, `countByConditions` 대신 Criteria 패턴을 사용해야 합니다.

**변경 전:**
```java
List<T> findByConditions(String field1, String field2, int offset, int limit);
long countByConditions(String field1, String field2);
```

**변경 후:**
```java
List<T> findByCriteria({Bc}SearchCriteria criteria);
long countByCriteria({Bc}SearchCriteria criteria);
```

### 3.2 ReadManager @Transactional 누락

대부분의 ReadManager에서 `@Transactional(readOnly = true)` 메서드 레벨 어노테이션이 누락되어 있습니다.

**변경 전:**
```java
@Component
public class XxxReadManager {
    public Xxx findById(Long id) { ... }  // @Transactional 없음
}
```

**변경 후:**
```java
@Component
public class XxxReadManager {
    @Transactional(readOnly = true)
    public Xxx findById(Long id) { ... }
}
```

### 3.3 QueryFactory 미생성

Query → Criteria 변환을 위한 QueryFactory가 없는 모듈이 많습니다.

**필수 구조:**
```java
@Component
public class {Bc}QueryFactory {
    public {Bc}SearchCriteria createCriteria({Bc}SearchQuery query) {
        return {Bc}SearchCriteria.builder()
            .field1(query.field1())
            .sortBy(query.sortBy())
            .sortDirection(query.sortDirection())
            // ...
            .build();
    }
}
```

### 3.4 어드민 조회 조건 미지원

대부분의 Query DTO에서 어드민에 필요한 조회 조건이 누락되어 있습니다.

**필수 조회 조건:**
- 복합 필터 (다중 조건 AND/OR)
- 키워드 검색 (LIKE)
- 기간 조회 (dateType + startDate + endDate)
- 정렬 (sortBy + sortDirection)
- 페이지네이션 (page + size)

---

## 4. 리팩토링 표준 템플릿

### 4.1 Query DTO 템플릿

```java
/**
 * {Bc} Search Query
 *
 * @param {field1} 필터 조건 1 (nullable)
 * @param {field2} 필터 조건 2 (nullable)
 * @param keyword 검색어 (nullable)
 * @param startDate 시작일 (nullable)
 * @param endDate 종료일 (nullable)
 * @param sortBy 정렬 필드 (nullable, 기본값: CREATED_AT)
 * @param sortDirection 정렬 방향 (nullable, 기본값: DESC)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 */
public record {Bc}SearchQuery(
    Long {field1}Id,
    String {field2},
    String keyword,
    LocalDateTime startDate,
    LocalDateTime endDate,
    {Bc}SortBy sortBy,
    SortDirection sortDirection,
    int page,
    int size
) {
    public {Bc}SearchQuery {
        if (sortBy == null) sortBy = {Bc}SortBy.CREATED_AT;
        if (sortDirection == null) sortDirection = SortDirection.DESC;
    }

    public int offset() {
        return page * size;
    }
}
```

### 4.2 QueryPort 템플릿

```java
/**
 * {Bc} Query Port
 */
public interface {Bc}QueryPort {

    Optional<{Bc}> findById({Bc}Id id);

    List<{Bc}> findByCriteria({Bc}SearchCriteria criteria);

    long countByCriteria({Bc}SearchCriteria criteria);

    boolean existsById({Bc}Id id);
}
```

### 4.3 QueryFactory 템플릿

```java
/**
 * {Bc} Query Factory
 */
@Component
public class {Bc}QueryFactory {

    public {Bc}SearchCriteria createCriteria({Bc}SearchQuery query) {
        return {Bc}SearchCriteria.builder()
            .{field1}Id(query.{field1}Id())
            .{field2}(query.{field2}())
            .keyword(query.keyword())
            .startDate(query.startDate())
            .endDate(query.endDate())
            .sortBy(query.sortBy())
            .sortDirection(query.sortDirection())
            .offset(query.offset())
            .limit(query.size())
            .build();
    }
}
```

### 4.4 ReadManager 템플릿

```java
/**
 * {Bc} Read Manager
 */
@Component
public class {Bc}ReadManager {

    private final {Bc}QueryPort {bc}QueryPort;

    public {Bc}ReadManager({Bc}QueryPort {bc}QueryPort) {
        this.{bc}QueryPort = {bc}QueryPort;
    }

    @Transactional(readOnly = true)
    public {Bc} findById(Long id) {
        {Bc}Id {bc}Id = {Bc}Id.of(id);
        return {bc}QueryPort.findById({bc}Id)
            .orElseThrow(() -> new {Bc}NotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<{Bc}> findByCriteria({Bc}SearchCriteria criteria) {
        return {bc}QueryPort.findByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public long countByCriteria({Bc}SearchCriteria criteria) {
        return {bc}QueryPort.countByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        {Bc}Id {bc}Id = {Bc}Id.of(id);
        return {bc}QueryPort.existsById({bc}Id);
    }
}
```

---

## 5. 정렬/기간 VO 표준

### 5.1 SortBy Enum 패턴

각 도메인별로 정렬 가능한 필드를 Enum으로 정의합니다.

```java
// Domain Layer - domain/{bc}/vo/{Bc}SortBy.java
public enum {Bc}SortBy {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    NAME("name");
    // 도메인별 추가 필드

    private final String field;

    {Bc}SortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static {Bc}SortBy defaultSortBy() {
        return CREATED_AT;
    }

    public static {Bc}SortBy fromString(String value) {
        if (value == null) return defaultSortBy();
        return Arrays.stream(values())
            .filter(v -> v.field.equalsIgnoreCase(value))
            .findFirst()
            .orElse(defaultSortBy());
    }
}
```

### 5.2 공통 SortDirection

```java
// Domain Layer - domain/common/vo/SortDirection.java
public enum SortDirection {
    ASC, DESC;

    public static SortDirection defaultDirection() {
        return DESC;
    }

    public static SortDirection fromString(String value) {
        if (value == null) return defaultDirection();
        return "asc".equalsIgnoreCase(value) ? ASC : DESC;
    }
}
```

### 5.3 DateRange VO

```java
// Domain Layer - domain/common/vo/DateRange.java
public record DateRange(
    LocalDate startDate,
    LocalDate endDate
) {
    public DateRange {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before or equal to endDate");
        }
    }

    public static DateRange of(LocalDate start, LocalDate end) {
        return new DateRange(start, end);
    }

    public boolean contains(LocalDate date) {
        if (date == null) return false;
        boolean afterStart = startDate == null || !date.isBefore(startDate);
        boolean beforeEnd = endDate == null || !date.isAfter(endDate);
        return afterStart && beforeEnd;
    }
}
```

---

## 6. 모듈별 분석 우선순위

### 6.1 우선순위 기준

| 우선순위 | 기준 | 모듈 예시 |
|---------|------|----------|
| P1 (높음) | 핵심 비즈니스 + 어드민 조회 빈번 | product, order, seller, member |
| P2 (중간) | 핵심 비즈니스 관련 | productstock, payment, cart, shipment |
| P3 (낮음) | 콘텐츠/UI 관련 | banner, board, faq, gnb |

### 6.2 분석 체크리스트

각 모듈 분석 시 확인할 항목:

```
□ QueryPort 메서드 네이밍 (findByCriteria/countByCriteria)
□ Criteria 패턴 사용 여부
□ QueryFactory 존재 여부
□ ReadManager @Transactional(readOnly=true)
□ Query DTO 정렬 필드 (sortBy, sortDirection)
□ Query DTO 기간 조회 (startDate, endDate)
□ Query DTO 복합 필터 조건
□ Domain Criteria 존재 여부
□ SortBy Enum 존재 여부
□ Service UseCase 단일 구현 여부
```

---

## 7. 다음 단계

1. **모듈별 현황 분석** - 39개 모듈 구조 파악
2. **세부 분석 문서 작성** - 각 모듈별 Gap 분석
3. **Domain Criteria 설계** - 각 도메인별 Criteria VO
4. **SortBy Enum 설계** - 각 도메인별 정렬 필드
5. **코드 리팩토링** - 문서 완료 후 실제 코드 수정

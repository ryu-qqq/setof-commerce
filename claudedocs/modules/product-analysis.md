# Product 모듈 분석 (Reference Model)

> 작성일: 2025-12-29
> 우선순위: P1 (핵심 비즈니스)
> 상태: **참조 모델** - 대부분의 컨벤션 준수

---

## 1. 현재 상태 요약

| 항목 | 상태 | 비고 |
|------|------|------|
| QueryPort 메서드 네이밍 | ✅ 준수 | `findByCriteria`, `countByCriteria` |
| Criteria 패턴 | ✅ 준수 | `ProductGroupSearchCriteria` 사용 |
| QueryFactory | ✅ 존재 | `ProductGroupQueryFactory` |
| ReadManager @Transactional | ❌ 미준수 | 메서드 레벨 어노테이션 누락 |
| Query DTO 정렬 지원 | ✅ 준수 | `sortBy`, `sortDirection` |
| Query DTO 기간 지원 | ✅ 준수 | `ProductSearchPeriod` |
| Domain Criteria | ✅ 존재 | `ProductGroupSearchCriteria` |
| SortBy Enum | ✅ 존재 | `ProductSortBy` |

---

## 2. 컴포넌트 분석

### 2.1 QueryPort (✅ Good)

**파일**: `application/src/main/java/com/ryuqq/setof/application/product/port/out/query/ProductGroupQueryPort.java`

```java
public interface ProductGroupQueryPort {
    Optional<ProductGroup> findById(ProductGroupId productGroupId);
    List<ProductGroup> findByCriteria(ProductGroupSearchCriteria criteria);  // ✅
    long countByCriteria(ProductGroupSearchCriteria criteria);               // ✅
    boolean existsById(ProductGroupId productGroupId);
    List<ProductGroup> findByIds(List<ProductGroupId> productGroupIds);
}
```

**준수 규칙**:
- APP-POQ-002: `findByCriteria` 메서드명 사용
- APP-POQ-004: Criteria 패턴 사용
- APP-POQ-001: Value Object ID 사용

### 2.2 Query DTO (✅ Good)

**파일**: `application/src/main/java/com/ryuqq/setof/application/product/dto/query/ProductGroupSearchQuery.java`

```java
public record ProductGroupSearchQuery(
    Long sellerId,                    // 필터: 셀러 ID
    Long categoryId,                  // 필터: 카테고리 ID
    Long brandId,                     // 필터: 브랜드 ID
    String name,                      // 검색: 상품그룹명
    String status,                    // 필터: 상태
    ProductSearchPeriod searchPeriod, // ✅ 기간 조회
    ProductSortBy sortBy,             // ✅ 정렬 필드
    SortDirection sortDirection,      // ✅ 정렬 방향
    int page,
    int size
) {}
```

**지원 기능**:
- ✅ 복합 필터 조건 (sellerId, categoryId, brandId, status)
- ✅ 키워드 검색 (name LIKE)
- ✅ 기간 조회 (ProductSearchPeriod: dateType + DateRange)
- ✅ 정렬 (sortBy + sortDirection)
- ✅ 페이지네이션 (page + size)

### 2.3 QueryFactory (✅ Good)

**파일**: `application/src/main/java/com/ryuqq/setof/application/product/factory/query/ProductGroupQueryFactory.java`

```java
@Component
public class ProductGroupQueryFactory {
    public ProductGroupSearchCriteria create(ProductGroupSearchQuery query) {
        ProductSortType sortType = convertToSortType(query.sortBy(), query.sortDirection());
        return ProductGroupSearchCriteria.of(...);
    }
}
```

**역할**: Query DTO → Domain Criteria 변환

### 2.4 ReadManager (❌ 수정 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/product/manager/query/ProductGroupReadManager.java`

```java
@Component
public class ProductGroupReadManager {

    // ❌ @Transactional(readOnly = true) 누락
    public ProductGroup findById(Long productGroupId) { ... }

    // ❌ @Transactional(readOnly = true) 누락
    public List<ProductGroup> findByCriteria(ProductGroupSearchCriteria criteria) { ... }

    // ❌ @Transactional(readOnly = true) 누락
    public long countByCriteria(ProductGroupSearchCriteria criteria) { ... }

    // ❌ @Transactional(readOnly = true) 누락
    public boolean existsById(Long productGroupId) { ... }

    // ❌ @Transactional(readOnly = true) 누락
    public List<ProductGroup> findByIds(List<Long> productGroupIds) { ... }
}
```

---

## 3. 리팩토링 항목

### 3.1 ReadManager @Transactional 추가

**우선순위**: 🔴 높음 (컨벤션 위반)

**변경 전**:
```java
public ProductGroup findById(Long productGroupId) { ... }
```

**변경 후**:
```java
@Transactional(readOnly = true)
public ProductGroup findById(Long productGroupId) { ... }
```

**적용 메서드**:
- `findById`
- `findByCriteria`
- `countByCriteria`
- `existsById`
- `findByIds`

---

## 4. 다른 모듈 참조용 템플릿

### 4.1 QueryPort 템플릿

```java
public interface {Bc}QueryPort {
    Optional<{Bc}> findById({Bc}Id id);
    List<{Bc}> findByCriteria({Bc}SearchCriteria criteria);
    long countByCriteria({Bc}SearchCriteria criteria);
    boolean existsById({Bc}Id id);
}
```

### 4.2 Query DTO 템플릿

```java
public record {Bc}SearchQuery(
    // 필터 조건
    Long {field1}Id,
    String {field2},

    // 검색
    String keyword,

    // 기간 조회
    LocalDateTime startDate,
    LocalDateTime endDate,

    // 정렬
    {Bc}SortBy sortBy,
    SortDirection sortDirection,

    // 페이지네이션
    int page,
    int size
) {
    public int offset() {
        return page * size;
    }
}
```

### 4.3 QueryFactory 템플릿

```java
@Component
public class {Bc}QueryFactory {
    public {Bc}SearchCriteria createCriteria({Bc}SearchQuery query) {
        return {Bc}SearchCriteria.builder()
            .{field1}Id(query.{field1}Id())
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
}
```

---

## 5. 체크리스트

- [x] QueryPort 메서드 네이밍 (findByCriteria/countByCriteria)
- [x] Criteria 패턴 사용 여부
- [x] QueryFactory 존재 여부
- [ ] ReadManager @Transactional(readOnly=true) ← **수정 필요**
- [x] Query DTO 정렬 필드 (sortBy, sortDirection)
- [x] Query DTO 기간 조회 (startDate, endDate)
- [x] Query DTO 복합 필터 조건
- [x] Domain Criteria 존재 여부
- [x] SortBy Enum 존재 여부

---

## 6. 예상 작업량

| 작업 | 예상 복잡도 | 영향 파일 수 |
|------|------------|-------------|
| ReadManager @Transactional 추가 | 🟢 낮음 | 1 |

**총 작업량**: 🟢 낮음 (참조 모델로 활용)

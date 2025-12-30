# Seller 모듈 분석

> 작성일: 2025-12-29
> 우선순위: P1 (핵심 비즈니스)
> 상태: **리팩토링 필요** - 다수 컨벤션 위반

---

## 1. 현재 상태 요약

| 항목 | 상태 | 비고 |
|------|------|------|
| QueryPort 메서드 네이밍 | ❌ 미준수 | `findByConditions`, `countByConditions` 사용 |
| Criteria 패턴 | ❌ 미사용 | 개별 파라미터 전달 |
| QueryFactory | ❌ 미존재 | 생성 필요 |
| ReadManager @Transactional | ❌ 미준수 | 메서드 레벨 어노테이션 누락 |
| Query DTO 정렬 지원 | ❌ 미지원 | sortBy, sortDirection 누락 |
| Query DTO 기간 지원 | ❌ 미지원 | startDate, endDate 누락 |
| Domain Criteria | ❌ 미존재 | 생성 필요 |
| SortBy Enum | ❌ 미존재 | 생성 필요 |

---

## 2. 컴포넌트 분석

### 2.1 QueryPort (❌ 리팩토링 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/seller/port/out/query/SellerQueryPort.java`

```java
public interface SellerQueryPort {

    Optional<Seller> findById(SellerId id);                       // ✅ OK

    // ❌ 위반: 개별 파라미터 사용 (Criteria 패턴 미사용)
    List<Seller> findByConditions(String sellerName, String approvalStatus, int offset, int limit);

    // ❌ 위반: 개별 파라미터 사용
    long countByConditions(String sellerName, String approvalStatus);

    boolean existsById(SellerId id);                              // ✅ OK

    boolean existsActiveById(Long sellerId);                      // ✅ OK

    boolean existsByRegistrationNumber(RegistrationNumber registrationNumber);  // ✅ OK
}
```

**위반 규칙**:
- APP-POQ-002: `findByConditions` → `findByCriteria`로 변경 필요
- APP-POQ-002: `countByConditions` → `countByCriteria`로 변경 필요
- APP-POQ-004: 개별 파라미터 대신 Criteria 패턴 사용 필요

### 2.2 Query DTO (❌ 리팩토링 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/seller/dto/query/SellerSearchQuery.java`

```java
// 현재 (매우 단순)
public record SellerSearchQuery(
    String sellerName,
    String approvalStatus,
    int page,
    int size
) {}
```

**누락 필드**:
- `sortBy` (SellerSortBy Enum)
- `sortDirection` (SortDirection)
- `registeredStartDate` (LocalDateTime)
- `registeredEndDate` (LocalDateTime)
- 추가 필터 조건 (대표자명, 사업자등록번호 등)

### 2.3 ReadManager (❌ 리팩토링 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/seller/manager/query/SellerReadManager.java` (존재 시)

```java
@Component
public class SellerReadManager {

    // ❌ @Transactional(readOnly = true) 누락
    public Seller findById(Long sellerId) { ... }

    // ❌ findByConditions 사용 (findByCriteria 아님)
    public List<Seller> findByConditions(String sellerName, String approvalStatus, int offset, int limit) { ... }
}
```

**위반 규칙**:
- APP-RM-002: `@Transactional(readOnly = true)` 누락
- APP-RM-001: Criteria 패턴 미사용

### 2.4 QueryFactory (❌ 미존재)

**생성 필요**: `application/src/main/java/com/ryuqq/setof/application/seller/factory/query/SellerQueryFactory.java`

### 2.5 Domain Criteria (❌ 미존재)

**생성 필요**: `domain/src/main/java/com/ryuqq/setof/domain/seller/query/criteria/SellerSearchCriteria.java`

---

## 3. 리팩토링 계획

### 3.1 Domain Layer 변경

#### 3.1.1 SellerSearchCriteria 생성

**파일**: `domain/src/main/java/com/ryuqq/setof/domain/seller/query/criteria/SellerSearchCriteria.java`

```java
public record SellerSearchCriteria(
    // 필터 조건
    String sellerName,
    String approvalStatus,
    String representativeName,
    String registrationNumber,

    // 기간 조회
    LocalDateTime registeredStartDate,
    LocalDateTime registeredEndDate,

    // 정렬
    SellerSortBy sortBy,
    SortDirection sortDirection,

    // 페이지네이션
    int offset,
    int limit
) {
    public static SellerSearchCriteriaBuilder builder() {
        return new SellerSearchCriteriaBuilder();
    }
}
```

#### 3.1.2 SellerSortBy Enum 생성

**파일**: `domain/src/main/java/com/ryuqq/setof/domain/seller/vo/SellerSortBy.java`

```java
public enum SellerSortBy {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    SELLER_NAME("sellerName"),
    APPROVAL_STATUS("approvalStatus");

    private final String field;

    SellerSortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static SellerSortBy defaultSortBy() {
        return CREATED_AT;
    }
}
```

### 3.2 Application Layer 변경

#### 3.2.1 QueryPort 수정

**변경 전**:
```java
List<Seller> findByConditions(String sellerName, String approvalStatus, int offset, int limit);
long countByConditions(String sellerName, String approvalStatus);
```

**변경 후**:
```java
List<Seller> findByCriteria(SellerSearchCriteria criteria);
long countByCriteria(SellerSearchCriteria criteria);
```

#### 3.2.2 Query DTO 수정

**변경 전**:
```java
public record SellerSearchQuery(
    String sellerName,
    String approvalStatus,
    int page,
    int size
) {}
```

**변경 후**:
```java
public record SellerSearchQuery(
    // 필터
    String sellerName,
    String approvalStatus,
    String representativeName,
    String registrationNumber,

    // 기간 조회
    LocalDateTime registeredStartDate,
    LocalDateTime registeredEndDate,

    // 정렬
    SellerSortBy sortBy,
    SortDirection sortDirection,

    // 페이지네이션
    int page,
    int size
) {
    public SellerSearchQuery {
        if (sortBy == null) sortBy = SellerSortBy.CREATED_AT;
        if (sortDirection == null) sortDirection = SortDirection.DESC;
    }

    public int offset() {
        return page * size;
    }
}
```

#### 3.2.3 QueryFactory 생성

**파일**: `application/src/main/java/com/ryuqq/setof/application/seller/factory/query/SellerQueryFactory.java`

```java
@Component
public class SellerQueryFactory {

    public SellerSearchCriteria createCriteria(SellerSearchQuery query) {
        return SellerSearchCriteria.builder()
            .sellerName(query.sellerName())
            .approvalStatus(query.approvalStatus())
            .representativeName(query.representativeName())
            .registrationNumber(query.registrationNumber())
            .registeredStartDate(query.registeredStartDate())
            .registeredEndDate(query.registeredEndDate())
            .sortBy(query.sortBy())
            .sortDirection(query.sortDirection())
            .offset(query.offset())
            .limit(query.size())
            .build();
    }
}
```

#### 3.2.4 ReadManager 수정

**변경 전**:
```java
@Component
public class SellerReadManager {
    public Seller findById(Long sellerId) { ... }
    public List<Seller> findByConditions(...) { ... }
}
```

**변경 후**:
```java
@Component
public class SellerReadManager {

    private final SellerQueryPort sellerQueryPort;

    public SellerReadManager(SellerQueryPort sellerQueryPort) {
        this.sellerQueryPort = sellerQueryPort;
    }

    @Transactional(readOnly = true)
    public Seller findById(Long sellerId) {
        SellerId id = SellerId.of(sellerId);
        return sellerQueryPort.findById(id)
            .orElseThrow(() -> new SellerNotFoundException(sellerId));
    }

    @Transactional(readOnly = true)
    public List<Seller> findByCriteria(SellerSearchCriteria criteria) {
        return sellerQueryPort.findByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(SellerSearchCriteria criteria) {
        return sellerQueryPort.countByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long sellerId) {
        SellerId id = SellerId.of(sellerId);
        return sellerQueryPort.existsById(id);
    }
}
```

### 3.3 Persistence Layer 변경

#### 3.3.1 QueryAdapter 수정

**변경 전**:
```java
@Override
public List<Seller> findByConditions(String sellerName, String approvalStatus, int offset, int limit) { ... }

@Override
public long countByConditions(String sellerName, String approvalStatus) { ... }
```

**변경 후**:
```java
@Override
public List<Seller> findByCriteria(SellerSearchCriteria criteria) { ... }

@Override
public long countByCriteria(SellerSearchCriteria criteria) { ... }
```

#### 3.3.2 QueryDslRepository 수정

Criteria 기반 조회 메서드 구현 필요. 정렬, 기간 조회 조건 추가.

---

## 4. 영향 파일 목록

### Domain Layer
| 파일 | 작업 |
|------|------|
| `domain/.../seller/query/criteria/SellerSearchCriteria.java` | 신규 생성 |
| `domain/.../seller/vo/SellerSortBy.java` | 신규 생성 |

### Application Layer
| 파일 | 작업 |
|------|------|
| `application/.../seller/port/out/query/SellerQueryPort.java` | 수정 |
| `application/.../seller/dto/query/SellerSearchQuery.java` | 수정 |
| `application/.../seller/factory/query/SellerQueryFactory.java` | 신규 생성 |
| `application/.../seller/manager/query/SellerReadManager.java` | 수정 |

### Persistence Layer
| 파일 | 작업 |
|------|------|
| `adapter-out/.../seller/adapter/SellerQueryAdapter.java` | 수정 |
| `adapter-out/.../seller/repository/SellerQueryDslRepository.java` | 수정 |

---

## 5. 체크리스트

- [ ] Domain: SellerSearchCriteria 생성
- [ ] Domain: SellerSortBy Enum 생성
- [ ] Application: QueryPort 메서드명 변경 (findByCriteria, countByCriteria)
- [ ] Application: Query DTO 리팩토링 (정렬/기간/추가 필터 추가)
- [ ] Application: QueryFactory 생성
- [ ] Application: ReadManager @Transactional 추가
- [ ] Persistence: QueryAdapter 수정
- [ ] Persistence: QueryDslRepository 수정

---

## 6. 예상 작업량

| 작업 | 예상 복잡도 | 영향 파일 수 |
|------|------------|-------------|
| Domain Criteria/SortBy 생성 | 🟢 낮음 | 2 |
| QueryPort 리팩토링 | 🟡 중간 | 1 |
| Query DTO 리팩토링 | 🟡 중간 | 1 |
| QueryFactory 생성 | 🟢 낮음 | 1 |
| ReadManager 수정 | 🟢 낮음 | 1 |
| Persistence 수정 | 🟡 중간 | 2 |

**총 작업량**: 🟡 중간 (8개 파일 수정/생성)

---

## 7. 특이사항

### 7.1 Admin 조회 조건 확장

Seller 모듈은 Admin에서 많이 사용되므로 다음 조회 조건이 필수:

- **복합 필터**: 셀러명, 승인상태, 대표자명, 사업자등록번호
- **기간 조회**: 등록일 기준 (registeredStartDate ~ registeredEndDate)
- **정렬**: 등록일, 수정일, 셀러명, 승인상태
- **페이지네이션**: offset + limit

### 7.2 기존 API 호환성

V1 API에서 기존 `findByConditions` 메서드를 사용하는 경우, 점진적 마이그레이션 필요.

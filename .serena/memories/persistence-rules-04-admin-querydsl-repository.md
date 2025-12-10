# Persistence Layer Rules - 04. Admin QueryDSL Repository

> **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/repository/admin-querydsl/`

---

## 규칙 목록

### 1. 클래스 구조 규칙

```json
{
  "ruleId": "ADMIN-QDSL-001",
  "name": "클래스 타입 필수",
  "severity": "CRITICAL",
  "description": "Admin QueryDSL Repository는 반드시 클래스여야 한다",
  "pattern": "public class {Bc}AdminQueryDslRepository",
  "antiPattern": "public interface {Bc}AdminQueryDslRepository"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-002",
  "name": "@Repository 어노테이션 필수",
  "severity": "CRITICAL",
  "description": "Admin QueryDSL Repository는 반드시 @Repository 어노테이션을 가져야 한다",
  "pattern": "@Repository\npublic class {Bc}AdminQueryDslRepository",
  "antiPattern": "public class {Bc}AdminQueryDslRepository // @Repository 없음"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-003",
  "name": "*AdminQueryDslRepository 네이밍",
  "severity": "CRITICAL",
  "description": "클래스명은 반드시 *AdminQueryDslRepository로 끝나야 한다",
  "pattern": "public class OrderAdminQueryDslRepository",
  "antiPattern": "public class AdminOrderQueryDslRepository, OrderAdminRepository"
}
```

### 2. 필드 규칙

```json
{
  "ruleId": "ADMIN-QDSL-004",
  "name": "JPAQueryFactory 필드 필수",
  "severity": "CRITICAL",
  "description": "Admin QueryDSL Repository는 반드시 JPAQueryFactory 필드를 가져야 한다",
  "pattern": "private final JPAQueryFactory queryFactory;",
  "antiPattern": "// JPAQueryFactory 없이 구현"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-005",
  "name": "final 필드 (생성자 주입)",
  "severity": "CRITICAL",
  "description": "모든 필드는 final로 선언하여 생성자 주입",
  "pattern": "private final JPAQueryFactory queryFactory;",
  "antiPattern": "private JPAQueryFactory queryFactory; // final 아님"
}
```

### 3. 메서드 규칙 (일반 QueryDSL과 다름)

```json
{
  "ruleId": "ADMIN-QDSL-006",
  "name": "메서드 개수 제한 없음",
  "severity": "INFO",
  "description": "Admin QueryDSL Repository는 메서드 개수 제한 없음 (자유롭게 정의)",
  "pattern": "// 관리자 요구사항에 맞는 다양한 조회 메서드 정의 가능",
  "antiPattern": "// 4개 메서드만 정의 (일반 QueryDSL과 혼동)"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-007",
  "name": "Join 허용",
  "severity": "INFO",
  "description": "Admin QueryDSL Repository에서는 Join 사용 허용",
  "pattern": "queryFactory.select(...).from({bc}).join({bc}.customer, customer)",
  "antiPattern": "// Join 금지 (일반 QueryDSL과 혼동)"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-008",
  "name": "DTO Projection 반환",
  "severity": "CRITICAL",
  "description": "Admin QueryDSL Repository는 DTO Projection을 반환해야 한다",
  "pattern": "public List<{Bc}AdminDto> findForAdminList(...)",
  "antiPattern": "public List<{Bc}JpaEntity> findForAdminList(...) // Entity 반환 금지"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-009",
  "name": "Projections.constructor() 패턴",
  "severity": "RECOMMENDED",
  "description": "DTO Projection은 Projections.constructor() 사용 권장",
  "pattern": "Projections.constructor({Bc}AdminDto.class, {bc}.id, {bc}.name, ...)",
  "antiPattern": "// Entity를 직접 반환 후 변환"
}
```

### 4. 금지 규칙

```json
{
  "ruleId": "ADMIN-QDSL-010",
  "name": "@Transactional 금지",
  "severity": "CRITICAL",
  "description": "트랜잭션은 Application Layer에서 관리",
  "pattern": "@Repository\npublic class {Bc}AdminQueryDslRepository",
  "antiPattern": "@Repository\n@Transactional\npublic class {Bc}AdminQueryDslRepository"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-011",
  "name": "Mapper 의존성 금지",
  "severity": "CRITICAL",
  "description": "Repository에서 Mapper 의존 금지 (DTO Projection으로 직접 반환)",
  "pattern": "// JPAQueryFactory만 의존, DTO는 QueryDSL Projection으로",
  "antiPattern": "private final {Bc}JpaEntityMapper mapper; // 금지"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-012",
  "name": "Domain 반환 금지",
  "severity": "CRITICAL",
  "description": "Admin Repository는 DTO 반환, Domain 변환 안 함",
  "pattern": "public List<{Bc}AdminDto> findForAdminList(...)",
  "antiPattern": "public List<{Bc}> findForAdminList(...) // Domain 반환 금지"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-013",
  "name": "Entity 반환 금지",
  "severity": "CRITICAL",
  "description": "Admin Repository는 DTO Projection 반환, Entity 반환 안 함",
  "pattern": "public List<{Bc}AdminDto> findForAdminList(...)",
  "antiPattern": "public List<{Bc}JpaEntity> findForAdminList(...) // Entity 반환 금지"
}
```

### 5. 용도 구분 규칙

```json
{
  "ruleId": "ADMIN-QDSL-014",
  "name": "관리자 전용 조회",
  "severity": "CRITICAL",
  "description": "Admin QueryDSL Repository는 관리자 화면/API 전용",
  "pattern": "// 관리자 목록, 통계, 복잡한 Join 조회용",
  "antiPattern": "// 일반 사용자 조회에 사용 (일반 QueryDSL 사용)"
}
```

```json
{
  "ruleId": "ADMIN-QDSL-015",
  "name": "복잡한 조회 전용",
  "severity": "INFO",
  "description": "Join이 필요한 복잡한 조회는 Admin QueryDSL Repository에서 처리",
  "pattern": "// 고객 정보 포함 주문 목록, 통계 데이터 등",
  "antiPattern": "// 단순 조회는 일반 QueryDslRepository 사용"
}
```

---

## 템플릿 코드

```java
@Repository
public class {Bc}AdminQueryDslRepository {

    private static final Q{Bc}JpaEntity {bc} = Q{Bc}JpaEntity.{bc}JpaEntity;
    private static final QCustomerJpaEntity customer = QCustomerJpaEntity.customerJpaEntity;

    private final JPAQueryFactory queryFactory;

    public {Bc}AdminQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 관리자용 주문 목록 조회 (고객 정보 포함)
     *
     * @param criteria 검색 조건
     * @return 관리자용 DTO 목록
     */
    public List<{Bc}AdminDto> findForAdminList({Bc}AdminSearchCriteria criteria) {
        return queryFactory
            .select(Projections.constructor(
                {Bc}AdminDto.class,
                {bc}.id,
                {bc}.orderNumber,
                customer.name,      // Join으로 가져온 고객명
                customer.email,     // Join으로 가져온 이메일
                {bc}.totalAmount,
                {bc}.status,
                {bc}.createdAt
            ))
            .from({bc})
            .join(customer).on({bc}.customerId.eq(customer.id))  // Join 허용
            .where(buildCondition(criteria))
            .offset(criteria.offset())
            .limit(criteria.limit())
            .orderBy({bc}.id.desc())
            .fetch();
    }

    /**
     * 관리자용 통계 조회
     */
    public {Bc}StatisticsDto findStatistics({Bc}StatisticsCriteria criteria) {
        // 복잡한 집계 쿼리 가능
    }

    private BooleanBuilder buildCondition({Bc}AdminSearchCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();
        // 동적 조건 추가
        return builder;
    }
}
```

---

## 일반 QueryDSL vs Admin QueryDSL 비교

| 항목 | QueryDslRepository | AdminQueryDslRepository |
|------|-------------------|------------------------|
| **메서드 수** | 4개 고정 | 제한 없음 |
| **Join** | ❌ 절대 금지 | ✅ 허용 |
| **반환 타입** | Entity | DTO Projection |
| **용도** | 일반 사용자 조회 | 관리자 조회 |
| **네이밍** | *QueryDslRepository | *AdminQueryDslRepository |

---

## 체크리스트

Admin QueryDSL Repository 구현 시:
- [ ] 클래스 타입 (interface 아님)
- [ ] @Repository 어노테이션
- [ ] *AdminQueryDslRepository 네이밍
- [ ] JPAQueryFactory 필드 존재
- [ ] DTO Projection 반환 (Entity/Domain 아님)
- [ ] @Transactional 어노테이션 없음
- [ ] Mapper 의존성 없음
- [ ] 관리자 전용 용도 확인

---

**총 규칙 수**: 15개
**작성일**: 2025-12-08

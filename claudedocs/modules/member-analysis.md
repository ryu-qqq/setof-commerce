# Member 모듈 분석

> 작성일: 2025-12-29
> 우선순위: P1 (핵심 비즈니스)
> 상태: **부분 리팩토링 필요** - 일부 컨벤션 미준수

---

## 1. 현재 상태 요약

| 항목 | 상태 | 비고 |
|------|------|------|
| QueryPort 메서드 네이밍 | ✅ 준수 | `findByCriteria`, `countByCriteria` |
| Criteria 패턴 | ✅ 준수 | `MemberSearchCriteria` 사용 |
| QueryFactory | ❌ 미존재 | 생성 필요 |
| ReadManager @Transactional | ✅ 준수 | 메서드 레벨 어노테이션 있음 |
| Query DTO 정렬 지원 | ❌ 미지원 | sortBy, sortDirection 누락 |
| Query DTO 기간 지원 | ❌ 미지원 | startDate, endDate 누락 |
| Domain Criteria | ✅ 존재 | `MemberSearchCriteria` |
| SortBy Enum | ❌ 미존재 | 생성 필요 |

---

## 2. 컴포넌트 분석

### 2.1 QueryPort (✅ Good)

**파일**: `application/src/main/java/com/ryuqq/setof/application/member/port/out/query/MemberQueryPort.java`

```java
public interface MemberQueryPort {

    Optional<Member> findById(MemberId id);                       // ✅ OK

    Optional<Member> findByPhoneNumber(PhoneNumber phoneNumber);  // ✅ OK

    Optional<Member> findBySocialId(SocialId socialId);           // ✅ OK

    boolean existsByPhoneNumber(PhoneNumber phoneNumber);         // ✅ OK

    List<Member> findByCriteria(MemberSearchCriteria criteria);   // ✅ Criteria 패턴 사용

    long countByCriteria(MemberSearchCriteria criteria);          // ✅ Criteria 패턴 사용
}
```

**준수 규칙**:
- APP-POQ-002: `findByCriteria`, `countByCriteria` 메서드명 사용 ✅
- APP-POQ-004: Criteria 패턴 사용 ✅
- APP-POQ-001: Value Object ID 사용 ✅

### 2.2 ReadManager (✅ Good)

**파일**: `application/src/main/java/com/ryuqq/setof/application/member/manager/query/MemberReadManager.java`

```java
@Component
public class MemberReadManager {

    private final MemberQueryPort memberQueryPort;

    public MemberReadManager(MemberQueryPort memberQueryPort) {
        this.memberQueryPort = memberQueryPort;
    }

    @Transactional(readOnly = true)  // ✅ 어노테이션 있음
    public Member findById(String memberId) {
        MemberId id = MemberId.fromString(memberId);
        return memberQueryPort.findById(id)
            .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    @Transactional(readOnly = true)  // ✅ 어노테이션 있음
    public List<Member> findByCriteria(MemberSearchCriteria criteria) {
        return memberQueryPort.findByCriteria(criteria);
    }

    @Transactional(readOnly = true)  // ✅ 어노테이션 있음
    public long countByCriteria(MemberSearchCriteria criteria) {
        return memberQueryPort.countByCriteria(criteria);
    }
}
```

**준수 규칙**:
- APP-RM-002: `@Transactional(readOnly = true)` 메서드 레벨 어노테이션 ✅

### 2.3 Query DTO (❌ 리팩토링 필요)

**파일**: `application/src/main/java/com/ryuqq/setof/application/member/dto/query/GetMembersQuery.java`

```java
// 현재 (정렬/기간 미지원)
public record GetMembersQuery(
    String name,
    String phoneNumber,
    String status,
    int page,
    int size
) {}
```

**누락 필드**:
- `sortBy` (MemberSortBy Enum)
- `sortDirection` (SortDirection)
- `registeredStartDate` (LocalDateTime)
- `registeredEndDate` (LocalDateTime)

### 2.4 QueryFactory (❌ 미존재)

**생성 필요**: `application/src/main/java/com/ryuqq/setof/application/member/factory/query/MemberQueryFactory.java`

### 2.5 SortBy Enum (❌ 미존재)

**생성 필요**: `domain/src/main/java/com/ryuqq/setof/domain/member/vo/MemberSortBy.java`

---

## 3. 리팩토링 계획

### 3.1 Domain Layer 변경

#### 3.1.1 MemberSortBy Enum 생성

**파일**: `domain/src/main/java/com/ryuqq/setof/domain/member/vo/MemberSortBy.java`

```java
public enum MemberSortBy {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    NAME("name"),
    LAST_LOGIN_AT("lastLoginAt");

    private final String field;

    MemberSortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static MemberSortBy defaultSortBy() {
        return CREATED_AT;
    }
}
```

#### 3.1.2 MemberSearchCriteria 확장 (기존 파일 수정)

기존 Criteria에 정렬/기간 필드 추가 필요:

```java
public record MemberSearchCriteria(
    // 기존 필터 조건
    String name,
    String phoneNumber,
    String status,

    // 추가: 기간 조회
    LocalDateTime registeredStartDate,
    LocalDateTime registeredEndDate,

    // 추가: 정렬
    MemberSortBy sortBy,
    SortDirection sortDirection,

    // 페이지네이션
    int offset,
    int limit
) {}
```

### 3.2 Application Layer 변경

#### 3.2.1 Query DTO 수정

**변경 전**:
```java
public record GetMembersQuery(
    String name,
    String phoneNumber,
    String status,
    int page,
    int size
) {}
```

**변경 후 (MemberSearchQuery로 명명 변경)**:
```java
public record MemberSearchQuery(
    // 필터
    String name,
    String phoneNumber,
    String status,
    String email,

    // 기간 조회
    LocalDateTime registeredStartDate,
    LocalDateTime registeredEndDate,

    // 정렬
    MemberSortBy sortBy,
    SortDirection sortDirection,

    // 페이지네이션
    int page,
    int size
) {
    public MemberSearchQuery {
        if (sortBy == null) sortBy = MemberSortBy.CREATED_AT;
        if (sortDirection == null) sortDirection = SortDirection.DESC;
    }

    public int offset() {
        return page * size;
    }
}
```

#### 3.2.2 QueryFactory 생성

**파일**: `application/src/main/java/com/ryuqq/setof/application/member/factory/query/MemberQueryFactory.java`

```java
@Component
public class MemberQueryFactory {

    public MemberSearchCriteria createCriteria(MemberSearchQuery query) {
        return MemberSearchCriteria.builder()
            .name(query.name())
            .phoneNumber(query.phoneNumber())
            .status(query.status())
            .email(query.email())
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

### 3.3 Persistence Layer 변경

#### 3.3.1 QueryDslRepository 수정

정렬, 기간 조회 조건 지원하도록 수정 필요.

---

## 4. 영향 파일 목록

### Domain Layer
| 파일 | 작업 |
|------|------|
| `domain/.../member/vo/MemberSortBy.java` | 신규 생성 |
| `domain/.../member/query/criteria/MemberSearchCriteria.java` | 수정 (정렬/기간 필드 추가) |

### Application Layer
| 파일 | 작업 |
|------|------|
| `application/.../member/dto/query/GetMembersQuery.java` | 수정 (또는 MemberSearchQuery로 교체) |
| `application/.../member/factory/query/MemberQueryFactory.java` | 신규 생성 |

### Persistence Layer
| 파일 | 작업 |
|------|------|
| `adapter-out/.../member/repository/MemberQueryDslRepository.java` | 수정 (정렬/기간 지원) |

---

## 5. 체크리스트

- [x] QueryPort 메서드 네이밍 (findByCriteria/countByCriteria)
- [x] Criteria 패턴 사용 여부
- [ ] QueryFactory 생성 ← **필요**
- [x] ReadManager @Transactional(readOnly=true)
- [ ] Query DTO 정렬 필드 (sortBy, sortDirection) ← **필요**
- [ ] Query DTO 기간 조회 (startDate, endDate) ← **필요**
- [x] Query DTO 복합 필터 조건
- [x] Domain Criteria 존재 여부 (확장 필요)
- [ ] SortBy Enum 생성 ← **필요**

---

## 6. 예상 작업량

| 작업 | 예상 복잡도 | 영향 파일 수 |
|------|------------|-------------|
| Domain SortBy Enum 생성 | 🟢 낮음 | 1 |
| Domain Criteria 확장 | 🟢 낮음 | 1 |
| Query DTO 리팩토링 | 🟢 낮음 | 1 |
| QueryFactory 생성 | 🟢 낮음 | 1 |
| Persistence 수정 | 🟡 중간 | 1 |

**총 작업량**: 🟢 낮음 (5개 파일 수정/생성)

---

## 7. 특이사항

### 7.1 기존 구조 유지

Member 모듈은 이미 Criteria 패턴과 ReadManager @Transactional을 준수하고 있어, 추가 기능(정렬/기간)만 확장하면 됩니다.

### 7.2 참조 모델로 활용

ReadManager의 `@Transactional(readOnly = true)` 사용 패턴은 다른 모듈의 **참조 모델**로 활용:

```java
@Transactional(readOnly = true)
public Member findById(String memberId) {
    MemberId id = MemberId.fromString(memberId);
    return memberQueryPort.findById(id)
        .orElseThrow(() -> new MemberNotFoundException(memberId));
}
```

### 7.3 Admin 조회 조건 확장

Member 모듈은 Admin에서 사용되므로 다음 조회 조건 추가 필요:

- **복합 필터**: 이름, 전화번호, 상태, 이메일
- **기간 조회**: 가입일 기준 (registeredStartDate ~ registeredEndDate)
- **정렬**: 가입일, 수정일, 이름, 마지막 로그인일
- **페이지네이션**: offset + limit

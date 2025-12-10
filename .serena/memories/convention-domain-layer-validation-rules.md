# Domain Layer Validation Rules (Index)

> Domain Layer 코드 검증을 위한 룰 정의 (Index 파일)
> 
> 📌 **적용 대상**: `domain/` 모듈의 모든 Java 코드
> 📌 **검증 방식**: ArchUnit 테스트 + 코드 리뷰 시 참조

---

## 📚 세분화된 룰 파일

| 파일명 | 카테고리 | 룰 개수 | 설명 |
|--------|----------|---------|------|
| `domain-rules-01-aggregate` | AGGREGATE | 22개 | Aggregate Root 관련 |
| `domain-rules-02-value-object` | VALUE_OBJECT | 34개 | VO, LockKey, CacheKey, SortKey |
| `domain-rules-03-exception` | EXCEPTION | 20개 | Exception, ErrorCode |
| `domain-rules-04-event` | DOMAIN_EVENT | 11개 | Domain Event |
| `domain-rules-05-criteria` | CRITERIA | 10개 | 복합 검색 조건 (신규) |
| `domain-rules-06-common` | COMMON | 10개 | 공통 금지 룰 |

---

## 검증 룰 전체 요약

| 카테고리 | 룰 개수 | ID Prefix |
|---------|--------|-----------|
| AGGREGATE | 22개 | AGG-001 ~ AGG-022 |
| VALUE_OBJECT | 9개 | VO-001 ~ VO-009 |
| LOCK_KEY | 10개 | LK-001 ~ LK-010 |
| CACHE_KEY | 10개 | CK-001 ~ CK-010 |
| SORT_KEY | 5개 | SK-001 ~ SK-005 |
| EXCEPTION | 20개 | EXC-001 ~ EXC-020 |
| DOMAIN_EVENT | 11개 | EVT-001 ~ EVT-011 |
| CRITERIA | 10개 | CRI-001 ~ CRI-010 |
| COMMON | 10개 | CMN-001 ~ CMN-010 |
| **총계** | **107개** | - |

---

## Severity 레벨

| Severity | 설명 | 조치 |
|----------|------|------|
| **ERROR** | Zero-Tolerance 위반 | 빌드 실패, 즉시 수정 필수 |
| **WARNING** | 권장 사항 위반 | 코드 리뷰에서 논의, 개선 권장 |
| **INFO** | 참고 사항 | 가이드라인 인지 목적 |

---

## ArchUnit 테스트 매핑

| 룰 카테고리 | ArchUnit 테스트 |
|------------|-----------------|
| AGGREGATE | `AggregateArchTest.java` |
| VALUE_OBJECT | `VOArchTest.java` |
| LOCK_KEY | `LockKeyArchTest.java` |
| CACHE_KEY | `CacheKeyArchTest.java` |
| EXCEPTION | `ExceptionArchTest.java` |
| DOMAIN_EVENT | `DomainEventArchTest.java` |
| CRITERIA | `CriteriaArchTest.java` |
| COMMON | `DomainPurityArchTest.java`, `PackageStructureArchTest.java` |

---

## 패키지 구조 (전체)

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

## Quick Reference

### 단건 조회 vs 복합 검색

```java
// 단건 조회 → 기존 VO 재사용
Optional<User> findByEmail(Email email);      // ✅
Optional<User> findById(UserId userId);       // ✅

// 복합 검색 → Criteria 사용
Page<Order> search(OrderSearchCriteria criteria);  // ✅
```

### 시간 처리

```java
// ❌ 금지
Instant.now();
LocalDateTime.now();

// ✅ 권장
public void place(Instant now) { this.placedAt = now; }
```

### FK 전략

```java
// BC 내부 → VO ID
private CustomerId customerId;  // ✅ 같은 BC

// BC 간 → Long (순환 의존 방지)
private Long externalUserId;    // ✅ 다른 BC
```

---

## 관련 문서

- [Domain Guide](docs/coding_convention/02-domain-layer/domain-guide.md)
- [Zero Tolerance Rules](memory: zero-tolerance-rules)
- 세분화된 룰 파일들 (위 목록 참조)

---

**버전**: 2.0.0 (세분화 + Criteria 추가)
**작성일**: 2025-12-08

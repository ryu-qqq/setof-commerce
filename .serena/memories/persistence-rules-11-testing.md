# Persistence MySQL Testing Validation Rules

> Persistence MySQL Layer 테스트 검증 규칙 인덱스
> 
> 총 **3개 카테고리**, **25개 규칙**

---

## 📋 카테고리 개요

| 카테고리 | 규칙 수 | 검증 도구 | 심각도 |
|---------|--------|----------|--------|
| Repository 통합 테스트 | 10 | Manual/ArchUnit | CRITICAL |
| Slice 테스트 | 7 | Manual | RECOMMENDED |
| Mapper 단위 테스트 | 8 | Manual | IMPORTANT |

---

## 🔗 상세 규칙 파일

- `mysql-test-rules-01-repository-integration.md` - Repository 통합 테스트 규칙
- `mysql-test-rules-02-slice.md` - @DataJpaTest Slice 테스트 규칙
- `mysql-test-rules-03-mapper-unit.md` - Mapper 단위 테스트 규칙

---

## 🎯 Zero-Tolerance 규칙 요약

### CRITICAL (빌드 실패)

```json
{
  "MYSQL_TEST_001": {
    "rule": "TestContainers MySQL 필수",
    "description": "통합 테스트는 반드시 TestContainers MySQL 사용",
    "violation": "H2 단독 통합 테스트"
  },
  "MYSQL_TEST_002": {
    "rule": "@Transactional 필수",
    "description": "테스트 격리를 위해 @Transactional 필수",
    "violation": "테스트 간 데이터 오염"
  },
  "MYSQL_TEST_003": {
    "rule": "@Sql INSERT만",
    "description": "@Sql에 DDL 금지, INSERT만 허용",
    "violation": "DDL 작성 (Flyway 역할 침범)"
  },
  "MYSQL_TEST_004": {
    "rule": "Lombok 금지",
    "description": "테스트 코드에서도 Lombok 금지",
    "violation": "Plain Java 원칙 위반"
  }
}
```

### IMPORTANT (경고)

```json
{
  "MYSQL_TEST_005": {
    "rule": "Given-When-Then 구조",
    "description": "모든 테스트 메서드는 Given-When-Then 구조 준수",
    "violation": "불명확한 테스트 구조"
  },
  "MYSQL_TEST_006": {
    "rule": "@DisplayName 필수",
    "description": "모든 테스트 메서드에 @DisplayName 작성",
    "violation": "테스트 의도 불명확"
  },
  "MYSQL_TEST_007": {
    "rule": "Mapper 테스트 필수",
    "description": "모든 EntityMapper에 대해 단위 테스트 작성",
    "violation": "Domain-Entity 변환 검증 누락"
  }
}
```

---

## 📂 테스트 지원 클래스

### 기반 클래스 상속 구조

```
RepositoryTestSupport (통합 테스트)
├── @SpringBootTest
├── TestContainers MySQL
├── EntityManager 주입
└── flushAndClear(), persistAndFlush() 유틸리티

JpaSliceTestSupport (@DataJpaTest)
├── @DataJpaTest
├── TestEntityManager 주입
├── QueryDslTestConfig Import
└── H2 또는 TestContainers MySQL

MapperTestSupport (단위 테스트)
├── Spring Context 불필요
├── 리플렉션 기반 필드 비교
└── 양방향 변환 검증
```

---

## ⚡ 빠른 참조

### 테스트 유형 선택

```
테스트 대상 선택:
    │
    ├─ Repository CRUD? ──────────── RepositoryTestSupport
    │
    ├─ QueryDSL 복잡 쿼리? ────────── RepositoryTestSupport
    │
    ├─ 빠른 JPA 검증? ─────────────── JpaSliceTestSupport
    │
    └─ Mapper 변환 로직? ──────────── MapperTestSupport (순수 JUnit)
```

### 필수 어노테이션

```java
// 통합 테스트
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Transactional

// Slice 테스트
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(QueryDslTestConfig.class)

// 단위 테스트
// (어노테이션 불필요, 순수 JUnit)
```

---

**문서 버전**: 1.0.0
**최종 수정일**: 2025-12-08

# 테스트 커버리지 개선 계획서

> **작성일**: 2024-12-10
> **목표**: 전 레이어 테스트 커버리지 80% 이상 달성

---

## 1. 현황 분석

### 1.1 테스트 커버리지 현황 (2024-12-10 기준)

| 모듈 | Instructions | Branches | 상태 | 우선순위 |
|------|-------------|----------|------|---------|
| **domain** | 95% | 88% | 🟢 우수 | - |
| **application** | 97% | 79% | 🟢 우수 | P3 (Branch 개선) |
| **persistence-mysql** | 3% | 13% | 🔴 매우 낮음 | **P1** |
| **persistence-redis** | 0% | 0% | 🔴 테스트 없음 | **P1** |
| **rest-api** | 3% | 0% | 🔴 매우 낮음 | **P2** |
| **rest-api-admin** | N/A | N/A | ⚪ 리포트 없음 | **P2** |

### 1.2 ArchUnit 테스트 현황

| 모듈 | 결과 | 실패 수 | 상태 |
|------|------|---------|------|
| domain | ✅ PASS | 0 | 완료 |
| application | ✅ PASS | 0 | 완료 |
| persistence-mysql | ✅ PASS | 0 | 완료 |
| persistence-redis | ❌ FAIL | 4 | 수정 필요 |
| rest-api | ✅ PASS | 0 | 완료 |
| rest-api-admin | ❌ FAIL | 21 | 수정 필요 |

---

## 2. 개선 계획

### 2.1 Phase 1: Persistence MySQL Layer (목표: 80%)

```
현재: 3% → 목표: 80%
예상 기간: 2주
```

#### 작업 항목

| 순서 | 컴포넌트 | 테스트 대상 | 가이드 문서 |
|------|---------|------------|------------|
| 1 | Entity | MemberJpaEntity, RefreshTokenJpaEntity | [entity-test-guide.md](../coding_convention/04-persistence-layer/mysql/entity/entity-test-guide.md) |
| 2 | Mapper | MemberJpaEntityMapper, RefreshTokenMapper | [mapper-test-guide.md](../coding_convention/04-persistence-layer/mysql/mapper/mapper-test-guide.md) |
| 3 | Repository | MemberQueryDslRepository | [querydsl-repository-test-guide.md](../coding_convention/04-persistence-layer/mysql/repository/querydsl/querydsl-repository-test-guide.md) |
| 4 | CommandAdapter | MemberCommandAdapter | [command-adapter-test-guide.md](../coding_convention/04-persistence-layer/mysql/adapter/command/command-adapter-test-guide.md) |
| 5 | QueryAdapter | MemberQueryAdapter | [query-adapter-test-guide.md](../coding_convention/04-persistence-layer/mysql/adapter/query/general/query-adapter-test-guide.md) |

#### 테스트 인프라 요구사항

```yaml
dependencies:
  - TestContainers (MySQL 8.0)
  - @DataJpaTest (Slice Test)
  - JUnit 5 (단위 테스트)

base_class:
  - MysqlTestContainerSupport (공통 설정)
```

#### 상세 가이드

📖 **필수 참고 문서**: [01_mysql-testing-guide.md](../coding_convention/04-persistence-layer/mysql/testing/01_mysql-testing-guide.md)

---

### 2.2 Phase 2: Persistence Redis Layer (목표: 80%)

```
현재: 0% → 목표: 80%
예상 기간: 1주
```

#### 작업 항목

| 순서 | 컴포넌트 | 테스트 대상 | 가이드 문서 |
|------|---------|------------|------------|
| 1 | CacheAdapter | RefreshTokenCacheAdapter | [cache-adapter-test-guide.md](../coding_convention/04-persistence-layer/redis/adapter/cache-adapter-test-guide.md) |
| 2 | LockAdapter | (해당 시 추가) | [lock-adapter-test-guide.md](../coding_convention/04-persistence-layer/redis/lock/lock-adapter-test-guide.md) |
| 3 | ArchUnit 수정 | CacheAdapterArchTest (4건) | - |

#### 테스트 인프라 요구사항

```yaml
dependencies:
  - TestContainers (Redis 7.x)
  - Embedded Redis (단위 테스트 옵션)
  - Mockito (Lettuce/Redisson Mocking)

base_class:
  - RedisTestContainerSupport (공통 설정)
```

#### 상세 가이드

📖 **필수 참고 문서**: [01_redis-testing-guide.md](../coding_convention/04-persistence-layer/redis/testing/01_redis-testing-guide.md)

---

### 2.3 Phase 3: REST API Layer (목표: 70%)

```
현재: 3% → 목표: 70%
예상 기간: 2주
```

#### 작업 항목

| 순서 | 컴포넌트 | 테스트 대상 | 가이드 문서 |
|------|---------|------------|------------|
| 1 | Controller | AuthController, MemberController | [controller-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/controller/controller-test-guide.md) |
| 2 | Command DTO | LoginApiRequest, RegisterMemberApiRequest | [command-dto-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/dto/command/command-dto-test-guide.md) |
| 3 | Response DTO | TokenApiResponse, MemberApiResponse | [response-dto-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/dto/response/response-dto-test-guide.md) |
| 4 | Mapper | AuthApiMapper, MemberApiMapper | [mapper-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/mapper/mapper-test-guide.md) |
| 5 | Error | GlobalExceptionHandler | [error-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/error/error-test-guide.md) |
| 6 | Security | SecurityConfig, JwtAuthenticationFilter | [security-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/security/security-test-guide.md) |

#### 테스트 인프라 요구사항

```yaml
dependencies:
  - TestRestTemplate (통합 테스트)
  - @SpringBootTest (RANDOM_PORT)
  - TestContainers (MySQL + Redis)
  - Spring Security Test

base_class:
  - RestApiIntegrationTestSupport (공통 설정)
```

#### 상세 가이드

📖 **필수 참고 문서**: [01_rest-api-testing-guide.md](../coding_convention/01-adapter-in-layer/rest-api/testing/01_rest-api-testing-guide.md)

---

### 2.4 Phase 4: REST API Admin Layer (목표: 70%)

```
현재: N/A → 목표: 70%
예상 기간: 2주
```

#### 작업 항목

| 순서 | 작업 | 설명 |
|------|------|------|
| 1 | ArchUnit 수정 | Legacy V1 패턴 제외 적용 (21건) |
| 2 | Controller 통합 테스트 | Admin Controller 테스트 작성 |
| 3 | DTO/Mapper 단위 테스트 | 필요 시 추가 |

#### 상세 가이드

📖 **참고 문서**: rest-api와 동일한 가이드 적용

---

### 2.5 Phase 5: Application Layer Branch 개선 (목표: 90%)

```
현재: 79% (Branch) → 목표: 90%
예상 기간: 1주
```

#### 작업 항목

| 컴포넌트 | 개선 내용 | 가이드 문서 |
|---------|----------|------------|
| Service | 조건문 분기 테스트 보강 | [command-service-test-guide.md](../coding_convention/03-application-layer/service/command/command-service-test-guide.md) |
| Facade | 예외 케이스 테스트 추가 | [facade-test-guide.md](../coding_convention/03-application-layer/facade/facade-test-guide.md) |
| Manager | 트랜잭션 경계 테스트 | [transaction-manager-test-guide.md](../coding_convention/03-application-layer/manager/transaction-manager-test-guide.md) |

#### 상세 가이드

📖 **필수 참고 문서**: [01_application-testing-guide.md](../coding_convention/03-application-layer/testing/01_application-testing-guide.md)

---

## 3. 테스트 인프라 공통 설정

### 3.1 TestContainers 설정

```java
// 공통 TestContainer 설정 예시
@Testcontainers
public abstract class IntegrationTestSupport {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);
}
```

### 3.2 테스트 프로파일 (application-test.yml)

```yaml
spring:
  datasource:
    url: jdbc:tc:mysql:8.0:///testdb
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver

  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
```

---

## 4. 실행 체크리스트

### Phase 1 체크리스트 (Persistence MySQL)

- [ ] TestContainers MySQL 설정 완료
- [ ] MysqlTestContainerSupport 베이스 클래스 생성
- [ ] MemberJpaEntity 단위 테스트 작성
- [ ] MemberJpaEntityMapper 단위 테스트 작성
- [ ] MemberQueryDslRepository 통합 테스트 작성
- [ ] MemberCommandAdapter 통합 테스트 작성
- [ ] MemberQueryAdapter 통합 테스트 작성
- [ ] RefreshToken 관련 테스트 작성
- [ ] 커버리지 80% 달성 확인

### Phase 2 체크리스트 (Persistence Redis)

- [ ] TestContainers Redis 설정 완료
- [ ] RedisTestContainerSupport 베이스 클래스 생성
- [ ] CacheAdapterArchTest 수정 (4건)
- [ ] RefreshTokenCacheAdapter 통합 테스트 작성
- [ ] 커버리지 80% 달성 확인

### Phase 3 체크리스트 (REST API)

- [ ] RestApiIntegrationTestSupport 베이스 클래스 생성
- [ ] AuthController 통합 테스트 작성
- [ ] MemberController 통합 테스트 작성
- [ ] DTO 단위 테스트 작성
- [ ] Mapper 단위 테스트 작성
- [ ] GlobalExceptionHandler 테스트 작성
- [ ] Security 통합 테스트 작성
- [ ] 커버리지 70% 달성 확인

### Phase 4 체크리스트 (REST API Admin)

- [ ] ArchUnit 테스트 수정 (21건)
- [ ] Admin Controller 통합 테스트 작성
- [ ] 커버리지 70% 달성 확인

### Phase 5 체크리스트 (Application Branch)

- [ ] 조건문 분기 테스트 보강
- [ ] 예외 케이스 테스트 추가
- [ ] Branch 커버리지 90% 달성 확인

---

## 5. 가이드 문서 링크 모음

### Domain Layer (현재 95%)
- [01_domain-testing-guide.md](../coding_convention/02-domain-layer/testing/01_domain-testing-guide.md)
- [aggregate-test-guide.md](../coding_convention/02-domain-layer/aggregate/aggregate-test-guide.md)
- [vo-test-guide.md](../coding_convention/02-domain-layer/vo/vo-test-guide.md)
- [exception-test-guide.md](../coding_convention/02-domain-layer/exception/exception-test-guide.md)

### Application Layer (현재 97%)
- [01_application-testing-guide.md](../coding_convention/03-application-layer/testing/01_application-testing-guide.md)
- [command-service-test-guide.md](../coding_convention/03-application-layer/service/command/command-service-test-guide.md)
- [query-service-test-guide.md](../coding_convention/03-application-layer/service/query/query-service-test-guide.md)
- [facade-test-guide.md](../coding_convention/03-application-layer/facade/facade-test-guide.md)
- [assembler-test-guide.md](../coding_convention/03-application-layer/assembler/assembler-test-guide.md)

### Persistence MySQL Layer (현재 3%)
- [01_mysql-testing-guide.md](../coding_convention/04-persistence-layer/mysql/testing/01_mysql-testing-guide.md)
- [entity-test-guide.md](../coding_convention/04-persistence-layer/mysql/entity/entity-test-guide.md)
- [mapper-test-guide.md](../coding_convention/04-persistence-layer/mysql/mapper/mapper-test-guide.md)
- [querydsl-repository-test-guide.md](../coding_convention/04-persistence-layer/mysql/repository/querydsl/querydsl-repository-test-guide.md)
- [command-adapter-test-guide.md](../coding_convention/04-persistence-layer/mysql/adapter/command/command-adapter-test-guide.md)
- [query-adapter-test-guide.md](../coding_convention/04-persistence-layer/mysql/adapter/query/general/query-adapter-test-guide.md)

### Persistence Redis Layer (현재 0%)
- [01_redis-testing-guide.md](../coding_convention/04-persistence-layer/redis/testing/01_redis-testing-guide.md)
- [cache-adapter-test-guide.md](../coding_convention/04-persistence-layer/redis/adapter/cache-adapter-test-guide.md)
- [lock-adapter-test-guide.md](../coding_convention/04-persistence-layer/redis/lock/lock-adapter-test-guide.md)

### REST API Layer (현재 3%)
- [01_rest-api-testing-guide.md](../coding_convention/01-adapter-in-layer/rest-api/testing/01_rest-api-testing-guide.md)
- [controller-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/controller/controller-test-guide.md)
- [command-dto-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/dto/command/command-dto-test-guide.md)
- [response-dto-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/dto/response/response-dto-test-guide.md)
- [mapper-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/mapper/mapper-test-guide.md)
- [error-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/error/error-test-guide.md)
- [security-test-guide.md](../coding_convention/01-adapter-in-layer/rest-api/security/security-test-guide.md)

### 공통 테스트 가이드
- [01_integration-testing-overview.md](../coding_convention/05-testing/integration-testing/01_integration-testing-overview.md)
- [01_test-fixtures-guide.md](../coding_convention/05-testing/test-fixtures/01_test-fixtures-guide.md)

---

## 6. 예상 일정

```
┌──────────────────────────────────────────────────────────────────┐
│  테스트 커버리지 개선 로드맵                                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Week 1-2: Phase 1 (Persistence MySQL)                           │
│  ├── Entity/Mapper 단위 테스트                                    │
│  └── Repository/Adapter 통합 테스트                               │
│                                                                   │
│  Week 3: Phase 2 (Persistence Redis)                             │
│  ├── ArchUnit 수정                                                │
│  └── CacheAdapter 통합 테스트                                     │
│                                                                   │
│  Week 4-5: Phase 3 (REST API)                                    │
│  ├── Controller 통합 테스트                                       │
│  └── DTO/Mapper/Security 테스트                                   │
│                                                                   │
│  Week 6-7: Phase 4 (REST API Admin)                              │
│  ├── ArchUnit 수정                                                │
│  └── Admin Controller 통합 테스트                                 │
│                                                                   │
│  Week 8: Phase 5 (Application Branch 개선)                       │
│  └── 조건문 분기 테스트 보강                                       │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

## 7. 성공 기준

| 레이어 | 현재 | 목표 (Instructions) | 목표 (Branches) |
|--------|------|---------------------|-----------------|
| Domain | 95% | 95% (유지) | 90% |
| Application | 97% | 97% (유지) | 90% |
| Persistence MySQL | 3% | **80%** | 70% |
| Persistence Redis | 0% | **80%** | 70% |
| REST API | 3% | **70%** | 60% |
| REST API Admin | N/A | **70%** | 60% |

**전체 평균 목표**: Instructions 80% / Branches 70%

---

## 8. 작업 시작 방법

각 Phase 작업 시작 시 다음 명령을 실행하세요:

```bash
# Phase 1: Persistence MySQL 테스트 작성
"Persistence MySQL 테스트 작성 시작해줘"
# → 자동으로 01_mysql-testing-guide.md 로드

# Phase 2: Persistence Redis 테스트 작성
"Persistence Redis 테스트 작성 시작해줘"
# → 자동으로 01_redis-testing-guide.md 로드

# Phase 3: REST API 테스트 작성
"REST API 테스트 작성 시작해줘"
# → 자동으로 01_rest-api-testing-guide.md 로드
```

각 가이드 문서에는 상세한 테스트 패턴, 예제 코드, Zero-Tolerance 규칙이 포함되어 있습니다.

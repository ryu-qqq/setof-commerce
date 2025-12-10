# 테스트 커버리지 개선 계획

> **작성일**: 2024-12-10
> **문서 위치**: docs/testing/test-improvement-plan.md

## 현황 요약

| 모듈 | Instructions | Branches | 우선순위 |
|------|-------------|----------|---------|
| domain | 95% | 88% | - (유지) |
| application | 97% | 79% | P3 |
| persistence-mysql | 3% | 13% | **P1** |
| persistence-redis | 0% | 0% | **P1** |
| rest-api | 3% | 0% | **P2** |
| rest-api-admin | N/A | N/A | **P2** |

## Phase별 작업 계획

### Phase 1: Persistence MySQL (P1)
- 목표: 3% → 80%
- 가이드: docs/coding_convention/04-persistence-layer/mysql/testing/01_mysql-testing-guide.md
- 작업: Entity, Mapper, Repository, Adapter 테스트

### Phase 2: Persistence Redis (P1)
- 목표: 0% → 80%
- 가이드: docs/coding_convention/04-persistence-layer/redis/testing/01_redis-testing-guide.md
- 작업: CacheAdapter 테스트, ArchUnit 수정 (4건)

### Phase 3: REST API (P2)
- 목표: 3% → 70%
- 가이드: docs/coding_convention/01-adapter-in-layer/rest-api/testing/01_rest-api-testing-guide.md
- 작업: Controller, DTO, Mapper, Error, Security 테스트

### Phase 4: REST API Admin (P2)
- 목표: N/A → 70%
- 작업: ArchUnit 수정 (21건), Controller 통합 테스트

### Phase 5: Application Branch 개선 (P3)
- 목표: 79% → 90% (Branch)
- 가이드: docs/coding_convention/03-application-layer/testing/01_application-testing-guide.md

## 작업 시작 명령

```
"Persistence MySQL 테스트 작성 시작해줘"
"Persistence Redis 테스트 작성 시작해줘"
"REST API 테스트 작성 시작해줘"
```

## 레이어별 테스트 가이드 경로

- Domain: docs/coding_convention/02-domain-layer/testing/01_domain-testing-guide.md
- Application: docs/coding_convention/03-application-layer/testing/01_application-testing-guide.md
- MySQL: docs/coding_convention/04-persistence-layer/mysql/testing/01_mysql-testing-guide.md
- Redis: docs/coding_convention/04-persistence-layer/redis/testing/01_redis-testing-guide.md
- REST API: docs/coding_convention/01-adapter-in-layer/rest-api/testing/01_rest-api-testing-guide.md

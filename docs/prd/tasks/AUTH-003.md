# AUTH-003: Persistence Layer 구현

**Epic**: B2B 인증/인가 시스템 (Customer Authentication)
**Layer**: Persistence Layer (Adapter-Out)
**브랜치**: feature/AUTH-003-persistence
**의존성**: AUTH-002 (Application Layer) 완료 필수
**Jira URL**: https://ryuqqq.atlassian.net/browse/SC-4

---

## 📝 목적

회원 데이터 저장을 위한 JPA Entity, Repository, Adapter를 구현합니다.
MySQL과 Redis를 사용한 영속성 계층을 구현합니다.

---

## 🎯 요구사항

### MySQL - JPA Entity

- [ ] **MemberJpaEntity** (SoftDeletableEntity 상속)
  - `id` (Long, PK, Auto Increment)
  - `phoneNumber` (String, 11자, unique, not null)
  - `email` (String, 100자, nullable)
  - `passwordHash` (String, 60자, nullable)
  - `name` (String, 50자, not null)
  - `dateOfBirth` (LocalDate, nullable)
  - `gender` (Gender Enum, nullable)
  - `provider` (AuthProvider Enum, not null)
  - `socialId` (String, 100자, nullable)
  - `status` (MemberStatus Enum, not null)
  - `privacyConsent` (boolean, not null)
  - `serviceTermsConsent` (boolean, not null)
  - `adConsent` (boolean, not null)
  - `withdrawalReason` (WithdrawalReason Enum, nullable)
  - `withdrawnAt` (LocalDateTime, nullable)

- [ ] **Static Factory Method**: `of()` - Domain → Entity 변환
- [ ] **Protected 기본 생성자**: JPA용
- [ ] **Private 생성자**: 외부 직접 생성 금지
- [ ] **Getter 명시적 작성**: Lombok 금지

- [ ] **SoftDeletableEntity** (공통 베이스)
  - `deleted` (boolean)
  - `deletedAt` (LocalDateTime)
  - BaseAuditEntity 상속 (createdAt, updatedAt)

### MySQL - Repository

- [ ] **MemberJpaRepository** (JpaRepository 상속)
  - `findByPhoneNumber(String phoneNumber)` - Optional<MemberJpaEntity>
  - `findBySocialId(String socialId)` - Optional<MemberJpaEntity>
  - `existsByPhoneNumber(String phoneNumber)` - boolean

### MySQL - Mapper

- [ ] **MemberPersistenceMapper**
  - `toEntity(Member domain)` - Domain → Entity
  - `toDomain(MemberJpaEntity entity)` - Entity → Domain

### MySQL - Adapter

- [ ] **MemberCommandAdapter** (MemberCommandPort 구현)
  - `save(Member member)` - 회원 저장
  - `delete(MemberId memberId)` - 회원 삭제 (Soft Delete)

- [ ] **MemberQueryAdapter** (MemberQueryPort 구현)
  - `findById(MemberId memberId)` - 회원 조회
  - `findByPhoneNumber(PhoneNumber phoneNumber)` - 핸드폰 번호로 조회
  - `findBySocialId(SocialId socialId)` - 소셜 ID로 조회
  - `existsByPhoneNumber(PhoneNumber phoneNumber)` - 중복 확인

### MySQL - 인덱스 전략

```sql
-- 핸드폰 번호 조회 (로그인)
CREATE UNIQUE INDEX idx_members_phone_number ON members(phone_number);

-- 카카오 ID 조회
CREATE INDEX idx_members_social_id ON members(social_id) WHERE social_id IS NOT NULL;

-- 상태별 조회
CREATE INDEX idx_members_status ON members(status);
```

### MySQL - Flyway Migration

- [ ] `V1__create_members_table.sql` - members 테이블 생성
- [ ] `V2__create_indexes.sql` - 인덱스 생성

### Redis - Entity

- [ ] **RefreshTokenRedis**
  - `@RedisHash(value = "refresh_token", timeToLive = 604800)` - 7일
  - `@Id memberId` (String)
  - `token` (String) - Refresh Token 값
  - `userGrade` (String) - 회원 등급

### Redis - Repository

- [ ] **RefreshTokenRedisRepository** (CrudRepository 상속)
  - `findByMemberId(String memberId)`
  - `deleteByMemberId(String memberId)`

### Redis - Adapter

- [ ] **RefreshTokenCommandAdapter** (RefreshTokenCommandPort 구현)
  - `save(Long memberId, String token, String userGrade)`
  - `delete(Long memberId)`

- [ ] **RefreshTokenQueryAdapter** (RefreshTokenQueryPort 구현)
  - `findByMemberId(Long memberId)`
  - `existsByMemberId(Long memberId)`

---

## ⚠️ 제약사항

### Zero-Tolerance 규칙 (Persistence Layer)
- [ ] **Long FK 전략** - JPA 관계 어노테이션(@ManyToOne, @OneToMany) 금지
- [ ] **Lombok 금지** - Getter 명시적 작성
- [ ] **QueryDSL DTO Projection** - 복잡한 조회 시 필수

### Entity 규칙
- [ ] `of()` Static Factory Method 필수
- [ ] Protected 기본 생성자 (JPA용)
- [ ] Private 생성자 (외부 직접 생성 금지)
- [ ] SoftDeletableEntity/BaseAuditEntity 상속

### 테스트 규칙
- [ ] Repository 테스트 (TestContainers MySQL)
- [ ] Redis 테스트 (Embedded Redis)
- [ ] ArchUnit 테스트 필수
- [ ] Flyway Migration 테스트

---

## ✅ 완료 조건

- [ ] MemberJpaEntity 구현 완료
- [ ] SoftDeletableEntity 구현 완료
- [ ] MemberJpaRepository 구현 완료
- [ ] MemberPersistenceMapper 구현 완료
- [ ] MemberCommandAdapter 구현 완료
- [ ] MemberQueryAdapter 구현 완료
- [ ] RefreshTokenRedis 구현 완료
- [ ] RefreshTokenRedisRepository 구현 완료
- [ ] RefreshTokenCommandAdapter 구현 완료
- [ ] RefreshTokenQueryAdapter 구현 완료
- [ ] Flyway Migration 스크립트 완료
- [ ] Integration Test 100% 통과
- [ ] ArchUnit Test 통과
- [ ] Long FK 전략 준수 확인
- [ ] 코드 리뷰 승인
- [ ] PR 머지 완료

---

## 🔗 관련 문서

- PRD: docs/prd/b2b-auth-hub.md
- Plan: docs/prd/plans/AUTH-003-persistence-plan.md (create-plan 후 생성)
- Coding Convention: docs/coding_convention/04-persistence-layer/mysql/
- Redis Convention: docs/coding_convention/04-persistence-layer/redis/
- Jira: (sync-to-jira 후 추가)

# AUTH-002: Application Layer 구현

**Epic**: B2B 인증/인가 시스템 (Customer Authentication)
**Layer**: Application Layer
**브랜치**: feature/AUTH-002-application
**의존성**: AUTH-001 (Domain Layer) 완료 필수
**Jira URL**: https://ryuqqq.atlassian.net/browse/SC-3

---

## 📝 목적

회원 인증/인가 관련 Use Case, Port, DTO, Assembler를 구현합니다.
비즈니스 로직 오케스트레이션과 트랜잭션 경계를 관리합니다.


여기서 카카오 관련된 기능은 아직 개발하지마세요

---

## 🎯 요구사항

### Command Use Cases

- [ ] **RegisterMemberUseCase** (회원가입)
  - Input: `RegisterMemberCommand`
  - Output: `RegisterMemberResult`
  - Transaction: Yes
  - 비즈니스 로직:
    1. 핸드폰 번호 중복 확인
    2. 비밀번호 BCrypt 해시화
    3. Member 생성 (provider: LOCAL)
    4. **트랜잭션 커밋**
    5. JWT 토큰 발급 (트랜잭션 외부!)

- [ ] **LoginMemberUseCase** (로그인)
  - Input: `LoginMemberCommand`
  - Output: `LoginMemberResult`
  - Transaction: ReadOnly
  - 비즈니스 로직:
    1. 핸드폰 번호로 회원 조회
    2. 카카오 회원 확인 → 차단 예외
    3. 비밀번호 검증
    4. JWT 토큰 발급
    5. Refresh Token 저장 (Redis + DB)

- [ ] **KakaoOAuthLoginUseCase** (카카오 로그인)
  - Input: `KakaoOAuthCommand`
  - Output: `KakaoOAuthResult`
  - Transaction: Yes
  - 비즈니스 로직:
    1. 카카오 ID로 회원 조회
    2. 신규 회원 → 자동 회원가입
    3. 기존 LOCAL 회원 → 통합 유도 플래그
    4. JWT 토큰 발급

- [ ] **IntegrateKakaoUseCase** (카카오 계정 통합)
  - Input: `IntegrateKakaoCommand`
  - Transaction: Yes
  - 비즈니스 로직:
    1. 회원 조회
    2. provider: LOCAL → KAKAO 변경
    3. socialId 저장

- [ ] **LogoutMemberUseCase** (로그아웃)
  - Input: `LogoutMemberCommand`
  - Transaction: Yes
  - 비즈니스 로직:
    1. Redis에서 Refresh Token 삭제
    2. DB에서 Refresh Token 삭제

- [ ] **RefreshTokenUseCase** (토큰 갱신)
  - Input: `RefreshTokenCommand`
  - Output: `RefreshTokenResult`
  - Transaction: Yes
  - 비즈니스 로직:
    1. Refresh Token 검증 (Redis)
    2. 새 Access/Refresh Token 발급
    3. 기존 Refresh Token 무효화

- [ ] **ResetPasswordUseCase** (비밀번호 재설정)
  - Input: `ResetPasswordCommand`
  - Transaction: Yes
  - 비즈니스 로직:
    1. 핸드폰 번호로 회원 조회
    2. 카카오 회원 → 비밀번호 변경 불가 예외
    3. 비밀번호 해시화 후 저장

- [ ] **WithdrawMemberUseCase** (회원 탈퇴)
  - Input: `WithdrawMemberCommand`
  - Transaction: Yes
  - 비즈니스 로직:
    1. 회원 조회
    2. 상태 → WITHDRAWN
    3. 탈퇴 사유 저장
    4. Refresh Token 무효화

### Query Use Cases

- [ ] **GetCurrentMemberUseCase** (내 정보 조회)
  - Input: `GetCurrentMemberQuery`
  - Output: `MemberDetailResult`
  - Transaction: ReadOnly

### Command DTOs (Application Layer)

- [ ] `RegisterMemberCommand` - 회원가입 입력
- [ ] `LoginMemberCommand` - 로그인 입력
- [ ] `KakaoOAuthCommand` - 카카오 로그인 입력
- [ ] `IntegrateKakaoCommand` - 카카오 통합 입력
- [ ] `LogoutMemberCommand` - 로그아웃 입력
- [ ] `RefreshTokenCommand` - 토큰 갱신 입력
- [ ] `ResetPasswordCommand` - 비밀번호 재설정 입력
- [ ] `WithdrawMemberCommand` - 회원 탈퇴 입력

### Query DTOs (Application Layer)

- [ ] `GetCurrentMemberQuery` - 내 정보 조회 입력

### Response DTOs (Application Layer)

- [ ] `RegisterMemberResult` - 회원가입 결과 (memberId, tokens)
- [ ] `LoginMemberResult` - 로그인 결과 (memberId, tokens)
- [ ] `KakaoOAuthResult` - 카카오 로그인 결과 (isNewMember, needsIntegration)
- [ ] `RefreshTokenResult` - 토큰 갱신 결과 (tokens)
- [ ] `MemberDetailResult` - 회원 상세 정보
- [ ] `TokenPair` - Access + Refresh Token 쌍

### Port 정의

**Port-In (Command)**:
- [ ] `RegisterMemberPort` - 회원가입
- [ ] `LoginMemberPort` - 로그인
- [ ] `KakaoOAuthLoginPort` - 카카오 로그인
- [ ] `IntegrateKakaoPort` - 카카오 통합
- [ ] `LogoutMemberPort` - 로그아웃
- [ ] `RefreshTokenPort` - 토큰 갱신
- [ ] `ResetPasswordPort` - 비밀번호 재설정
- [ ] `WithdrawMemberPort` - 회원 탈퇴

**Port-In (Query)**:
- [ ] `GetCurrentMemberPort` - 내 정보 조회

**Port-Out (Command)**:
- [ ] `MemberCommandPort` - save(), delete()
- [ ] `RefreshTokenCommandPort` - save(), delete()

**Port-Out (Query)**:
- [ ] `MemberQueryPort` - findById(), findByPhoneNumber(), findBySocialId(), existsByPhoneNumber()
- [ ] `RefreshTokenQueryPort` - findByMemberId()

### Assembler

- [ ] `MemberAssembler` - Domain ↔ Application DTO 변환

### Application Exceptions

- [ ] `MemberNotFoundException` - 회원 없음
- [ ] `DuplicatePhoneNumberException` - 핸드폰 번호 중복
- [ ] `InvalidPasswordException` - 비밀번호 불일치 (Application)
- [ ] `KakaoMemberLocalLoginException` - 카카오 회원 LOCAL 로그인 시도
- [ ] `InvalidTokenException` - 토큰 검증 실패
- [ ] `ExpiredTokenException` - 토큰 만료
- [ ] `WithdrawnMemberException` - 탈퇴한 회원
- [ ] `InactiveMemberException` - 휴면 회원

---

## ⚠️ 제약사항

### Zero-Tolerance 규칙 (Application Layer)
- [ ] **Transaction 경계 준수** - @Transactional 내 외부 API 호출 금지!
  - JWT 발급은 트랜잭션 외부에서 수행
  - Redis 저장은 트랜잭션 외부에서 수행
- [ ] **CQRS 분리** - Command/Query UseCase 분리
- [ ] **Assembler 필수** - Domain ↔ DTO 변환은 반드시 Assembler 사용

### 테스트 규칙
- [ ] UseCase Unit Test (Mock Port)
- [ ] ArchUnit 테스트 필수
- [ ] TestFixture 사용 필수
- [ ] 테스트 커버리지 > 80%

---

## ✅ 완료 조건

- [ ] 모든 Command UseCase 구현 완료
- [ ] 모든 Query UseCase 구현 완료
- [ ] 모든 Port 정의 완료
- [ ] 모든 DTO 구현 완료
- [ ] Assembler 구현 완료
- [ ] Unit Test 100% 통과
- [ ] ArchUnit Test 통과
- [ ] Transaction 경계 규칙 준수 확인
- [ ] 코드 리뷰 승인
- [ ] PR 머지 완료

---

## 🔗 관련 문서

- PRD: docs/prd/b2b-auth-hub.md
- Plan: docs/prd/plans/AUTH-002-application-plan.md (create-plan 후 생성)
- Coding Convention: docs/coding_convention/03-application-layer/
- Jira: (sync-to-jira 후 추가)

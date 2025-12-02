# AUTH-001: Domain Layer 구현

**Epic**: B2B 인증/인가 시스템 (Customer Authentication)
**Layer**: Domain Layer
**브랜치**: feature/AUTH-001-domain
**Jira URL**: https://ryuqqq.atlassian.net/browse/SC-2
**상태**: In Progress
**시작일**: 2025-12-02
**담당자**: 류상

---

## 📝 목적

회원(Member) Aggregate와 관련 Value Objects, Enum, Domain Exception을 구현합니다.
헥사고날 아키텍처의 가장 안쪽 레이어로, 순수 비즈니스 로직만 포함합니다.

---

## 🎯 요구사항

### Aggregate Root: Member

- [ ] Member Aggregate 설계 및 구현
  - `MemberId id` (Long, PK)
  - `PhoneNumber phoneNumber` (핸드폰 번호 VO)
  - `Email email` (이메일 VO, nullable)
  - `Password password` (BCrypt 해시 VO)
  - `MemberName name` (이름 VO)
  - `LocalDate dateOfBirth` (생년월일)
  - `Gender gender` (성별 Enum)
  - `AuthProvider provider` (LOCAL, KAKAO)
  - `SocialId socialId` (카카오 고유 ID VO, nullable)
  - `MemberStatus status` (ACTIVE, INACTIVE, SUSPENDED, WITHDRAWN)
  - `Consent consent` (동의 정보 VO)
  - `WithdrawalInfo withdrawalInfo` (탈퇴 정보 VO, nullable)
  - `LocalDateTime createdAt`, `LocalDateTime updatedAt`

- [ ] Static Factory Methods (컨벤션 준수)
  - `forNew()` - 신규 회원 생성 (ID 없음)
  - `of()` - 기존 회원 조회용
  - `reconstitute()` - Persistence에서 복원

- [ ] Law of Demeter Helper Methods
  - `getIdValue()` - `id.value()` 대신 사용
  - `getPhoneNumberValue()` - `phoneNumber.value()` 대신 사용
  - `getSocialIdValue()` - `socialId.value()` 대신 사용

- [ ] 비즈니스 메서드 (Tell, Don't Ask)
  - `withdraw(WithdrawalReason reason, Clock clock)` - 회원 탈퇴
  - `linkKakao(SocialId kakaoSocialId, Clock clock)` - 카카오 연동
  - `changePassword(Password newPassword, Clock clock)` - 비밀번호 변경
  - `isLocalMember()` - LOCAL 회원 여부
  - `isKakaoMember()` - KAKAO 회원 여부

### Value Objects (Java 21 Record)

- [ ] `MemberId` - Long > 0 검증
- [ ] `PhoneNumber` - `010[0-9]{8}` 정규식 검증
- [ ] `Email` - RFC 5322 형식 검증
- [ ] `Password` - BCrypt 해시값 저장, 정책 검증 메서드
- [ ] `MemberName` - 2~5자 길이 검증
- [ ] `SocialId` - NotBlank 검증
- [ ] `Consent` - 개인정보/서비스 동의 필수 검증
- [ ] `WithdrawalInfo` - reason + withdrawnAt 필수 검증

### Enum 정의

- [ ] `AuthProvider` - LOCAL, KAKAO
- [ ] `MemberStatus` - ACTIVE, INACTIVE, SUSPENDED, WITHDRAWN
- [ ] `Gender` - M, W, N
- [ ] `WithdrawalReason` - RARELY_USED, SERVICE_DISSATISFIED, PRIVACY_CONCERN, OTHER

### Domain Exceptions

- [ ] `InvalidMemberIdException` - 잘못된 회원 ID
- [ ] `InvalidPhoneNumberException` - 잘못된 핸드폰 번호 형식
- [ ] `InvalidEmailException` - 잘못된 이메일 형식
- [ ] `InvalidPasswordException` - 잘못된 비밀번호
- [ ] `PasswordPolicyViolationException` - 비밀번호 정책 위반
- [ ] `InvalidMemberNameException` - 잘못된 이름
- [ ] `InvalidSocialIdException` - 잘못된 소셜 ID
- [ ] `RequiredConsentMissingException` - 필수 동의 누락
- [ ] `InvalidWithdrawalInfoException` - 잘못된 탈퇴 정보
- [ ] `AlreadyWithdrawnMemberException` - 이미 탈퇴한 회원
- [ ] `AlreadyKakaoMemberException` - 이미 카카오 연동된 회원
- [ ] `KakaoMemberCannotChangePasswordException` - 카카오 회원 비밀번호 변경 불가

### 비즈니스 규칙

- [ ] 회원가입: 핸드폰 번호 중복 불가
- [ ] 비밀번호 정책: 8자 이상, 영문+숫자+특수문자 필수
- [ ] 로그인 정책:
  - LOCAL → LOCAL ✅
  - KAKAO → KAKAO ✅
  - LOCAL → KAKAO (통합) ✅
  - KAKAO → LOCAL ❌ (차단)
- [ ] 카카오 계정 통합: LOCAL → KAKAO 변환 시 socialId 저장
- [ ] 회원 탈퇴: Soft Delete (WITHDRAWN 상태)

---

## ⚠️ 제약사항

### Zero-Tolerance 규칙 (Domain Layer)
- [ ] **Lombok 절대 금지** - Pure Java/Record 사용
- [ ] **Law of Demeter 준수** - Getter 체이닝 금지
- [ ] **Tell, Don't Ask 원칙** - 상태 질의 후 행동 금지
- [ ] **Private 생성자 + Static Factory** - 외부 직접 생성 금지

### 테스트 규칙
- [ ] ArchUnit 테스트 필수 (Domain Layer 규칙)
- [ ] TestFixture 사용 필수 (MemberFixture)
- [ ] 단위 테스트 커버리지 > 80%
- [ ] Value Object 경계값 테스트 필수

---

## ✅ 완료 조건

- [ ] Member Aggregate 구현 완료
- [ ] 모든 Value Objects 구현 완료
- [ ] 모든 Enum 구현 완료
- [ ] 모든 Domain Exception 구현 완료
- [ ] 모든 비즈니스 규칙 구현 완료
- [ ] Unit Test 100% 통과
- [ ] ArchUnit Test 통과
- [ ] Zero-Tolerance 규칙 준수 확인
- [ ] 코드 리뷰 승인
- [ ] PR 머지 완료

---

## 🔗 관련 문서

- PRD: docs/prd/b2b-auth-hub.md
- Plan: docs/prd/plans/AUTH-001-domain-plan.md (create-plan 후 생성)
- Coding Convention: docs/coding_convention/02-domain-layer/
- Jira: (sync-to-jira 후 추가)

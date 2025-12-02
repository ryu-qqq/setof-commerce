# PRD: B2B 인증/인가 시스템 (Customer Authentication)

**작성일**: 2025-12-02
**상태**: Draft
**Bounded Context**: Authentication & Authorization

---

## 📋 프로젝트 개요

### 비즈니스 목적
레거시 `legacy-admin`, `legacy-web` 서버의 인증/인가 시스템을 헥사고날 아키텍처 기반으로 통합 리팩토링.
- **Admin 요청**: API Gateway → B2B AuthHub Server에서 인증 완료 → 커스텀 헤더로 사용자 정보 전달 (인증 스킵)
- **Customer 요청**: 자체 인증/인가 처리 (JWT + 카카오 OAuth2)

### 아키텍처 개요

```
┌─────────────────────────────────────────────────────────────────┐
│                         API Gateway                              │
├─────────────────────────────────────────────────────────────────┤
│  Admin 요청                       │   Customer 요청               │
│       ↓                           │        ↓                     │
│  B2B AuthHub Server               │   (직접 처리)                 │
│  (외부 인증/인가)                  │                              │
│       ↓                           │        ↓                     │
│  Headers:                         │   인증 필요                   │
│  - Authorization: Bearer <jwt>    │                              │
│  - X-User-Id: 12345               │                              │
│  - X-Tenant-Id: tenant-1          │                              │
│  - X-Roles: ["ADMIN","USER"]      │                              │
└─────────────────────────────────────────────────────────────────┘
                               ↓
┌─────────────────────────────────────────────────────────────────┐
│                      setof-commerce                             │
├───────────────────────┬─────────────────────────────────────────┤
│  admin-web-api        │  web-api                                │
│  (Bootstrap Module)   │  (Bootstrap Module)                     │
├───────────────────────┼─────────────────────────────────────────┤
│  admin-rest-api       │  rest-api                               │
│  (Adapter-In)         │  (Adapter-In)                           │
│  → 인증 스킵           │  → 자체 인증/인가                        │
│  → 헤더에서 사용자 추출 │  → JWT 발급/갱신                         │
│  → X-User-Id 등       │  → 카카오 OAuth2                         │
└───────────────────────┴─────────────────────────────────────────┘
```

### 주요 사용자
- **일반 고객 (Customer)**: 커머스 서비스 이용자
- **관리자 (Admin)**: 백오피스 운영자 (B2B AuthHub 인증 거침)

### 성공 기준
- JWT 토큰 발급/검증 응답 시간 < 100ms (P95)
- 카카오 OAuth2 로그인 성공률 > 99%
- 마이그레이션 후 기존 회원 로그인 정상 동작

---

## 🏗️ Layer별 요구사항

### 1. Domain Layer

#### Aggregate: Member (회원)

**레거시 테이블 분석 (USERS)**:
```
- USER_ID: Long (PK, Auto Increment)
- SOCIAL_PK_ID: String (카카오 고유 ID)
- USER_GRADE_ID: Long (FK)
- PHONE_NUMBER: String
- SOCIAL_LOGIN_TYPE: Enum (kakao, naver, none)
- EMAIL: String
- PASSWORD_HASH: String (BCrypt)
- NAME: String
- DATE_OF_BIRTH: LocalDate
- GENDER: Enum (M, W, N)
- PRIVACY_CONSENT: Enum (Y, N)
- SERVICE_TERMS_CONSENT: Enum (Y, N)
- AD_CONSENT: Enum (Y, N)
- WITHDRAWAL_YN: Enum (Y, N)
- WITHDRAWAL_REASON: Enum
```

**새 설계: Member Aggregate**

```java
// Domain Entity (컨벤션 준수: private 생성자 + static factory methods)
public class Member {
    private final MemberId id;
    private final PhoneNumber phoneNumber;
    private Email email;
    private Password password;  // BCrypt 해시
    private MemberName name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private AuthProvider provider;  // LOCAL, KAKAO
    private SocialId socialId;      // 카카오 고유 ID VO (provider가 KAKAO일 때)
    private MemberStatus status;    // ACTIVE, INACTIVE, SUSPENDED, WITHDRAWN
    private Consent consent;        // 동의 정보 VO
    private WithdrawalInfo withdrawalInfo;  // 탈퇴 정보 (nullable)
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== Static Factory Methods (컨벤션 준수) =====

    /**
     * 신규 회원 생성 (ID 없음 → Persistence에서 할당)
     */
    public static Member forNew(
        PhoneNumber phoneNumber,
        Password password,
        MemberName name,
        Consent consent,
        Clock clock
    ) {
        return new Member(
            null, phoneNumber, null, password, name, null, Gender.N,
            AuthProvider.LOCAL, null, MemberStatus.ACTIVE, consent, null,
            LocalDateTime.now(clock), LocalDateTime.now(clock)
        );
    }

    /**
     * 기존 회원 조회 (ID 있음)
     */
    public static Member of(MemberId id) {
        // 기본 조회용 (다른 필드는 추후 설정)
        return new Member(id, null, null, null, null, null, null,
            null, null, null, null, null, null, null);
    }

    /**
     * Persistence에서 복원 (모든 필드 포함)
     */
    public static Member reconstitute(
        MemberId id,
        PhoneNumber phoneNumber,
        Email email,
        Password password,
        MemberName name,
        LocalDate dateOfBirth,
        Gender gender,
        AuthProvider provider,
        SocialId socialId,
        MemberStatus status,
        Consent consent,
        WithdrawalInfo withdrawalInfo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        return new Member(id, phoneNumber, email, password, name, dateOfBirth,
            gender, provider, socialId, status, consent, withdrawalInfo,
            createdAt, updatedAt);
    }

    // Private Constructor (외부 직접 생성 금지)
    private Member(
        MemberId id,
        PhoneNumber phoneNumber,
        Email email,
        Password password,
        MemberName name,
        LocalDate dateOfBirth,
        Gender gender,
        AuthProvider provider,
        SocialId socialId,
        MemberStatus status,
        Consent consent,
        WithdrawalInfo withdrawalInfo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.provider = provider;
        this.socialId = socialId;
        this.status = status;
        this.consent = consent;
        this.withdrawalInfo = withdrawalInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ===== Law of Demeter Helper Methods =====

    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    public String getPhoneNumberValue() {
        return phoneNumber != null ? phoneNumber.value() : null;
    }

    public String getSocialIdValue() {
        return socialId != null ? socialId.value() : null;
    }

    // ===== 비즈니스 메서드 (Tell, Don't Ask) =====

    public void withdraw(WithdrawalReason reason, Clock clock) {
        validateCanWithdraw();
        this.status = MemberStatus.WITHDRAWN;
        this.withdrawalInfo = WithdrawalInfo.of(reason, LocalDateTime.now(clock));
        this.updatedAt = LocalDateTime.now(clock);
    }

    public void linkKakao(SocialId kakaoSocialId, Clock clock) {
        validateCanLinkKakao();
        this.provider = AuthProvider.KAKAO;
        this.socialId = kakaoSocialId;
        this.updatedAt = LocalDateTime.now(clock);
    }

    public void changePassword(Password newPassword, Clock clock) {
        validateCanChangePassword();
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now(clock);
    }

    public boolean isLocalMember() {
        return this.provider == AuthProvider.LOCAL;
    }

    public boolean isKakaoMember() {
        return this.provider == AuthProvider.KAKAO;
    }

    // Private validation methods
    private void validateCanWithdraw() {
        if (this.status == MemberStatus.WITHDRAWN) {
            throw new AlreadyWithdrawnMemberException(this.id);
        }
    }

    private void validateCanLinkKakao() {
        if (this.provider == AuthProvider.KAKAO) {
            throw new AlreadyKakaoMemberException(this.id);
        }
    }

    private void validateCanChangePassword() {
        if (this.provider == AuthProvider.KAKAO) {
            throw new KakaoMemberCannotChangePasswordException(this.id);
        }
    }

    // Getter는 명시적으로 작성 (Lombok 금지)
    public MemberId getId() { return id; }
    public PhoneNumber getPhoneNumber() { return phoneNumber; }
    public Email getEmail() { return email; }
    public Password getPassword() { return password; }
    public MemberName getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public AuthProvider getProvider() { return provider; }
    public SocialId getSocialId() { return socialId; }
    public MemberStatus getStatus() { return status; }
    public Consent getConsent() { return consent; }
    public WithdrawalInfo getWithdrawalInfo() { return withdrawalInfo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
```

**Value Objects** (Java 21 Record 사용, Compact Constructor 검증):

| VO | 검증 규칙 | 설명 |
|-----|---------|------|
| `MemberId` | Long > 0 | 회원 고유 식별자 |
| `PhoneNumber` | `010[0-9]{8}` | 핸드폰 번호 (11자리) |
| `Email` | RFC 5322 | 이메일 주소 |
| `Password` | 8자 이상, 영문+숫자+특수문자 | BCrypt 해시 저장 |
| `MemberName` | 2~5자 | 회원 이름 |
| `SocialId` | NotBlank | 소셜 로그인 고유 ID |
| `Consent` | 개인정보/서비스/광고 동의 | 동의 정보 묶음 |
| `WithdrawalInfo` | reason + withdrawnAt | 탈퇴 정보 |

**Value Object 구현 예시** (Java 21 Record):

```java
// MemberId - 식별자 VO
public record MemberId(Long value) {
    public MemberId {
        if (value == null || value <= 0) {
            throw new InvalidMemberIdException(value);
        }
    }

    public static MemberId of(Long value) {
        return new MemberId(value);
    }
}

// PhoneNumber - 핸드폰 번호 VO
public record PhoneNumber(String value) {
    private static final Pattern PATTERN = Pattern.compile("^010[0-9]{8}$");

    public PhoneNumber {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new InvalidPhoneNumberException(value);
        }
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}

// Email - 이메일 VO (RFC 5322)
public record Email(String value) {
    private static final Pattern PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public Email {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new InvalidEmailException(value);
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }
}

// Password - 비밀번호 VO (BCrypt 해시 저장)
public record Password(String hashedValue) {
    public Password {
        if (hashedValue == null || hashedValue.isBlank()) {
            throw new InvalidPasswordException();
        }
    }

    public static Password of(String hashedValue) {
        return new Password(hashedValue);
    }

    /**
     * 원본 비밀번호 정책 검증 (해싱 전 호출)
     * - 8자 이상
     * - 영문, 숫자, 특수문자 포함
     */
    public static void validatePolicy(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new PasswordPolicyViolationException("8자 이상");
        }
        if (!rawPassword.matches(".*[a-zA-Z].*")) {
            throw new PasswordPolicyViolationException("영문 포함");
        }
        if (!rawPassword.matches(".*[0-9].*")) {
            throw new PasswordPolicyViolationException("숫자 포함");
        }
        if (!rawPassword.matches(".*[!@#$%^&*].*")) {
            throw new PasswordPolicyViolationException("특수문자 포함");
        }
    }
}

// MemberName - 회원 이름 VO
public record MemberName(String value) {
    public MemberName {
        if (value == null || value.length() < 2 || value.length() > 5) {
            throw new InvalidMemberNameException(value);
        }
    }

    public static MemberName of(String value) {
        return new MemberName(value);
    }
}

// SocialId - 소셜 로그인 고유 ID VO
public record SocialId(String value) {
    public SocialId {
        if (value == null || value.isBlank()) {
            throw new InvalidSocialIdException(value);
        }
    }

    public static SocialId of(String value) {
        return new SocialId(value);
    }
}

// Consent - 동의 정보 VO
public record Consent(
    boolean privacyConsent,
    boolean serviceTermsConsent,
    boolean adConsent
) {
    public Consent {
        // 개인정보, 서비스 동의는 필수
        if (!privacyConsent) {
            throw new RequiredConsentMissingException("개인정보 처리 동의");
        }
        if (!serviceTermsConsent) {
            throw new RequiredConsentMissingException("서비스 이용약관 동의");
        }
        // 광고 동의는 선택
    }

    public static Consent of(boolean privacy, boolean service, boolean ad) {
        return new Consent(privacy, service, ad);
    }
}

// WithdrawalInfo - 탈퇴 정보 VO
public record WithdrawalInfo(
    WithdrawalReason reason,
    LocalDateTime withdrawnAt
) {
    public WithdrawalInfo {
        if (reason == null) {
            throw new InvalidWithdrawalInfoException("탈퇴 사유 필수");
        }
        if (withdrawnAt == null) {
            throw new InvalidWithdrawalInfoException("탈퇴 일시 필수");
        }
    }

    public static WithdrawalInfo of(WithdrawalReason reason, LocalDateTime withdrawnAt) {
        return new WithdrawalInfo(reason, withdrawnAt);
    }
}
```

**Enum 정의**:

```java
public enum AuthProvider {
    LOCAL,   // 핸드폰/비밀번호 회원
    KAKAO    // 카카오 소셜 회원
}

public enum MemberStatus {
    ACTIVE,      // 활성
    INACTIVE,    // 휴면 (1년 미접속)
    SUSPENDED,   // 정지
    WITHDRAWN    // 탈퇴
}

public enum Gender {
    M,  // 남성
    W,  // 여성
    N   // 미지정
}

public enum WithdrawalReason {
    RARELY_USED,           // 이용 빈도 낮음
    SERVICE_DISSATISFIED,  // 서비스 불만족
    PRIVACY_CONCERN,       // 개인정보 우려
    OTHER                  // 기타
}
```

**비즈니스 규칙**:

1. **회원가입 규칙**:
   - 핸드폰 번호 중복 불가
   - 핸드폰 인증은 프론트에서 처리 완료 후 요청
   - 비밀번호 정책: 8자 이상, 영문+숫자+특수문자 필수

2. **로그인 정책** (중요!):
   ```
   ✅ 허용 플로우:
   - LOCAL → LOCAL (핸드폰/비밀번호 → 핸드폰/비밀번호)
   - KAKAO → KAKAO (카카오 → 카카오)
   - LOCAL → KAKAO (핸드폰/비밀번호 회원이 카카오로 통합)

   ❌ 차단 플로우:
   - KAKAO → LOCAL (카카오 회원이 핸드폰/비밀번호 로그인 시도)
     → "이미 카카오 회원으로 가입되어 있습니다. 카카오로 로그인해주세요."
   ```

3. **카카오 계정 통합**:
   - LOCAL 회원이 카카오 로그인 시도 시 → "카카오 계정으로 통합하시겠습니까?"
   - 통합 승인 시 → `provider: LOCAL → KAKAO`, `socialId` 저장

4. **회원 탈퇴**:
   - Soft Delete (WITHDRAWN 상태)
   - 개인정보 익명화 (30일 후 배치 처리 - 별도 PRD)

**Zero-Tolerance 규칙 준수**:
- ✅ Law of Demeter (Getter 체이닝 금지)
- ✅ Lombok 금지 (Pure Java/Record 사용)
- ✅ Tell, Don't Ask 원칙

---

### 2. Application Layer

#### 2.1 Command UseCases

**RegisterMemberUseCase** (회원가입):
```java
// Input
public record RegisterMemberCommand(
    String phoneNumber,
    String password,
    String name,
    boolean privacyConsent,
    boolean serviceTermsConsent,
    boolean adConsent
) {}

// Output
public record RegisterMemberResult(
    Long memberId,
    TokenPair tokens  // Access + Refresh
) {}
```
- **Transaction**: Yes
- **비즈니스 로직**:
  1. 핸드폰 번호 중복 확인
  2. 비밀번호 BCrypt 해시화
  3. Member 생성 (provider: LOCAL)
  4. 초기 등급 설정 (NORMAL_GRADE)
  5. **트랜잭션 커밋**
  6. JWT 토큰 발급 (트랜잭션 외부)

**LoginMemberUseCase** (로그인):
```java
// Input
public record LoginMemberCommand(
    String phoneNumber,
    String password
) {}

// Output
public record LoginMemberResult(
    Long memberId,
    TokenPair tokens
) {}
```
- **Transaction**: ReadOnly (조회만)
- **비즈니스 로직**:
  1. 핸드폰 번호로 회원 조회
  2. 카카오 회원인지 확인 → 카카오 로그인 유도 예외
  3. 비밀번호 검증
  4. JWT 토큰 발급
  5. Refresh Token Redis + DB 저장

**KakaoOAuthLoginUseCase** (카카오 로그인):
```java
// Input (Spring Security OAuth2에서 전달)
public record KakaoOAuthCommand(
    String kakaoId,
    String email,
    String phoneNumber,
    String name,
    LocalDate dateOfBirth,
    Gender gender
) {}

// Output
public record KakaoOAuthResult(
    Long memberId,
    TokenPair tokens,
    boolean isNewMember,
    boolean needsIntegration  // LOCAL 회원 존재 시
) {}
```
- **Transaction**: Yes
- **비즈니스 로직**:
  1. 카카오 ID로 회원 조회
  2. 신규 회원 → 자동 회원가입
  3. 기존 LOCAL 회원 (동일 핸드폰) → 통합 유도 플래그
  4. JWT 토큰 발급

**IntegrateKakaoUseCase** (카카오 계정 통합):
```java
// Input
public record IntegrateKakaoCommand(
    Long memberId,
    String kakaoId
) {}
```
- **Transaction**: Yes
- **비즈니스 로직**:
  1. 회원 조회
  2. provider: LOCAL → KAKAO 변경
  3. socialId 저장
  4. 비밀번호 null 처리 (선택적)

**LogoutMemberUseCase** (로그아웃):
```java
// Input
public record LogoutMemberCommand(
    Long memberId,
    String refreshToken
) {}
```
- **Transaction**: Yes
- **비즈니스 로직**:
  1. Redis에서 Refresh Token 삭제
  2. DB에서 Refresh Token 삭제 (있다면)
  3. Access Token은 자연 만료 (30분)

**RefreshTokenUseCase** (토큰 갱신):
```java
// Input
public record RefreshTokenCommand(
    String refreshToken
) {}

// Output
public record RefreshTokenResult(
    TokenPair tokens
) {}
```
- **Transaction**: Yes
- **비즈니스 로직**:
  1. Refresh Token 검증 (Redis 조회)
  2. 새 Access Token + Refresh Token 발급
  3. 기존 Refresh Token 무효화 (Redis 삭제)
  4. 새 Refresh Token 저장

**ResetPasswordUseCase** (비밀번호 재설정):
```java
// Input
public record ResetPasswordCommand(
    String phoneNumber,
    String newPassword
) {}
```
- **Transaction**: Yes
- **비즈니스 로직**:
  1. 핸드폰 번호로 회원 조회
  2. 카카오 회원 확인 → 비밀번호 변경 불가 예외
  3. 비밀번호 BCrypt 해시화 후 저장

**WithdrawMemberUseCase** (회원 탈퇴):
```java
// Input
public record WithdrawMemberCommand(
    Long memberId,
    WithdrawalReason reason
) {}
```
- **Transaction**: Yes
- **비즈니스 로직**:
  1. 회원 조회
  2. 상태 → WITHDRAWN
  3. 탈퇴 사유 저장
  4. Refresh Token 무효화 (Redis + DB)

#### 2.2 Query UseCases

**GetCurrentMemberUseCase** (내 정보 조회):
```java
// Input
public record GetCurrentMemberQuery(
    Long memberId
) {}

// Output
public record MemberDetailResult(
    Long memberId,
    String phoneNumber,
    String email,
    String name,
    LocalDate dateOfBirth,
    Gender gender,
    AuthProvider provider,
    MemberStatus status
) {}
```
- **Transaction**: ReadOnly

#### 2.3 Transaction 경계 설계

```
⚠️ Zero-Tolerance: @Transactional 내 외부 API 호출 금지!

✅ 올바른 패턴:
┌─────────────────────────────────────────┐
│  @Transactional                         │
│  1. 회원 조회                            │
│  2. 비즈니스 검증                         │
│  3. 회원 저장                            │
│  → 커밋                                  │
└─────────────────────────────────────────┘
         ↓ (트랜잭션 외부)
┌─────────────────────────────────────────┐
│  토큰 발급 (Redis 저장)                  │
│  응답 반환                               │
└─────────────────────────────────────────┘

❌ 잘못된 패턴:
┌─────────────────────────────────────────┐
│  @Transactional                         │
│  1. 회원 저장                            │
│  2. 토큰 발급 (외부 API!)  ← 위험!       │
│  → Redis 호출 실패 시 전체 롤백          │
└─────────────────────────────────────────┘
```

---

### 3. Persistence Layer

#### 3.1 JPA Entity (컨벤션 준수: of() factory, private 생성자, SoftDeletableEntity 상속)

**MemberJpaEntity**:
```java
@Entity
@Table(name = "members")
public class MemberJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "phone_number", length = 11, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password_hash", length = 60)
    private String passwordHash;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 10, nullable = false)
    private AuthProvider provider;

    @Column(name = "social_id", length = 100)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MemberStatus status;

    // 동의 정보
    @Column(name = "privacy_consent", nullable = false)
    private boolean privacyConsent;

    @Column(name = "service_terms_consent", nullable = false)
    private boolean serviceTermsConsent;

    @Column(name = "ad_consent", nullable = false)
    private boolean adConsent;

    // 탈퇴 정보
    @Enumerated(EnumType.STRING)
    @Column(name = "withdrawal_reason", length = 30)
    private WithdrawalReason withdrawalReason;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    // ===== Static Factory Method (컨벤션 준수) =====

    /**
     * Domain → Entity 변환용 팩토리 메서드
     */
    public static MemberJpaEntity of(
        Long id,
        String phoneNumber,
        String email,
        String passwordHash,
        String name,
        LocalDate dateOfBirth,
        Gender gender,
        AuthProvider provider,
        String socialId,
        MemberStatus status,
        boolean privacyConsent,
        boolean serviceTermsConsent,
        boolean adConsent,
        WithdrawalReason withdrawalReason,
        LocalDateTime withdrawnAt
    ) {
        return new MemberJpaEntity(
            id, phoneNumber, email, passwordHash, name, dateOfBirth,
            gender, provider, socialId, status, privacyConsent,
            serviceTermsConsent, adConsent, withdrawalReason, withdrawnAt
        );
    }

    // ===== Private Constructor (외부 직접 생성 금지) =====

    // JPA 기본 생성자 (protected for JPA)
    protected MemberJpaEntity() {}

    private MemberJpaEntity(
        Long id,
        String phoneNumber,
        String email,
        String passwordHash,
        String name,
        LocalDate dateOfBirth,
        Gender gender,
        AuthProvider provider,
        String socialId,
        MemberStatus status,
        boolean privacyConsent,
        boolean serviceTermsConsent,
        boolean adConsent,
        WithdrawalReason withdrawalReason,
        LocalDateTime withdrawnAt
    ) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.provider = provider;
        this.socialId = socialId;
        this.status = status;
        this.privacyConsent = privacyConsent;
        this.serviceTermsConsent = serviceTermsConsent;
        this.adConsent = adConsent;
        this.withdrawalReason = withdrawalReason;
        this.withdrawnAt = withdrawnAt;
    }

    // ===== Getter (Lombok 금지, 명시적 작성) =====

    public Long getId() { return id; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public AuthProvider getProvider() { return provider; }
    public String getSocialId() { return socialId; }
    public MemberStatus getStatus() { return status; }
    public boolean isPrivacyConsent() { return privacyConsent; }
    public boolean isServiceTermsConsent() { return serviceTermsConsent; }
    public boolean isAdConsent() { return adConsent; }
    public WithdrawalReason getWithdrawalReason() { return withdrawalReason; }
    public LocalDateTime getWithdrawnAt() { return withdrawnAt; }
}
```

**SoftDeletableEntity (공통 베이스)**:
```java
@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseAuditEntity {

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isDeleted() { return deleted; }
    public LocalDateTime getDeletedAt() { return deletedAt; }

    // Soft Delete는 Domain에서 수행, Entity는 상태만 저장
}
```

**인덱스 전략**:
```sql
-- 핸드폰 번호 조회 (로그인)
CREATE UNIQUE INDEX idx_members_phone_number ON members(phone_number);

-- 카카오 ID 조회
CREATE INDEX idx_members_social_id ON members(social_id) WHERE social_id IS NOT NULL;

-- 상태별 조회
CREATE INDEX idx_members_status ON members(status);
```

#### 3.2 Redis 구조

**RefreshToken**:
```java
@RedisHash(value = "refresh_token", timeToLive = 604800)  // 7일
public class RefreshTokenRedis {
    @Id
    private String memberId;  // 회원 ID
    private String token;     // Refresh Token 값
    private String userGrade; // 회원 등급
}
```

#### 3.3 Repository

**MemberJpaRepository**:
```java
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
    Optional<MemberJpaEntity> findByPhoneNumber(String phoneNumber);
    Optional<MemberJpaEntity> findBySocialId(String socialId);
    boolean existsByPhoneNumber(String phoneNumber);
}
```

**Zero-Tolerance 규칙 준수**:
- ✅ Long FK 전략 (JPA 관계 어노테이션 금지)
- ✅ QueryDSL DTO Projection (필요 시)

---

### 4. REST API Layer

#### 4.1 Module 구조

```
bootstrap/
├── web-api/                    # Customer용 Bootstrap
│   └── WebApiApplication.java
└── admin-web-api/              # Admin용 Bootstrap
    └── AdminWebApiApplication.java

adapter-in/
├── rest-api/                   # Customer용 API
│   └── auth/
│       ├── controller/
│       ├── dto/
│       └── mapper/
└── admin-rest-api/             # Admin용 API (인증 스킵)
    └── ...
```

#### 4.2 Customer API 엔드포인트 (컨벤션 준수: DELETE 금지 → PATCH 사용)

| Method | Path | Description | Auth | Request | Response |
|--------|------|-------------|------|---------|----------|
| POST | `/api/v1/members` | 회원가입 | - | RegisterMemberRequest | `ApiResponse<TokenResponse>` |
| POST | `/api/v1/auth/login` | 로그인 | - | LoginRequest | `ApiResponse<TokenResponse>` + Cookie |
| GET | `/api/v1/auth/oauth2/kakao` | 카카오 로그인 시작 | - | - | Redirect |
| GET | `/api/v1/auth/oauth2/kakao/callback` | 카카오 콜백 | - | code | `ApiResponse<TokenResponse>` + Cookie |
| POST | `/api/v1/auth/logout` | 로그아웃 | JWT | - | `ApiResponse<Void>` |
| POST | `/api/v1/auth/refresh` | 토큰 갱신 | Cookie | - | `ApiResponse<TokenResponse>` + Cookie |
| GET | `/api/v1/members/me` | 내 정보 조회 | JWT | - | `ApiResponse<MemberResponse>` |
| POST | `/api/v1/members/me/password` | 비밀번호 재설정 | - | ResetPasswordRequest | `ApiResponse<Void>` |
| **PATCH** | `/api/v1/members/me/withdraw` | 회원 탈퇴 (Soft Delete) | JWT | WithdrawRequest | `ApiResponse<Void>` |
| POST | `/api/v1/members/me/link/kakao` | 카카오 연동 | JWT | KakaoLinkRequest | `ApiResponse<Void>` |

> ⚠️ **컨벤션 준수**: DELETE 메서드 금지! 회원 탈퇴는 Soft Delete이므로 `PATCH /withdraw` 사용

#### 4.3 Request/Response DTO

**RegisterMemberRequest**:
```java
public record RegisterMemberRequest(
    @NotBlank
    @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
    String phoneNumber,

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
             message = "비밀번호는 8자 이상, 영문+숫자+특수문자를 포함해야 합니다.")
    String password,

    @NotBlank
    @Size(min = 2, max = 5, message = "이름은 2~5자 사이여야 합니다.")
    String name,

    @NotNull
    Boolean privacyConsent,

    @NotNull
    Boolean serviceTermsConsent,

    @NotNull
    Boolean adConsent
) {}
```

**LoginRequest**:
```java
public record LoginRequest(
    @NotBlank
    @Pattern(regexp = "010[0-9]{8}")
    String phoneNumber,

    @NotBlank
    String password
) {}
```

**TokenResponse**:
```java
public record TokenResponse(
    String accessToken,  // 응답 본문 (optional)
    Long expiresIn       // Access Token 만료 시간 (초)
) {}
// Access Token, Refresh Token은 HttpOnly 쿠키로 전달
```

**MemberResponse**:
```java
public record MemberResponse(
    Long memberId,
    String phoneNumber,
    String email,
    String name,
    LocalDate dateOfBirth,
    String gender,
    String provider,
    String status
) {}
```

#### 4.4 Controller 구현 예시 (컨벤션 준수: Thin Controller + ResponseEntity<ApiResponse<T>>)

```java
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final RegisterMemberUseCase registerMemberUseCase;
    private final LoginMemberUseCase loginMemberUseCase;
    private final LogoutMemberUseCase logoutMemberUseCase;
    private final AuthRequestMapper authRequestMapper;
    private final AuthResponseMapper authResponseMapper;

    // Constructor 생성자 주입 (Lombok 금지)
    public AuthController(
        RegisterMemberUseCase registerMemberUseCase,
        LoginMemberUseCase loginMemberUseCase,
        LogoutMemberUseCase logoutMemberUseCase,
        AuthRequestMapper authRequestMapper,
        AuthResponseMapper authResponseMapper
    ) {
        this.registerMemberUseCase = registerMemberUseCase;
        this.loginMemberUseCase = loginMemberUseCase;
        this.logoutMemberUseCase = logoutMemberUseCase;
        this.authRequestMapper = authRequestMapper;
        this.authResponseMapper = authResponseMapper;
    }

    /**
     * 회원가입
     * POST /api/v1/members
     */
    @PostMapping("/members")
    public ResponseEntity<ApiResponse<TokenResponse>> register(
        @Valid @RequestBody RegisterMemberRequest request
    ) {
        // 1. Request → Command 변환 (Mapper 사용)
        RegisterMemberCommand command = authRequestMapper.toCommand(request);

        // 2. UseCase 실행
        RegisterMemberResult result = registerMemberUseCase.execute(command);

        // 3. Result → Response 변환 (Mapper 사용)
        TokenResponse response = authResponseMapper.toResponse(result);

        // 4. ApiResponse 래핑하여 반환
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(response));
    }

    /**
     * 로그인
     * POST /api/v1/auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse httpResponse
    ) {
        LoginMemberCommand command = authRequestMapper.toCommand(request);
        LoginMemberResult result = loginMemberUseCase.execute(command);

        // 쿠키 설정
        addTokenCookies(httpResponse, result.tokens());

        TokenResponse response = authResponseMapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 로그아웃
     * POST /api/v1/auth/logout
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
        @AuthenticationPrincipal MemberPrincipal principal,
        @CookieValue("refresh_token") String refreshToken,
        HttpServletResponse httpResponse
    ) {
        LogoutMemberCommand command = new LogoutMemberCommand(
            principal.getMemberId(), refreshToken
        );
        logoutMemberUseCase.execute(command);

        // 쿠키 삭제
        clearTokenCookies(httpResponse);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // Private helper methods
    private void addTokenCookies(HttpServletResponse response, TokenPair tokens) {
        // Access Token 쿠키
        response.addHeader(HttpHeaders.SET_COOKIE,
            ResponseCookie.from("access_token", tokens.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build().toString()
        );
        // Refresh Token 쿠키
        response.addHeader(HttpHeaders.SET_COOKIE,
            ResponseCookie.from("refresh_token", tokens.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build().toString()
        );
    }

    private void clearTokenCookies(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE,
            ResponseCookie.from("access_token", "")
                .maxAge(0).path("/").build().toString()
        );
        response.addHeader(HttpHeaders.SET_COOKIE,
            ResponseCookie.from("refresh_token", "")
                .maxAge(0).path("/api/v1/auth/refresh").build().toString()
        );
    }
}

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final GetCurrentMemberUseCase getCurrentMemberUseCase;
    private final WithdrawMemberUseCase withdrawMemberUseCase;
    private final MemberResponseMapper memberResponseMapper;

    public MemberController(
        GetCurrentMemberUseCase getCurrentMemberUseCase,
        WithdrawMemberUseCase withdrawMemberUseCase,
        MemberResponseMapper memberResponseMapper
    ) {
        this.getCurrentMemberUseCase = getCurrentMemberUseCase;
        this.withdrawMemberUseCase = withdrawMemberUseCase;
        this.memberResponseMapper = memberResponseMapper;
    }

    /**
     * 내 정보 조회
     * GET /api/v1/members/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getCurrentMember(
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        GetCurrentMemberQuery query = new GetCurrentMemberQuery(principal.getMemberId());
        MemberDetailResult result = getCurrentMemberUseCase.execute(query);
        MemberResponse response = memberResponseMapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 회원 탈퇴 (Soft Delete)
     * PATCH /api/v1/members/me/withdraw (DELETE 금지!)
     */
    @PatchMapping("/me/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
        @AuthenticationPrincipal MemberPrincipal principal,
        @Valid @RequestBody WithdrawRequest request,
        HttpServletResponse httpResponse
    ) {
        WithdrawMemberCommand command = new WithdrawMemberCommand(
            principal.getMemberId(), request.reason()
        );
        withdrawMemberUseCase.execute(command);

        // 쿠키 삭제
        clearTokenCookies(httpResponse);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private void clearTokenCookies(HttpServletResponse response) {
        // ... 쿠키 삭제 로직
    }
}
```

**ApiResponse 공통 응답 래퍼**:
```java
public record ApiResponse<T>(
    boolean success,
    T data,
    ErrorInfo error,
    LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return new ApiResponse<>(false, null,
            new ErrorInfo(errorCode, message), LocalDateTime.now());
    }

    public record ErrorInfo(String code, String message) {}
}
```

#### 4.5 쿠키 설정

```java
// Access Token 쿠키
ResponseCookie.from("access_token", accessToken)
    .httpOnly(true)
    .secure(true)  // HTTPS only
    .sameSite("Strict")
    .path("/")
    .maxAge(Duration.ofMinutes(30))
    .build();

// Refresh Token 쿠키
ResponseCookie.from("refresh_token", refreshToken)
    .httpOnly(true)
    .secure(true)
    .sameSite("Strict")
    .path("/api/v1/auth/refresh")  // refresh 경로에서만 전송
    .maxAge(Duration.ofDays(7))
    .build();
```

#### 4.6 Admin API (인증 스킵)

Admin API는 Gateway에서 B2B AuthHub를 통해 인증 완료 후 호출됩니다.

**헤더에서 사용자 정보 추출**:
```java
// Filter 또는 ArgumentResolver에서 처리
public class AdminAuthHeaderResolver {
    public AdminPrincipal resolve(HttpServletRequest request) {
        return new AdminPrincipal(
            request.getHeader("X-User-Id"),
            request.getHeader("X-Tenant-Id"),
            parseRoles(request.getHeader("X-Roles"))
        );
    }
}
```

#### 4.7 Error Response

```json
{
  "errorCode": "MEMBER_NOT_FOUND",
  "message": "회원을 찾을 수 없습니다.",
  "timestamp": "2025-12-02T12:34:56Z",
  "path": "/api/v1/auth/login"
}
```

**Error Code 정의**:

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| `MEMBER_NOT_FOUND` | 404 | 회원 없음 |
| `DUPLICATE_PHONE_NUMBER` | 409 | 핸드폰 번호 중복 |
| `INVALID_PASSWORD` | 401 | 비밀번호 불일치 |
| `KAKAO_MEMBER_LOCAL_LOGIN` | 400 | 카카오 회원이 LOCAL 로그인 시도 |
| `INVALID_TOKEN` | 401 | 토큰 검증 실패 |
| `EXPIRED_TOKEN` | 401 | 토큰 만료 |
| `WITHDRAWN_MEMBER` | 403 | 탈퇴한 회원 |
| `INACTIVE_MEMBER` | 403 | 휴면 회원 |

---

## ⚠️ 제약사항

### 비기능 요구사항

**성능**:
- 토큰 발급 응답 시간: < 100ms (P95)
- 토큰 검증 응답 시간: < 50ms (P95)
- TPS: 500 requests/sec

**보안**:
- JWT 서명: HMAC SHA-256 (HS256)
- Access Token 만료: 30분
- Refresh Token 만료: 7일
- 쿠키: HttpOnly, Secure, SameSite=Strict
- 비밀번호: BCrypt (cost factor: 10)

**데이터 마이그레이션**:
- 레거시 `USERS` 테이블 → 새 `members` 테이블
- BCrypt 해시 호환 (동일 알고리즘)
- `SOCIAL_LOGIN_TYPE` → `provider` 매핑
  - `none` → `LOCAL`
  - `kakao` → `KAKAO`
  - `naver` → 지원 중단 (별도 안내)

---

## 🧪 테스트 전략

### Unit Test

**Domain**:
- Member Aggregate 비즈니스 로직 (withdraw, linkKakao)
- Value Object 검증 (PhoneNumber, Password, Email)
- AuthProvider, MemberStatus Enum 동작

**Application**:
- RegisterMemberUseCase (Mock Port)
- LoginMemberUseCase (카카오 회원 분기 테스트)
- KakaoOAuthLoginUseCase (신규/기존 회원 분기)

### Integration Test

**Persistence**:
- MemberJpaRepository CRUD (TestContainers MySQL)
- RefreshToken Redis 저장/조회/삭제

**REST API**:
- 회원가입 → 로그인 → 토큰 갱신 → 로그아웃 플로우
- 카카오 OAuth2 플로우 (MockMvc + Mock OAuth2)
- 에러 케이스 (중복 가입, 잘못된 비밀번호)

### E2E Test

- 회원가입 → 로그인 → 내 정보 조회 → 탈퇴 전체 플로우
- LOCAL 회원 → 카카오 통합 플로우

---

## 🚀 개발 계획

### Phase 1: Domain Layer
- [ ] Member Aggregate 구현
- [ ] Value Objects 구현 (PhoneNumber, Email, Password, MemberName, Consent)
- [ ] Enum 정의 (AuthProvider, MemberStatus, Gender, WithdrawalReason)
- [ ] Domain Unit Test

### Phase 2: Application Layer
- [ ] Port 인터페이스 정의 (In/Out)
- [ ] Command DTO 구현
- [ ] Query DTO 구현
- [ ] UseCase 구현
- [ ] Application Unit Test (Mock Port)

### Phase 3: Persistence Layer
- [ ] MemberJpaEntity 구현
- [ ] MemberJpaRepository 구현
- [ ] RefreshTokenRedis 구현
- [ ] MemberPersistenceAdapter 구현
- [ ] Flyway 마이그레이션 스크립트
- [ ] Integration Test (TestContainers)

### Phase 4: REST API Layer (rest-api)
- [ ] AuthController 구현
- [ ] MemberController 구현
- [ ] Request/Response DTO 구현
- [ ] Spring Security OAuth2 설정
- [ ] JWT Filter 구현
- [ ] Exception Handler 구현
- [ ] REST API Integration Test

### Phase 5: Admin REST API Layer (admin-rest-api)
- [ ] Admin Header Resolver 구현
- [ ] Admin Security 설정 (인증 스킵)

### Phase 6: Bootstrap Modules
- [ ] web-api Bootstrap 설정
- [ ] admin-web-api Bootstrap 설정

---

## 📚 참고 문서

- [Domain Layer 규칙](../coding_convention/02-domain-layer/)
- [Application Layer 규칙](../coding_convention/03-application-layer/)
- [Persistence Layer 규칙](../coding_convention/04-persistence-layer/)
- [REST API Layer 규칙](../coding_convention/01-adapter-in-layer/rest-api/)
- [레거시 분석: legacy-web/auth](../../legacy-web/auth/)
- [레거시 분석: legacy-web/module/user](../../legacy-web/module/user/)

---

## 📝 별도 PRD 예정

- **휴면 회원 처리**: 1년 미접속 회원 휴면 처리 배치
- **회원 익명화**: 탈퇴 30일 후 개인정보 익명화 배치
- **Admin 회원 관리**: 관리자 회원 조회/정지/해제 기능

---

**다음 단계**: `/jira-from-prd docs/prd/b2b-auth-hub.md`

# AUTH-001 TDD Plan

**Task**: Domain Layer 구현 - Member Aggregate & Value Objects
**Layer**: Domain Layer
**브랜치**: feature/AUTH-001-domain
**예상 소요 시간**: 375분 (25 사이클 × 15분)

---

## 📝 TDD 사이클 체크리스트

---

### 1️⃣ MemberId Value Object (Cycle 1) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `MemberIdTest.java` 생성
- [x] `shouldCreateMemberIdWithValidValue()` 작성 - Long > 0 검증
- [x] `shouldThrowExceptionWhenIdIsNull()` 작성
- [x] `shouldThrowExceptionWhenIdIsZeroOrNegative()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: `test: MemberId VO 검증 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [x] `MemberId.java` 생성 (Record)
- [x] Long > 0 검증 로직 추가
- [x] `InvalidMemberIdException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: `feat: MemberId VO 및 InvalidMemberIdException 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [x] VO ArchUnit 테스트 통과 확인
- [x] Lombok 사용 여부 확인 (금지) - ✅ Pure Java Record 사용
- [x] 리팩토링 필요 없음 (코드가 이미 깔끔함)

#### 🧹 Tidy: TestFixture 정리
- [x] `MemberIdFixture.java` 생성 (Object Mother 패턴)
- [x] `MemberIdFixture.create()` 메서드 작성
- [x] 커밋: `chore: MemberIdFixture 추가 (Object Mother Pattern)`

---

### 2️⃣ PhoneNumber Value Object (Cycle 2) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `PhoneNumberTest.java` 생성
- [x] `shouldCreatePhoneNumberWithValidFormat()` 작성 - 01012345678 형식
- [x] `shouldThrowExceptionWhenPhoneNumberIsNull()` 작성
- [x] `shouldThrowExceptionWhenPhoneNumberIsInvalidFormat()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: `test: PhoneNumber VO 검증 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [x] `PhoneNumber.java` 생성 (Record)
- [x] `010[0-9]{8}` 정규식 검증 로직 추가
- [x] `InvalidPhoneNumberException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: `feat: PhoneNumber VO 및 InvalidPhoneNumberException 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [x] Regex 패턴 상수로 추출 - ✅ 이미 `PHONE_NUMBER_PATTERN` 상수로 구현됨
- [x] VO ArchUnit 테스트 통과 확인
- [x] 리팩토링 필요 없음 (코드가 이미 깔끔함)

#### 🧹 Tidy: TestFixture 정리
- [x] `PhoneNumberFixture.java` 생성
- [x] `PhoneNumberFixture.create()` 메서드 작성
- [x] 커밋: `chore: PhoneNumberFixture 추가 (Object Mother Pattern)`

---

### 3️⃣ Email Value Object (Cycle 3) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `EmailTest.java` 생성
- [x] `shouldCreateEmailWithValidFormat()` 작성 - RFC 5322 형식
- [x] `shouldThrowExceptionWhenEmailIsInvalidFormat()` 작성
- [x] `shouldAllowNullEmail()` 작성 - nullable 허용
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: `test: Email VO 검증 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [x] `Email.java` 생성 (Record)
- [x] RFC 5322 형식 검증 로직 추가
- [x] `InvalidEmailException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: `feat: Email VO 및 InvalidEmailException 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [x] Regex 패턴 상수로 추출 - ✅ 이미 `EMAIL_PATTERN` 상수로 구현됨
- [x] VO ArchUnit 테스트 통과 확인
- [x] 리팩토링 필요 없음 (코드가 이미 깔끔함)

#### 🧹 Tidy: TestFixture 정리
- [x] `EmailFixture.java` 생성
- [x] `EmailFixture.create()` 메서드 작성
- [x] `EmailFixture.createNull()` 메서드 추가 - nullable 테스트용
- [x] 커밋: `chore: EmailFixture 추가 (Object Mother Pattern)`

---

### 4️⃣ Password Value Object (Cycle 4) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `PasswordTest.java` 생성
- [x] `shouldCreatePasswordWithValidHash()` 작성 - BCrypt 해시값 저장
- [x] `shouldThrowExceptionWhenPasswordIsNull()` 작성
- [x] `shouldThrowExceptionWhenPasswordIsBlank()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: `test: Password VO 검증 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [x] `Password.java` 생성 (Record)
- [x] NotBlank 검증 로직 추가
- [x] `InvalidPasswordException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: `feat: Password VO 및 InvalidPasswordException 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [x] VO ArchUnit 테스트 통과 확인
- [x] 리팩토링 필요 없음 (코드가 이미 깔끔함)

#### 🧹 Tidy: TestFixture 정리
- [x] `PasswordFixture.java` 생성
- [x] `PasswordFixture.create()` 메서드 작성
- [x] `PasswordFixture.createWithHash()` 메서드 작성
- [x] 커밋: `chore: PasswordFixture 추가 (Object Mother Pattern)`

---

### 5️⃣ Password 비밀번호 정책 검증 (Cycle 5) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `PasswordTest.java`에 정책 검증 테스트 추가
- [x] `shouldValidatePasswordPolicy()` 작성 - 8자 이상, 영문+숫자+특수문자
- [x] `shouldThrowExceptionWhenPasswordPolicyViolated()` 작성
- [x] 테스트 실행 → 실패 확인
- [x] 커밋: `test: Password 정책 검증 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [x] `Password.java`에 `validatePolicy(String rawPassword)` 메서드 추가
- [x] 비밀번호 정책 검증 로직 (8자 이상, 영문 대문자+소문자+숫자+특수문자)
- [x] `PasswordPolicyViolationException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: `feat: Password 정책 검증 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [x] 정책 검증 Regex 상수로 추출 - ✅ 이미 상수로 구현됨
- [x] 리팩토링 필요 없음 (코드가 이미 깔끔함)

#### 🧹 Tidy: TestFixture 정리
- [x] `PasswordFixture.java`에 정책 위반 메서드 추가
- [x] `validRawPassword()`, `invalidRawPasswordXxx()` 메서드 작성
- [x] 커밋: `chore: PasswordFixture 정책 검증 메서드 추가 (Tidy)`

---

### 6️⃣ MemberName Value Object (Cycle 6) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `MemberNameTest.java` 생성
- [x] `shouldCreateMemberNameWithValidLength()` 작성 - 2~5자
- [x] `shouldThrowExceptionWhenNameIsTooShort()` 작성
- [x] `shouldThrowExceptionWhenNameIsTooLong()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `MemberName.java` 생성 (Record)
- [x] 2~5자 길이 검증 로직 추가
- [x] `InvalidMemberNameException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] VO ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] `MemberNameFixture.java` 생성
- [x] `MemberNameFixture.create()` 메서드 작성
- [x] 커밋: (배치 구현)

---

### 7️⃣ SocialId Value Object (Cycle 7) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `SocialIdTest.java` 생성
- [x] `shouldCreateSocialIdWithValidValue()` 작성 - NotBlank
- [x] `shouldThrowExceptionWhenSocialIdIsNull()` 작성
- [x] `shouldThrowExceptionWhenSocialIdIsBlank()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `SocialId.java` 생성 (Record)
- [x] NotBlank 검증 로직 추가
- [x] `InvalidSocialIdException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] VO ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] `SocialIdFixture.java` 생성
- [x] `SocialIdFixture.create()` 메서드 작성
- [x] 커밋: (배치 구현)

---

### 8️⃣ Consent Value Object (Cycle 8) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `ConsentTest.java` 생성
- [x] `shouldCreateConsentWithRequiredConsents()` 작성 - 개인정보/서비스 동의 필수
- [x] `shouldThrowExceptionWhenPrivacyConsentIsFalse()` 작성
- [x] `shouldThrowExceptionWhenServiceConsentIsFalse()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `Consent.java` 생성 (Record)
- [x] 필수 동의 검증 로직 추가 (privacyConsent, serviceConsent = true 필수)
- [x] `RequiredConsentMissingException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] VO ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] `ConsentFixture.java` 생성
- [x] `ConsentFixture.create()` 메서드 작성
- [x] 커밋: (배치 구현)

---

### 9️⃣ WithdrawalInfo Value Object (Cycle 9) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `WithdrawalInfoTest.java` 생성
- [x] `shouldCreateWithdrawalInfoWithValidData()` 작성 - reason + withdrawnAt 필수
- [x] `shouldThrowExceptionWhenReasonIsNull()` 작성
- [x] `shouldThrowExceptionWhenWithdrawnAtIsNull()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `WithdrawalInfo.java` 생성 (Record)
- [x] reason + withdrawnAt 필수 검증 로직 추가
- [x] `InvalidWithdrawalInfoException` 생성
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] VO ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] `WithdrawalInfoFixture.java` 생성
- [x] `WithdrawalInfoFixture.create()` 메서드 작성
- [x] 커밋: (배치 구현)

---

### 🔟 AuthProvider Enum (Cycle 10) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `AuthProviderTest.java` 생성
- [x] `shouldHaveLocalAndKakaoValues()` 작성
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `AuthProvider.java` 생성 (Enum)
- [x] LOCAL, KAKAO 값 정의
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] Enum ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] (Enum은 Fixture 불필요 - Skip)
- [x] 커밋: (Skip)

---

### 1️⃣1️⃣ MemberStatus Enum (Cycle 11) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `MemberStatusTest.java` 생성
- [x] `shouldHaveAllStatusValues()` 작성 - ACTIVE, INACTIVE, SUSPENDED, WITHDRAWN
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `MemberStatus.java` 생성 (Enum)
- [x] ACTIVE, INACTIVE, SUSPENDED, WITHDRAWN 값 정의
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] Enum ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] (Enum은 Fixture 불필요 - Skip)
- [x] 커밋: (Skip)

---

### 1️⃣2️⃣ Gender Enum (Cycle 12) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `GenderTest.java` 생성
- [x] `shouldHaveAllGenderValues()` 작성 - M, W, N
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `Gender.java` 생성 (Enum)
- [x] M, W, N 값 정의
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] Enum ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] (Enum은 Fixture 불필요 - Skip)
- [x] 커밋: (Skip)

---

### 1️⃣3️⃣ WithdrawalReason Enum (Cycle 13) ✅ COMPLETED

#### 🔴 Red: 테스트 작성
- [x] `WithdrawalReasonTest.java` 생성
- [x] `shouldHaveAllReasonValues()` 작성 - RARELY_USED, SERVICE_DISSATISFIED, PRIVACY_CONCERN, OTHER
- [x] 테스트 실행 → 컴파일 에러 확인
- [x] 커밋: (배치 구현)

#### 🟢 Green: 최소 구현
- [x] `WithdrawalReason.java` 생성 (Enum)
- [x] RARELY_USED, SERVICE_DISSATISFIED, PRIVACY_CONCERN, OTHER 값 정의
- [x] 테스트 실행 → 통과 확인
- [x] 커밋: (배치 구현)

#### ♻️ Refactor: 리팩토링
- [x] Enum ArchUnit 테스트 통과 확인
- [x] 커밋: (배치 구현)

#### 🧹 Tidy: TestFixture 정리
- [x] (Enum은 Fixture 불필요 - Skip)
- [x] 커밋: (Skip)

---

### 1️⃣4️⃣ Member Aggregate - 기본 구조 (Cycle 14)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java` 생성
- [ ] `shouldCreateMemberWithForNew()` 작성 - Static Factory: forNew()
- [ ] 모든 필드 검증 (id, phoneNumber, email, password, name, dateOfBirth, gender, provider, socialId, status, consent)
- [ ] 테스트 실행 → 컴파일 에러 확인
- [ ] 커밋: `test: Member Aggregate 생성 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java` 생성 (Plain Java, Lombok 금지)
- [ ] Private 생성자 + forNew() Static Factory 메서드
- [ ] 모든 필드 정의 (MemberId, PhoneNumber, Email, Password, MemberName, LocalDate, Gender, AuthProvider, SocialId, MemberStatus, Consent, WithdrawalInfo, createdAt, updatedAt)
- [ ] Getter 메서드 작성 (Lombok 금지, 직접 작성)
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member Aggregate 기본 구조 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] 불변성 보장 (final 필드)
- [ ] Aggregate ArchUnit 테스트 통과 확인
- [ ] Lombok 사용 여부 확인 (금지)
- [ ] 커밋: `struct: Member Aggregate 기본 구조 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] `MemberFixture.java` 생성 (Object Mother 패턴)
- [ ] `MemberFixture.aMember()` 메서드 작성
- [ ] `MemberTest` → Fixture 사용으로 리팩토링
- [ ] 커밋: `test: MemberFixture 정리 (Tidy)`

---

### 1️⃣5️⃣ Member Aggregate - of() 메서드 (Cycle 15)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java`에 of() 테스트 추가
- [ ] `shouldCreateMemberWithOf()` 작성 - 기존 회원 조회용
- [ ] 테스트 실행 → 실패 확인
- [ ] 커밋: `test: Member of() 메서드 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java`에 `of()` Static Factory 메서드 추가
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member of() 메서드 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] of()와 forNew() 차이 명확화
- [ ] 커밋: `struct: Member of() 메서드 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] `MemberFixture.aMemberWithId()` 메서드 추가
- [ ] 커밋: `test: MemberFixture of() 정리 (Tidy)`

---

### 1️⃣6️⃣ Member Aggregate - reconstitute() 메서드 (Cycle 16)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java`에 reconstitute() 테스트 추가
- [ ] `shouldReconstituteMemberFromPersistence()` 작성 - Persistence에서 복원
- [ ] 테스트 실행 → 실패 확인
- [ ] 커밋: `test: Member reconstitute() 메서드 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java`에 `reconstitute()` Static Factory 메서드 추가
- [ ] 모든 필드를 받아 Member 복원
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member reconstitute() 메서드 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] 복원 로직 검증 (검증 없이 복원)
- [ ] 커밋: `struct: Member reconstitute() 메서드 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] `MemberFixture.aMemberReconstituted()` 메서드 추가
- [ ] 커밋: `test: MemberFixture reconstitute() 정리 (Tidy)`

---

### 1️⃣7️⃣ Member Aggregate - Law of Demeter Helper Methods (Cycle 17)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java`에 Helper 메서드 테스트 추가
- [ ] `shouldReturnIdValueDirectly()` 작성 - getIdValue()
- [ ] `shouldReturnPhoneNumberValueDirectly()` 작성 - getPhoneNumberValue()
- [ ] `shouldReturnSocialIdValueDirectly()` 작성 - getSocialIdValue()
- [ ] 테스트 실행 → 실패 확인
- [ ] 커밋: `test: Member Helper 메서드 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java`에 `getIdValue()` 메서드 추가 - `return id.value()`
- [ ] `getPhoneNumberValue()` 메서드 추가 - `return phoneNumber.value()`
- [ ] `getSocialIdValue()` 메서드 추가 - `return socialId != null ? socialId.value() : null`
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member Helper 메서드 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] Law of Demeter 준수 확인 - 외부에서 `member.getId().value()` 사용 금지
- [ ] 커밋: `struct: Member Helper 메서드 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] (Helper 메서드는 별도 Fixture 불필요)
- [ ] 커밋: (Skip)

---

### 1️⃣8️⃣ Member Aggregate - withdraw() 메서드 (Cycle 18)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java`에 withdraw() 테스트 추가
- [ ] `shouldWithdrawMember()` 작성 - 상태 WITHDRAWN 변경, withdrawalInfo 설정
- [ ] `shouldThrowExceptionWhenAlreadyWithdrawn()` 작성
- [ ] 테스트 실행 → 실패 확인
- [ ] 커밋: `test: Member withdraw() 메서드 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java`에 `withdraw(WithdrawalReason reason, Clock clock)` 메서드 추가
- [ ] status = WITHDRAWN 변경
- [ ] withdrawalInfo 생성 및 설정
- [ ] `AlreadyWithdrawnMemberException` 생성 및 처리
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member withdraw() 메서드 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] Tell, Don't Ask 원칙 준수 확인
- [ ] 커밋: `struct: Member withdraw() 메서드 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] `MemberFixture.aWithdrawnMember()` 메서드 추가
- [ ] 커밋: `test: MemberFixture withdraw() 정리 (Tidy)`

---

### 1️⃣9️⃣ Member Aggregate - linkKakao() 메서드 (Cycle 19)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java`에 linkKakao() 테스트 추가
- [ ] `shouldLinkKakaoToLocalMember()` 작성 - LOCAL → KAKAO 연동
- [ ] `shouldThrowExceptionWhenAlreadyKakaoMember()` 작성
- [ ] 테스트 실행 → 실패 확인
- [ ] 커밋: `test: Member linkKakao() 메서드 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java`에 `linkKakao(SocialId kakaoSocialId, Clock clock)` 메서드 추가
- [ ] provider = KAKAO 변경
- [ ] socialId 설정
- [ ] `AlreadyKakaoMemberException` 생성 및 처리
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member linkKakao() 메서드 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] 비즈니스 규칙 준수 확인 (LOCAL → KAKAO 통합만 가능)
- [ ] 커밋: `struct: Member linkKakao() 메서드 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] `MemberFixture.aKakaoLinkedMember()` 메서드 추가
- [ ] 커밋: `test: MemberFixture linkKakao() 정리 (Tidy)`

---

### 2️⃣0️⃣ Member Aggregate - changePassword() 메서드 (Cycle 20)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java`에 changePassword() 테스트 추가
- [ ] `shouldChangePasswordForLocalMember()` 작성 - LOCAL 회원만 비밀번호 변경 가능
- [ ] `shouldThrowExceptionWhenKakaoMemberChangesPassword()` 작성
- [ ] 테스트 실행 → 실패 확인
- [ ] 커밋: `test: Member changePassword() 메서드 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java`에 `changePassword(Password newPassword, Clock clock)` 메서드 추가
- [ ] KAKAO 회원은 비밀번호 변경 불가 검증
- [ ] `KakaoMemberCannotChangePasswordException` 생성 및 처리
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member changePassword() 메서드 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] 비즈니스 규칙 준수 확인
- [ ] 커밋: `struct: Member changePassword() 메서드 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] (changePassword는 별도 Fixture 불필요)
- [ ] 커밋: (Skip)

---

### 2️⃣1️⃣ Member Aggregate - isLocalMember() / isKakaoMember() (Cycle 21)

#### 🔴 Red: 테스트 작성
- [ ] `MemberTest.java`에 상태 확인 메서드 테스트 추가
- [ ] `shouldReturnTrueForLocalMember()` 작성
- [ ] `shouldReturnTrueForKakaoMember()` 작성
- [ ] 테스트 실행 → 실패 확인
- [ ] 커밋: `test: Member 상태 확인 메서드 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `Member.java`에 `isLocalMember()` 메서드 추가 - `return provider == AuthProvider.LOCAL`
- [ ] `isKakaoMember()` 메서드 추가 - `return provider == AuthProvider.KAKAO`
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Member 상태 확인 메서드 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] Tell, Don't Ask 원칙 준수 확인 - 외부에서 provider 직접 비교 금지
- [ ] 커밋: `struct: Member 상태 확인 메서드 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] `MemberFixture.aLocalMember()` 메서드 추가
- [ ] `MemberFixture.aKakaoMember()` 메서드 추가
- [ ] 커밋: `test: MemberFixture 상태별 정리 (Tidy)`

---

### 2️⃣2️⃣ Domain Exception 정리 - 기본 예외 (Cycle 22)

#### 🔴 Red: 테스트 작성
- [ ] `DomainExceptionTest.java` 생성
- [ ] 모든 도메인 예외 인스턴스화 테스트
- [ ] 예외 메시지 검증
- [ ] 테스트 실행 → 컴파일 에러 확인
- [ ] 커밋: `test: Domain Exception 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] `DomainException.java` 생성 (Base Exception)
- [ ] 모든 개별 예외가 DomainException 상속 확인
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Domain Exception 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] Exception ArchUnit 테스트 통과 확인
- [ ] 커밋: `struct: Domain Exception 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] (Exception은 Fixture 불필요)
- [ ] 커밋: (Skip)

---

### 2️⃣3️⃣ ArchUnit 테스트 - Domain Layer 규칙 (Cycle 23)

#### 🔴 Red: 테스트 작성
- [ ] `DomainArchitectureTest.java` 생성
- [ ] `aggregatesShouldNotDependOnOtherLayers()` 작성
- [ ] `vosShouldBeRecords()` 작성
- [ ] `domainShouldNotUseLombok()` 작성
- [ ] `domainExceptionsShouldExtendDomainException()` 작성
- [ ] 테스트 실행 → 실패 확인 (위반 사항 있을 경우)
- [ ] 커밋: `test: Domain ArchUnit 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] 모든 ArchUnit 규칙 위반 수정
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: Domain ArchUnit 규칙 준수 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] ArchUnit 규칙 정리 및 문서화
- [ ] 커밋: `struct: Domain ArchUnit 테스트 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] (ArchUnit은 Fixture 불필요)
- [ ] 커밋: (Skip)

---

### 2️⃣4️⃣ TestFixture 전체 정리 (Cycle 24)

#### 🔴 Red: 테스트 작성
- [ ] `MemberFixtureTest.java` 생성
- [ ] 모든 Fixture 메서드 동작 테스트
- [ ] 테스트 실행 → 실패 확인 (누락된 Fixture 있을 경우)
- [ ] 커밋: `test: MemberFixture 전체 테스트 추가 (Red)`

#### 🟢 Green: 최소 구현
- [ ] 누락된 Fixture 메서드 추가
- [ ] 테스트 실행 → 통과 확인
- [ ] 커밋: `feat: MemberFixture 전체 구현 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] Fixture 구조 정리 (Object Mother 패턴 준수)
- [ ] 커밋: `struct: MemberFixture 전체 개선 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] 모든 테스트가 Fixture 사용하도록 리팩토링
- [ ] 커밋: `test: 전체 Fixture 사용 정리 (Tidy)`

---

### 2️⃣5️⃣ 최종 검증 (Cycle 25)

#### 🔴 Red: 테스트 작성
- [ ] 전체 테스트 실행 (`./gradlew test`)
- [ ] 테스트 커버리지 확인 (> 80%)
- [ ] 실패하는 테스트 없는지 확인
- [ ] 커밋: (Skip - 검증만)

#### 🟢 Green: 최소 구현
- [ ] 실패하는 테스트 수정
- [ ] 커버리지 부족 영역 테스트 추가
- [ ] 커밋: `feat: Domain Layer 최종 테스트 보완 (Green)`

#### ♻️ Refactor: 리팩토링
- [ ] 전체 코드 정리
- [ ] 커밋: `struct: Domain Layer 최종 정리 (Refactor)`

#### 🧹 Tidy: TestFixture 정리
- [ ] 최종 Fixture 정리
- [ ] 커밋: `test: Domain Layer Fixture 최종 정리 (Tidy)`

---

## ✅ 완료 조건

- [ ] 모든 TDD 사이클 완료 (체크박스 모두 ✅)
- [ ] 모든 테스트 통과 (`./gradlew test`)
- [ ] ArchUnit 테스트 통과
- [ ] Zero-Tolerance 규칙 준수
  - [ ] Lombok 사용 없음
  - [ ] Law of Demeter 준수
  - [ ] Tell, Don't Ask 원칙 준수
  - [ ] Private 생성자 + Static Factory 사용
- [ ] TestFixture 모두 정리 (Object Mother 패턴)
- [ ] 테스트 커버리지 > 80%

---

## 📊 사이클 요약

| 카테고리 | 사이클 수 | 예상 시간 |
|---------|----------|----------|
| Value Objects | 9 | 135분 |
| Enums | 4 | 60분 |
| Member Aggregate | 8 | 120분 |
| ArchUnit & Exception | 2 | 30분 |
| Fixture 정리 | 1 | 15분 |
| 최종 검증 | 1 | 15분 |
| **총합** | **25** | **375분** |

---

## 🔗 관련 문서

- Task: docs/prd/tasks/AUTH-001.md
- PRD: docs/prd/b2b-auth-hub.md
- Coding Convention: docs/coding_convention/02-domain-layer/

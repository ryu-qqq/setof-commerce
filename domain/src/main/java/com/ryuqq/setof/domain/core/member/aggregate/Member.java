package com.ryuqq.setof.domain.core.member.aggregate;

import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.core.member.exception.KakaoMemberCannotChangePasswordException;
import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import com.ryuqq.setof.domain.core.member.vo.MemberStatus;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalReason;
import com.ryuqq.setof.domain.core.member.vo.Consent;
import com.ryuqq.setof.domain.core.member.vo.Email;
import com.ryuqq.setof.domain.core.member.vo.MemberId;
import com.ryuqq.setof.domain.core.member.vo.MemberName;
import com.ryuqq.setof.domain.core.member.vo.Password;
import com.ryuqq.setof.domain.core.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.core.member.vo.SocialId;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalInfo;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Member Aggregate Root
 *
 * <p>Domain Layer Zero-Tolerance 규칙:</p>
 * <ul>
 *     <li>Lombok 금지 - Pure Java 사용</li>
 *     <li>불변성 보장 - final 필드</li>
 *     <li>Private 생성자 + Static Factory - 외부 직접 생성 금지</li>
 *     <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공</li>
 *     <li>Tell, Don't Ask - 상태 변경은 도메인 메서드로만</li>
 * </ul>
 */
public class Member {

    private final MemberId id;
    private final PhoneNumber phoneNumber;
    private final Email email;
    private Password password;
    private final MemberName name;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private AuthProvider provider;
    private SocialId socialId;
    private MemberStatus status;
    private final Consent consent;
    private WithdrawalInfo withdrawalInfo;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Private 생성자 - 외부 직접 생성 금지
     */
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

    /**
     * 신규 회원 생성용 Static Factory Method
     *
     * @param phoneNumber 휴대폰 번호
     * @param email 이메일 (nullable)
     * @param password 비밀번호 (BCrypt 해시)
     * @param name 회원명
     * @param dateOfBirth 생년월일
     * @param gender 성별
     * @param provider 인증 제공자
     * @param socialId 소셜 ID (nullable)
     * @param consent 동의 정보
     * @param clock 시간 제공자
     * @return Member 인스턴스
     */
    public static Member forNew(
            PhoneNumber phoneNumber,
            Email email,
            Password password,
            MemberName name,
            LocalDate dateOfBirth,
            Gender gender,
            AuthProvider provider,
            SocialId socialId,
            Consent consent,
            Clock clock
    ) {
        LocalDateTime now = LocalDateTime.now(clock);
        return new Member(
                null,
                phoneNumber,
                email,
                password,
                name,
                dateOfBirth,
                gender,
                provider,
                socialId,
                MemberStatus.ACTIVE,
                consent,
                null,
                now,
                now
        );
    }

    /**
     * ID가 할당된 회원 생성용 Static Factory Method
     *
     * @param id 회원 ID
     * @param phoneNumber 휴대폰 번호
     * @param email 이메일 (nullable)
     * @param password 비밀번호 (BCrypt 해시)
     * @param name 회원명
     * @param dateOfBirth 생년월일
     * @param gender 성별
     * @param provider 인증 제공자
     * @param socialId 소셜 ID (nullable)
     * @param consent 동의 정보
     * @param clock 시간 제공자
     * @return Member 인스턴스
     */
    public static Member of(
            MemberId id,
            PhoneNumber phoneNumber,
            Email email,
            Password password,
            MemberName name,
            LocalDate dateOfBirth,
            Gender gender,
            AuthProvider provider,
            SocialId socialId,
            Consent consent,
            Clock clock
    ) {
        LocalDateTime now = LocalDateTime.now(clock);
        return new Member(
                id,
                phoneNumber,
                email,
                password,
                name,
                dateOfBirth,
                gender,
                provider,
                socialId,
                MemberStatus.ACTIVE,
                consent,
                null,
                now,
                now
        );
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     * 검증 없이 모든 필드를 그대로 복원
     *
     * @return Member 인스턴스
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
        return new Member(
                id,
                phoneNumber,
                email,
                password,
                name,
                dateOfBirth,
                gender,
                provider,
                socialId,
                status,
                consent,
                withdrawalInfo,
                createdAt,
                updatedAt
        );
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 회원 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 회원 ID 값 또는 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 휴대폰 번호 값 반환 (Law of Demeter 준수)
     *
     * @return 휴대폰 번호 문자열
     */
    public String getPhoneNumberValue() {
        return phoneNumber.value();
    }

    /**
     * 이메일 값 반환 (Law of Demeter 준수)
     *
     * @return 이메일 문자열 또는 null
     */
    public String getEmailValue() {
        return email != null ? email.value() : null;
    }

    /**
     * 비밀번호 해시 값 반환 (Law of Demeter 준수)
     *
     * @return 비밀번호 해시 문자열
     */
    public String getPasswordValue() {
        return password.value();
    }

    /**
     * 회원명 값 반환 (Law of Demeter 준수)
     *
     * @return 회원명 문자열
     */
    public String getNameValue() {
        return name.value();
    }

    /**
     * 소셜 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 소셜 ID 문자열 또는 null
     */
    public String getSocialIdValue() {
        return socialId != null ? socialId.value() : null;
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 회원 탈퇴 처리
     *
     * @param reason 탈퇴 사유
     * @param clock 시간 제공자
     * @throws AlreadyWithdrawnMemberException 이미 탈퇴한 회원인 경우
     */
    public void withdraw(WithdrawalReason reason, Clock clock) {
        if (this.status == MemberStatus.WITHDRAWN) {
            throw new AlreadyWithdrawnMemberException();
        }
        LocalDateTime now = LocalDateTime.now(clock);
        this.status = MemberStatus.WITHDRAWN;
        this.withdrawalInfo = WithdrawalInfo.of(reason, now);
        this.updatedAt = now;
    }

    /**
     * 카카오 계정 연동
     *
     * @param kakaoSocialId 카카오 소셜 ID
     * @param clock 시간 제공자
     * @throws AlreadyKakaoMemberException 이미 카카오 회원인 경우
     */
    public void linkKakao(SocialId kakaoSocialId, Clock clock) {
        if (this.provider == AuthProvider.KAKAO) {
            throw new AlreadyKakaoMemberException();
        }
        this.provider = AuthProvider.KAKAO;
        this.socialId = kakaoSocialId;
        this.updatedAt = LocalDateTime.now(clock);
    }

    /**
     * 비밀번호 변경 (LOCAL 회원만 가능)
     *
     * @param newPassword 새 비밀번호
     * @param clock 시간 제공자
     * @throws KakaoMemberCannotChangePasswordException 카카오 회원인 경우
     */
    public void changePassword(Password newPassword, Clock clock) {
        if (this.provider == AuthProvider.KAKAO) {
            throw new KakaoMemberCannotChangePasswordException();
        }
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now(clock);
    }

    /**
     * LOCAL 회원 여부 확인 (Tell, Don't Ask)
     *
     * @return LOCAL 회원이면 true
     */
    public boolean isLocalMember() {
        return this.provider == AuthProvider.LOCAL;
    }

    /**
     * KAKAO 회원 여부 확인 (Tell, Don't Ask)
     *
     * @return KAKAO 회원이면 true
     */
    public boolean isKakaoMember() {
        return this.provider == AuthProvider.KAKAO;
    }

    /**
     * 탈퇴 회원 여부 확인 (Tell, Don't Ask)
     *
     * @return 탈퇴 회원이면 true
     */
    public boolean isWithdrawn() {
        return this.status == MemberStatus.WITHDRAWN;
    }

    /**
     * 활성 회원 여부 확인 (Tell, Don't Ask)
     *
     * @return 활성 회원이면 true
     */
    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public MemberId getId() {
        return id;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public MemberName getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public SocialId getSocialId() {
        return socialId;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public Consent getConsent() {
        return consent;
    }

    public WithdrawalInfo getWithdrawalInfo() {
        return withdrawalInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

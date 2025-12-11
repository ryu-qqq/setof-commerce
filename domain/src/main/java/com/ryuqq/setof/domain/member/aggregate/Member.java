package com.ryuqq.setof.domain.member.aggregate;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.member.event.MemberRegisteredEvent;
import com.ryuqq.setof.domain.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.member.exception.KakaoMemberCannotChangePasswordException;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.Consent;
import com.ryuqq.setof.domain.member.vo.Email;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import com.ryuqq.setof.domain.member.vo.Password;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.SocialId;
import com.ryuqq.setof.domain.member.vo.WithdrawalInfo;
import com.ryuqq.setof.domain.member.vo.WithdrawalReason;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Member Aggregate Root
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Tell, Don't Ask - 상태 변경은 도메인 메서드로만
 * </ul>
 */
public class Member {

    private final MemberId id;
    private final PhoneNumber phoneNumber;
    private Email email;
    private Password password;
    private MemberName name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private AuthProvider provider;
    private SocialId socialId;
    private MemberStatus status;
    private final Consent consent;
    private WithdrawalInfo withdrawalInfo;
    private final Instant createdAt;
    private Instant updatedAt;

    /** 도메인 이벤트 목록 (Transient - 영속화 대상 아님) */
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /** Private 생성자 - 외부 직접 생성 금지 */
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
            Instant createdAt,
            Instant updatedAt) {
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
     * <p>MemberRegisteredEvent 자동 등록
     *
     * <p>UUID v7 생성은 Application Layer에서 UuidCreator.getTimeOrderedEpoch()로 수행
     *
     * @param id Application Layer에서 생성한 MemberId (UUID v7)
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
            Clock clock) {
        Instant now = clock.instant();

        Member member =
                new Member(
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
                        now);

        member.registerMemberRegisteredEvent(id, now);

        return member;
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
            Clock clock) {
        Instant now = clock.instant();
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
                now);
    }

    /**
     * Persistence에서 복원용 Static Factory Method 검증 없이 모든 필드를 그대로 복원
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
            Instant createdAt,
            Instant updatedAt) {
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
                updatedAt);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 회원 ID 문자열 반환 (Law of Demeter 준수)
     *
     * @return 회원 ID UUID 문자열 또는 null
     */
    public String getIdValue() {
        return id != null ? id.value().toString() : null;
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
     * @return 비밀번호 해시 문자열 또는 null (소셜 로그인 시)
     */
    public String getPasswordValue() {
        return password != null ? password.value() : null;
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
            throw new AlreadyWithdrawnMemberException(this.id.value());
        }
        Instant now = clock.instant();
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
            throw new AlreadyKakaoMemberException(this.id.value());
        }
        this.provider = AuthProvider.KAKAO;
        this.socialId = kakaoSocialId;
        this.updatedAt = clock.instant();
    }

    /**
     * 카카오 계정 연동 및 프로필 업데이트
     *
     * <p>카카오에서 받은 프로필 정보로 회원 정보를 업데이트합니다. null이 아닌 값만 업데이트됩니다.
     *
     * @param kakaoSocialId 카카오 소셜 ID
     * @param email 카카오에서 받은 이메일 (null 허용)
     * @param name 카카오에서 받은 이름 (null 허용)
     * @param dateOfBirth 카카오에서 받은 생년월일 (null 허용)
     * @param gender 카카오에서 받은 성별 (null 허용)
     * @param clock 시간 제공자
     * @throws AlreadyKakaoMemberException 이미 카카오 회원인 경우
     */
    public void linkKakaoWithProfile(
            SocialId kakaoSocialId,
            Email email,
            MemberName name,
            LocalDate dateOfBirth,
            Gender gender,
            Clock clock) {
        if (this.provider == AuthProvider.KAKAO) {
            throw new AlreadyKakaoMemberException(this.id.value());
        }
        this.provider = AuthProvider.KAKAO;
        this.socialId = kakaoSocialId;

        if (email != null) {
            this.email = email;
        }
        if (name != null) {
            this.name = name;
        }
        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth;
        }
        if (gender != null) {
            this.gender = gender;
        }

        this.updatedAt = clock.instant();
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
            throw new KakaoMemberCannotChangePasswordException(this.id.value());
        }
        this.password = newPassword;
        this.updatedAt = clock.instant();
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

    // ========== 도메인 이벤트 메서드 ==========

    /**
     * 도메인 이벤트 등록 (Aggregate 내부용)
     *
     * @param event 등록할 도메인 이벤트
     */
    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /**
     * 등록된 도메인 이벤트 조회 및 초기화
     *
     * <p>이벤트 수집 후 반환하고, 내부 목록을 초기화하여 중복 발행을 방지합니다.
     *
     * @return 불변 이벤트 목록 (List.copyOf 사용)
     */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    private void registerMemberRegisteredEvent(MemberId memberId, Instant registeredAt) {
        MemberRegisteredEvent event =
                MemberRegisteredEvent.of(
                        memberId.value().toString(),
                        phoneNumber.value(),
                        email != null ? email.value() : null,
                        name.value(),
                        provider.name(),
                        registeredAt);
        registerEvent(event);
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

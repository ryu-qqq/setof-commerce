package com.ryuqq.setof.storage.legacy.user.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LegacyUserEntity - 레거시 사용자 엔티티.
 *
 * <p>레거시 DB의 users 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "users")
public class LegacyUserEntity extends LegacyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "social_pk_id")
    private String socialPkId;

    @Column(name = "user_grade_id")
    private long userGradeId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "social_login_type")
    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "privacy_consent")
    @Enumerated(EnumType.STRING)
    private Yn privacyConsent;

    @Column(name = "service_terms_consent")
    @Enumerated(EnumType.STRING)
    private Yn serviceTermsConsent;

    @Column(name = "ad_consent")
    @Enumerated(EnumType.STRING)
    private Yn adConsent;

    @Column(name = "withdrawal_yn")
    @Enumerated(EnumType.STRING)
    private Yn withdrawalYn;

    @Column(name = "withdrawal_reason")
    private String withdrawalReason;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyUserEntity() {}

    private LegacyUserEntity(
            LocalDateTime insertDate, LocalDateTime updateDate, LegacyUserEntity source) {
        super(insertDate, updateDate);
        this.id = source.id;
        this.socialPkId = source.socialPkId;
        this.userGradeId = source.userGradeId;
        this.phoneNumber = source.phoneNumber;
        this.socialLoginType = source.socialLoginType;
        this.email = source.email;
        this.passwordHash = source.passwordHash;
        this.name = source.name;
        this.dateOfBirth = source.dateOfBirth;
        this.gender = source.gender;
        this.privacyConsent = source.privacyConsent;
        this.serviceTermsConsent = source.serviceTermsConsent;
        this.adConsent = source.adConsent;
        this.withdrawalYn = source.withdrawalYn;
        this.withdrawalReason = source.withdrawalReason;
        this.deleteYn = source.deleteYn;
    }

    /**
     * 신규 회원 생성.
     *
     * @param phoneNumber 전화번호
     * @param passwordHash BCrypt 해시된 비밀번호
     * @param name 이름
     * @param socialLoginType 로그인 타입
     * @param socialPkId 소셜 PK ID
     * @param privacyConsent 개인정보 동의
     * @param serviceTermsConsent 서비스 약관 동의
     * @param adConsent 광고 수신 동의
     * @param insertDate 생성 시각
     * @param updateDate 수정 시각
     * @return 새 LegacyUserEntity
     */
    public static LegacyUserEntity create(
            String phoneNumber,
            String passwordHash,
            String name,
            SocialLoginType socialLoginType,
            String socialPkId,
            Yn privacyConsent,
            Yn serviceTermsConsent,
            Yn adConsent,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyUserEntity entity = new LegacyUserEntity();
        entity.userGradeId = 1L;
        entity.phoneNumber = phoneNumber;
        entity.passwordHash = passwordHash;
        entity.name = name;
        entity.socialLoginType = socialLoginType;
        entity.socialPkId = socialPkId;
        entity.gender = Gender.N;
        entity.privacyConsent = privacyConsent;
        entity.serviceTermsConsent = serviceTermsConsent;
        entity.adConsent = adConsent;
        entity.withdrawalYn = Yn.N;
        entity.deleteYn = Yn.N;
        return new LegacyUserEntity(insertDate, updateDate, entity);
    }

    /** 영속성 레이어에서 복원. */
    public static LegacyUserEntity reconstitute(
            Long id,
            String socialPkId,
            long userGradeId,
            String phoneNumber,
            SocialLoginType socialLoginType,
            String email,
            String passwordHash,
            String name,
            LocalDate dateOfBirth,
            Gender gender,
            Yn privacyConsent,
            Yn serviceTermsConsent,
            Yn adConsent,
            Yn withdrawalYn,
            String withdrawalReason,
            Yn deleteYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyUserEntity entity = new LegacyUserEntity();
        entity.id = id;
        entity.socialPkId = socialPkId;
        entity.userGradeId = userGradeId;
        entity.phoneNumber = phoneNumber;
        entity.socialLoginType = socialLoginType;
        entity.email = email;
        entity.passwordHash = passwordHash;
        entity.name = name;
        entity.dateOfBirth = dateOfBirth;
        entity.gender = gender;
        entity.privacyConsent = privacyConsent;
        entity.serviceTermsConsent = serviceTermsConsent;
        entity.adConsent = adConsent;
        entity.withdrawalYn = withdrawalYn;
        entity.withdrawalReason = withdrawalReason;
        entity.deleteYn = deleteYn;
        return new LegacyUserEntity(insertDate, updateDate, entity);
    }

    /**
     * 회원 탈퇴 처리 (소프트 삭제).
     *
     * @param reason 탈퇴 사유
     */
    public void withdrawal(String reason) {
        this.withdrawalYn = Yn.Y;
        this.withdrawalReason = reason;
        this.deleteYn = Yn.Y;
        updateDate(LocalDateTime.now());
    }

    /**
     * 비밀번호 재설정.
     *
     * @param encodedPassword BCrypt 해시된 새 비밀번호
     */
    public void resetPassword(String encodedPassword) {
        this.passwordHash = encodedPassword;
        updateDate(LocalDateTime.now());
    }

    /**
     * 소셜 로그인 통합.
     *
     * <p>기존 전화번호 회원에 소셜 로그인 정보를 업데이트합니다.
     *
     * @param socialLoginType 소셜 로그인 타입
     * @param socialPkId 소셜 PK ID
     * @param gender 성별
     * @param dateOfBirth 생년월일
     */
    public void integrateSocial(
            SocialLoginType socialLoginType,
            String socialPkId,
            Gender gender,
            LocalDate dateOfBirth) {
        this.socialLoginType = socialLoginType;
        this.socialPkId = socialPkId;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        updateDate(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public String getSocialPkId() {
        return socialPkId;
    }

    public long getUserGradeId() {
        return userGradeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SocialLoginType getSocialLoginType() {
        return socialLoginType;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public Yn getPrivacyConsent() {
        return privacyConsent;
    }

    public Yn getServiceTermsConsent() {
        return serviceTermsConsent;
    }

    public Yn getAdConsent() {
        return adConsent;
    }

    public Yn getWithdrawalYn() {
        return withdrawalYn;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public boolean isWithdrawn() {
        return withdrawalYn == Yn.Y;
    }

    public boolean isDeleted() {
        return deleteYn == Yn.Y;
    }

    public boolean isEmailUser() {
        return socialLoginType == SocialLoginType.EMAIL;
    }

    /** SocialLoginType - 소셜 로그인 타입. */
    public enum SocialLoginType {
        KAKAO,
        NAVER,
        GOOGLE,
        APPLE,
        EMAIL
    }

    /** Gender - 성별. */
    public enum Gender {
        MALE,
        FEMALE,
        OTHER,
        N
    }
}

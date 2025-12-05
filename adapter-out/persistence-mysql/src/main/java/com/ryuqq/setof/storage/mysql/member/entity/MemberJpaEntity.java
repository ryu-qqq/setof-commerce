package com.ryuqq.setof.storage.mysql.member.entity;

import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import com.ryuqq.setof.domain.core.member.vo.MemberStatus;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalReason;
import com.ryuqq.setof.storage.mysql.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MemberJpaEntity - 회원 JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 members 테이블과 매핑됩니다.
 *
 * <p><strong>UUID v7 기반 PK:</strong>
 *
 * <ul>
 *   <li>보안성: 순차적 ID(1,2,3)는 예측 가능하여 보안 취약점
 *   <li>UUID v7: 시간 기반 정렬 가능 + 예측 불가능
 *   <li>Domain Layer에서 생성한 MemberId(UUID v7)를 그대로 PK로 사용
 * </ul>
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>소프트 딜리트 지원 (deletedAt != null → 삭제)
 *   <li>isDeleted(), isActive() 메서드 제공
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 사용 금지 (@ManyToOne, @OneToMany 등)
 *   <li>외래키 참조 시 String UUID 또는 Long ID로 직접 관리
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Entity
@Table(name = "members")
public class MemberJpaEntity extends SoftDeletableEntity {

    /**
     * 기본 키 - UUID v7 (Domain MemberId)
     *
     * <p>Domain Layer에서 UUID v7으로 생성된 ID를 그대로 PK로 사용합니다.
     *
     * <p>AUTO_INCREMENT 대신 UUID v7을 사용하여 보안성을 확보합니다.
     */
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    /**
     * 휴대폰 번호
     *
     * <p>010으로 시작하는 11자리 숫자, unique 제약
     */
    @Column(name = "phone_number", nullable = false, length = 11, unique = true)
    private String phoneNumber;

    /**
     * 이메일 주소
     *
     * <p>RFC 5322 형식, nullable
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 비밀번호 해시
     *
     * <p>BCrypt 해시값 (60자), nullable (소셜 로그인 회원)
     */
    @Column(name = "password_hash", length = 60)
    private String passwordHash;

    /** 회원명 */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 생년월일
     *
     * <p>nullable (소셜 로그인 회원)
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /** 성별 */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    /**
     * 인증 제공자
     *
     * <p>LOCAL, KAKAO 등
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 20)
    private AuthProvider provider;

    /**
     * 소셜 ID
     *
     * <p>카카오 등 소셜 로그인 시 사용, nullable
     */
    @Column(name = "social_id", length = 100)
    private String socialId;

    /**
     * 회원 상태
     *
     * <p>ACTIVE, INACTIVE, WITHDRAWN 등
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MemberStatus status;

    /** 개인정보 수집 동의 */
    @Column(name = "privacy_consent", nullable = false)
    private boolean privacyConsent;

    /** 서비스 이용약관 동의 */
    @Column(name = "service_terms_consent", nullable = false)
    private boolean serviceTermsConsent;

    /** 광고 수신 동의 */
    @Column(name = "ad_consent", nullable = false)
    private boolean adConsent;

    /**
     * 탈퇴 사유
     *
     * <p>회원 탈퇴 시에만 설정, nullable
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "withdrawal_reason", length = 50)
    private WithdrawalReason withdrawalReason;

    /**
     * 탈퇴 일시
     *
     * <p>회원 탈퇴 시에만 설정, nullable
     */
    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected MemberJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private MemberJpaEntity(
            String id,
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
            LocalDateTime withdrawnAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        super(createdAt, updatedAt, deletedAt);
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

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * <p>Mapper에서 Domain → Entity 변환 시 사용합니다.
     *
     * @param id 회원 ID (UUID v7 문자열, Domain MemberId에서 추출)
     * @param phoneNumber 휴대폰 번호
     * @param email 이메일 (nullable)
     * @param passwordHash 비밀번호 해시 (nullable)
     * @param name 회원명
     * @param dateOfBirth 생년월일 (nullable)
     * @param gender 성별 (nullable)
     * @param provider 인증 제공자
     * @param socialId 소셜 ID (nullable)
     * @param status 회원 상태
     * @param privacyConsent 개인정보 수집 동의
     * @param serviceTermsConsent 서비스 이용약관 동의
     * @param adConsent 광고 수신 동의
     * @param withdrawalReason 탈퇴 사유 (nullable)
     * @param withdrawnAt 탈퇴 일시 (nullable)
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @param deletedAt 삭제 일시 (nullable)
     * @return MemberJpaEntity 인스턴스
     */
    public static MemberJpaEntity of(
            String id,
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
            LocalDateTime withdrawnAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        return new MemberJpaEntity(
                id,
                phoneNumber,
                email,
                passwordHash,
                name,
                dateOfBirth,
                gender,
                provider,
                socialId,
                status,
                privacyConsent,
                serviceTermsConsent,
                adConsent,
                withdrawalReason,
                withdrawnAt,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    public AuthProvider getProvider() {
        return provider;
    }

    public String getSocialId() {
        return socialId;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public boolean isPrivacyConsent() {
        return privacyConsent;
    }

    public boolean isServiceTermsConsent() {
        return serviceTermsConsent;
    }

    public boolean isAdConsent() {
        return adConsent;
    }

    public WithdrawalReason getWithdrawalReason() {
        return withdrawalReason;
    }

    public LocalDateTime getWithdrawnAt() {
        return withdrawnAt;
    }
}

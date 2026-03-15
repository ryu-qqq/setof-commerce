package com.ryuqq.setof.adapter.out.persistence.memberconsent.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * MemberConsentJpaEntity - 회원 동의 정보 JPA 엔티티.
 *
 * <p>개인정보, 서비스 약관, 광고 수신 동의를 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "member_consents")
public class MemberConsentJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "privacy_consent", nullable = false)
    private boolean privacyConsent;

    @Column(name = "service_terms_consent", nullable = false)
    private boolean serviceTermsConsent;

    @Column(name = "ad_consent", nullable = false)
    private boolean adConsent;

    protected MemberConsentJpaEntity() {
        super();
    }

    private MemberConsentJpaEntity(
            Long id,
            Long memberId,
            boolean privacyConsent,
            boolean serviceTermsConsent,
            boolean adConsent,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
        this.privacyConsent = privacyConsent;
        this.serviceTermsConsent = serviceTermsConsent;
        this.adConsent = adConsent;
    }

    public static MemberConsentJpaEntity create(
            Long id,
            Long memberId,
            boolean privacyConsent,
            boolean serviceTermsConsent,
            boolean adConsent,
            Instant createdAt,
            Instant updatedAt) {
        return new MemberConsentJpaEntity(
                id, memberId, privacyConsent, serviceTermsConsent, adConsent, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
}

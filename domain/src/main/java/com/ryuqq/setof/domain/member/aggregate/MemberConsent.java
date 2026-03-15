package com.ryuqq.setof.domain.member.aggregate;

import com.ryuqq.setof.domain.member.id.MemberId;
import java.time.Instant;

/**
 * 회원 동의 정보 Aggregate.
 *
 * <p>개인정보, 서비스 약관, 광고 수신 동의를 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class MemberConsent {

    private final Long id;
    private MemberId memberId;
    private boolean privacyConsent;
    private boolean serviceTermsConsent;
    private boolean adConsent;
    private final Instant createdAt;
    private Instant updatedAt;

    private MemberConsent(
            Long id,
            MemberId memberId,
            boolean privacyConsent,
            boolean serviceTermsConsent,
            boolean adConsent,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.privacyConsent = privacyConsent;
        this.serviceTermsConsent = serviceTermsConsent;
        this.adConsent = adConsent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MemberConsent forNew(
            MemberId memberId,
            boolean privacyConsent,
            boolean serviceTermsConsent,
            boolean adConsent,
            Instant occurredAt) {
        return new MemberConsent(
                null,
                memberId,
                privacyConsent,
                serviceTermsConsent,
                adConsent,
                occurredAt,
                occurredAt);
    }

    public static MemberConsent reconstitute(
            Long id,
            MemberId memberId,
            boolean privacyConsent,
            boolean serviceTermsConsent,
            boolean adConsent,
            Instant createdAt,
            Instant updatedAt) {
        return new MemberConsent(
                id, memberId, privacyConsent, serviceTermsConsent, adConsent, createdAt, updatedAt);
    }

    /**
     * 회원 PK 할당 (auto-increment 후 1회만 호출).
     *
     * @param memberId 생성된 회원 PK
     * @throws IllegalStateException 이미 할당된 경우
     */
    public void assignMemberId(MemberId memberId) {
        if (this.memberId != null) {
            throw new IllegalStateException("memberId는 이미 할당되었습니다");
        }
        this.memberId = memberId;
    }

    public void updateConsent(
            boolean privacyConsent,
            boolean serviceTermsConsent,
            boolean adConsent,
            Instant occurredAt) {
        this.privacyConsent = privacyConsent;
        this.serviceTermsConsent = serviceTermsConsent;
        this.adConsent = adConsent;
        this.updatedAt = occurredAt;
    }

    public Long id() {
        return id;
    }

    public MemberId memberId() {
        return memberId;
    }

    public Long memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public boolean privacyConsent() {
        return privacyConsent;
    }

    public boolean serviceTermsConsent() {
        return serviceTermsConsent;
    }

    public boolean adConsent() {
        return adConsent;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}

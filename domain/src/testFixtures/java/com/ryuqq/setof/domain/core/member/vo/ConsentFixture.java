package com.ryuqq.setof.domain.core.member.vo;

/**
 * Consent TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Consent 인스턴스 생성을 위한 팩토리 클래스</p>
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 * Consent consent = ConsentFixture.create();
 * Consent allConsents = ConsentFixture.createWithAllConsents();
 * Consent requiredOnly = ConsentFixture.createWithRequiredOnly();
 * }</pre>
 */
public final class ConsentFixture {

    private ConsentFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 Consent 생성 (필수 동의만, 마케팅 미동의)
     *
     * @return Consent 인스턴스
     */
    public static Consent create() {
        return Consent.ofRequired(true, true);
    }

    /**
     * 모든 동의 항목에 동의한 Consent 생성
     *
     * @return Consent 인스턴스
     */
    public static Consent createWithAllConsents() {
        return Consent.of(true, true, true);
    }

    /**
     * 필수 동의만 한 Consent 생성 (마케팅 미동의)
     *
     * @return Consent 인스턴스
     */
    public static Consent createWithRequiredOnly() {
        return Consent.ofRequired(true, true);
    }

    /**
     * 마케팅 동의 포함한 Consent 생성
     *
     * @param marketingConsent 마케팅 동의 여부
     * @return Consent 인스턴스
     */
    public static Consent createWithMarketing(boolean marketingConsent) {
        return Consent.of(true, true, marketingConsent);
    }

    /**
     * 지정된 동의 항목으로 Consent 생성
     *
     * @param privacyConsent 개인정보 처리방침 동의
     * @param serviceConsent 서비스 이용약관 동의
     * @param marketingConsent 마케팅 정보 수신 동의
     * @return Consent 인스턴스
     */
    public static Consent create(boolean privacyConsent, boolean serviceConsent, boolean marketingConsent) {
        return Consent.of(privacyConsent, serviceConsent, marketingConsent);
    }
}

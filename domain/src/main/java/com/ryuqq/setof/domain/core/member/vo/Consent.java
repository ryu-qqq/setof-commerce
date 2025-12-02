package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.RequiredConsentMissingException;

/**
 * 회원 동의 정보 Value Object
 *
 * <p>필수 동의 항목:</p>
 * <ul>
 *     <li>privacyConsent: 개인정보 수집/이용 동의 (필수)</li>
 *     <li>serviceConsent: 서비스 이용약관 동의 (필수)</li>
 * </ul>
 *
 * <p>선택 동의 항목:</p>
 * <ul>
 *     <li>marketingConsent: 마케팅 정보 수신 동의 (선택)</li>
 * </ul>
 *
 * @param privacyConsent 개인정보 수집/이용 동의
 * @param serviceConsent 서비스 이용약관 동의
 * @param marketingConsent 마케팅 정보 수신 동의
 */
public record Consent(
    boolean privacyConsent,
    boolean serviceConsent,
    boolean marketingConsent
) {

    public Consent {
        validateRequiredConsents(privacyConsent, serviceConsent);
    }

    public static Consent of(boolean privacyConsent, boolean serviceConsent, boolean marketingConsent) {
        return new Consent(privacyConsent, serviceConsent, marketingConsent);
    }

    /**
     * 필수 동의만 체크하여 생성 (마케팅 동의 = false)
     */
    public static Consent ofRequired(boolean privacyConsent, boolean serviceConsent) {
        return new Consent(privacyConsent, serviceConsent, false);
    }

    private static void validateRequiredConsents(boolean privacyConsent, boolean serviceConsent) {
        if (!privacyConsent) {
            throw new RequiredConsentMissingException("개인정보 수집/이용");
        }
        if (!serviceConsent) {
            throw new RequiredConsentMissingException("서비스 이용약관");
        }
    }
}

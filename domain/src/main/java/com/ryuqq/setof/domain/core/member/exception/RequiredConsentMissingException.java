package com.ryuqq.setof.domain.core.member.exception;

/**
 * 필수 동의 누락 예외
 */
public final class RequiredConsentMissingException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "필수 동의 항목이 누락되었습니다.";

    public RequiredConsentMissingException() {
        super(DEFAULT_MESSAGE);
    }

    public RequiredConsentMissingException(String consentType) {
        super(String.format("필수 동의 항목이 누락되었습니다: %s 동의가 필요합니다.", consentType));
    }
}

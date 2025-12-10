package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;

/**
 * 필수 동의 누락 예외
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class RequiredConsentMissingException extends DomainException {

    public RequiredConsentMissingException(String consentType) {
        super(
            MemberErrorCode.REQUIRED_CONSENT_MISSING,
            String.format("필수 동의 항목이 누락되었습니다: %s 동의가 필요합니다.", consentType),
            Map.of("consentType", consentType)
        );
    }
}

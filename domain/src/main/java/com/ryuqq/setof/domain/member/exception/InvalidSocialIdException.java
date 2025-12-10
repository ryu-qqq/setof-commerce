package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 소셜 ID에 대한 도메인 예외
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidSocialIdException extends DomainException {

    public InvalidSocialIdException(String invalidValue) {
        super(
                MemberErrorCode.INVALID_SOCIAL_ID,
                String.format("소셜 ID가 올바르지 않습니다: %s", invalidValue != null ? invalidValue : "null"),
                Map.of("invalidValue", invalidValue != null ? invalidValue : "null"));
    }

    public InvalidSocialIdException(String provider, String invalidValue) {
        super(
                MemberErrorCode.INVALID_SOCIAL_ID,
                String.format("소셜 ID가 올바르지 않습니다. 제공자: %s, 값: %s", provider, invalidValue),
                Map.of(
                        "provider",
                        provider,
                        "invalidValue",
                        invalidValue != null ? invalidValue : "null"));
    }
}

package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;
import java.util.UUID;

/**
 * 유효하지 않은 Refresh Token일 때 발생하는 예외
 *
 * <p>HTTP 응답: 401 UNAUTHORIZED
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidRefreshTokenException extends DomainException {

    public InvalidRefreshTokenException() {
        super(MemberErrorCode.INVALID_REFRESH_TOKEN);
    }

    public InvalidRefreshTokenException(UUID memberId) {
        super(
            MemberErrorCode.INVALID_REFRESH_TOKEN,
            String.format("유효하지 않은 Refresh Token입니다. 회원 ID: %s", memberId),
            Map.of("memberId", memberId)
        );
    }

    public InvalidRefreshTokenException(String reason) {
        super(
            MemberErrorCode.INVALID_REFRESH_TOKEN,
            String.format("유효하지 않은 Refresh Token입니다: %s", reason),
            Map.of("reason", reason)
        );
    }
}

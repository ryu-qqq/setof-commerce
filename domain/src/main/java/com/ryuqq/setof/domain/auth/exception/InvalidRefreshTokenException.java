package com.ryuqq.setof.domain.auth.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * Invalid Refresh Token Exception
 *
 * <p>유효하지 않거나 만료된 Refresh Token에 대한 예외
 *
 * <p><strong>발생 조건:</strong>
 *
 * <ul>
 *   <li>존재하지 않는 토큰
 *   <li>만료된 토큰
 *   <li>이미 사용된 토큰
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidRefreshTokenException extends DomainException {

    private static final AuthErrorCode ERROR_CODE = AuthErrorCode.INVALID_REFRESH_TOKEN;

    public InvalidRefreshTokenException() {
        super(ERROR_CODE, ERROR_CODE.getMessage());
    }

    public InvalidRefreshTokenException(String message) {
        super(ERROR_CODE, message);
    }
}

package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/** 유효하지 않은 Refresh Token일 때 발생하는 예외 */
public final class InvalidRefreshTokenException extends DomainException {

    private static final String DEFAULT_MESSAGE = "유효하지 않은 Refresh Token입니다.";

    public InvalidRefreshTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}

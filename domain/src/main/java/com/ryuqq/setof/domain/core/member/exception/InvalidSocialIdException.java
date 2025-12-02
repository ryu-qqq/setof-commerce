package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/**
 * 잘못된 소셜 ID에 대한 도메인 예외
 */
public final class InvalidSocialIdException extends DomainException {

    private static final String DEFAULT_MESSAGE = "소셜 ID가 올바르지 않습니다.";

    public InvalidSocialIdException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidSocialIdException(String message) {
        super(message);
    }
}

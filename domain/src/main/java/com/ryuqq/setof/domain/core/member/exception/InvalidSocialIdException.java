package com.ryuqq.setof.domain.core.member.exception;

/**
 * 잘못된 소셜 ID에 대한 도메인 예외
 */
public final class InvalidSocialIdException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "소셜 ID가 올바르지 않습니다.";

    public InvalidSocialIdException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidSocialIdException(String message) {
        super(message);
    }
}

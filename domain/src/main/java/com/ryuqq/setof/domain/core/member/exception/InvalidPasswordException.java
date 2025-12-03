package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/**
 * 잘못된 비밀번호에 대한 도메인 예외
 *
 * <p>Password 생성 시 다음 조건을 위반할 때 발생:
 *
 * <ul>
 *   <li>null 값
 *   <li>빈 문자열 또는 공백 문자열
 * </ul>
 */
public final class InvalidPasswordException extends DomainException {

    private static final String DEFAULT_MESSAGE = "비밀번호가 올바르지 않습니다.";

    public InvalidPasswordException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}

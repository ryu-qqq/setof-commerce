package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/** 휴면 또는 정지된 회원이 로그인 시도 시 발생하는 예외 */
public final class InactiveMemberException extends DomainException {

    private static final String DEFAULT_MESSAGE = "휴면 또는 정지된 회원입니다. 고객센터에 문의하세요.";

    public InactiveMemberException() {
        super(DEFAULT_MESSAGE);
    }

    public InactiveMemberException(String message) {
        super(message);
    }
}

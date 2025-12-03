package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/** 회원을 찾을 수 없을 때 발생하는 예외 */
public final class MemberNotFoundException extends DomainException {

    private static final String DEFAULT_MESSAGE = "회원을 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}

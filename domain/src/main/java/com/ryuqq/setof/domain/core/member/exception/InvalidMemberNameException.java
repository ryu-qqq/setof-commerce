package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/** 잘못된 회원 이름에 대한 도메인 예외 */
public final class InvalidMemberNameException extends DomainException {

    private static final String DEFAULT_MESSAGE = "회원 이름이 올바르지 않습니다.";

    public InvalidMemberNameException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidMemberNameException(String value, String reason) {
        super(String.format("회원 이름이 올바르지 않습니다. 입력값: %s, 사유: %s", value, reason));
    }
}

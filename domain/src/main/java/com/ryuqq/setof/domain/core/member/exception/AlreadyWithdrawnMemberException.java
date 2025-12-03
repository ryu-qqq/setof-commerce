package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/** 이미 탈퇴한 회원에 대한 작업 시 발생하는 예외 */
public final class AlreadyWithdrawnMemberException extends DomainException {

    private static final String DEFAULT_MESSAGE = "이미 탈퇴한 회원입니다.";

    public AlreadyWithdrawnMemberException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyWithdrawnMemberException(String message) {
        super(message);
    }
}

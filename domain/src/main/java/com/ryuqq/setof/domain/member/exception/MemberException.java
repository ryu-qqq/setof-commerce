package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 회원 도메인 예외. */
public class MemberException extends DomainException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }

    public MemberException(MemberErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public MemberException(MemberErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}

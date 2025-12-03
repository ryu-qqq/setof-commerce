package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/** 잘못된 탈퇴 정보에 대한 도메인 예외 */
public final class InvalidWithdrawalInfoException extends DomainException {

    private static final String DEFAULT_MESSAGE = "탈퇴 정보가 올바르지 않습니다.";

    public InvalidWithdrawalInfoException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidWithdrawalInfoException(String message) {
        super(message);
    }
}

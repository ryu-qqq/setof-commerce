package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/**
 * 이미 카카오 연동된 회원에 대해 카카오 연동 시도 시 발생하는 예외
 */
public final class AlreadyKakaoMemberException extends DomainException {

    private static final String DEFAULT_MESSAGE = "이미 카카오로 연동된 회원입니다.";

    public AlreadyKakaoMemberException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyKakaoMemberException(String message) {
        super(message);
    }
}

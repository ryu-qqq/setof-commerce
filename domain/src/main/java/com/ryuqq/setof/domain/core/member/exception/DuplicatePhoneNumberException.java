package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/** 이미 등록된 핸드폰 번호로 가입 시도 시 발생하는 예외 */
public final class DuplicatePhoneNumberException extends DomainException {

    private static final String DEFAULT_MESSAGE = "이미 등록된 핸드폰 번호입니다.";

    public DuplicatePhoneNumberException() {
        super(DEFAULT_MESSAGE);
    }

    public DuplicatePhoneNumberException(String message) {
        super(message);
    }
}

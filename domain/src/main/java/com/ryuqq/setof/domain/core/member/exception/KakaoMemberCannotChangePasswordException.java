package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/**
 * 카카오 회원이 비밀번호 변경 시도 시 발생하는 예외
 */
public final class KakaoMemberCannotChangePasswordException extends DomainException {

    private static final String DEFAULT_MESSAGE = "카카오 회원은 비밀번호를 변경할 수 없습니다.";

    public KakaoMemberCannotChangePasswordException() {
        super(DEFAULT_MESSAGE);
    }

    public KakaoMemberCannotChangePasswordException(String message) {
        super(message);
    }
}

package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/**
 * 비밀번호 정책 위반 예외
 *
 * <p>비밀번호가 다음 정책을 위반할 때 발생:</p>
 * <ul>
 *     <li>8자 이상</li>
 *     <li>영문 대문자 포함</li>
 *     <li>영문 소문자 포함</li>
 *     <li>숫자 포함</li>
 *     <li>특수문자 포함</li>
 * </ul>
 */
public final class PasswordPolicyViolationException extends DomainException {

    private static final String DEFAULT_MESSAGE =
        "비밀번호는 8자 이상이며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.";

    public PasswordPolicyViolationException() {
        super(DEFAULT_MESSAGE);
    }

    public PasswordPolicyViolationException(String message) {
        super(message);
    }
}

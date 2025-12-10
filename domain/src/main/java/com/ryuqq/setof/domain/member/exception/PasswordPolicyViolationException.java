package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 비밀번호 정책 위반 예외
 *
 * <p>비밀번호가 다음 정책을 위반할 때 발생:
 *
 * <ul>
 *   <li>8자 이상
 *   <li>영문 대문자 포함
 *   <li>영문 소문자 포함
 *   <li>숫자 포함
 *   <li>특수문자 포함
 * </ul>
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class PasswordPolicyViolationException extends DomainException {

    public PasswordPolicyViolationException() {
        super(MemberErrorCode.PASSWORD_POLICY_VIOLATION);
    }

    public PasswordPolicyViolationException(String violatedRule) {
        super(
                MemberErrorCode.PASSWORD_POLICY_VIOLATION,
                String.format("비밀번호 정책 위반: %s", violatedRule),
                Map.of("violatedRule", violatedRule));
    }
}

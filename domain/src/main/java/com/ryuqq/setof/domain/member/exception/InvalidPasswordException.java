package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;

/**
 * 잘못된 비밀번호에 대한 도메인 예외
 *
 * <p>Password 생성 시 다음 조건을 위반할 때 발생:
 *
 * <ul>
 *   <li>null 값
 *   <li>빈 문자열 또는 공백 문자열
 * </ul>
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidPasswordException extends DomainException {

    public InvalidPasswordException() {
        super(MemberErrorCode.INVALID_PASSWORD);
    }

    public InvalidPasswordException(String reason) {
        super(
            MemberErrorCode.INVALID_PASSWORD,
            String.format("비밀번호가 올바르지 않습니다: %s", reason),
            Map.of("reason", reason)
        );
    }
}

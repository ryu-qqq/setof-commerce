package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;

/**
 * 잘못된 이메일 형식에 대한 도메인 예외
 *
 * <p>Email 생성 시 다음 조건을 위반할 때 발생:
 *
 * <ul>
 *   <li>빈 문자열 또는 공백 문자열
 *   <li>RFC 5322 형식 불일치
 * </ul>
 *
 * <p>참고: null 값은 허용됨 (nullable 필드)
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidEmailException extends DomainException {

    public InvalidEmailException(String invalidValue) {
        super(
            MemberErrorCode.INVALID_EMAIL,
            String.format("이메일 형식이 올바르지 않습니다: %s", invalidValue),
            Map.of("invalidValue", invalidValue != null ? invalidValue : "null")
        );
    }

    public InvalidEmailException(String invalidValue, String reason) {
        super(
            MemberErrorCode.INVALID_EMAIL,
            String.format("잘못된 이메일: %s. %s", invalidValue, reason),
            Map.of(
                "invalidValue", invalidValue != null ? invalidValue : "null",
                "reason", reason
            )
        );
    }
}

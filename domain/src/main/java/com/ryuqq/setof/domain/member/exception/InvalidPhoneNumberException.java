package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 핸드폰 번호에 대한 도메인 예외
 *
 * <p>PhoneNumber 생성 시 다음 조건을 위반할 때 발생:
 *
 * <ul>
 *   <li>null 또는 빈 문자열
 *   <li>010[0-9]{8} 형식 불일치
 * </ul>
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidPhoneNumberException extends DomainException {

    public InvalidPhoneNumberException(String invalidValue) {
        super(
                MemberErrorCode.INVALID_PHONE_NUMBER,
                String.format(
                        "잘못된 핸드폰 번호: %s. 010으로 시작하는 11자리 숫자여야 합니다.",
                        invalidValue != null ? invalidValue : "null"),
                Map.of("invalidValue", invalidValue != null ? invalidValue : "null"));
    }

    public InvalidPhoneNumberException(String invalidValue, String reason) {
        super(
                MemberErrorCode.INVALID_PHONE_NUMBER,
                String.format("잘못된 핸드폰 번호: %s. %s", invalidValue, reason),
                Map.of(
                        "invalidValue",
                        invalidValue != null ? invalidValue : "null",
                        "reason",
                        reason));
    }
}

package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;

/**
 * 이미 등록된 핸드폰 번호로 가입 시도 시 발생하는 예외
 *
 * <p>HTTP 응답: 409 CONFLICT
 *
 * @author development-team
 * @since 1.0.0
 */
public final class DuplicatePhoneNumberException extends DomainException {

    public DuplicatePhoneNumberException(String phoneNumber) {
        super(
            MemberErrorCode.DUPLICATE_PHONE_NUMBER,
            String.format("이미 등록된 핸드폰 번호입니다: %s", maskPhoneNumber(phoneNumber)),
            Map.of("phoneNumber", maskPhoneNumber(phoneNumber))
        );
    }

    private static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return "***";
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
    }
}

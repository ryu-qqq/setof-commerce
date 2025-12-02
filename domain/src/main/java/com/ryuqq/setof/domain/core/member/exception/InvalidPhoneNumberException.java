package com.ryuqq.setof.domain.core.member.exception;

/**
 * 잘못된 핸드폰 번호에 대한 도메인 예외
 *
 * <p>PhoneNumber 생성 시 다음 조건을 위반할 때 발생:</p>
 * <ul>
 *     <li>null 또는 빈 문자열</li>
 *     <li>010[0-9]{8} 형식 불일치</li>
 * </ul>
 */
public final class InvalidPhoneNumberException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "핸드폰 번호는 010으로 시작하는 11자리 숫자여야 합니다.";

    public InvalidPhoneNumberException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException(String invalidValue, String reason) {
        super(String.format("잘못된 핸드폰 번호: %s. %s", invalidValue, reason));
    }
}

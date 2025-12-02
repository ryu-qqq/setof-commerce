package com.ryuqq.setof.domain.core.member.exception;

/**
 * 잘못된 이메일 형식에 대한 도메인 예외
 *
 * <p>Email 생성 시 다음 조건을 위반할 때 발생:</p>
 * <ul>
 *     <li>빈 문자열 또는 공백 문자열</li>
 *     <li>RFC 5322 형식 불일치</li>
 * </ul>
 *
 * <p>참고: null 값은 허용됨 (nullable 필드)</p>
 */
public final class InvalidEmailException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이메일 형식이 올바르지 않습니다.";

    public InvalidEmailException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String invalidValue, String reason) {
        super(String.format("잘못된 이메일: %s. %s", invalidValue, reason));
    }
}

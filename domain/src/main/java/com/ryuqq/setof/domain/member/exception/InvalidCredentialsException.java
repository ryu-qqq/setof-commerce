package com.ryuqq.setof.domain.member.exception;

/**
 * 비밀번호 불일치 예외.
 *
 * <p>입력한 비밀번호가 저장된 비밀번호와 일치하지 않을 때 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class InvalidCredentialsException extends MemberException {

    private static final MemberErrorCode ERROR_CODE = MemberErrorCode.INVALID_CREDENTIALS;

    public InvalidCredentialsException() {
        super(ERROR_CODE);
    }

    public InvalidCredentialsException(String providerUserId) {
        super(ERROR_CODE, String.format("인증 실패: %s", providerUserId));
    }
}

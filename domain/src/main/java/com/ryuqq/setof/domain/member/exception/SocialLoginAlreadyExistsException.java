package com.ryuqq.setof.domain.member.exception;

/**
 * 소셜 로그인 이미 존재 예외.
 *
 * <p>소셜 인증으로 가입된 회원이 비밀번호 기반 인증으로 전환을 시도할 때 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class SocialLoginAlreadyExistsException extends MemberException {

    private static final MemberErrorCode ERROR_CODE = MemberErrorCode.SOCIAL_LOGIN_ALREADY_EXISTS;

    public SocialLoginAlreadyExistsException() {
        super(ERROR_CODE);
    }

    public SocialLoginAlreadyExistsException(String authProvider) {
        super(ERROR_CODE, String.format("%s 소셜로그인으로 이미 가입되어 있습니다", authProvider));
    }
}

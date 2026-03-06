package com.ryuqq.setof.domain.member.exception;

/**
 * 미지원 인증 제공자 예외.
 *
 * <p>시스템에서 지원하지 않는 인증 제공자로 로그인을 시도할 때 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class UnsupportedAuthProviderException extends MemberException {

    private static final MemberErrorCode ERROR_CODE = MemberErrorCode.UNSUPPORTED_AUTH_PROVIDER;

    public UnsupportedAuthProviderException() {
        super(ERROR_CODE);
    }

    public UnsupportedAuthProviderException(String authProvider) {
        super(ERROR_CODE, String.format("지원하지 않는 인증 제공자입니다: %s", authProvider));
    }
}

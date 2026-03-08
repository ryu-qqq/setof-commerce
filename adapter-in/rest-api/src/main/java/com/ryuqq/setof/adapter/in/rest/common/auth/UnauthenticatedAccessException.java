package com.ryuqq.setof.adapter.in.rest.common.auth;

/**
 * UnauthenticatedAccessException - 인증되지 않은 사용자의 인증 필수 리소스 접근 시 발생.
 *
 * <p>Spring Security의 AuthenticationException 대신 사용하여, GlobalExceptionHandler에서 401로 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class UnauthenticatedAccessException extends RuntimeException {

    public UnauthenticatedAccessException(String message) {
        super(message);
    }
}

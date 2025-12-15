package com.setof.connectly.module.exception.auth;

public class UnAuthorizedException extends AuthException {

    public static final String CODE = "UNAUTHORIZED_MEMBER-401";
    public static final String MESSAGE = "인증되지 않은 사용자 입니다. 다시 로그인 해주세요";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.UNAUTHORIZED;

    public UnAuthorizedException() {
        super(CODE, HttpStatus, MESSAGE);
    }
}

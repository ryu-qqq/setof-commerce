package com.setof.connectly.module.exception.auth;

public class AuthForbiddenException extends AuthException {

    public static final String CODE = "FORBIDDEN_MEMBER-400";
    public static final String MESSAGE = "인가되지 않은 사용자 입니다.";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.FORBIDDEN;

    public AuthForbiddenException() {
        super(CODE, HttpStatus, MESSAGE);
    }
}

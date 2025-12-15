package com.setof.connectly.module.exception.auth;

import org.springframework.http.HttpStatus;

public class ExpireRefreshTokenException extends AuthException {

    public static final String CODE = "TOKEN-401";
    public static final String MESSAGE = "로그인 권한이 만료되었습니다. 다시 로그인 해주세요";

    public ExpireRefreshTokenException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}

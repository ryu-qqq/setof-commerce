package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends UserException {

    public static final String CODE = "MEMBER-403";
    public static final String MESSAGE = "접근 권한이 없는 Url 입니다.";

    public UnauthorizedException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}

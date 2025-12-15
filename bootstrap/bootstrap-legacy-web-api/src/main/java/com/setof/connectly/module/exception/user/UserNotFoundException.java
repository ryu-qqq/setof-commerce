package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {

    public static final String CODE = "USER-404";
    public static final String MESSAGE = "존재하지 않는 회원입니다.";

    public UserNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

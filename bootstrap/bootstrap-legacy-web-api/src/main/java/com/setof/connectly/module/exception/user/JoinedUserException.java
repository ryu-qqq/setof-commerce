package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class JoinedUserException extends UserException {

    public static final String CODE = "MEMBER-400";
    public static final String MESSAGE = "카카오로 로그인해주세요.";

    public JoinedUserException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

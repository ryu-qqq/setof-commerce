package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends UserException {

    public static final String CODE = "INVALID_PASSWORD-400";
    public static final String MESSAGE = "로그인 아이디와 비밀번호를 확인해주세요.";

    public InvalidPasswordException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

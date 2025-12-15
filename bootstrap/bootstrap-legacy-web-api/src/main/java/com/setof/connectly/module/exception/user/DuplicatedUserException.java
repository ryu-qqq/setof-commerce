package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class DuplicatedUserException extends UserException {

    public static final String CODE = "MEMBER-400";

    public DuplicatedUserException(String field) {
        super(CODE, HttpStatus.BAD_REQUEST, buildMessage(field));
    }

    private static String buildMessage(String field) {
        return String.format("이미 회원 가입 되어있는 고객입니다. 중복되는 필드 값 %s ", field);
    }
}

package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class WithdrawalUserException extends UserException {

    public static final String CODE = "USER-400";
    public static final String MESSAGE = "이미 탈퇴 한 회원 입니다. 고객 센터로 문의 주세요";

    public WithdrawalUserException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class InvalidWithdrawalException extends UserException {

    public static final String CODE = "USER-400";
    public static final String MESSAGE = "회원 탈퇴를 위해선 탈퇴 약관에 동의 하셔야 합니다.";

    public InvalidWithdrawalException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

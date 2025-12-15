package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class RefundAccountNotFoundException extends UserException {

    public static final String CODE = "REFUND_ACCOUNT-404";
    public static final String MESSAGE = "해당 회원의 환불 계좌를 찾을 수 없습니다.";

    public RefundAccountNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

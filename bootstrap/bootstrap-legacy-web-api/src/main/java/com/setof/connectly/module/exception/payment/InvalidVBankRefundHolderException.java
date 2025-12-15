package com.setof.connectly.module.exception.payment;

import org.springframework.http.HttpStatus;

public class InvalidVBankRefundHolderException extends PaymentException {

    public static final String CODE = "VBANK-REFUND-400";
    public static final String MESSAGE = "환불 계좌 주인이 일치하지 않습니다. 접속한 계정 이름  ";

    public InvalidVBankRefundHolderException(String message) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE + message);
    }
}

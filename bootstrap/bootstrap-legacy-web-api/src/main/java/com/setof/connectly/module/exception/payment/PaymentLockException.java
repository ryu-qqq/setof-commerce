package com.setof.connectly.module.exception.payment;

public class PaymentLockException extends PaymentException {

    public static final String CODE = "PAYMENT-400";
    public static final String MESSAGE = "결제가 이미 진행중입니다. ";

    public PaymentLockException() {
        super(CODE, org.springframework.http.HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

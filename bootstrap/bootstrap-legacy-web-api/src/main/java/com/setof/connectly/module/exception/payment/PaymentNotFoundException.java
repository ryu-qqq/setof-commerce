package com.setof.connectly.module.exception.payment;

public class PaymentNotFoundException extends PaymentException {

    public static final String CODE = "PAYMENT-404";
    public static final String MESSAGE = "해당 결제 건이 존재하지 않습니다";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.NOT_FOUND;

    public PaymentNotFoundException(long paymentId) {
        super(CODE, HttpStatus, MESSAGE + paymentId);
    }

    public PaymentNotFoundException() {
        super(CODE, HttpStatus, MESSAGE);
    }
}

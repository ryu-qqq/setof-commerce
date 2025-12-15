package com.setof.connectly.module.exception.payment;

import org.springframework.http.HttpStatus;

public class InValidMileageUseException extends PaymentException {

    public static final String CODE = "PAYMENT-400";
    public static final String MESSAGE = "마일리지 금액이 결제 금액보다 높습니다.";

    public InValidMileageUseException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

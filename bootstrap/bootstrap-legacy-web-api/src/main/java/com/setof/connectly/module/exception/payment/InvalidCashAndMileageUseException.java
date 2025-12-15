package com.setof.connectly.module.exception.payment;

import org.springframework.http.HttpStatus;

public class InvalidCashAndMileageUseException extends PaymentException {

    public static final String CODE = "PAYMENT-400";
    public static final String TITLE = "앗! 잠깐만요";

    public InvalidCashAndMileageUseException(String message) {
        super(CODE, HttpStatus.BAD_REQUEST, message, TITLE);
    }
}

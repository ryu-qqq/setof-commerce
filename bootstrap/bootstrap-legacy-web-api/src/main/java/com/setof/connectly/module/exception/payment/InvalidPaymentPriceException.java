package com.setof.connectly.module.exception.payment;

import org.springframework.http.HttpStatus;

public class InvalidPaymentPriceException extends PaymentException {

    public static final String CODE = "PAYMENT-400";
    public static final String MESSAGE = "주문 금액이 일치하지 않습니다.";

    public InvalidPaymentPriceException(long requestPrice, long price) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE + buildMessage(requestPrice, price));
    }

    private static String buildMessage(long requestPrice, long price) {
        return String.format("요청 주문 금액 %d, 실제 결제 금액 %d ", requestPrice, price);
    }
}

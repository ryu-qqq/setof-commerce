package com.setof.connectly.module.exception.payment;

import org.springframework.http.HttpStatus;

public class RefundFailException extends PaymentException {

    public static final String CODE = "PAYMENT_REFUND-400";
    public static final String MESSAGE = "결제 환불이 실패했습니다";

    public RefundFailException(long paymentId) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE + buildMessage(paymentId));
    }

    public RefundFailException(long paymentId, String message) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE + buildMessage(paymentId, message));
    }

    private static String buildMessage(long paymentId) {
        return String.format("결제 번호  %s, 실패 사유  %s ", paymentId, "고객센터로 문의 주세요");
    }

    private static String buildMessage(long paymentId, String message) {
        return String.format("결제 번호  %s, 실패 사유  %s ", paymentId, message);
    }
}

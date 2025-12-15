package com.setof.connectly.module.exception.payment;

public class PaymentBillNotFoundException extends PaymentException {

    public static final String CODE = "PAYMENT_BILL-404";
    public static final String MESSAGE = "결제 청구서를 찾을 수 없습니다.";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.NOT_FOUND;

    public PaymentBillNotFoundException(long paymentId) {
        super(CODE, HttpStatus, MESSAGE + paymentId);
    }

    public PaymentBillNotFoundException(String paymentId) {
        super(CODE, HttpStatus, MESSAGE + paymentId);
    }
}

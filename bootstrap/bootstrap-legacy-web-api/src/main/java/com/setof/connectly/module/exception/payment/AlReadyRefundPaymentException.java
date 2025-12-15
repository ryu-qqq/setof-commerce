package com.setof.connectly.module.exception.payment;

import com.setof.connectly.module.payment.entity.Payment;
import org.springframework.http.HttpStatus;

public class AlReadyRefundPaymentException extends PaymentException {

    public static final String CODE = "PAYMENT_REFUND-400";
    public static final String MESSAGE = "이미 취소된 주문 입니다. ";

    public AlReadyRefundPaymentException(Payment payment) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE + buildMessage(payment));
    }

    private static String buildMessage(Payment payment) {
        return String.format(
                "결제 번호 %s, 결제 상태 %s, 취소 날짜 %s ",
                payment.getId(),
                payment.getPaymentDetails().getPaymentStatus(),
                payment.getPaymentDetails().getCanceledDate());
    }
}

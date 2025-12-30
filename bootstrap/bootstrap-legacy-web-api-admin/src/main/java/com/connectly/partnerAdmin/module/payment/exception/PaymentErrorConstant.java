package com.connectly.partnerAdmin.module.payment.exception;

public class PaymentErrorConstant {

    public static final String PAYMENT_NOT_FOUND_MSG = "Payment Not Found.";



    public static String buildRefundFailMessage(long paymentId, String message) {
        return String.format("Payment Id %s, Fail Reason ::: %s ", paymentId, message);
    }

}

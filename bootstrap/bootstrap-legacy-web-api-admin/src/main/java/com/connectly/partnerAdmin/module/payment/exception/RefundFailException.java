package com.connectly.partnerAdmin.module.payment.exception;

import com.connectly.partnerAdmin.module.payment.enums.PaymentErrorCode;

public class RefundFailException extends PaymentException {

    public RefundFailException(long paymentId, String message) {
        super(PaymentErrorCode.INVALID_PAYMENT_REFUND_INFO.getCode(), PaymentErrorCode.INVALID_PAYMENT_REFUND_INFO.getHttpStatus(), PaymentErrorConstant.buildRefundFailMessage(paymentId, message));
    }

}


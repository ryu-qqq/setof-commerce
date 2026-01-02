package com.connectly.partnerAdmin.module.payment.exception;

import com.connectly.partnerAdmin.module.payment.enums.PaymentErrorCode;

public class PaymentNotFoundException extends PaymentException {

    public PaymentNotFoundException() {
        super(PaymentErrorCode.PAYMENT_NOT_FOUND.getCode(), PaymentErrorCode.PAYMENT_NOT_FOUND.getHttpStatus(),  PaymentErrorConstant.PAYMENT_NOT_FOUND_MSG);
    }
}

package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 결제 도메인 예외. */
public class PaymentException extends DomainException {

    public PaymentException(PaymentErrorCode errorCode) {
        super(errorCode);
    }

    public PaymentException(PaymentErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public PaymentException(PaymentErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}

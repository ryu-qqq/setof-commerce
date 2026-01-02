package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * InvalidPaymentIdException - 잘못된 PaymentId 예외
 *
 * <p>PaymentId가 null이거나 잘못된 형식인 경우 발생합니다.
 */
public class InvalidPaymentIdException extends DomainException {

    public InvalidPaymentIdException(String message) {
        super(PaymentErrorCode.INVALID_PAYMENT_ID, message);
    }
}

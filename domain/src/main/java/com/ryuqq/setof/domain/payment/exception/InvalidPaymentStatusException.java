package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.payment.vo.PaymentStatus;

/**
 * 유효하지 않은 결제 상태 전이 예외.
 *
 * @since 1.1.0
 */
public class InvalidPaymentStatusException extends PaymentException {

    private static final PaymentErrorCode ERROR_CODE = PaymentErrorCode.INVALID_PAYMENT_STATUS;

    public InvalidPaymentStatusException(PaymentStatus from, PaymentStatus to) {
        super(
                ERROR_CODE,
                String.format(
                        "%s(%s) 상태에서 %s(%s) 상태로 전이할 수 없습니다",
                        from.name(), from.getDescription(), to.name(), to.getDescription()));
    }
}

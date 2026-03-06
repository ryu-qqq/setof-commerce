package com.ryuqq.setof.domain.payment.exception;

/**
 * 결제 정보를 찾을 수 없는 경우 예외.
 *
 * @since 1.1.0
 */
public class PaymentNotFoundException extends PaymentException {

    private static final PaymentErrorCode ERROR_CODE = PaymentErrorCode.PAYMENT_NOT_FOUND;

    public PaymentNotFoundException() {
        super(ERROR_CODE);
    }

    public PaymentNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 결제 정보를 찾을 수 없습니다", id));
    }
}

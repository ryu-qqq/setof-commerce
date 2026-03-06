package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.vo.Money;

/**
 * 결제 금액 불일치 예외.
 *
 * @since 1.1.0
 */
public class PaymentAmountMismatchException extends PaymentException {

    private static final PaymentErrorCode ERROR_CODE = PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH;

    public PaymentAmountMismatchException(Money expected, Money actual) {
        super(
                ERROR_CODE,
                String.format(
                        "결제 금액이 일치하지 않습니다. 예상: %d원, 실제: %d원", expected.value(), actual.value()));
    }
}

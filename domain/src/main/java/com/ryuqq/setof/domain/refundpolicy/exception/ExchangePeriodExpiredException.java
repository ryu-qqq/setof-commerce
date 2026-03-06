package com.ryuqq.setof.domain.refundpolicy.exception;

/**
 * 교환 가능 기간 만료 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ExchangePeriodExpiredException extends RefundPolicyException {

    private static final RefundPolicyErrorCode ERROR_CODE =
            RefundPolicyErrorCode.EXCHANGE_PERIOD_EXPIRED;

    public ExchangePeriodExpiredException() {
        super(ERROR_CODE);
    }

    public ExchangePeriodExpiredException(String detail) {
        super(ERROR_CODE, String.format("교환 가능 기간이 만료되었습니다: %s", detail));
    }
}

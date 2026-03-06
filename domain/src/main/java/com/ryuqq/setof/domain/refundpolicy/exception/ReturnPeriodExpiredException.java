package com.ryuqq.setof.domain.refundpolicy.exception;

/**
 * 반품 가능 기간 만료 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReturnPeriodExpiredException extends RefundPolicyException {

    private static final RefundPolicyErrorCode ERROR_CODE =
            RefundPolicyErrorCode.RETURN_PERIOD_EXPIRED;

    public ReturnPeriodExpiredException() {
        super(ERROR_CODE);
    }

    public ReturnPeriodExpiredException(String detail) {
        super(ERROR_CODE, String.format("반품 가능 기간이 만료되었습니다: %s", detail));
    }
}

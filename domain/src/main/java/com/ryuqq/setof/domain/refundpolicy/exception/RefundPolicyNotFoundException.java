package com.ryuqq.setof.domain.refundpolicy.exception;

/**
 * 환불 정책을 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class RefundPolicyNotFoundException extends RefundPolicyException {

    private static final RefundPolicyErrorCode ERROR_CODE =
            RefundPolicyErrorCode.REFUND_POLICY_NOT_FOUND;

    public RefundPolicyNotFoundException() {
        super(ERROR_CODE);
    }

    public RefundPolicyNotFoundException(String detail) {
        super(ERROR_CODE, String.format("환불 정책을 찾을 수 없습니다: %s", detail));
    }
}

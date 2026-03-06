package com.ryuqq.setof.domain.refundpolicy.exception;

/**
 * 비활성화된 환불 정책 접근 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class RefundPolicyInactiveException extends RefundPolicyException {

    private static final RefundPolicyErrorCode ERROR_CODE =
            RefundPolicyErrorCode.REFUND_POLICY_INACTIVE;

    public RefundPolicyInactiveException() {
        super(ERROR_CODE);
    }

    public RefundPolicyInactiveException(String detail) {
        super(ERROR_CODE, String.format("비활성화된 환불 정책입니다: %s", detail));
    }
}

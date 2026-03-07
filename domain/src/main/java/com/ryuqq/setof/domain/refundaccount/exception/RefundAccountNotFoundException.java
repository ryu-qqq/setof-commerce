package com.ryuqq.setof.domain.refundaccount.exception;

/**
 * 환불 계좌를 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class RefundAccountNotFoundException extends RefundAccountException {

    private static final RefundAccountErrorCode ERROR_CODE =
            RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND;

    public RefundAccountNotFoundException() {
        super(ERROR_CODE);
    }

    public RefundAccountNotFoundException(Long refundAccountId) {
        super(ERROR_CODE, String.format("ID가 %d인 환불 계좌를 찾을 수 없습니다", refundAccountId));
    }
}

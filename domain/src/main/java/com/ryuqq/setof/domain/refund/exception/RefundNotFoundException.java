package com.ryuqq.setof.domain.refund.exception;

/**
 * 반품을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 반품이 존재하지 않을 때 발생합니다.
 */
public class RefundNotFoundException extends RefundException {

    private static final RefundErrorCode ERROR_CODE = RefundErrorCode.REFUND_NOT_FOUND;

    public RefundNotFoundException() {
        super(ERROR_CODE);
    }

    public RefundNotFoundException(Long refundId) {
        super(ERROR_CODE, String.format("ID가 %d인 반품을 찾을 수 없습니다", refundId));
    }
}

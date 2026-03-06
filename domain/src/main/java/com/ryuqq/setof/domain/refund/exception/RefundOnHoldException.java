package com.ryuqq.setof.domain.refund.exception;

/**
 * 보류 중인 반품 예외.
 *
 * <p>보류 중인 반품에 대해 상태 전이를 시도할 때 발생합니다.
 */
public class RefundOnHoldException extends RefundException {

    private static final RefundErrorCode ERROR_CODE = RefundErrorCode.REFUND_ON_HOLD;

    public RefundOnHoldException() {
        super(ERROR_CODE);
    }

    public RefundOnHoldException(Long refundId) {
        super(ERROR_CODE, String.format("ID가 %d인 반품이 보류 중입니다", refundId));
    }
}

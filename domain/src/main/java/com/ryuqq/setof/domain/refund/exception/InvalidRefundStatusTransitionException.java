package com.ryuqq.setof.domain.refund.exception;

import com.ryuqq.setof.domain.refund.vo.RefundStatus;

/**
 * 유효하지 않은 반품 상태 전이 예외.
 *
 * <p>허용되지 않는 반품 상태 전이를 시도할 때 발생합니다.
 */
public class InvalidRefundStatusTransitionException extends RefundException {

    private static final RefundErrorCode ERROR_CODE =
            RefundErrorCode.INVALID_REFUND_STATUS_TRANSITION;

    public InvalidRefundStatusTransitionException() {
        super(ERROR_CODE);
    }

    public InvalidRefundStatusTransitionException(RefundStatus current, RefundStatus target) {
        super(ERROR_CODE, String.format("반품 상태를 %s에서 %s(으)로 전이할 수 없습니다", current, target));
    }
}

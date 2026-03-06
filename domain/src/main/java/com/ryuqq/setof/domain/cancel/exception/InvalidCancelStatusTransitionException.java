package com.ryuqq.setof.domain.cancel.exception;

import com.ryuqq.setof.domain.cancel.vo.CancelStatus;

/**
 * 유효하지 않은 취소 상태 전이 예외.
 *
 * <p>허용되지 않는 취소 상태 전이를 시도할 때 발생합니다.
 */
public class InvalidCancelStatusTransitionException extends CancelException {

    private static final CancelErrorCode ERROR_CODE =
            CancelErrorCode.INVALID_CANCEL_STATUS_TRANSITION;

    public InvalidCancelStatusTransitionException() {
        super(ERROR_CODE);
    }

    public InvalidCancelStatusTransitionException(CancelStatus from, CancelStatus to) {
        super(ERROR_CODE, String.format("취소 상태를 %s에서 %s(으)로 전이할 수 없습니다", from, to));
    }
}

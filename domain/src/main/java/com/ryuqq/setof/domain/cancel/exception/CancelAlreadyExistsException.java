package com.ryuqq.setof.domain.cancel.exception;

/**
 * 취소가 이미 존재하는 경우 예외.
 *
 * <p>해당 주문 아이템에 대한 취소가 이미 존재할 때 발생합니다.
 */
public class CancelAlreadyExistsException extends CancelException {

    private static final CancelErrorCode ERROR_CODE = CancelErrorCode.CANCEL_ALREADY_EXISTS;

    public CancelAlreadyExistsException() {
        super(ERROR_CODE);
    }

    public CancelAlreadyExistsException(Long orderItemId) {
        super(ERROR_CODE, String.format("주문 아이템 ID %d에 대한 취소가 이미 존재합니다", orderItemId));
    }
}

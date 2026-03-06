package com.ryuqq.setof.domain.refund.exception;

/**
 * 반품이 이미 존재하는 경우 예외.
 *
 * <p>해당 주문 아이템에 대한 반품이 이미 존재할 때 발생합니다.
 */
public class RefundAlreadyExistsException extends RefundException {

    private static final RefundErrorCode ERROR_CODE = RefundErrorCode.REFUND_ALREADY_EXISTS;

    public RefundAlreadyExistsException() {
        super(ERROR_CODE);
    }

    public RefundAlreadyExistsException(Long orderItemId) {
        super(ERROR_CODE, String.format("주문 아이템 ID %d에 대한 반품이 이미 존재합니다", orderItemId));
    }
}

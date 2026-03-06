package com.ryuqq.setof.domain.cancel.exception;

/**
 * 취소를 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 취소가 존재하지 않을 때 발생합니다.
 */
public class CancelNotFoundException extends CancelException {

    private static final CancelErrorCode ERROR_CODE = CancelErrorCode.CANCEL_NOT_FOUND;

    public CancelNotFoundException() {
        super(ERROR_CODE);
    }

    public CancelNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 취소를 찾을 수 없습니다", id));
    }
}

package com.ryuqq.setof.domain.discount.exception;

/**
 * 잘못된 할인 설정 예외.
 *
 * <p>할인 방식(RATE/FIXED)과 설정 값이 일치하지 않을 때 발생합니다.
 */
public class InvalidDiscountConfigException extends DiscountException {

    private static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.INVALID_DISCOUNT_CONFIG;

    public InvalidDiscountConfigException() {
        super(ERROR_CODE);
    }

    public InvalidDiscountConfigException(String detail) {
        super(ERROR_CODE, detail);
    }
}

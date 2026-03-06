package com.ryuqq.setof.domain.discount.exception;

/**
 * 쿠폰을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 쿠폰이 존재하지 않을 때 발생합니다.
 */
public class CouponNotFoundException extends DiscountException {

    private static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.COUPON_NOT_FOUND;

    public CouponNotFoundException() {
        super(ERROR_CODE);
    }

    public CouponNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 쿠폰을 찾을 수 없습니다", id));
    }
}

package com.ryuqq.setof.domain.discount.exception;

/**
 * 만료된 쿠폰 사용 예외.
 *
 * <p>만료된 쿠폰을 사용하려 할 때 발생합니다.
 */
public class CouponExpiredException extends DiscountException {

    private static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.COUPON_EXPIRED;

    public CouponExpiredException() {
        super(ERROR_CODE);
    }

    public CouponExpiredException(Long issuedCouponId) {
        super(ERROR_CODE, String.format("발급된 쿠폰 %d이 만료되었습니다", issuedCouponId));
    }
}

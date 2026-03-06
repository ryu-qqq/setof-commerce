package com.ryuqq.setof.domain.discount.exception;

/**
 * 쿠폰 중복 발급 예외.
 *
 * <p>동일 유저에게 동일 쿠폰이 이미 발급되었을 때 발생합니다.
 */
public class CouponAlreadyIssuedException extends DiscountException {

    private static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.COUPON_ALREADY_ISSUED;

    public CouponAlreadyIssuedException() {
        super(ERROR_CODE);
    }

    public CouponAlreadyIssuedException(Long couponId, long userId) {
        super(ERROR_CODE, String.format("쿠폰 %d은 유저 %d에게 이미 발급되었습니다", couponId, userId));
    }
}

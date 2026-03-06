package com.ryuqq.setof.domain.discount.exception;

/**
 * 쿠폰 발급 한도 초과 예외.
 *
 * <p>총 발급 수량 또는 인당 발급 수량 한도를 초과했을 때 발생합니다.
 */
public class CouponIssuanceLimitExceededException extends DiscountException {

    private static final DiscountErrorCode ERROR_CODE =
            DiscountErrorCode.COUPON_ISSUANCE_LIMIT_EXCEEDED;

    public CouponIssuanceLimitExceededException() {
        super(ERROR_CODE);
    }

    public CouponIssuanceLimitExceededException(Long couponId) {
        super(ERROR_CODE, String.format("쿠폰 %d의 발급 한도를 초과했습니다", couponId));
    }
}

package com.ryuqq.setof.domain.shippingpolicy.exception;

/**
 * 배송 정책을 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ShippingPolicyNotFoundException extends ShippingPolicyException {

    private static final ShippingPolicyErrorCode ERROR_CODE =
            ShippingPolicyErrorCode.SHIPPING_POLICY_NOT_FOUND;

    public ShippingPolicyNotFoundException() {
        super(ERROR_CODE);
    }

    public ShippingPolicyNotFoundException(String detail) {
        super(ERROR_CODE, String.format("배송 정책을 찾을 수 없습니다: %s", detail));
    }
}

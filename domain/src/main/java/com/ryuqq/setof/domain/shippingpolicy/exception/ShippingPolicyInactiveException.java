package com.ryuqq.setof.domain.shippingpolicy.exception;

/**
 * 비활성화된 배송 정책 접근 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ShippingPolicyInactiveException extends ShippingPolicyException {

    private static final ShippingPolicyErrorCode ERROR_CODE =
            ShippingPolicyErrorCode.SHIPPING_POLICY_INACTIVE;

    public ShippingPolicyInactiveException() {
        super(ERROR_CODE);
    }

    public ShippingPolicyInactiveException(String detail) {
        super(ERROR_CODE, String.format("비활성화된 배송 정책입니다: %s", detail));
    }
}

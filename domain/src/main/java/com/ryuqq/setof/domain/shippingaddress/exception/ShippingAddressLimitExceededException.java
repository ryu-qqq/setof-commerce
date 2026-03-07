package com.ryuqq.setof.domain.shippingaddress.exception;

/**
 * 배송지 개수 제한 초과 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ShippingAddressLimitExceededException extends ShippingAddressException {

    private static final ShippingAddressErrorCode ERROR_CODE =
            ShippingAddressErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED;

    public ShippingAddressLimitExceededException() {
        super(ERROR_CODE);
    }
}

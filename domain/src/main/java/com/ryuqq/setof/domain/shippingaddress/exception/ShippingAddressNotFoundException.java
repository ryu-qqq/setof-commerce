package com.ryuqq.setof.domain.shippingaddress.exception;

/**
 * 배송지를 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ShippingAddressNotFoundException extends ShippingAddressException {

    private static final ShippingAddressErrorCode ERROR_CODE =
            ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND;

    public ShippingAddressNotFoundException() {
        super(ERROR_CODE);
    }

    public ShippingAddressNotFoundException(Long shippingAddressId) {
        super(ERROR_CODE, String.format("ID가 %d인 배송지를 찾을 수 없습니다", shippingAddressId));
    }
}

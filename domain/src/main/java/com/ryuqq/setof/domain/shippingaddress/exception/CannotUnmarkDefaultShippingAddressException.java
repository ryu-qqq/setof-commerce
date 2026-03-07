package com.ryuqq.setof.domain.shippingaddress.exception;

/**
 * 기본 배송지 설정을 해제할 수 없는 경우 예외.
 *
 * <p>기본 배송지는 다른 배송지를 기본으로 설정해야만 해제됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class CannotUnmarkDefaultShippingAddressException extends ShippingAddressException {

    private static final ShippingAddressErrorCode ERROR_CODE =
            ShippingAddressErrorCode.CANNOT_UNMARK_DEFAULT_SHIPPING_ADDRESS;

    public CannotUnmarkDefaultShippingAddressException() {
        super(ERROR_CODE);
    }
}

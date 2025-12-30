package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * InvalidShippingAddressException - 잘못된 배송지 정보 예외
 *
 * <p>배송지 정보가 유효하지 않은 경우 발생합니다.
 */
public class InvalidShippingAddressException extends DomainException {

    public InvalidShippingAddressException(String message) {
        super(CheckoutErrorCode.INVALID_SHIPPING_ADDRESS, message);
    }
}

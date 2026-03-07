package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 배송지 도메인 예외. */
public class ShippingAddressException extends DomainException {

    public ShippingAddressException(ShippingAddressErrorCode errorCode) {
        super(errorCode);
    }

    public ShippingAddressException(ShippingAddressErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ShippingAddressException(ShippingAddressErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}

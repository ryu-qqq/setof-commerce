package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 배송 정책 도메인 예외. */
public class ShippingPolicyException extends DomainException {

    public ShippingPolicyException(ShippingPolicyErrorCode errorCode) {
        super(errorCode);
    }

    public ShippingPolicyException(ShippingPolicyErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ShippingPolicyException(ShippingPolicyErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}

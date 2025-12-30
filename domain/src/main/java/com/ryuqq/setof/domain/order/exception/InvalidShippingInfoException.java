package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** InvalidShippingInfoException - 잘못된 배송 정보 예외 */
public class InvalidShippingInfoException extends DomainException {

    public InvalidShippingInfoException(String message) {
        super(OrderErrorCode.INVALID_SHIPPING_INFO, message);
    }
}

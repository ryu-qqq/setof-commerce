package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** InvalidOrderIdException - 잘못된 OrderId 예외 */
public class InvalidOrderIdException extends DomainException {

    public InvalidOrderIdException(String message) {
        super(OrderErrorCode.INVALID_ORDER_ID, message);
    }
}

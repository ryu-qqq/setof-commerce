package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** InvalidOrderNumberException - 잘못된 OrderNumber 예외 */
public class InvalidOrderNumberException extends DomainException {

    public InvalidOrderNumberException(String message) {
        super(OrderErrorCode.INVALID_ORDER_NUMBER, message);
    }
}

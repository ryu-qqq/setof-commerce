package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** InvalidOrderItemIdException - 잘못된 OrderItemId 예외 */
public class InvalidOrderItemIdException extends DomainException {

    public InvalidOrderItemIdException(String message) {
        super(OrderErrorCode.INVALID_ORDER_ITEM_ID, message);
    }
}

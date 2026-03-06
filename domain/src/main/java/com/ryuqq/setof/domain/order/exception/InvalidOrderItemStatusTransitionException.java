package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.order.vo.OrderItemStatus;

/**
 * 유효하지 않은 주문 아이템 상태 전이 예외.
 *
 * <p>허용되지 않는 주문 아이템 상태 전이를 시도할 때 발생합니다.
 */
public class InvalidOrderItemStatusTransitionException extends OrderException {

    private static final OrderErrorCode ERROR_CODE =
            OrderErrorCode.INVALID_ORDER_ITEM_STATUS_TRANSITION;

    public InvalidOrderItemStatusTransitionException() {
        super(ERROR_CODE);
    }

    public InvalidOrderItemStatusTransitionException(OrderItemStatus from, OrderItemStatus to) {
        super(ERROR_CODE, String.format("%s에서 %s로의 주문 아이템 상태 전이는 허용되지 않습니다", from, to));
    }
}

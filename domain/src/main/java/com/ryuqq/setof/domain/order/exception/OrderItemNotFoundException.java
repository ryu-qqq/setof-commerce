package com.ryuqq.setof.domain.order.exception;

/**
 * 주문 아이템을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 주문 아이템이 존재하지 않을 때 발생합니다.
 */
public class OrderItemNotFoundException extends OrderException {

    private static final OrderErrorCode ERROR_CODE = OrderErrorCode.ORDER_ITEM_NOT_FOUND;

    public OrderItemNotFoundException() {
        super(ERROR_CODE);
    }

    public OrderItemNotFoundException(Long orderItemId) {
        super(ERROR_CODE, String.format("ID가 %d인 주문 아이템을 찾을 수 없습니다", orderItemId));
    }
}

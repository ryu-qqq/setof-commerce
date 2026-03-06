package com.ryuqq.setof.domain.order.exception;

/**
 * 주문을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 주문이 존재하지 않을 때 발생합니다.
 */
public class OrderNotFoundException extends OrderException {

    private static final OrderErrorCode ERROR_CODE = OrderErrorCode.ORDER_NOT_FOUND;

    public OrderNotFoundException() {
        super(ERROR_CODE);
    }

    public OrderNotFoundException(Long orderId) {
        super(ERROR_CODE, String.format("ID가 %d인 주문을 찾을 수 없습니다", orderId));
    }
}

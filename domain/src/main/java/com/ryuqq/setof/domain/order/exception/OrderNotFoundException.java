package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.order.vo.OrderId;
import java.util.Map;
import java.util.UUID;

/** OrderNotFoundException - 주문 미존재 예외 */
public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(OrderId orderId) {
        super(
                OrderErrorCode.ORDER_NOT_FOUND,
                String.format("주문을 찾을 수 없습니다: %s", orderId.value()),
                Map.of("orderId", orderId.value().toString()));
    }

    public OrderNotFoundException(UUID orderId) {
        super(
                OrderErrorCode.ORDER_NOT_FOUND,
                String.format("주문을 찾을 수 없습니다: %s", orderId),
                Map.of("orderId", orderId.toString()));
    }

    public OrderNotFoundException(String identifier) {
        super(
                OrderErrorCode.ORDER_NOT_FOUND,
                String.format("주문을 찾을 수 없습니다: %s", identifier),
                Map.of("identifier", identifier));
    }
}

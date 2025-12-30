package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import java.util.Map;

/** OrderStatusException - 주문 상태 예외 */
public class OrderStatusException extends DomainException {

    public static OrderStatusException alreadyConfirmed(OrderId orderId) {
        return new OrderStatusException(
                OrderErrorCode.ORDER_ALREADY_CONFIRMED, orderId, OrderStatus.CONFIRMED);
    }

    public static OrderStatusException alreadyCancelled(OrderId orderId) {
        return new OrderStatusException(
                OrderErrorCode.ORDER_ALREADY_CANCELLED, orderId, OrderStatus.CANCELLED);
    }

    public static OrderStatusException alreadyCompleted(OrderId orderId) {
        return new OrderStatusException(
                OrderErrorCode.ORDER_ALREADY_COMPLETED, orderId, OrderStatus.COMPLETED);
    }

    public static OrderStatusException notConfirmable(OrderId orderId, OrderStatus currentStatus) {
        return new OrderStatusException(
                OrderErrorCode.ORDER_NOT_CONFIRMABLE, orderId, currentStatus);
    }

    public static OrderStatusException notCancellable(OrderId orderId, OrderStatus currentStatus) {
        return new OrderStatusException(
                OrderErrorCode.ORDER_NOT_CANCELLABLE, orderId, currentStatus);
    }

    public static OrderStatusException notShippable(OrderId orderId, OrderStatus currentStatus) {
        return new OrderStatusException(OrderErrorCode.ORDER_NOT_SHIPPABLE, orderId, currentStatus);
    }

    private OrderStatusException(OrderErrorCode errorCode, OrderId orderId, OrderStatus status) {
        super(
                errorCode,
                String.format("주문 상태 오류 - orderId: %s, status: %s", orderId.value(), status),
                Map.of(
                        "orderId", orderId.value().toString(),
                        "status", status.name()));
    }
}

package com.connectly.partnerAdmin.module.order.exception;

import com.connectly.partnerAdmin.module.order.enums.OrderErrorCode;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

public class InvalidOrderStatusException extends OrderException {

    public InvalidOrderStatusException(long orderId, OrderStatus pastOrderStatus, OrderStatus toOrderStatus) {
        super(OrderErrorCode.INVALID_ORDER_STATUS.getCode(), OrderErrorCode.INVALID_ORDER_STATUS.getHttpStatus(), OrderErrorConstant.buildMessage(orderId, pastOrderStatus, toOrderStatus));
    }
}

package com.connectly.partnerAdmin.module.order.exception;

import com.connectly.partnerAdmin.module.order.enums.OrderErrorCode;

public class OrderNotFoundException extends OrderException{

    public OrderNotFoundException(long orderId) {
        super(OrderErrorCode.ORDER_NOT_FOUND.getCode(), OrderErrorCode.ORDER_NOT_FOUND.getHttpStatus(), OrderErrorConstant.ORDER_NOT_FOUND_MSG + orderId);
    }

    public OrderNotFoundException(String orderIds) {
        super(OrderErrorCode.ORDER_NOT_FOUND.getCode(), OrderErrorCode.ORDER_NOT_FOUND.getHttpStatus(), OrderErrorConstant.ORDER_NOT_FOUND_MSG + orderIds);
    }

}

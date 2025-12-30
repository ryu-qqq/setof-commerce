package com.connectly.partnerAdmin.module.order.exception;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

public class OrderErrorConstant {
    protected static final String TITLE = "Oops! The request information is incorrect";

    private static final String INVALID_ORDER_STATUS_MSG = "Cannot change the order status for this order.";

    public static final String ORDER_NOT_FOUND_MSG = "ORDER Not Found.";

    protected static String buildMessage(long orderId, OrderStatus pastOrderStatus, OrderStatus toOrderStatus) {
        return String.format("%s Order ID ::: %d Current Order Status ::: %s Unchangeable Status ::: %s", INVALID_ORDER_STATUS_MSG, orderId, pastOrderStatus.getDisplayName(), toOrderStatus.getDisplayName());
    }

}
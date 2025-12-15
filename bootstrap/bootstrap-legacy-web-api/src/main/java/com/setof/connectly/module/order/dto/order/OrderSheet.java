package com.setof.connectly.module.order.dto.order;

import com.setof.connectly.module.order.enums.OrderStatus;

public interface OrderSheet {
    OrderStatus getOrderStatus();

    long getProductId();

    long getProductGroupId();

    Long getPaymentId();

    long getSellerId();

    long getOrderAmount();

    int getQuantity();
}

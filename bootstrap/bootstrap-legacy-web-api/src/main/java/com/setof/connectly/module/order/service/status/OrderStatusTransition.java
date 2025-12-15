package com.setof.connectly.module.order.service.status;

import com.setof.connectly.module.order.enums.OrderStatus;

public interface OrderStatusTransition {
    OrderStatus getHandledStatus();

    boolean canTransition(OrderStatus toStatus);
}

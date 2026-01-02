package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

public interface OrderStatusTransition {
    OrderStatus getHandledStatus();
    boolean canTransition(OrderStatus toStatus);
}

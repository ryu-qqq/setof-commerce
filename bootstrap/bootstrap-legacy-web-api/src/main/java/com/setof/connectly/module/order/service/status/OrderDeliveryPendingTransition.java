package com.setof.connectly.module.order.service.status;

import com.setof.connectly.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveryPendingTransition implements OrderStatusTransition {
    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.DELIVERY_PENDING;
    }

    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isOrderCompleted() || toStatus.isOrderCancelRecant();
    }
}

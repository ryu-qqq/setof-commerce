package com.setof.connectly.module.order.service.status;

import com.setof.connectly.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingTransition implements OrderStatusTransition {

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.ORDER_PROCESSING;
    }

    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isOrderProcessing();
    }
}

package com.setof.connectly.module.order.service.status;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusProcessor extends AbstractProvider<OrderStatus, OrderStatusTransition> {

    public OrderStatusProcessor(List<OrderStatusTransition> services) {
        for (OrderStatusTransition service : services) {
            map.put(service.getHandledStatus(), service);
        }
    }
}

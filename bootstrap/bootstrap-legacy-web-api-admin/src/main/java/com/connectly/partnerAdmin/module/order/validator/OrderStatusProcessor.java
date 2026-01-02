package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderStatusProcessor extends AbstractProvider<OrderStatus, OrderStatusTransition> {

    public OrderStatusProcessor(List<OrderStatusTransition> services) {
        for (OrderStatusTransition service : services) {
            map.put(service.getHandledStatus(), service);
        }
    }

}

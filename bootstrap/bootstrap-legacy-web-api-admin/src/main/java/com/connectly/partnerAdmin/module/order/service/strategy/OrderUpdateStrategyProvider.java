package com.connectly.partnerAdmin.module.order.service.strategy;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderUpdateStrategyProvider<T extends UpdateOrder>  extends AbstractProvider<OrderStatus, OrderUpdateStrategy<T>> {

    public OrderUpdateStrategyProvider(List<OrderUpdateStrategy<T>> services) {
        for (OrderUpdateStrategy<T> service : services) {
            map.put(service.getOrderStatus(), service);
        }
    }

}

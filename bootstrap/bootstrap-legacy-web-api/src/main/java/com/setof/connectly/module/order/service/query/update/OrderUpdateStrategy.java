package com.setof.connectly.module.order.service.query.update;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.order.dto.query.UpdateOrder;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderUpdateStrategy<T extends UpdateOrder>
        extends AbstractProvider<OrderStatus, OrderUpdateService<T>> {

    public OrderUpdateStrategy(List<OrderUpdateService<T>> services) {
        for (OrderUpdateService<T> service : services) {
            map.put(service.getOrderStatus(), service);
        }
    }
}

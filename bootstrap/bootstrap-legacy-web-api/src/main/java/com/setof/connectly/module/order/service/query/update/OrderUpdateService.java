package com.setof.connectly.module.order.service.query.update;

import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.UpdateOrder;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.util.List;

public interface OrderUpdateService<T extends UpdateOrder> {
    OrderStatus getOrderStatus();

    List<UpdateOrderResponse> updateOrder(T t);
}

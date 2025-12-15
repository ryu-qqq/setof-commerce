package com.setof.connectly.module.order.service.query.update;

import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.UpdateOrder;
import java.util.List;

public interface OrderQueryService<T extends UpdateOrder> {
    List<UpdateOrderResponse> updateOrder(T t);
}

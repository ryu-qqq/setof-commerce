package com.connectly.partnerAdmin.module.order.service.strategy;

import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

import java.util.List;

public interface OrderUpdateStrategy<T extends UpdateOrder>{

    OrderStatus getOrderStatus();
    UpdateOrderResponse updateOrder(T t);
    List<UpdateOrderResponse> updateOrders(List<T> tList);

}

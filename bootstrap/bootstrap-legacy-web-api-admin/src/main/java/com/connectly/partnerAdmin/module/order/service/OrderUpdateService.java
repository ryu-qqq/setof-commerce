package com.connectly.partnerAdmin.module.order.service;

import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;

import java.util.List;

public interface OrderUpdateService<T extends UpdateOrder> {

    UpdateOrderResponse updateOrder(T dto);

    UpdateOrderResponse updateOrderExternalMall(T dto);

    List<UpdateOrderResponse> updateOrders(List<T> dto);
}

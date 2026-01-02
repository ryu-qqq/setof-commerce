package com.connectly.partnerAdmin.module.external.service.order.oco;

import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

public interface OcoOrderUpdateService<T extends UpdateOrder> {
    OrderStatus getOrderStatus();
    void updateOrder(ExMallOrderUpdate<T> exMallOrderUpdate);

}

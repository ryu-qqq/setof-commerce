package com.connectly.partnerAdmin.module.external.service.order.sewon;

import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

public interface SellicOrderUpdateService<T extends UpdateOrder> {

    OrderStatus getOrderStatus();
    void updateOrder(ExMallOrderUpdate<T> exMallOrderUpdate);

}

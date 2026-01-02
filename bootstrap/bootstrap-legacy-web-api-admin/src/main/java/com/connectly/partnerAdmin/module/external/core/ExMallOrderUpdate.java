package com.connectly.partnerAdmin.module.external.core;

import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

public interface ExMallOrderUpdate<T extends UpdateOrder> {

    long getOrderId();
    long getExMallOrderId();
    SiteName getSiteName();
    T getUpdateOrder();
    void setUpdateOrder(T t);

}

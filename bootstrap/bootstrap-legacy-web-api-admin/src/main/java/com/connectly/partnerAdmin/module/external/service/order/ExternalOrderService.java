package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

import java.util.List;

public interface ExternalOrderService<T extends ExMallOrder>{

    SiteName getSiteName();
    List<ExternalOrder> syncOrder(T t);
    void syncOrdersUpdate(ExMallOrderUpdate<? extends UpdateOrder> exMallOrderUpdate);

}

package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

public interface ExternalClaimOrderService<T extends ExMallClaimOrder> {

    SiteName getSiteName();

    UpdateOrderResponse interlockClaimOrder(T t);

}

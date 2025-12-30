package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.ExcelOrderSheet;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;

import java.util.List;

public interface ExternalOrderIssueService {

    <T extends ExMallOrder> List<ExternalOrder> syncOrders(T t);

    void syncOrdersUpdate(List<ExMallOrderUpdate<? extends UpdateOrder>> syncOrders);

    <R extends ExMallClaimOrder> UpdateOrderResponse syncClaimOrder(R r);

}

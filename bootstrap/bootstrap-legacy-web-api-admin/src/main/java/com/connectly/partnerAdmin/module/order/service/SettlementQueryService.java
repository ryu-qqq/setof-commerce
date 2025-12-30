package com.connectly.partnerAdmin.module.order.service;

import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;

public interface SettlementQueryService {
    void saveSettlement(Order order, BusinessSellerContext businessSellerContext);
}

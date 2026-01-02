package com.connectly.partnerAdmin.module.portone.service;

import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.connectly.partnerAdmin.module.seller.controller.request.CreateSellerSettlementAccount;

public interface PgPaymentService {

    void refundOrder(RefundOrder refundOrder);

    String validateAccount(CreateSellerSettlementAccount createRefundAccount);
}

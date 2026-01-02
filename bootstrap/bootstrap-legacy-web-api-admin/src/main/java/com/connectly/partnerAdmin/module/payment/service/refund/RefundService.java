package com.connectly.partnerAdmin.module.payment.service.refund;

import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodGroup;

public interface RefundService {
    void refundOrder(Order order);
    PaymentMethodGroup getPaymentMethodGroup();

}

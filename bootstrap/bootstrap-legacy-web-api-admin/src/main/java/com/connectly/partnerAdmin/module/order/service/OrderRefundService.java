package com.connectly.partnerAdmin.module.order.service;

import com.connectly.partnerAdmin.module.order.entity.Order;

public interface OrderRefundService {
    void refund(Order order);
}

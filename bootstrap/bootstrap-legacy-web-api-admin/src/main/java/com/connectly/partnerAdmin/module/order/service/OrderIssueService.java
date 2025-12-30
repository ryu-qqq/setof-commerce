package com.connectly.partnerAdmin.module.order.service;

import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.entity.Payment;

import java.util.List;

public interface OrderIssueService {

    List<Order> doOrders(Payment payment, List<? extends OrderSheet> orders);

}

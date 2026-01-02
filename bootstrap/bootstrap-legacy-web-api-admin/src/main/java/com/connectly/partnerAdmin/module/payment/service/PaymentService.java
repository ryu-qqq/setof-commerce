package com.connectly.partnerAdmin.module.payment.service;

import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;

import java.util.List;

public interface PaymentService {

    List<Order> doPay(CreatePayment payment);

}

package com.connectly.partnerAdmin.module.order.service;

import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.entity.PaymentMethod;
import com.connectly.partnerAdmin.module.payment.service.PaymentUpdateStrategy;
import com.connectly.partnerAdmin.module.payment.service.refund.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderRefundServiceImpl implements OrderRefundService{

    private final PaymentUpdateStrategy paymentUpdateStrategy;

    public void refund(Order order) {
        PaymentMethod paymentMethod = order.getPayment().getPaymentBill().getPaymentMethod();
        RefundService serviceByPayMethod = paymentUpdateStrategy.getServiceByPayMethod(paymentMethod.getPaymentMethodEnum());
        serviceByPayMethod.refundOrder(order);
    }

}

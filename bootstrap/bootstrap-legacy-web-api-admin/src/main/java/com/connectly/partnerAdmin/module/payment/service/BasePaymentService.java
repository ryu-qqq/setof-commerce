package com.connectly.partnerAdmin.module.payment.service;

import com.connectly.partnerAdmin.module.order.service.OrderIssueService;
import com.connectly.partnerAdmin.module.payment.mapper.PaymentMapper;
import com.connectly.partnerAdmin.module.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BasePaymentService extends AbstractPaymentService{

    public BasePaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, OrderIssueService orderIssueService) {
        super(paymentRepository, paymentMapper, orderIssueService);
    }

}

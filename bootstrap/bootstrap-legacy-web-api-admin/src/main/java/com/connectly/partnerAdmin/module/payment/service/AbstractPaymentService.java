package com.connectly.partnerAdmin.module.payment.service;

import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.service.OrderIssueService;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import com.connectly.partnerAdmin.module.payment.mapper.PaymentMapper;
import com.connectly.partnerAdmin.module.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public abstract class AbstractPaymentService implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderIssueService orderIssueService;

    @Override
    public List<Order> doPay(CreatePayment createPayment) {
        Payment payment = paymentMapper.toPayment(createPayment);
        Payment savedPayment = paymentRepository.save(payment);
        return orderIssueService.doOrders(savedPayment, createPayment.getOrders());
    }

}

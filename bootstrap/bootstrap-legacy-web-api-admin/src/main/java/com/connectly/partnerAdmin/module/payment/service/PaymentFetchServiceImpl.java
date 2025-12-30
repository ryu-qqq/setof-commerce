package com.connectly.partnerAdmin.module.payment.service;


import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.order.service.OrderSnapShotFetchService;
import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;
import com.connectly.partnerAdmin.module.payment.exception.PaymentNotFoundException;
import com.connectly.partnerAdmin.module.payment.repository.PaymentFetchRepository;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentFetchServiceImpl implements PaymentFetchService {

    private final PaymentFetchRepository paymentFetchRepository;
    private final OrderSnapShotFetchService orderSnapShotFetchService;

    @Override
    public PaymentResponse fetchPayment(long paymentId) {

        PaymentResponse paymentResponse = paymentFetchRepository.fetchPayment(paymentId, SecurityUtils.currentSellerIdOpt())
                .orElseThrow(PaymentNotFoundException::new);

        List<OrderProduct> orderProducts = orderSnapShotFetchService.fetchOrderSnapShotProducts(paymentId, Collections.emptyList());
        paymentResponse.setOrderProducts(orderProducts);

        return paymentResponse;
    }




}

package com.connectly.partnerAdmin.module.payment.service;

import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;

public interface PaymentFetchService {
    PaymentResponse fetchPayment(long paymentId);
}

package com.connectly.partnerAdmin.module.payment.repository;

import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;

import java.util.Optional;

public interface PaymentFetchRepository {

    Optional<PaymentResponse> fetchPayment(long paymentId, Optional<Long> sellerId);

}

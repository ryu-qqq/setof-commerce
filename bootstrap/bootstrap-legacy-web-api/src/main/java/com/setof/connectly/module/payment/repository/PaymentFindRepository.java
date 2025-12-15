package com.setof.connectly.module.payment.repository;

import com.setof.connectly.module.payment.dto.filter.PaymentFilter;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import com.setof.connectly.module.payment.entity.Payment;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface PaymentFindRepository {
    Optional<PaymentStatus> fetchPaymentStatus(long paymentId);

    Optional<Payment> fetchPaymentEntity(long paymentId);

    Optional<Payment> fetchPaymentEntity(long paymentId, long userId);

    Optional<PaymentResponse> fetchPayment(long paymentId, long userId);

    Optional<PaymentResponse> fetchPayment(long paymentId);

    List<PaymentResponse> fetchPayments(PaymentFilter filter, Pageable pageable, long userId);
}

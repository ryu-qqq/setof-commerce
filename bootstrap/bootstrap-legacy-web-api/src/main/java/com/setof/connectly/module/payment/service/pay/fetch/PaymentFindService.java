package com.setof.connectly.module.payment.service.pay.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payment.dto.filter.PaymentFilter;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import com.setof.connectly.module.payment.entity.Payment;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import org.springframework.data.domain.Pageable;

public interface PaymentFindService {

    PaymentStatus fetchPaymentStatus(long paymentId);

    Payment fetchPaymentEntity(long paymentId);

    Payment fetchPaymentEntityForWebhook(long paymentId);

    PaymentResponse fetchPayment(long paymentId);

    PaymentResponse fetchPaymentForSlack(long paymentId);

    CustomSlice<PaymentResponse> fetchPayments(PaymentFilter filter, Pageable pageable);
}

package com.setof.connectly.module.payment.repository.bill.fetch;

import com.setof.connectly.module.payment.dto.payment.FailPaymentResponse;
import com.setof.connectly.module.payment.entity.PaymentBill;
import java.util.Optional;

public interface PaymentBillFindRepository {

    Optional<PaymentBill> fetchPaymentBillEntity(long paymentId);

    Optional<PaymentBill> fetchPaymentBillEntityByUniqueId(String paymentUniqueId);

    Optional<FailPaymentResponse> fetchPaymentMethod(String paymentUniqueId, long userId);

    Optional<String> fetchPaymentAgencyId(long paymentId);
}

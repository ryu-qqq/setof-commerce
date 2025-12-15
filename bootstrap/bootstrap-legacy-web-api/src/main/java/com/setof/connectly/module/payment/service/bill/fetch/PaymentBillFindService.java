package com.setof.connectly.module.payment.service.bill.fetch;

import com.setof.connectly.module.payment.dto.payment.FailPaymentResponse;
import com.setof.connectly.module.payment.dto.payment.PaymentResult;
import com.setof.connectly.module.payment.entity.PaymentBill;

public interface PaymentBillFindService {

    PaymentBill fetchPaymentBillEntity(long paymentId);

    PaymentBill fetchPaymentBillEntityByUniqueId(String paymentUniqueId);

    FailPaymentResponse fetchPaymentMethod(String paymentUniqueId);

    PaymentResult fetchPaymentResult(long paymentId);

    String fetchPaymentAgencyId(long paymentId);
}

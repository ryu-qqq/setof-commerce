package com.setof.connectly.module.portone.service.payment;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.siot.IamportRestClient.response.Payment;

public interface PgPaymentService {

    Payment getPayment(String impUid);

    void refundOrder(String pgPaymentId, long paymentId, RefundOrderSheet refundOrderSheet);
}

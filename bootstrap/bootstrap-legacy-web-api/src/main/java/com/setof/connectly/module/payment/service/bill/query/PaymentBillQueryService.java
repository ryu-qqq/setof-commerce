package com.setof.connectly.module.payment.service.bill.query;

import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;

public interface PaymentBillQueryService {

    void savePaymentBill(PaymentBill paymentBill);

    PaymentBill updatePaymentBill(PgProviderTransDto pgProviderTransDto);
}

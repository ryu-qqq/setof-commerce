package com.connectly.partnerAdmin.module.payment.mapper;

import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.entity.Payment;

public interface PaymentMapper {
    Payment toPayment(CreatePayment payment);

}

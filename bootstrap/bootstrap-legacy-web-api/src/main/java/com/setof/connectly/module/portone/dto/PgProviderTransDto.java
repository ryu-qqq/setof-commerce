package com.setof.connectly.module.portone.dto;

import com.setof.connectly.module.payment.entity.VBankAccount;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.payment.enums.PaymentChannel;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;

public interface PgProviderTransDto {

    long getPaymentId();

    PaymentMethodEnum getPayMethod();

    PaymentChannel getPaymentChannel();

    VBankAccount getVBankAccount();

    BuyerInfo getBuyerInfo();

    PaymentStatus getPaymentStatus();

    String getReceiptUrl();

    String getPgPaymentId();

    String getPaymentUniqueId();

    long getPayAmount();

    String getCardName();

    String getCardNumber();
}

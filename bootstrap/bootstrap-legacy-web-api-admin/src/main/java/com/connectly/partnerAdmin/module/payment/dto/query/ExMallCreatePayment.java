package com.connectly.partnerAdmin.module.payment.dto.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class ExMallCreatePayment extends CreatePayment{

    private String exMallUniqueId;

    @Override
    public String getPaymentUniqueId() {
        return exMallUniqueId;
    }

}

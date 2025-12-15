package com.setof.connectly.module.payment.dto.payment;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailPaymentResponse {

    private long paymentId;
    private PaymentMethodEnum paymentMethod;
    private long payAmount;

    @QueryProjection
    public FailPaymentResponse(long paymentId, PaymentMethodEnum paymentMethod, long payAmount) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.payAmount = payAmount;
    }
}

package com.connectly.partnerAdmin.module.payment.dto.bill;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentBillDto {
    private long paymentId;
    private long userId;
    private String paymentAgencyId;
    private long paymentAmount;
    private double usedMileageAmount;
    private long orderId;

    @QueryProjection
    public PaymentBillDto(long paymentId, long userId, String paymentAgencyId, long paymentAmount, double usedMileageAmount, long orderId) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.paymentAgencyId = paymentAgencyId;
        this.paymentAmount = paymentAmount;
        this.usedMileageAmount = usedMileageAmount;
        this.orderId = orderId;
    }
}

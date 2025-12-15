package com.setof.connectly.module.payment.dto.mileage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentMileageSnapShotDto {
    private long paymentId;
    private long mileageId;
    private double changeAmount;

    @QueryProjection
    public PaymentMileageSnapShotDto(long paymentId, long mileageId, double changeAmount) {
        this.paymentId = paymentId;
        this.mileageId = mileageId;
        this.changeAmount = changeAmount;
    }
}

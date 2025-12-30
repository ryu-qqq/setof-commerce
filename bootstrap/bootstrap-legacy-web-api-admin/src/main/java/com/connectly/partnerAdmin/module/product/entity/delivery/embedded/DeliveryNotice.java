package com.connectly.partnerAdmin.module.product.entity.delivery.embedded;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DeliveryNotice  {

    @Column(name = "DELIVERY_AREA")
    private String deliveryArea;

    @Column(name = "DELIVERY_FEE")
    private long deliveryFee;

    @Column(name = "DELIVERY_PERIOD_AVERAGE")
    private int deliveryPeriodAverage;

    @Builder
    public DeliveryNotice(String deliveryArea, long deliveryFee, int deliveryPeriodAverage) {
        this.deliveryArea = deliveryArea;
        this.deliveryFee = deliveryFee;
        this.deliveryPeriodAverage = deliveryPeriodAverage;
    }

}

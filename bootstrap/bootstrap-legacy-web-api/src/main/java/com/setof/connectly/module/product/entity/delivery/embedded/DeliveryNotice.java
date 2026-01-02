package com.setof.connectly.module.product.entity.delivery.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Embeddable
public class DeliveryNotice {
    @Column(name = "delivery_area")
    private String deliveryArea;

    @Column(name = "delivery_fee")
    private long deliveryFee;

    @Column(name = "delivery_period_average")
    private int deliveryPeriodAverage;
}

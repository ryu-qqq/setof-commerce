package com.connectly.partnerAdmin.module.external.dto.order.sellic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SellicShipmentInfo {

    @JsonProperty("order_id")
    private long orderId;
    private int delivery;
    private String invoice;
}

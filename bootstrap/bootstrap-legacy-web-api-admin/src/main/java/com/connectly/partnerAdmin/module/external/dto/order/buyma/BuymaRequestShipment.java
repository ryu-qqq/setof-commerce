package com.connectly.partnerAdmin.module.external.dto.order.buyma;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuymaRequestShipment {
    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("is_domestic")
    private Boolean isDomestic;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("tracking")
    private Boolean tracking;

    @JsonProperty("delivery_period_from")
    private Integer deliveryPeriodFrom;

    @JsonProperty("delivery_period_to")
    private Integer deliveryPeriodTo;

    @JsonProperty("cash_on_delivery_cost_from")
    private Integer cashOnDeliveryCostFrom;

    @JsonProperty("cash_on_delivery_cost_to")
    private Integer cashOnDeliveryCostTo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_anonymous")
    private boolean isAnonymous;

}

package com.connectly.partnerAdmin.module.external.dto.order.buyma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuymaOrderShipment {

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("tracking")
    private Boolean tracking;

    @JsonProperty("tracking_number_input_url")
    private String trackingNumberInputUrl;

    @JsonProperty("tracking_number_url")
    private String trackingNumberUrl;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("receipt_number")
    private String receiptNumber;

    @JsonProperty("message")
    private String message;

    @JsonProperty("shipped_at")
    private String shippedAt;

    @JsonProperty("received_at")
    private String receivedAt;

}

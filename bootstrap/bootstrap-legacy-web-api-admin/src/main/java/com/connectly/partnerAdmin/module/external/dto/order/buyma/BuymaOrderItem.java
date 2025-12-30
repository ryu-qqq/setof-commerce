package com.connectly.partnerAdmin.module.external.dto.order.buyma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuymaOrderItem {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("reference_number")
    private String referenceNumber;

    @JsonProperty("name")
    private String name;

    @JsonProperty("comments")
    private String comments;

}

package com.connectly.partnerAdmin.module.external.dto.order.buyma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuymaCoupon {

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("code")
    private String code;

}

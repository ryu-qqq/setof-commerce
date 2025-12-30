package com.connectly.partnerAdmin.module.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SeWonRequestDto {

    @JsonProperty("customer_id")
    protected String customerId;

    @JsonProperty("api_key")
    protected String apiKey;

    public SeWonRequestDto(String customerId, String apiKey) {
        this.customerId = customerId;
        this.apiKey = apiKey;
    }


    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

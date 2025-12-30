package com.connectly.partnerAdmin.module.external.dto.order.sellic;

import com.connectly.partnerAdmin.module.external.dto.SeWonRequestDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellicClaimOrderRequest extends SeWonRequestDto {

    @JsonProperty("s_date")
    private LocalDateTime startDate;
    @JsonProperty("e_date")
    private LocalDateTime endDate;
    @JsonProperty("order_id")
    private long orderId;


    public SellicClaimOrderRequest(String customerId, String apiKey, LocalDateTime startDate, LocalDateTime endDate, long orderId) {
        super(customerId, apiKey);
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderId = orderId;
    }
}

package com.connectly.partnerAdmin.module.external.dto.order.oco.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcoNormalOrder extends AbstractOcoOrderUpdate{

    @JsonProperty("delivery_condition")
    private String deliveryCondition;

    public OcoNormalOrder(long otid, String deliveryCondition) {
        super(otid);
        this.deliveryCondition = deliveryCondition;
    }

    public OcoNormalOrder(String deliveryCondition) {
        this.deliveryCondition = deliveryCondition;
    }
}

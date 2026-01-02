package com.connectly.partnerAdmin.module.external.dto.order.oco.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcoShipOrder extends OcoNormalOrder {

    private String scid;

    @JsonProperty("delivery_number")
    private String deliveryNumber;


    public OcoShipOrder(int scid, String deliveryNumber) {
        this.scid = String.valueOf(scid);
        this.deliveryNumber = deliveryNumber;
    }

    public OcoShipOrder(long otid, String deliveryCondition, int scid, String deliveryNumber) {
        super(otid, deliveryCondition);
        this.scid = String.valueOf(scid);
        this.deliveryNumber = deliveryNumber;
    }

    public OcoShipOrder(String deliveryCondition, int scid, String deliveryNumber) {
        super(deliveryCondition);
        this.scid = String.valueOf(scid);
        this.deliveryNumber = deliveryNumber;
    }
}

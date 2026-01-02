package com.connectly.partnerAdmin.module.external.dto.order.oco.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcoCancelSaleOrder extends AbstractOcoOrderUpdate{

    private String oid;
    @JsonProperty("cancel_type")
    private String cancelType;


    public OcoCancelSaleOrder(long otid, long oid, String cancelType) {
        super(otid);
        this.oid = String.valueOf(oid);
        this.cancelType = cancelType;
    }

    public OcoCancelSaleOrder(long oid, String cancelType) {
        this.oid = String.valueOf(oid);
        this.cancelType = cancelType;
    }
}

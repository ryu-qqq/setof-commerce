package com.connectly.partnerAdmin.module.external.core;

import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoClaimOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicClaimOrder;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SellicClaimOrder.class, name = "sellicClaimOrder"),
        @JsonSubTypes.Type(value = OcoClaimOrder.class, name = "ocoClaimOrder"),

})
public interface ExMallClaimOrder extends ExMall {

    long getExMallOrderId();

    String getClaimReason();
    String getClaimDetailReason();

}

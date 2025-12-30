package com.connectly.partnerAdmin.module.order.dto.query;


import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ShipOrder.class, name = "shipOrder"),
        @JsonSubTypes.Type(value = ClaimOrder.class, name = "claimOrder"),
        @JsonSubTypes.Type(value = ClaimRejectedAndShipmentOrder.class, name = "claimRejectedAndShipmentOrder"),
        @JsonSubTypes.Type(value = NormalOrder.class, name = "normalOrder"),
})
public interface UpdateOrder {

    long getOrderId();
    OrderStatus getOrderStatus();
    String getChangeReason();
    String getChangeDetailReason();
    boolean isByPass();

}

package com.setof.connectly.module.order.dto.query;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.setof.connectly.module.order.enums.OrderStatus;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = NormalOrder.class, name = "normalOrder"),
    @JsonSubTypes.Type(value = RefundOrder.class, name = "refundOrder"),
    @JsonSubTypes.Type(value = ClaimOrder.class, name = "claimOrder")
})
public interface UpdateOrder {

    Long getPaymentId();

    Long getOrderId();

    OrderStatus getOrderStatus();

    String getChangeReason();

    String getChangeDetailReason();
}

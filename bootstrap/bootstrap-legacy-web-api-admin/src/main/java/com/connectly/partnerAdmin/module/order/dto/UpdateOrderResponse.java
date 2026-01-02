package com.connectly.partnerAdmin.module.order.dto;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UpdateOrderResponse {
    private long orderId;
    private long userId;
    private OrderStatus toBeOrderStatus;
    private OrderStatus asIsOrderStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String changeReason;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String changeDetailReason;

}

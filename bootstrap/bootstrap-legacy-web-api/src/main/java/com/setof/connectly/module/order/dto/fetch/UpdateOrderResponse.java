package com.setof.connectly.module.order.dto.fetch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.setof.connectly.module.order.enums.OrderStatus;
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

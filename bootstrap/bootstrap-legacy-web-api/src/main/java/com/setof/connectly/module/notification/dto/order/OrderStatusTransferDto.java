package com.setof.connectly.module.notification.dto.order;

import com.setof.connectly.module.order.enums.OrderStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderStatusTransferDto {

    private long orderId;
    private OrderStatus orderStatus;
    private String reason;
    private String detailReason;
}

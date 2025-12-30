package com.connectly.partnerAdmin.module.notification.mapper.order;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
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

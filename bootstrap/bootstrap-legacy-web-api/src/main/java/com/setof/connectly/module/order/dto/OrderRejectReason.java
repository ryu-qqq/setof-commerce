package com.setof.connectly.module.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRejectReason {
    @JsonIgnore private long orderId;
    private OrderStatus orderStatus;
    private String changeReason;
    private String changeDetailReason;
    private LocalDateTime insertDate;

    @QueryProjection
    public OrderRejectReason(
            long orderId,
            OrderStatus orderStatus,
            String changeReason,
            String changeDetailReason,
            LocalDateTime insertDate) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.changeReason = changeReason;
        this.changeDetailReason = changeDetailReason;
        this.insertDate = insertDate;
    }
}

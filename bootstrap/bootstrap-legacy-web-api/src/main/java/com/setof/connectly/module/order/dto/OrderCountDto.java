package com.setof.connectly.module.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCountDto {

    private OrderStatus orderStatus;
    private long count;

    @QueryProjection
    public OrderCountDto(OrderStatus orderStatus, Long count) {
        this.orderStatus = orderStatus;
        this.count = count;
    }

    public void setProcessingOrderCount(Long count) {
        this.count = Objects.requireNonNullElse(count, 0L);
    }
}

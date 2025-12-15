package com.setof.connectly.module.order.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAmountDto {

    private long orderId;
    private long orderAmount;
    private double realOrderAmount;

    @QueryProjection
    public OrderAmountDto(long orderId, long orderAmount) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
    }

    public void setRealOrderAmount(double realOrderAmount) {
        this.realOrderAmount = realOrderAmount;
    }
}

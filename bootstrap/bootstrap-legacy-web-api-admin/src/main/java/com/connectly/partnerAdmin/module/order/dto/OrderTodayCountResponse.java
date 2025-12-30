package com.connectly.partnerAdmin.module.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderTodayCountResponse {
    private Long orderCount;
    private Long claimCount;

    @QueryProjection
    public OrderTodayCountResponse(Long orderCount, Long claimCount) {
        this.orderCount = orderCount;
        this.claimCount = claimCount;
    }
}

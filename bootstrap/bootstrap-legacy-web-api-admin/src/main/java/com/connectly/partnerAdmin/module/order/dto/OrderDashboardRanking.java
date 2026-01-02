package com.connectly.partnerAdmin.module.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDashboardRanking {
    private long productGroupId;
    private String productGroupName;
    private long totalOrderCount;
    private long pureOrderCount;

    @QueryProjection
    public OrderDashboardRanking(long productGroupId, String productGroupName, long totalOrderCount, long pureOrderCount) {
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.totalOrderCount = totalOrderCount;
        this.pureOrderCount = pureOrderCount;
    }
}

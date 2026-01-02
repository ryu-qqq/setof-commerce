package com.connectly.partnerAdmin.module.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Builder
public class OrderStatistics {
    private String flag;
    private Long orderCount;
    private Long pureOrderCount;
    private BigDecimal orderAmount;
    private BigDecimal pureOrderAmount;

    @QueryProjection
    public OrderStatistics(String flag, Long orderCount, Long pureOrderCount, BigDecimal orderAmount, BigDecimal pureOrderAmount) {
        this.flag = flag;
        this.orderCount = orderCount;
        this.pureOrderCount = pureOrderCount;
        this.orderAmount = orderAmount;
        this.pureOrderAmount = pureOrderAmount;
    }
}

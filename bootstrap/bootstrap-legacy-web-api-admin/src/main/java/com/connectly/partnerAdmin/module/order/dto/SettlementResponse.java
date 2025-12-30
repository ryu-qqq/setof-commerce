package com.connectly.partnerAdmin.module.order.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementResponse {
    private long orderCount;
    private long ourMallOrderCount;
    private long externalMallOrderCount;
    private String settlementDay;
    private String settlementCompleteDay;
    private BigDecimal totalCurrentPrice;
    private BigDecimal totalDiscountAmount;
    private BigDecimal sellerDiscountAmount;
    private BigDecimal ourDiscountAmount;
    private BigDecimal totalMileageAmount;
    private BigDecimal sellerMileageAmount;
    private BigDecimal ourMileageAmount;
    private BigDecimal settlementAmount;
    private BigDecimal expectationSettlementAmount;
    private BigDecimal totalFee;


    @QueryProjection
    public SettlementResponse(long orderCount, long ourMallOrderCount, long externalMallOrderCount,  String settlementDay, String settlementCompleteDay, BigDecimal totalCurrentPrice, BigDecimal totalDiscountAmount, BigDecimal sellerDiscountAmount,  BigDecimal totalMileageAmount, BigDecimal sellerMileageAmount,  BigDecimal totalFee) {
        BigDecimal settlementAmount = totalCurrentPrice
                .subtract(sellerDiscountAmount)
                .subtract(sellerMileageAmount);

        this.orderCount = orderCount;
        this.ourMallOrderCount = ourMallOrderCount;
        this.externalMallOrderCount = externalMallOrderCount;
        this.settlementDay = settlementDay;
        this.settlementCompleteDay = settlementCompleteDay;
        this.totalCurrentPrice = totalCurrentPrice;
        this.totalDiscountAmount = totalDiscountAmount;
        this.sellerDiscountAmount = sellerDiscountAmount;
        this.ourDiscountAmount = totalDiscountAmount.subtract(sellerDiscountAmount);
        this.totalMileageAmount = totalMileageAmount;
        this.sellerMileageAmount = sellerMileageAmount;
        this.ourMileageAmount = totalMileageAmount.subtract(sellerMileageAmount);
        this.settlementAmount = settlementAmount;
        this.expectationSettlementAmount = settlementAmount.subtract(totalFee);
        this.totalFee = totalFee;
    }
}

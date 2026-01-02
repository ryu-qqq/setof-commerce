package com.connectly.partnerAdmin.module.discount.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountUseHistoryDto {

    @JsonIgnore
    private long discountUseHistoryId;
    private long userId;
    private String name;
    private long orderId;
    private BigDecimal orderAmount;
    private long paymentId;
    private long productGroupId;
    private BigDecimal directDiscountPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime useDate;
    private BigDecimal totalDirectDiscountPrice;

    @QueryProjection
    public DiscountUseHistoryDto(long discountUseHistoryId, long userId, String name, long orderId, BigDecimal orderAmount, long paymentId, long productGroupId, BigDecimal directDiscountPrice, LocalDateTime useDate, BigDecimal totalDirectDiscountPrice) {
        this.discountUseHistoryId = discountUseHistoryId;
        this.userId = userId;
        this.name = name;
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.paymentId = paymentId;
        this.productGroupId = productGroupId;
        this.directDiscountPrice = directDiscountPrice;
        this.useDate = useDate;
        this.totalDirectDiscountPrice = totalDirectDiscountPrice;
    }
}

package com.connectly.partnerAdmin.module.order.dto.settlement;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementInfo {

    private double commissionRate;

    private double fee;

    @Setter
    private Money expectationSettlementAmount;

    @Setter
    private Money settlementAmount;

    private double shareRatio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedSettlementDay;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementDay;


    @QueryProjection
    public SettlementInfo(double commissionRate, LocalDateTime expectedSettlementDay, double shareRatio, LocalDateTime settlementDay, BigDecimal currentPrice, BigDecimal directDiscountPrice, int qty) {
        this.commissionRate = commissionRate;
        this.expectedSettlementDay = expectedSettlementDay;
        this.shareRatio = 100 - shareRatio;
        this.settlementDay = settlementDay;
        this.fee = setFee(currentPrice, qty);
        this.expectationSettlementAmount = setNoMileageSettlementAmount(currentPrice, directDiscountPrice, qty);
        this.settlementAmount = setNoMileageSettlementAmount(currentPrice, directDiscountPrice, qty);
    }


    private double setFee(BigDecimal currentPrice, int qty){
        Money fee = Money.wons(currentPrice)
                .times(commissionRate * 0.01)
                .times(qty);
        return Money.wons(fee.toPlainStringWithoutDecimal()).toPlainStringWithoutDecimal();
    }

    private Money setNoMileageSettlementAmount(BigDecimal currentPrice, BigDecimal directDiscountPrice, int qty){
        Money sellerShareAmount = Money.wons(directDiscountPrice)
                .times((100.0 - shareRatio) / 100.0)
                .times(qty);

        Money totalCurrentPrice = Money.wons(currentPrice).times(qty);

        return totalCurrentPrice.minus(sellerShareAmount.plus(Money.wons(fee)));
    }

}

package com.setof.connectly.module.mileage.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageRefundDto extends MileageDto {

    private double usedAmount;

    @QueryProjection
    public MileageRefundDto(
            long mileageId,
            double mileageAmount,
            double usedMileageAmount,
            LocalDateTime issuedDate,
            LocalDateTime expirationDate,
            Yn activeYn,
            double usedAmount) {
        super(mileageId, mileageAmount, usedMileageAmount, issuedDate, expirationDate, activeYn);
        this.usedAmount = usedAmount;
    }

    @Override
    public void deActiveYn() {
        super.deActiveYn();
    }
}

package com.setof.connectly.module.mileage.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.MileageStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PendingMileageDto {

    private double mileageTransactionAmount;
    private MileageIssueType saveType;
    private MileageStatus status;

    @QueryProjection
    public PendingMileageDto(
            double mileageTransactionAmount, MileageIssueType saveType, MileageStatus status) {
        this.mileageTransactionAmount = mileageTransactionAmount;
        this.saveType = saveType;
        this.status = status;
    }
}

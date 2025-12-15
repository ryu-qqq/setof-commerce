package com.setof.connectly.module.mileage.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.MileageStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageTransactionDto {

    private long mileageId;
    private long targetId;
    private MileageIssueType saveType;
    private double mileageAmount;
    private MileageStatus status;

    @QueryProjection
    public MileageTransactionDto(
            long mileageId,
            long targetId,
            MileageIssueType saveType,
            double mileageAmount,
            MileageStatus status) {
        this.mileageId = mileageId;
        this.targetId = targetId;
        this.saveType = saveType;
        this.mileageAmount = mileageAmount;
        this.status = status;
    }
}

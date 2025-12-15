package com.setof.connectly.module.mileage.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMileageQueryDto {
    private long userId;
    private long mileageId;
    private double mileageAmount;
    private double usedMileageAmount;
    private Yn activeYn;
    private LocalDateTime issuedDate;
    private LocalDateTime expirationDate;

    @QueryProjection
    public UserMileageQueryDto(
            long userId,
            long mileageId,
            double mileageAmount,
            double usedMileageAmount,
            Yn activeYn,
            LocalDateTime issuedDate,
            LocalDateTime expirationDate) {
        this.userId = userId;
        this.mileageId = mileageId;
        this.mileageAmount = mileageAmount;
        this.usedMileageAmount = usedMileageAmount;
        this.activeYn = activeYn;
        this.issuedDate = issuedDate;
        this.expirationDate = expirationDate;
    }

    public double getCurrentMileage() {
        return this.mileageAmount - usedMileageAmount;
    }
}

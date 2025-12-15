package com.setof.connectly.module.user.dto.mileage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMileageDto {

    private long userId;
    private double currentMileage;
    private double expectedSaveMileage;
    private double expectedExpireMileage;

    @Builder
    @QueryProjection
    public UserMileageDto(long userId, double currentMileage) {
        this.userId = userId;
        this.currentMileage = currentMileage;
    }

    @Builder
    @QueryProjection
    public UserMileageDto(
            long userId,
            double currentMileage,
            double expectedSaveMileage,
            double expectedExpireMileage) {
        this.userId = userId;
        this.currentMileage = currentMileage;
        this.expectedSaveMileage = expectedSaveMileage;
        this.expectedExpireMileage = expectedExpireMileage;
    }
}

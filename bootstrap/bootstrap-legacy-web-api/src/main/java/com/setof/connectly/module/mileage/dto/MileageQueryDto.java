package com.setof.connectly.module.mileage.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageQueryDto {

    private double currentMileage;
    private Set<MileageDto> mileages = new HashSet<>();

    @QueryProjection
    public MileageQueryDto(double currentMileage, Set<MileageDto> mileages) {
        this.currentMileage = currentMileage;
        this.mileages = mileages;
    }
}

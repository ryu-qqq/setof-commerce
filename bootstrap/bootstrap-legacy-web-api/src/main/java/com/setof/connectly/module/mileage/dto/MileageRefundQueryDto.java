package com.setof.connectly.module.mileage.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageRefundQueryDto {

    private double currentMileage;
    private Set<MileageRefundDto> mileages = new HashSet<>();

    @QueryProjection
    public MileageRefundQueryDto(double currentMileage, Set<MileageRefundDto> mileages) {
        this.currentMileage = currentMileage;
        this.mileages = mileages;
    }
}

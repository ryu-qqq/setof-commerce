package com.setof.connectly.module.mileage.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MileageDeductionResult {

    private List<MileageDto> usedMileages; // 사용된 마일리지 목록
    private double currentUsingMileageAmount;
}

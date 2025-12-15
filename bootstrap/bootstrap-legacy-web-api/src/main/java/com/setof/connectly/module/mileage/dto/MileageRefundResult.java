package com.setof.connectly.module.mileage.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MileageRefundResult {

    private List<MileageRefundDto> refundMileages;
    private List<MileageRefundDto> expiredMileages;
    private double totalRefundMileage;
}

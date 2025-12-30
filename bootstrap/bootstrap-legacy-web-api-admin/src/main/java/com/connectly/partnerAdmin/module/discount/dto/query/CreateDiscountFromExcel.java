package com.connectly.partnerAdmin.module.discount.dto.query;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDiscountFromExcel {

    @Valid
    private CreateDiscountDetails discountDetails;
    @Size(min = 1, message = "적어도 하나 이상의 targetId가 필요합니다.")
    private List<Long> targetIds;

}

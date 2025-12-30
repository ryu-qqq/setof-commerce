package com.connectly.partnerAdmin.module.discount.service;

import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.query.CreateDiscount;
import com.connectly.partnerAdmin.module.discount.dto.query.CreateDiscountFromExcel;
import com.connectly.partnerAdmin.module.discount.dto.query.UpdateDiscount;
import com.connectly.partnerAdmin.module.discount.dto.query.UpdateUseDiscount;

import java.util.List;

public interface DiscountQueryService {

    DiscountPolicyResponseDto createDiscount(CreateDiscount createDiscount);

    List<DiscountPolicyResponseDto> createDiscountFromExcel(List<CreateDiscountFromExcel> createDiscountFromExcels);

    DiscountPolicyResponseDto updateDiscount(long discountPolicyId, UpdateDiscount updateDiscount);

    List<DiscountPolicyResponseDto> updateDiscountUseYn(UpdateUseDiscount updateUseDiscount);

}

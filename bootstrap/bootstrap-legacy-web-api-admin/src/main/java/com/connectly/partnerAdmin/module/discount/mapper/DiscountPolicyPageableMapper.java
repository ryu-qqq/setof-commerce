package com.connectly.partnerAdmin.module.discount.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscountPolicyPageableMapper {
    CustomPageable<DiscountPolicyResponseDto> toProductCategoryContext(List<DiscountPolicyResponseDto> discountPolicyResponses, Pageable pageable, long total);

}

package com.connectly.partnerAdmin.module.discount.mapper;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountPolicyPageableMapperImpl implements DiscountPolicyPageableMapper {

    @Override
    public CustomPageable<DiscountPolicyResponseDto> toProductCategoryContext(List<DiscountPolicyResponseDto> discountPolicyResponses, Pageable pageable, long total) {
        Long lastDomainId = discountPolicyResponses.isEmpty() ? null : discountPolicyResponses.getLast().getDiscountPolicyId();
        return new CustomPageable<>(discountPolicyResponses, pageable, total, lastDomainId);
    }

}

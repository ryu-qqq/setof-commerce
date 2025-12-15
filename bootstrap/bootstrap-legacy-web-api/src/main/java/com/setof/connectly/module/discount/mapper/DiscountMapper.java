package com.setof.connectly.module.discount.mapper;

import com.setof.connectly.module.discount.dto.DiscountCalculateDto;
import com.setof.connectly.module.discount.dto.DiscountRedisFetchDto;
import com.setof.connectly.module.discount.dto.DiscountUseDto;
import com.setof.connectly.module.discount.entity.history.DiscountUseHistory;

public interface DiscountMapper {

    DiscountUseHistory toDiscountUseHistory(long discountPolicyId, DiscountUseDto discountUse);

    DiscountRedisFetchDto toDiscountRedisFetchDto(DiscountCalculateDto dto);
}

package com.connectly.partnerAdmin.module.discount.mapper;

import com.connectly.partnerAdmin.module.discount.dto.query.CreateDiscount;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;

public interface DiscountMapper {

    DiscountPolicy toEntity(CreateDiscount createDiscount);

}

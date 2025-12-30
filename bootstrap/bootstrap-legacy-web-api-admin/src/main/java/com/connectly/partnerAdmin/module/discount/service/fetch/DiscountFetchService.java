package com.connectly.partnerAdmin.module.discount.service.fetch;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DiscountFetchService {

    DiscountPolicyResponseDto fetchDiscountPolicy(long discountPolicyId);
    CustomPageable<DiscountPolicyResponseDto> fetchDiscountPolicies(DiscountFilter filter, Pageable pageable);
    DiscountPolicy fetchDiscountEntity(long discountPolicyId);
    List<DiscountPolicy> fetchDiscountEntities(List<Long> discountPolicyIds);


    Optional<BaseDiscountInfo> fetchDiscountCache(PriceHolder priceHolder);
    List<BaseDiscountInfo> fetchDiscountCaches(List<? extends PriceHolder> priceHolders);

}

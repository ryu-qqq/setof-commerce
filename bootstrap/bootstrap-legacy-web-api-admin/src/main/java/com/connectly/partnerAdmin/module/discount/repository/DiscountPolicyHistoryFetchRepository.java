package com.connectly.partnerAdmin.module.discount.repository;

import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.DiscountUseHistoryDto;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscountPolicyHistoryFetchRepository {

    List<DiscountPolicyResponseDto> fetchDiscountPolicyHistories(DiscountFilter filter, Pageable pageable);
    List<DiscountUseHistoryDto> fetchDiscountUseHistories(long discountPolicyId, DiscountFilter filter, Pageable pageable);

    JPAQuery<Long> fetchDiscountPolicyCountQuery(DiscountFilter filter);
    JPAQuery<Long> fetchDiscountUsePolicyCountQuery(long discountPolicyId, DiscountFilter filter);
}

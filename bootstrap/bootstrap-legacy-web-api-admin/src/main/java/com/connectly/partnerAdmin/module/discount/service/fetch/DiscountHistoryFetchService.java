package com.connectly.partnerAdmin.module.discount.service.fetch;

import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.DiscountUseHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscountHistoryFetchService {
    Page<DiscountPolicyResponseDto> fetchDiscountPolicyHistories(DiscountFilter filter, Pageable pageable);

    Page<DiscountUseHistoryDto> fetchDiscountUseHistories(long discountPolicyId, DiscountFilter filterDto, Pageable pageable);
}

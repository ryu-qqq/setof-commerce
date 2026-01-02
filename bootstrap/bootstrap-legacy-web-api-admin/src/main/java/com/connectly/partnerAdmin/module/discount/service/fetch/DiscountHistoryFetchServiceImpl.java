package com.connectly.partnerAdmin.module.discount.service.fetch;

import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.DiscountUseHistoryDto;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.connectly.partnerAdmin.module.discount.repository.DiscountPolicyHistoryFetchRepository;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DiscountHistoryFetchServiceImpl implements DiscountHistoryFetchService {

    private final DiscountPolicyHistoryFetchRepository discountPolicyHistoryFetchRepository;

    @Override
    public Page<DiscountPolicyResponseDto> fetchDiscountPolicyHistories(DiscountFilter filter, Pageable pageable) {
        List<DiscountPolicyResponseDto> results = discountPolicyHistoryFetchRepository.fetchDiscountPolicyHistories(filter, pageable);
        long totalCount = fetchDiscountPolicyCountQuery(filter);
        return PageableExecutionUtils.getPage(results, pageable, () -> totalCount);
    }

    @Override
    public Page<DiscountUseHistoryDto> fetchDiscountUseHistories(long discountPolicyId, DiscountFilter filter, Pageable pageable) {
        List<DiscountUseHistoryDto> results = discountPolicyHistoryFetchRepository.fetchDiscountUseHistories(discountPolicyId, filter, pageable);
        long totalCount = fetchDiscountUsePolicyCountQuery(discountPolicyId, filter);
        return PageableExecutionUtils.getPage(results, pageable, () -> totalCount);
    }

    private long fetchDiscountPolicyCountQuery(DiscountFilter filter){
        JPAQuery<Long> longJPAQuery = discountPolicyHistoryFetchRepository.fetchDiscountPolicyCountQuery(filter);
        Long l = longJPAQuery.fetchOne();
        if(l == null) return 0L;
        return l;
    }

    private long fetchDiscountUsePolicyCountQuery(long discountPolicyId, DiscountFilter filter){
        JPAQuery<Long> longJPAQuery = discountPolicyHistoryFetchRepository.fetchDiscountUsePolicyCountQuery(discountPolicyId, filter);
        Long l = longJPAQuery.fetchOne();
        if(l == null) return 0L;
        return l;
    }
}

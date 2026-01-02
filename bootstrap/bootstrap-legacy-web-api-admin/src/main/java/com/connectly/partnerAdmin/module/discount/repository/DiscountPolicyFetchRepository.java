package com.connectly.partnerAdmin.module.discount.repository;

import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DiscountPolicyFetchRepository {


    Optional<DiscountPolicyResponseDto> fetchDiscountPolicy(long discountPolicyId);

    List<DiscountPolicyResponseDto> fetchDiscountPolicies(DiscountFilter filterDto, Pageable pageable);

    JPAQuery<Long> fetchDiscountPolicyCountQuery(DiscountFilter filterDto);

    Optional<DiscountPolicy> fetchDiscountEntity(long discountPolicyId);

    List<DiscountPolicy> fetchDiscountEntities(List<Long> discountPolicyIds);


    Optional<BaseDiscountInfo> fetchDiscountInfo(long targetId, IssueType issueType);

    List<BaseDiscountInfo> fetchDiscountInfos(Set<Long> targetIds, IssueType issueType);



}

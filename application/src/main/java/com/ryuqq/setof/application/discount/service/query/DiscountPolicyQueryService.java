package com.ryuqq.setof.application.discount.service.query;

import com.ryuqq.setof.application.discount.assembler.DiscountPolicyAssembler;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import com.ryuqq.setof.application.discount.manager.query.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPoliciesUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 할인 정책 조회 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DiscountPolicyQueryService
        implements GetDiscountPolicyUseCase, GetDiscountPoliciesUseCase {

    private final DiscountPolicyReadManager discountPolicyReadManager;
    private final DiscountPolicyAssembler discountPolicyAssembler;

    public DiscountPolicyQueryService(
            DiscountPolicyReadManager discountPolicyReadManager,
            DiscountPolicyAssembler discountPolicyAssembler) {
        this.discountPolicyReadManager = discountPolicyReadManager;
        this.discountPolicyAssembler = discountPolicyAssembler;
    }

    @Override
    public DiscountPolicyResponse execute(Long discountPolicyId) {
        DiscountPolicy discountPolicy = discountPolicyReadManager.findById(discountPolicyId);
        return discountPolicyAssembler.toResponse(discountPolicy);
    }

    @Override
    public List<DiscountPolicyResponse> execute(DiscountPolicySearchQuery query) {
        List<DiscountPolicy> policies = findPolicies(query);
        List<DiscountPolicy> filteredPolicies = applyFilters(policies, query);
        return discountPolicyAssembler.toResponses(filteredPolicies);
    }

    private List<DiscountPolicy> findPolicies(DiscountPolicySearchQuery query) {
        if (query.discountGroup() != null) {
            return discountPolicyReadManager.findBySellerIdAndGroup(
                    query.sellerId(), query.discountGroup(), query.activeOnly());
        }

        if (query.validOnly()) {
            return discountPolicyReadManager.findValidPolicies(query.sellerId());
        }

        return discountPolicyReadManager.findBySellerId(query.sellerId(), query.includeDeleted());
    }

    private List<DiscountPolicy> applyFilters(
            List<DiscountPolicy> policies, DiscountPolicySearchQuery query) {
        return policies.stream()
                .filter(
                        policy ->
                                query.discountType() == null
                                        || policy.getDiscountType() == query.discountType())
                .filter(
                        policy ->
                                query.discountTargetType() == null
                                        || policy.getTargetType() == query.discountTargetType())
                .filter(
                        policy ->
                                query.targetId() == null
                                        || (policy.getTargetId() != null
                                                && policy.getTargetId().equals(query.targetId())))
                .filter(policy -> !query.activeOnly() || policy.isActive())
                .filter(policy -> query.includeDeleted() || !policy.isDeleted())
                .filter(policy -> !query.validOnly() || policy.isCurrentlyValid())
                .toList();
    }
}

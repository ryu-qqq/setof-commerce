package com.ryuqq.setof.application.discount.assembler;

import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicy Assembler
 *
 * <p>Domain 객체 → Response DTO 변환을 담당
 *
 * <p>주의: toDomain은 CommandFactory 책임입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountPolicyAssembler {

    /**
     * DiscountPolicy 도메인을 DiscountPolicyResponse로 변환
     *
     * @param discountPolicy DiscountPolicy 도메인 객체
     * @return DiscountPolicyResponse
     */
    public DiscountPolicyResponse toResponse(DiscountPolicy discountPolicy) {
        return DiscountPolicyResponse.of(
                discountPolicy.getIdValue(),
                discountPolicy.getSellerId(),
                discountPolicy.getPolicyNameValue(),
                discountPolicy.getDiscountGroup(),
                discountPolicy.getDiscountType(),
                discountPolicy.getTargetType(),
                discountPolicy.getTargetId(),
                discountPolicy.getTargetIds(),
                discountPolicy.getDiscountRateValue(),
                discountPolicy.getDiscountAmountValue(),
                discountPolicy.getMaximumDiscountAmountValue(),
                discountPolicy.getMinimumOrderAmountValue(),
                discountPolicy.getValidStartAt(),
                discountPolicy.getValidEndAt(),
                discountPolicy.getMaxUsagePerCustomer(),
                discountPolicy.getMaxTotalUsage(),
                discountPolicy.getPlatformCostShareRatio(),
                discountPolicy.getSellerCostShareRatio(),
                discountPolicy.getPriorityValue(),
                discountPolicy.isActive(),
                discountPolicy.isDeleted(),
                discountPolicy.isCurrentlyValid(),
                discountPolicy.getCreatedAt(),
                discountPolicy.getUpdatedAt());
    }

    /**
     * DiscountPolicy 도메인 목록을 DiscountPolicyResponse 목록으로 변환
     *
     * @param discountPolicies DiscountPolicy 도메인 목록
     * @return DiscountPolicyResponse 목록
     */
    public List<DiscountPolicyResponse> toResponses(List<DiscountPolicy> discountPolicies) {
        return discountPolicies.stream().map(this::toResponse).toList();
    }
}

package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.time.Instant;
import java.util.List;

/**
 * 할인 정책 상태 스냅샷.
 *
 * <p>변경 이력 저장 시 변경 전/후 정책 상태를 캡처합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DiscountPolicySnapshot(
        String name,
        String description,
        DiscountMethod discountMethod,
        Double discountRate,
        Integer discountAmount,
        Integer maxDiscountAmount,
        boolean discountCapped,
        Integer minimumOrderAmount,
        ApplicationType applicationType,
        PublisherType publisherType,
        Long sellerId,
        StackingGroup stackingGroup,
        int priority,
        Instant periodStart,
        Instant periodEnd,
        long totalBudget,
        long usedBudget,
        boolean active,
        List<DiscountTargetSnapshot> targets) {

    /**
     * DiscountPolicy Aggregate에서 스냅샷 생성.
     *
     * @param policy 스냅샷 대상 할인 정책
     * @return 현재 상태의 스냅샷
     */
    public static DiscountPolicySnapshot from(DiscountPolicy policy) {
        List<DiscountTargetSnapshot> targetSnapshots =
                policy.activeTargets().stream().map(DiscountTargetSnapshot::from).toList();

        return new DiscountPolicySnapshot(
                policy.nameValue(),
                policy.description(),
                policy.discountMethod(),
                policy.discountRateValue(),
                policy.discountAmountValue(),
                policy.maxDiscountAmountValue(),
                policy.isDiscountCapped(),
                policy.minimumOrderAmountValue(),
                policy.applicationType(),
                policy.publisherType(),
                policy.sellerIdValue(),
                policy.stackingGroup(),
                policy.priorityValue(),
                policy.period().startAt(),
                policy.period().endAt(),
                policy.budget().totalBudget().value(),
                policy.budget().usedBudget().value(),
                policy.isActive(),
                targetSnapshots);
    }

    /**
     * 할인 대상 스냅샷.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     */
    public record DiscountTargetSnapshot(DiscountTargetType targetType, long targetId) {

        public static DiscountTargetSnapshot from(DiscountTarget target) {
            return new DiscountTargetSnapshot(target.targetType(), target.targetId());
        }
    }
}

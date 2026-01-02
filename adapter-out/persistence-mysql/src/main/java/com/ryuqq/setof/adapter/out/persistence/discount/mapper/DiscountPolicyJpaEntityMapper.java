package com.ryuqq.setof.adapter.out.persistence.discount.mapper;

import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountGroupType;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTargetTypeEnum;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTypeEnum;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import com.ryuqq.setof.domain.discount.vo.MaximumDiscountAmount;
import com.ryuqq.setof.domain.discount.vo.MinimumOrderAmount;
import com.ryuqq.setof.domain.discount.vo.PolicyName;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.UsageLimit;
import com.ryuqq.setof.domain.discount.vo.ValidPeriod;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountPolicyJpaEntityMapper {

    /** Domain -> Entity 변환 */
    public DiscountPolicyJpaEntity toEntity(DiscountPolicy domain) {
        return DiscountPolicyJpaEntity.of(
                domain.getIdValue(),
                domain.getSellerId(),
                domain.getPolicyNameValue(),
                toEntityDiscountGroup(domain.getDiscountGroup()),
                toEntityDiscountType(domain.getDiscountType()),
                toEntityTargetType(domain.getTargetType()),
                toTargetIdsString(domain.getTargetIds()),
                domain.getDiscountRateValue(),
                domain.getDiscountAmountValue(),
                domain.getMaximumDiscountAmountValue(),
                domain.getMinimumOrderAmountValue(),
                domain.getValidStartAt(),
                domain.getValidEndAt(),
                domain.getMaxUsagePerCustomer(),
                domain.getMaxTotalUsage(),
                domain.getPlatformCostShareRatio(),
                domain.getSellerCostShareRatio(),
                domain.getPriorityValue(),
                domain.isActive(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /** Entity -> Domain 변환 */
    public DiscountPolicy toDomain(DiscountPolicyJpaEntity entity) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(entity.getId()),
                entity.getSellerId(),
                PolicyName.of(entity.getPolicyName()),
                toDomainDiscountGroup(entity.getDiscountGroup()),
                toDomainDiscountType(entity.getDiscountType()),
                toDomainTargetType(entity.getTargetType()),
                parseTargetIds(entity.getTargetIds()),
                entity.getDiscountRate() != null ? DiscountRate.of(entity.getDiscountRate()) : null,
                entity.getDiscountAmount() != null
                        ? DiscountAmount.of(entity.getDiscountAmount())
                        : null,
                entity.getMaximumDiscountAmount() != null
                        ? MaximumDiscountAmount.of(entity.getMaximumDiscountAmount())
                        : MaximumDiscountAmount.unlimited(),
                entity.getMinimumOrderAmount() != null
                        ? MinimumOrderAmount.of(entity.getMinimumOrderAmount())
                        : MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(entity.getValidStartAt(), entity.getValidEndAt()),
                UsageLimit.of(entity.getMaxUsagePerCustomer(), entity.getMaxTotalUsage(), null),
                CostShare.of(entity.getPlatformCostShareRatio(), entity.getSellerCostShareRatio()),
                Priority.of(entity.getPriority()),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }

    // ===== Private Helper Methods =====

    private DiscountGroupType toEntityDiscountGroup(DiscountGroup domainGroup) {
        return DiscountGroupType.valueOf(domainGroup.name());
    }

    private DiscountGroup toDomainDiscountGroup(DiscountGroupType entityGroup) {
        return DiscountGroup.valueOf(entityGroup.name());
    }

    private DiscountTypeEnum toEntityDiscountType(DiscountType domainType) {
        return DiscountTypeEnum.valueOf(domainType.name());
    }

    private DiscountType toDomainDiscountType(DiscountTypeEnum entityType) {
        return DiscountType.valueOf(entityType.name());
    }

    private DiscountTargetTypeEnum toEntityTargetType(DiscountTargetType domainType) {
        return DiscountTargetTypeEnum.valueOf(domainType.name());
    }

    private DiscountTargetType toDomainTargetType(DiscountTargetTypeEnum entityType) {
        return DiscountTargetType.valueOf(entityType.name());
    }

    private String toTargetIdsString(List<Long> targetIds) {
        if (targetIds == null || targetIds.isEmpty()) {
            return null;
        }
        return targetIds.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private List<Long> parseTargetIds(String targetIdsString) {
        if (targetIdsString == null || targetIdsString.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(targetIdsString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();
    }
}

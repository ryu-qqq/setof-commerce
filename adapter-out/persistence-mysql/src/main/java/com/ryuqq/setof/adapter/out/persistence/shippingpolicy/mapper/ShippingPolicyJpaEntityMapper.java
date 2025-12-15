package com.ryuqq.setof.adapter.out.persistence.shippingpolicy.mapper;

import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import org.springframework.stereotype.Component;

/**
 * ShippingPolicyJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingPolicyJpaEntityMapper {

    /** Domain -> Entity 변환 */
    public ShippingPolicyJpaEntity toEntity(ShippingPolicy domain) {
        return ShippingPolicyJpaEntity.of(
                domain.getIdValue(),
                domain.getSellerId(),
                domain.getPolicyNameValue(),
                domain.getDefaultDeliveryCostValue(),
                domain.getFreeShippingThresholdValue(),
                domain.getDeliveryGuideValue(),
                domain.isDefault(),
                domain.getDisplayOrderValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /** Entity -> Domain 변환 */
    public ShippingPolicy toDomain(ShippingPolicyJpaEntity entity) {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(entity.getId()),
                entity.getSellerId(),
                PolicyName.of(entity.getPolicyName()),
                DeliveryCost.of(entity.getDefaultDeliveryCost()),
                entity.getFreeShippingThreshold() != null
                        ? FreeShippingThreshold.of(entity.getFreeShippingThreshold())
                        : null,
                entity.getDeliveryGuide() != null
                        ? DeliveryGuide.of(entity.getDeliveryGuide())
                        : null,
                entity.getIsDefault(),
                DisplayOrder.of(entity.getDisplayOrder()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}

package com.ryuqq.setof.adapter.out.persistence.refundpolicy.mapper;

import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import org.springframework.stereotype.Component;

/**
 * RefundPolicyJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundPolicyJpaEntityMapper {

    /** Domain -> Entity 변환 */
    public RefundPolicyJpaEntity toEntity(RefundPolicy domain) {
        return RefundPolicyJpaEntity.of(
                domain.getIdValue(),
                domain.getSellerId(),
                domain.getPolicyNameValue(),
                domain.getReturnAddressLine1(),
                domain.getReturnAddressLine2(),
                domain.getReturnZipCode(),
                domain.getRefundPeriodDaysValue(),
                domain.getRefundDeliveryCostValue(),
                domain.getRefundGuideValue(),
                domain.isDefault(),
                domain.getDisplayOrder(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /** Entity -> Domain 변환 */
    public RefundPolicy toDomain(RefundPolicyJpaEntity entity) {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(entity.getId()),
                entity.getSellerId(),
                PolicyName.of(entity.getPolicyName()),
                ReturnAddress.of(
                        entity.getReturnAddressLine1(),
                        entity.getReturnAddressLine2(),
                        entity.getReturnZipCode()),
                RefundPeriodDays.of(entity.getRefundPeriodDays()),
                RefundDeliveryCost.of(entity.getRefundDeliveryCost()),
                entity.getRefundGuide() != null ? RefundGuide.of(entity.getRefundGuide()) : null,
                entity.getIsDefault(),
                entity.getDisplayOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}

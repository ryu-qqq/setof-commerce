package com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.id.DiscountTargetId;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.DiscountBudget;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountPeriod;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyName;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyJpaEntityMapper - 할인 정책 Entity-Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyJpaEntityMapper {

    /**
     * Domain → Entity 변환 (DiscountPolicy).
     *
     * @param domain DiscountPolicy 도메인 객체
     * @return DiscountPolicyJpaEntity
     */
    public DiscountPolicyJpaEntity toEntity(DiscountPolicy domain) {
        return DiscountPolicyJpaEntity.create(
                domain.isNew() ? null : domain.idValue(),
                domain.nameValue(),
                domain.description(),
                toEntityDiscountMethod(domain.discountMethod()),
                domain.discountRateValue(),
                domain.discountAmountValue(),
                domain.maxDiscountAmountValue(),
                domain.isDiscountCapped(),
                domain.minimumOrderAmountValue(),
                toEntityApplicationType(domain.applicationType()),
                toEntityPublisherType(domain.publisherType()),
                domain.sellerIdValue(),
                toEntityStackingGroup(domain.stackingGroup()),
                domain.priorityValue(),
                domain.period().startAt(),
                domain.period().endAt(),
                domain.budget().totalBudget().value(),
                domain.budget().usedBudget().value(),
                domain.isActive(),
                domain.deletedAt(),
                domain.createdAt(),
                domain.updatedAt());
    }

    /**
     * Domain → Entity 변환 (DiscountTarget).
     *
     * @param target DiscountTarget 도메인 객체
     * @param discountPolicyId 소속 정책 ID
     * @return DiscountTargetJpaEntity
     */
    public DiscountTargetJpaEntity toTargetEntity(
            DiscountTarget target, long discountPolicyId, Instant now) {
        return DiscountTargetJpaEntity.create(
                target.isNew() ? null : target.idValue(),
                discountPolicyId,
                toEntityTargetType(target.targetType()),
                target.targetId(),
                target.isActive(),
                now,
                now);
    }

    /**
     * Entity → Domain 변환 (DiscountPolicy + DiscountTarget 목록).
     *
     * @param entity DiscountPolicyJpaEntity
     * @param targetEntities 소속 DiscountTargetJpaEntity 목록
     * @return DiscountPolicy 도메인 객체
     */
    public DiscountPolicy toDomain(
            DiscountPolicyJpaEntity entity, List<DiscountTargetJpaEntity> targetEntities) {
        List<DiscountTarget> targets = targetEntities.stream().map(this::toTargetDomain).toList();

        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(entity.getId()),
                DiscountPolicyName.of(entity.getName()),
                entity.getDescription(),
                toDomainDiscountMethod(entity.getDiscountMethod()),
                entity.getDiscountRate() != null ? DiscountRate.of(entity.getDiscountRate()) : null,
                entity.getDiscountAmount() != null ? Money.of(entity.getDiscountAmount()) : null,
                entity.getMaxDiscountAmount() != null
                        ? Money.of(entity.getMaxDiscountAmount())
                        : null,
                entity.isDiscountCapped(),
                entity.getMinimumOrderAmount() != null
                        ? Money.of(entity.getMinimumOrderAmount())
                        : null,
                toDomainApplicationType(entity.getApplicationType()),
                toDomainPublisherType(entity.getPublisherType()),
                entity.getSellerId() != null ? SellerId.of(entity.getSellerId()) : null,
                toDomainStackingGroup(entity.getStackingGroup()),
                Priority.of(entity.getPriority()),
                DiscountPeriod.of(entity.getStartAt(), entity.getEndAt()),
                DiscountBudget.of(
                        Money.of(entity.getTotalBudget()), Money.of(entity.getUsedBudget())),
                entity.isActive(),
                entity.getDeletedAt(),
                targets,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private DiscountTarget toTargetDomain(DiscountTargetJpaEntity entity) {
        return DiscountTarget.reconstitute(
                DiscountTargetId.of(entity.getId()),
                toDomainTargetType(entity.getTargetType()),
                entity.getTargetId(),
                entity.isActive());
    }

    // ========== Enum Conversions ==========

    private DiscountPolicyJpaEntity.DiscountMethod toEntityDiscountMethod(DiscountMethod domain) {
        return DiscountPolicyJpaEntity.DiscountMethod.valueOf(domain.name());
    }

    private DiscountMethod toDomainDiscountMethod(DiscountPolicyJpaEntity.DiscountMethod entity) {
        return DiscountMethod.valueOf(entity.name());
    }

    private DiscountPolicyJpaEntity.ApplicationType toEntityApplicationType(
            ApplicationType domain) {
        return DiscountPolicyJpaEntity.ApplicationType.valueOf(domain.name());
    }

    private ApplicationType toDomainApplicationType(
            DiscountPolicyJpaEntity.ApplicationType entity) {
        return ApplicationType.valueOf(entity.name());
    }

    private DiscountPolicyJpaEntity.PublisherType toEntityPublisherType(PublisherType domain) {
        return DiscountPolicyJpaEntity.PublisherType.valueOf(domain.name());
    }

    private PublisherType toDomainPublisherType(DiscountPolicyJpaEntity.PublisherType entity) {
        return PublisherType.valueOf(entity.name());
    }

    private DiscountPolicyJpaEntity.StackingGroup toEntityStackingGroup(StackingGroup domain) {
        return DiscountPolicyJpaEntity.StackingGroup.valueOf(domain.name());
    }

    private StackingGroup toDomainStackingGroup(DiscountPolicyJpaEntity.StackingGroup entity) {
        return StackingGroup.valueOf(entity.name());
    }

    private DiscountTargetJpaEntity.TargetType toEntityTargetType(DiscountTargetType domain) {
        return DiscountTargetJpaEntity.TargetType.valueOf(domain.name());
    }

    private DiscountTargetType toDomainTargetType(DiscountTargetJpaEntity.TargetType entity) {
        return DiscountTargetType.valueOf(entity.name());
    }
}

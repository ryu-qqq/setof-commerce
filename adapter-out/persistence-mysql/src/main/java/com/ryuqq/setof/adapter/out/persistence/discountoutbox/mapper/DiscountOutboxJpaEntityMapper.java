package com.ryuqq.setof.adapter.out.persistence.discountoutbox.mapper;

import com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.DiscountOutboxJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.DiscountOutboxJpaEntity.Status;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.id.DiscountOutboxId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import com.ryuqq.setof.domain.discount.vo.OutboxTargetKey;
import org.springframework.stereotype.Component;

/**
 * DiscountOutboxJpaEntityMapper - 할인 아웃박스 Entity-Domain 매퍼.
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
public class DiscountOutboxJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain DiscountOutbox 도메인 객체
     * @return DiscountOutboxJpaEntity
     */
    public DiscountOutboxJpaEntity toEntity(DiscountOutbox domain) {
        return DiscountOutboxJpaEntity.create(
                domain.isNew() ? null : domain.idValue(),
                domain.targetType().name(),
                domain.targetId(),
                toEntityStatus(domain.status()),
                domain.retryCount(),
                domain.payload(),
                domain.failReason(),
                domain.createdAt(),
                domain.updatedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity DiscountOutboxJpaEntity
     * @return DiscountOutbox 도메인 객체
     */
    public DiscountOutbox toDomain(DiscountOutboxJpaEntity entity) {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(entity.getId()),
                OutboxTargetKey.of(
                        DiscountTargetType.valueOf(entity.getTargetType()), entity.getTargetId()),
                toDomainStatus(entity.getStatus()),
                entity.getRetryCount(),
                entity.getPayload(),
                entity.getFailReason(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private Status toEntityStatus(OutboxStatus domainStatus) {
        return Status.valueOf(domainStatus.name());
    }

    private OutboxStatus toDomainStatus(Status entityStatus) {
        return OutboxStatus.valueOf(entityStatus.name());
    }
}

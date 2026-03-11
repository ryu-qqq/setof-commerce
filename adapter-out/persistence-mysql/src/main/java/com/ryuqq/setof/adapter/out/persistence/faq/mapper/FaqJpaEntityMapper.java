package com.ryuqq.setof.adapter.out.persistence.faq.mapper;

import com.ryuqq.setof.adapter.out.persistence.faq.entity.FaqJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.id.FaqId;
import com.ryuqq.setof.domain.faq.vo.FaqContents;
import com.ryuqq.setof.domain.faq.vo.FaqDisplayOrder;
import com.ryuqq.setof.domain.faq.vo.FaqTitle;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import org.springframework.stereotype.Component;

/**
 * FaqJpaEntityMapper - FAQ Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
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
public class FaqJpaEntityMapper {

    /**
     * Faq Domain → FaqJpaEntity 변환.
     *
     * @param domain Faq 도메인 객체
     * @return FaqJpaEntity
     */
    public FaqJpaEntity toEntity(Faq domain) {
        return FaqJpaEntity.create(
                domain.idValue(),
                domain.faqType().name(),
                domain.titleValue(),
                domain.contentsValue(),
                domain.displayOrderValue(),
                domain.topDisplayOrder(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * FaqJpaEntity → Faq Domain 변환.
     *
     * @param entity FaqJpaEntity
     * @return Faq 도메인 객체
     */
    public Faq toDomain(FaqJpaEntity entity) {
        return Faq.reconstitute(
                FaqId.of(entity.getId()),
                FaqType.valueOf(entity.getFaqType()),
                FaqTitle.of(entity.getTitle()),
                FaqContents.of(entity.getContents()),
                FaqDisplayOrder.of(entity.getDisplayOrder()),
                entity.getTopDisplayOrder(),
                DeletionStatus.reconstitute(entity.getDeletedAt() != null, entity.getDeletedAt()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

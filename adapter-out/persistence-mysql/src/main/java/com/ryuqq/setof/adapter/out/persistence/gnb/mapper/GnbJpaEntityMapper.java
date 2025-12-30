package com.ryuqq.setof.adapter.out.persistence.gnb.mapper;

import com.ryuqq.setof.adapter.out.persistence.gnb.entity.GnbJpaEntity;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import com.ryuqq.setof.domain.cms.vo.GnbStatus;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Component;

/**
 * GnbJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class GnbJpaEntityMapper {

    private final ClockHolder clockHolder;

    public GnbJpaEntityMapper(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /** Domain -> Entity 변환 */
    public GnbJpaEntity toEntity(Gnb domain) {
        return GnbJpaEntity.of(
                domain.id() != null ? domain.id().value() : null,
                domain.title().value(),
                domain.linkUrl() != null ? domain.linkUrl().value() : null,
                domain.displayOrder() != null ? domain.displayOrder().value() : 0,
                domain.status().name(),
                domain.displayPeriod() != null ? domain.displayPeriod().startDate() : null,
                domain.displayPeriod() != null ? domain.displayPeriod().endDate() : null,
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    /** Entity -> Domain 변환 */
    public Gnb toDomain(GnbJpaEntity entity) {
        DisplayPeriod displayPeriod = null;
        if (entity.getDisplayStartDate() != null && entity.getDisplayEndDate() != null) {
            displayPeriod =
                    DisplayPeriod.of(entity.getDisplayStartDate(), entity.getDisplayEndDate());
        }

        return Gnb.reconstitute(
                GnbId.of(entity.getId()),
                ContentTitle.of(entity.getTitle()),
                entity.getLinkUrl() != null ? ImageUrl.of(entity.getLinkUrl()) : ImageUrl.empty(),
                DisplayOrder.of(entity.getDisplayOrder()),
                GnbStatus.valueOf(entity.getStatus()),
                displayPeriod,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                clockHolder.getClock());
    }
}

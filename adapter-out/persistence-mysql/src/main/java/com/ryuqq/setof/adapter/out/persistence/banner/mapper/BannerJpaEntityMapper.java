package com.ryuqq.setof.adapter.out.persistence.banner.mapper;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerJpaEntity;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerStatus;
import com.ryuqq.setof.domain.cms.vo.BannerType;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Component;

/**
 * BannerJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerJpaEntityMapper {

    private final ClockHolder clockHolder;

    public BannerJpaEntityMapper(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /** Domain -> Entity 변환 */
    public BannerJpaEntity toEntity(Banner domain) {
        return BannerJpaEntity.of(
                domain.id() != null ? domain.id().value() : null,
                domain.title().value(),
                domain.bannerType().name(),
                domain.status().name(),
                domain.displayPeriod().startDate(),
                domain.displayPeriod().endDate(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    /** Entity -> Domain 변환 */
    public Banner toDomain(BannerJpaEntity entity) {
        DisplayPeriod displayPeriod =
                DisplayPeriod.of(entity.getDisplayStartDate(), entity.getDisplayEndDate());

        return Banner.reconstitute(
                BannerId.of(entity.getId()),
                ContentTitle.of(entity.getTitle()),
                BannerType.valueOf(entity.getBannerType()),
                BannerStatus.valueOf(entity.getStatus()),
                displayPeriod,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                clockHolder.getClock());
    }
}

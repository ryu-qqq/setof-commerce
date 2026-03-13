package com.ryuqq.setof.adapter.out.persistence.banner.mapper;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerSlideId;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import org.springframework.stereotype.Component;

/**
 * BannerSlideJpaEntityMapper - 배너 슬라이드 Entity-Domain 매퍼.
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
public class BannerSlideJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain BannerSlide 도메인 객체
     * @return BannerSlideJpaEntity
     */
    public BannerSlideJpaEntity toEntity(long bannerGroupId, BannerSlide domain) {
        return BannerSlideJpaEntity.create(
                domain.idValue(),
                bannerGroupId,
                domain.title(),
                domain.imageUrl(),
                domain.linkUrl(),
                domain.displayOrder(),
                domain.displayPeriod().startDate(),
                domain.displayPeriod().endDate(),
                domain.isActive(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity BannerSlideJpaEntity
     * @return BannerSlide 도메인 객체
     */
    public BannerSlide toDomain(BannerSlideJpaEntity entity) {
        return BannerSlide.reconstitute(
                BannerSlideId.of(entity.getId()),
                entity.getTitle(),
                entity.getImageUrl(),
                entity.getLinkUrl(),
                entity.getDisplayOrder(),
                DisplayPeriod.of(entity.getDisplayStartAt(), entity.getDisplayEndAt()),
                entity.isActive(),
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

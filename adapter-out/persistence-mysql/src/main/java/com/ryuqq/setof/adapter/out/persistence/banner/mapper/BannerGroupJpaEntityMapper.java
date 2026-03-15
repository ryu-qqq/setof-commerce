package com.ryuqq.setof.adapter.out.persistence.banner.mapper;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerGroupJpaEntityMapper - 배너 그룹 Entity-Domain 매퍼.
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
public class BannerGroupJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain BannerGroup 도메인 객체
     * @return BannerGroupJpaEntity
     */
    public BannerGroupJpaEntity toEntity(BannerGroup domain) {
        return BannerGroupJpaEntity.create(
                domain.idValue(),
                domain.title(),
                domain.bannerType().name(),
                domain.displayPeriod().startDate(),
                domain.displayPeriod().endDate(),
                domain.isActive(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity + 슬라이드 목록 → Domain 변환.
     *
     * @param entity BannerGroupJpaEntity
     * @param slides 변환된 BannerSlide 목록
     * @return BannerGroup 도메인 객체
     */
    public BannerGroup toDomain(BannerGroupJpaEntity entity, List<BannerSlide> slides) {
        return BannerGroup.reconstitute(
                BannerGroupId.of(entity.getId()),
                entity.getTitle(),
                BannerType.valueOf(entity.getBannerType()),
                DisplayPeriod.of(entity.getDisplayStartAt(), entity.getDisplayEndAt()),
                entity.isActive(),
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt()),
                slides,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

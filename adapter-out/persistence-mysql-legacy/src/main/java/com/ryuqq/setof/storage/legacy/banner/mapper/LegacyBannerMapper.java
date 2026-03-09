package com.ryuqq.setof.storage.legacy.banner.mapper;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerSlideId;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.storage.legacy.banner.entity.LegacyBannerItemEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyBannerMapper - 레거시 BannerItem Entity → BannerSlide 도메인 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyBannerMapper {

    private static final ZoneId LEGACY_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * LegacyBannerItemEntity → BannerSlide 도메인 변환.
     *
     * @param entity 레거시 배너 아이템 엔티티
     * @return BannerSlide 도메인 객체
     */
    public BannerSlide toDomain(LegacyBannerItemEntity entity) {
        Instant startDate = toInstant(entity.getDisplayStartDate());
        Instant endDate = toInstant(entity.getDisplayEndDate());

        return BannerSlide.reconstitute(
                BannerSlideId.of(entity.getId()),
                entity.getTitle(),
                entity.getImageUrl(),
                entity.getLinkUrl(),
                entity.getDisplayOrder(),
                DisplayPeriod.of(startDate, endDate),
                true,
                DeletionStatus.active(),
                startDate,
                startDate);
    }

    /**
     * LegacyBannerItemEntity 목록 → BannerSlide 목록 변환.
     *
     * @param entities 레거시 배너 아이템 엔티티 목록
     * @return BannerSlide 목록
     */
    public List<BannerSlide> toDomains(List<LegacyBannerItemEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(this::toDomain).toList();
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(LEGACY_ZONE).toInstant();
    }
}

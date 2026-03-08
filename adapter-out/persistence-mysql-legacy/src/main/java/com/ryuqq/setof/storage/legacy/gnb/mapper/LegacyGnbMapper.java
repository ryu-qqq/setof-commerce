package com.ryuqq.setof.storage.legacy.gnb.mapper;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import com.ryuqq.setof.storage.legacy.gnb.entity.LegacyGnbEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyGnbMapper - 레거시 GNB Entity → NavigationMenu 도메인 변환.
 *
 * <p>레거시 DB 조회 결과를 도메인 객체로 변환하여 Application 레이어에 전달한다. LocalDateTime → Instant 변환은 이 Mapper에서 담당한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyGnbMapper {

    private static final ZoneId LEGACY_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * LegacyGnbEntity → NavigationMenu 도메인 변환.
     *
     * @param entity 레거시 GNB 엔티티
     * @return NavigationMenu 도메인 객체
     */
    public NavigationMenu toDomain(LegacyGnbEntity entity) {
        Instant startDate = toInstant(entity.getDisplayStartDate());
        Instant endDate = toInstant(entity.getDisplayEndDate());

        return NavigationMenu.reconstitute(
                NavigationMenuId.of(entity.getId()),
                entity.getTitle(),
                entity.getLinkUrl(),
                entity.getDisplayOrder(),
                DisplayPeriod.of(startDate, endDate),
                true,
                DeletionStatus.active(),
                startDate,
                startDate);
    }

    /**
     * LegacyGnbEntity 목록 → NavigationMenu 목록 변환.
     *
     * @param entities 레거시 GNB 엔티티 목록
     * @return NavigationMenu 목록
     */
    public List<NavigationMenu> toDomains(List<LegacyGnbEntity> entities) {
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

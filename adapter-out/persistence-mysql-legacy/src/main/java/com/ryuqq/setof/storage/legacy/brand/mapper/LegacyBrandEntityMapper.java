package com.ryuqq.setof.storage.legacy.brand.mapper;

import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandIconImageUrl;
import com.ryuqq.setof.domain.brand.vo.BrandName;
import com.ryuqq.setof.domain.brand.vo.DisplayName;
import com.ryuqq.setof.domain.brand.vo.DisplayOrder;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity.MainDisplayNameType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyBrandEntityMapper - 레거시 브랜드 Entity-Domain 매퍼.
 *
 * <p>레거시 Entity → 새 Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyBrandEntityMapper {

    /**
     * 레거시 Entity → Domain 변환.
     *
     * <p>레거시 스키마의 displayEnglishName/displayKoreanName을 mainDisplayType에 따라 단일 displayName으로 변환합니다.
     *
     * @param entity LegacyBrandEntity
     * @return Brand 도메인 객체
     */
    public Brand toDomain(LegacyBrandEntity entity) {
        String displayName =
                resolveDisplayName(
                        entity.getDisplayEnglishName(),
                        entity.getDisplayKoreanName(),
                        entity.getMainDisplayType());

        boolean displayed = entity.getDisplayYn().toBoolean();
        Instant createdAt = toInstant(entity.getInsertDate());
        Instant updatedAt = toInstant(entity.getUpdateDate());

        return Brand.reconstitute(
                BrandId.of(entity.getId()),
                BrandName.of(entity.getBrandName()),
                BrandIconImageUrl.of(entity.getBrandIconImageUrl()),
                DisplayName.of(displayName),
                DisplayOrder.of(entity.getDisplayOrder()),
                displayed,
                null,
                createdAt,
                updatedAt);
    }

    private String resolveDisplayName(
            String englishName, String koreanName, MainDisplayNameType type) {
        return koreanName != null ? koreanName : englishName;
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}

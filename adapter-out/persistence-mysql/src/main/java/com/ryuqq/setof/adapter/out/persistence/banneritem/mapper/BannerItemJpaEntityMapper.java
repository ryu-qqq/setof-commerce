package com.ryuqq.setof.adapter.out.persistence.banneritem.mapper;

import com.ryuqq.setof.adapter.out.persistence.banneritem.entity.CmsBannerItemJpaEntity;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import com.ryuqq.setof.domain.cms.vo.BannerStatus;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageSize;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerItemJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>BannerItem의 JPA Entity와 Domain 객체 간 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerItemJpaEntityMapper {

    private final ClockHolder clockHolder;

    public BannerItemJpaEntityMapper(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /** Domain -> Entity 변환 */
    public CmsBannerItemJpaEntity toEntity(BannerItem domain) {
        return CmsBannerItemJpaEntity.of(
                domain.id() != null ? domain.id().value() : null,
                domain.bannerId().value(),
                domain.title() != null ? domain.title().value() : null,
                domain.imageUrl().value(),
                domain.linkUrl() != null ? domain.linkUrl().value() : null,
                domain.displayOrder() != null ? domain.displayOrder().value() : 0,
                domain.status().name(),
                domain.displayPeriod() != null ? domain.displayPeriod().startDate() : null,
                domain.displayPeriod() != null ? domain.displayPeriod().endDate() : null,
                domain.imageSize() != null && domain.imageSize().width() != null
                        ? domain.imageSize().width().intValue()
                        : null,
                domain.imageSize() != null && domain.imageSize().height() != null
                        ? domain.imageSize().height().intValue()
                        : null,
                domain.title() != null ? domain.title().value() : null, // altText = title
                domain.createdAt(),
                null, // updatedAt은 DB 트리거로 처리
                domain.deletedAt());
    }

    /** Entity -> Domain 변환 */
    public BannerItem toDomain(CmsBannerItemJpaEntity entity) {
        DisplayPeriod displayPeriod = null;
        if (entity.getDisplayStartDate() != null && entity.getDisplayEndDate() != null) {
            displayPeriod =
                    DisplayPeriod.of(entity.getDisplayStartDate(), entity.getDisplayEndDate());
        }

        ImageSize imageSize = null;
        if (entity.getImageWidth() != null && entity.getImageHeight() != null) {
            imageSize =
                    ImageSize.of(
                            entity.getImageWidth().doubleValue(),
                            entity.getImageHeight().doubleValue());
        }

        return BannerItem.reconstitute(
                BannerItemId.of(entity.getId()),
                BannerId.of(entity.getBannerId()),
                entity.getTitle() != null ? ContentTitle.of(entity.getTitle()) : null,
                ImageUrl.of(entity.getImageUrl()),
                entity.getLinkUrl() != null ? ImageUrl.of(entity.getLinkUrl()) : ImageUrl.empty(),
                imageSize != null ? imageSize : ImageSize.empty(),
                DisplayOrder.of(entity.getDisplayOrder()),
                BannerStatus.valueOf(entity.getStatus()),
                displayPeriod,
                entity.getCreatedAt(),
                entity.getDeletedAt(),
                clockHolder.getClock());
    }

    /** Entity 목록 -> Domain 목록 변환 */
    public List<BannerItem> toDomainList(List<CmsBannerItemJpaEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }

    /** Domain 목록 -> Entity 목록 변환 */
    public List<CmsBannerItemJpaEntity> toEntityList(List<BannerItem> domains) {
        return domains.stream().map(this::toEntity).toList();
    }
}

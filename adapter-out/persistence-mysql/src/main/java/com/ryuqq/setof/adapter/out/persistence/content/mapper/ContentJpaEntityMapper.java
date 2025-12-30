package com.ryuqq.setof.adapter.out.persistence.content.mapper;

import com.ryuqq.setof.adapter.out.persistence.content.entity.ContentJpaEntity;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import com.ryuqq.setof.domain.cms.vo.ContentMemo;
import com.ryuqq.setof.domain.cms.vo.ContentStatus;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Component;

/**
 * ContentJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentJpaEntityMapper {

    private final ClockHolder clockHolder;

    public ContentJpaEntityMapper(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /** Domain -> Entity 변환 */
    public ContentJpaEntity toEntity(Content domain) {
        return ContentJpaEntity.of(
                domain.getIdValue(),
                domain.getTitleValue(),
                domain.getMemoValue(),
                domain.getImageUrlValue(),
                domain.getStatusValue(),
                domain.getDisplayStartDate(),
                domain.getDisplayEndDate(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    /** Entity -> Domain 변환 */
    public Content toDomain(ContentJpaEntity entity) {
        return Content.reconstitute(
                ContentId.of(entity.getId()),
                ContentTitle.of(entity.getTitle()),
                entity.getMemo() != null ? ContentMemo.of(entity.getMemo()) : ContentMemo.empty(),
                entity.getImageUrl() != null ? ImageUrl.of(entity.getImageUrl()) : ImageUrl.empty(),
                ContentStatus.valueOf(entity.getStatus()),
                DisplayPeriod.of(entity.getDisplayStartDate(), entity.getDisplayEndDate()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                clockHolder.getClock());
    }
}

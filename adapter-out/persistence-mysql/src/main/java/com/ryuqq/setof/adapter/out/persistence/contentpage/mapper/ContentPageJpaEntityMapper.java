package com.ryuqq.setof.adapter.out.persistence.contentpage.mapper;

import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import org.springframework.stereotype.Component;

@Component
public class ContentPageJpaEntityMapper {

    public ContentPage toDomain(ContentPageJpaEntity entity) {
        DeletionStatus deletionStatus =
                entity.getDeletedAt() != null
                        ? DeletionStatus.deletedAt(entity.getDeletedAt())
                        : DeletionStatus.active();

        return ContentPage.reconstitute(
                ContentPageId.of(entity.getId()),
                entity.getTitle(),
                entity.getMemo(),
                entity.getImageUrl(),
                DisplayPeriod.of(entity.getDisplayStartAt(), entity.getDisplayEndAt()),
                entity.isActive(),
                deletionStatus,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public ContentPageJpaEntity toEntity(ContentPage domain) {
        return ContentPageJpaEntity.create(
                domain.idValue(),
                domain.title(),
                domain.memo(),
                domain.imageUrl(),
                domain.displayPeriod().startDate(),
                domain.displayPeriod().endDate(),
                domain.isActive(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }
}

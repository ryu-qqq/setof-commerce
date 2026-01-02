package com.ryuqq.setof.adapter.out.persistence.componentitem.mapper;

import com.ryuqq.setof.adapter.out.persistence.componentitem.entity.CmsComponentItemJpaEntity;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemType;
import com.ryuqq.setof.domain.cms.vo.ComponentStatus;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.cms.vo.SortType;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ComponentItemJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>ComponentItem의 JPA Entity와 Domain 객체 간 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ComponentItemJpaEntityMapper {

    private final ClockHolder clockHolder;

    public ComponentItemJpaEntityMapper(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /** Domain -> Entity 변환 */
    public CmsComponentItemJpaEntity toEntity(ComponentItem domain) {
        return CmsComponentItemJpaEntity.of(
                domain.id() != null ? domain.id().value() : null,
                domain.componentId().value(),
                domain.itemType().name(),
                domain.referenceId(),
                domain.title() != null ? domain.title().value() : null,
                domain.imageUrl() != null ? domain.imageUrl().value() : null,
                domain.linkUrl() != null ? domain.linkUrl().value() : null,
                domain.displayOrder() != null ? domain.displayOrder().value() : 0,
                domain.status().name(),
                domain.sortType() != null ? domain.sortType().name() : null,
                domain.extraData(),
                domain.createdAt(),
                null, // updatedAt은 DB 트리거로 처리
                domain.deletedAt());
    }

    /** Entity -> Domain 변환 */
    public ComponentItem toDomain(CmsComponentItemJpaEntity entity) {
        return ComponentItem.reconstitute(
                ComponentItemId.of(entity.getId()),
                ComponentId.of(entity.getComponentId()),
                ComponentItemType.valueOf(entity.getItemType()),
                entity.getReferenceId(),
                entity.getTitle() != null ? ContentTitle.of(entity.getTitle()) : null,
                entity.getImageUrl() != null ? ImageUrl.of(entity.getImageUrl()) : ImageUrl.empty(),
                entity.getLinkUrl() != null ? ImageUrl.of(entity.getLinkUrl()) : ImageUrl.empty(),
                DisplayOrder.of(entity.getDisplayOrder()),
                ComponentStatus.valueOf(entity.getStatus()),
                parseSortType(entity.getSortType()),
                entity.getExtraData(),
                entity.getCreatedAt(),
                entity.getDeletedAt(),
                clockHolder.getClock());
    }

    /** Entity 목록 -> Domain 목록 변환 */
    public List<ComponentItem> toDomainList(List<CmsComponentItemJpaEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }

    /** Domain 목록 -> Entity 목록 변환 */
    public List<CmsComponentItemJpaEntity> toEntityList(List<ComponentItem> domains) {
        return domains.stream().map(this::toEntity).toList();
    }

    /** SortType 문자열 파싱 (nullable) */
    private SortType parseSortType(String sortType) {
        if (sortType == null || sortType.isBlank()) {
            return null;
        }
        try {
            return SortType.valueOf(sortType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

package com.ryuqq.setof.adapter.out.persistence.navigation.mapper;

import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuJpaEntityMapper - 네비게이션 메뉴 Entity-Domain 매퍼.
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
public class NavigationMenuJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain NavigationMenu 도메인 객체
     * @return NavigationMenuJpaEntity
     */
    public NavigationMenuJpaEntity toEntity(NavigationMenu domain) {
        return NavigationMenuJpaEntity.create(
                domain.idValue(),
                domain.title(),
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
     * @param entity NavigationMenuJpaEntity
     * @return NavigationMenu 도메인 객체
     */
    public NavigationMenu toDomain(NavigationMenuJpaEntity entity) {
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(entity.getId()),
                entity.getTitle(),
                entity.getLinkUrl(),
                entity.getDisplayOrder(),
                DisplayPeriod.of(entity.getDisplayStartAt(), entity.getDisplayEndAt()),
                entity.isActive(),
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

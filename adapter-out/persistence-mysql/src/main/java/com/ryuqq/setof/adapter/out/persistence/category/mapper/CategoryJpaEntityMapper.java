package com.ryuqq.setof.adapter.out.persistence.category.mapper;

import com.ryuqq.setof.adapter.out.persistence.category.dto.CategoryTreeDto;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryDisplayName;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import org.springframework.stereotype.Component;

/**
 * CategoryJpaEntityMapper - 카테고리 Entity-Domain 매퍼.
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
 * @since 1.0.0
 */
@Component
public class CategoryJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain Category 도메인 객체
     * @return CategoryJpaEntity
     */
    public CategoryJpaEntity toEntity(Category domain) {
        return CategoryJpaEntity.create(
                domain.idValue(),
                domain.categoryNameValue(),
                domain.categoryDepthValue(),
                domain.parentCategoryIdValue(),
                domain.displayNameValue(),
                domain.isDisplayed(),
                domain.targetGroup(),
                domain.categoryType(),
                domain.pathValue(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity CategoryJpaEntity
     * @return Category 도메인 객체
     */
    public Category toDomain(CategoryJpaEntity entity) {
        return Category.reconstitute(
                CategoryId.of(entity.getId()),
                CategoryName.of(entity.getCategoryName()),
                CategoryDepth.of(entity.getCategoryDepth()),
                CategoryId.of(entity.getParentCategoryId()),
                CategoryDisplayName.of(entity.getDisplayName()),
                entity.isDisplayed(),
                entity.getTargetGroup(),
                entity.getCategoryType(),
                CategoryPath.of(entity.getPath()),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * TreeDto → Domain 변환.
     *
     * <p>Recursive CTE 쿼리 결과를 Domain으로 변환합니다.
     *
     * @param dto CategoryTreeDto
     * @return Category 도메인 객체
     */
    public Category toDomain(CategoryTreeDto dto) {
        return Category.reconstitute(
                CategoryId.of(dto.getId()),
                CategoryName.of(dto.getCategoryName()),
                CategoryDepth.of(dto.getCategoryDepth()),
                CategoryId.of(dto.getParentCategoryId()),
                CategoryDisplayName.of(dto.getDisplayName()),
                dto.isDisplayed(),
                dto.getTargetGroup(),
                dto.getCategoryType(),
                CategoryPath.of(dto.getPath()),
                dto.getDeletedAt(),
                dto.getCreatedAt(),
                dto.getUpdatedAt());
    }
}

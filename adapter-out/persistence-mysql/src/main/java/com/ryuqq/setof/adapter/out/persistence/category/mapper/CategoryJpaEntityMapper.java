package com.ryuqq.setof.adapter.out.persistence.category.mapper;

import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import com.ryuqq.setof.domain.category.vo.CategoryStatus;
import org.springframework.stereotype.Component;

/**
 * CategoryJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Category 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Category -> CategoryJpaEntity (저장용)
 *   <li>CategoryJpaEntity -> Category (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CategoryJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * @param domain Category 도메인
     * @return CategoryJpaEntity
     */
    public CategoryJpaEntity toEntity(Category domain) {
        return CategoryJpaEntity.of(
                domain.getIdValue(),
                domain.getCodeValue(),
                domain.getNameKoValue(),
                domain.getParentId(),
                domain.getDepthValue(),
                domain.getPathValue(),
                domain.getSortOrder(),
                domain.isLeaf(),
                domain.getStatusValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity CategoryJpaEntity
     * @return Category 도메인
     */
    public Category toDomain(CategoryJpaEntity entity) {
        return Category.reconstitute(
                CategoryId.of(entity.getId()),
                CategoryCode.of(entity.getCode()),
                CategoryName.of(entity.getNameKo()),
                entity.getParentId(),
                CategoryDepth.of(entity.getDepth()),
                CategoryPath.of(entity.getPath()),
                entity.getSortOrder(),
                entity.isLeaf(),
                CategoryStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

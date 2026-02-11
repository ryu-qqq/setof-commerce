package com.ryuqq.setof.storage.legacy.category.mapper;

import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryDisplayName;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import com.ryuqq.setof.storage.legacy.category.dto.LegacyCategoryTreeDto;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyCategoryEntityMapper - 레거시 카테고리 Entity-Domain 매퍼.
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
public class LegacyCategoryEntityMapper {

    /**
     * 레거시 Entity → Domain 변환.
     *
     * @param entity LegacyCategoryEntity
     * @return Category 도메인 객체
     */
    public Category toDomain(LegacyCategoryEntity entity) {
        boolean displayed = entity.getDisplayYn().toBoolean();
        Instant createdAt = toInstant(entity.getInsertDate());
        Instant updatedAt = toInstant(entity.getUpdateDate());

        return Category.reconstitute(
                CategoryId.of(entity.getId()),
                CategoryName.of(entity.getCategoryName()),
                CategoryDepth.of(entity.getCategoryDepth()),
                CategoryId.of(entity.getParentCategoryId()),
                CategoryDisplayName.of(entity.getDisplayName()),
                displayed,
                entity.getTargetGroup(),
                entity.getCategoryType(),
                CategoryPath.of(entity.getPath()),
                null,
                createdAt,
                updatedAt);
    }

    /**
     * TreeDto → Domain 변환.
     *
     * <p>Recursive CTE 쿼리 결과를 Domain으로 변환합니다.
     *
     * @param dto LegacyCategoryTreeDto
     * @return Category 도메인 객체
     */
    public Category toDomain(LegacyCategoryTreeDto dto) {
        boolean displayed = dto.getDisplayYn() != null ? dto.getDisplayYn().toBoolean() : false;
        Instant createdAt = toInstant(dto.getInsertDate());
        Instant updatedAt = toInstant(dto.getUpdateDate());

        return Category.reconstitute(
                CategoryId.of(dto.getId()),
                CategoryName.of(dto.getCategoryName()),
                CategoryDepth.of(dto.getCategoryDepth()),
                CategoryId.of(dto.getParentCategoryId()),
                CategoryDisplayName.of(dto.getDisplayName()),
                displayed,
                dto.getTargetGroup(),
                dto.getCategoryType(),
                CategoryPath.of(dto.getPath()),
                null,
                createdAt,
                updatedAt);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}

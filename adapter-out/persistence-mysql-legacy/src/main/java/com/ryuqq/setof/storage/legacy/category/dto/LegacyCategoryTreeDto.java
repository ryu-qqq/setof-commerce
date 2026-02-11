package com.ryuqq.setof.storage.legacy.category.dto;

import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * LegacyCategoryTreeDto - 레거시 카테고리 트리 조회용 Projection DTO.
 *
 * <p>Recursive CTE 쿼리 결과를 담는 내부 DTO입니다.
 *
 * <p>Persistence Layer 내부에서만 사용되며 외부로 노출되지 않습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@SuppressWarnings({"PMD.DataClass", "CT_CONSTRUCTOR_THROW"})
public class LegacyCategoryTreeDto {

    private final Long id;
    private final String categoryName;
    private final int categoryDepth;
    private final Long parentCategoryId;
    private final String displayName;
    private final Yn displayYn;
    private final TargetGroup targetGroup;
    private final CategoryType categoryType;
    private final String path;
    private final LocalDateTime insertDate;
    private final LocalDateTime updateDate;

    /**
     * Projections.constructor()용 생성자.
     *
     * <p>Native SQL에서 MySQL은 java.sql.Timestamp 또는 LocalDateTime을 반환할 수 있으므로 Temporal 타입으로 받아
     * LocalDateTime으로 변환합니다.
     */
    public LegacyCategoryTreeDto(
            Long id,
            String categoryName,
            int categoryDepth,
            Long parentCategoryId,
            String displayName,
            Yn displayYn,
            TargetGroup targetGroup,
            CategoryType categoryType,
            String path,
            Temporal insertDate,
            Temporal updateDate) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.parentCategoryId = parentCategoryId;
        this.displayName = displayName;
        this.displayYn = displayYn;
        this.targetGroup = targetGroup;
        this.categoryType = categoryType;
        this.path = path;
        this.insertDate = toLocalDateTime(insertDate);
        this.updateDate = toLocalDateTime(updateDate);
    }

    private static LocalDateTime toLocalDateTime(Temporal temporal) {
        if (temporal == null) {
            return null;
        }
        if (temporal instanceof LocalDateTime ldt) {
            return ldt;
        }
        if (temporal instanceof java.time.Instant instant) {
            return LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault());
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryDepth() {
        return categoryDepth;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    public TargetGroup getTargetGroup() {
        return targetGroup;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

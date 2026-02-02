package com.ryuqq.setof.adapter.out.persistence.category.dto;

import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;

/**
 * CategoryTreeDto - 카테고리 트리 조회용 Projection DTO.
 *
 * <p>Recursive CTE 쿼리 결과를 담는 내부 DTO입니다.
 *
 * <p>Persistence Layer 내부에서만 사용되며 외부로 노출되지 않습니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@SuppressWarnings({"PMD.DataClass", "CT_CONSTRUCTOR_THROW"})
public class CategoryTreeDto {

    private final Long id;
    private final String categoryName;
    private final int categoryDepth;
    private final Long parentCategoryId;
    private final String displayName;
    private final boolean displayed;
    private final TargetGroup targetGroup;
    private final CategoryType categoryType;
    private final String path;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /**
     * Projections.constructor()용 생성자.
     *
     * <p>Native SQL에서 H2는 OffsetDateTime, MySQL은 Instant를 반환할 수 있으므로 Temporal 타입으로 받아 Instant로
     * 변환합니다.
     */
    public CategoryTreeDto(
            Long id,
            String categoryName,
            int categoryDepth,
            Long parentCategoryId,
            String displayName,
            boolean displayed,
            TargetGroup targetGroup,
            CategoryType categoryType,
            String path,
            Temporal createdAt,
            Temporal updatedAt,
            Temporal deletedAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.parentCategoryId = parentCategoryId;
        this.displayName = displayName;
        this.displayed = displayed;
        this.targetGroup = targetGroup;
        this.categoryType = categoryType;
        this.path = path;
        this.createdAt = toInstant(createdAt);
        this.updatedAt = toInstant(updatedAt);
        this.deletedAt = toInstant(deletedAt);
    }

    private static Instant toInstant(Temporal temporal) {
        if (temporal == null) {
            return null;
        }
        if (temporal instanceof Instant instant) {
            return instant;
        }
        if (temporal instanceof OffsetDateTime odt) {
            return odt.toInstant();
        }
        // 지원하지 않는 타입은 null 반환 (예외를 던지면 생성자에서 SpotBugs 경고 발생)
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

    public boolean isDisplayed() {
        return displayed;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}

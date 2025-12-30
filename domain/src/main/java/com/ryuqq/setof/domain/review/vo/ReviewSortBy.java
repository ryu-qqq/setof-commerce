package com.ryuqq.setof.domain.review.vo;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * Review 정렬 기준 Enum
 *
 * @author development-team
 * @since 1.0.0
 */
public enum ReviewSortBy implements SortKey {
    ID("id", "ID순"),
    CREATED_AT("createdAt", "등록일순"),
    RATING("rating", "평점순");

    private final String fieldName;
    private final String displayName;

    ReviewSortBy(String fieldName, String displayName) {
        this.fieldName = fieldName;
        this.displayName = displayName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public String displayName() {
        return displayName;
    }

    public static ReviewSortBy defaultSortBy() {
        return CREATED_AT;
    }

    public static ReviewSortBy fromString(String value) {
        if (value == null || value.isBlank()) {
            return defaultSortBy();
        }
        for (ReviewSortBy sortBy : values()) {
            if (sortBy.name().equalsIgnoreCase(value) || sortBy.fieldName.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        return defaultSortBy();
    }
}

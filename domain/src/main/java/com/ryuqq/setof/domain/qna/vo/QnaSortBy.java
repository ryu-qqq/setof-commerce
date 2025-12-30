package com.ryuqq.setof.domain.qna.vo;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * QnA 정렬 기준 Enum
 *
 * @author development-team
 * @since 1.0.0
 */
public enum QnaSortBy implements SortKey {
    ID("id", "ID순"),
    CREATED_AT("createdAt", "등록일순"),
    UPDATED_AT("updatedAt", "수정일순");

    private final String fieldName;
    private final String displayName;

    QnaSortBy(String fieldName, String displayName) {
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

    public static QnaSortBy defaultSortBy() {
        return CREATED_AT;
    }

    public static QnaSortBy fromString(String value) {
        if (value == null || value.isBlank()) {
            return defaultSortBy();
        }
        for (QnaSortBy sortBy : values()) {
            if (sortBy.name().equalsIgnoreCase(value)
                    || sortBy.fieldName.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        return defaultSortBy();
    }
}

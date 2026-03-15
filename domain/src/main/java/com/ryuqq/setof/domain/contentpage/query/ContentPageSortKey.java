package com.ryuqq.setof.domain.contentpage.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * ContentPage 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum ContentPageSortKey implements SortKey {
    CREATED_AT("createdAt"),
    TITLE("title");

    private final String fieldName;

    ContentPageSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static ContentPageSortKey defaultKey() {
        return CREATED_AT;
    }
}

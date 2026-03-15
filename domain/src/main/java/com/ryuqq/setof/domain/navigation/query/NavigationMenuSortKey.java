package com.ryuqq.setof.domain.navigation.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * NavigationMenu 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum NavigationMenuSortKey implements SortKey {
    CREATED_AT("createdAt"),
    DISPLAY_ORDER("displayOrder"),
    TITLE("title");

    private final String fieldName;

    NavigationMenuSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static NavigationMenuSortKey defaultKey() {
        return DISPLAY_ORDER;
    }
}

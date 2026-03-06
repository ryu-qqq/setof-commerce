package com.ryuqq.setof.domain.wishlist.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * 찜 목록 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum WishlistSortKey implements SortKey {
    CREATED_AT("createdAt");

    private final String fieldName;

    WishlistSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static WishlistSortKey defaultKey() {
        return CREATED_AT;
    }
}

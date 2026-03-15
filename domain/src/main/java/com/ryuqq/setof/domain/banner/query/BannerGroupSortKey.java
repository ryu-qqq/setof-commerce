package com.ryuqq.setof.domain.banner.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * BannerGroup 정렬 키.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum BannerGroupSortKey implements SortKey {
    CREATED_AT("createdAt"),
    TITLE("title"),
    BANNER_TYPE("bannerType");

    private final String fieldName;

    BannerGroupSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    public static BannerGroupSortKey defaultKey() {
        return CREATED_AT;
    }
}

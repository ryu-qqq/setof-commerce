package com.ryuqq.setof.domain.category.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * Category 정렬 키.
 *
 * <p>카테고리 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 */
public enum CategorySortKey implements SortKey {

    /** 등록일시 순 (기본값) */
    CREATED_AT("createdAt"),

    /** 카테고리명 순 */
    CATEGORY_NAME("categoryName"),

    /** 카테고리 깊이 순 */
    CATEGORY_DEPTH("categoryDepth"),

    /** 표시명 순 */
    DISPLAY_NAME("displayName");

    private final String fieldName;

    CategorySortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (등록일시) */
    public static CategorySortKey defaultKey() {
        return CREATED_AT;
    }
}

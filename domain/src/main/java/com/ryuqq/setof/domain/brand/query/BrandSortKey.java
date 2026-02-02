package com.ryuqq.setof.domain.brand.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * Brand 정렬 키.
 *
 * <p>브랜드 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 */
public enum BrandSortKey implements SortKey {

    /** 등록일시 순 (기본값) */
    CREATED_AT("createdAt"),

    /** 표시 순서 */
    DISPLAY_ORDER("displayOrder"),

    /** 브랜드명 순 */
    BRAND_NAME("brandName");

    private final String fieldName;

    BrandSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (표시 순서) */
    public static BrandSortKey defaultKey() {
        return DISPLAY_ORDER;
    }
}

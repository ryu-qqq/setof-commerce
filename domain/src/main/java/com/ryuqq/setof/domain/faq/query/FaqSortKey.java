package com.ryuqq.setof.domain.faq.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * FaqSortKey - FAQ 정렬 키.
 *
 * <p>FAQ 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum FaqSortKey implements SortKey {

    /** 표시 순서 (기본값) */
    DISPLAY_ORDER("displayOrder"),

    /** 상단 고정 순서 (TOP FAQ용) */
    TOP_DISPLAY_ORDER("topDisplayOrder");

    private final String fieldName;

    FaqSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (표시 순서). */
    public static FaqSortKey defaultKey() {
        return DISPLAY_ORDER;
    }
}

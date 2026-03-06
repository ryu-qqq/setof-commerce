package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * DiscountPolicy 정렬 키.
 *
 * <p>할인 정책 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 */
public enum DiscountPolicySortKey implements SortKey {

    /** 등록일시 순 (기본값) */
    CREATED_AT("createdAt"),

    /** 정책명 순 */
    NAME("name"),

    /** 우선순위 순 */
    PRIORITY("priority"),

    /** 시작일 순 */
    START_AT("startAt");

    private final String fieldName;

    DiscountPolicySortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (등록일시) */
    public static DiscountPolicySortKey defaultKey() {
        return CREATED_AT;
    }
}

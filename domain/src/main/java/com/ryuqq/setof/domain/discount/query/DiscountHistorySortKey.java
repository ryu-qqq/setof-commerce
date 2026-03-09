package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * DiscountPolicyHistory 정렬 키.
 *
 * <p>할인 정책 변경 이력 조회 시 사용 가능한 정렬 필드를 정의합니다.
 */
public enum DiscountHistorySortKey implements SortKey {

    /** 변경일시 순 (기본값) */
    CHANGED_AT("changedAt");

    private final String fieldName;

    DiscountHistorySortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (변경일시) */
    public static DiscountHistorySortKey defaultKey() {
        return CHANGED_AT;
    }
}

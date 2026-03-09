package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * DiscountUsage 정렬 키.
 *
 * <p>할인 사용 이력 조회 시 사용 가능한 정렬 필드를 정의합니다.
 */
public enum DiscountUsageSortKey implements SortKey {

    /** 사용일시 순 (기본값) */
    USED_AT("usedAt");

    private final String fieldName;

    DiscountUsageSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (사용일시) */
    public static DiscountUsageSortKey defaultKey() {
        return USED_AT;
    }
}

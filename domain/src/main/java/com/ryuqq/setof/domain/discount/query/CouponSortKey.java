package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * Coupon 정렬 키.
 *
 * <p>쿠폰 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 */
public enum CouponSortKey implements SortKey {

    /** 등록일시 순 (기본값) */
    CREATED_AT("createdAt"),

    /** 쿠폰명 순 */
    COUPON_NAME("couponName"),

    /** 발급 수량 순 */
    ISSUED_COUNT("issuedCount");

    private final String fieldName;

    CouponSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (등록일시) */
    public static CouponSortKey defaultKey() {
        return CREATED_AT;
    }
}

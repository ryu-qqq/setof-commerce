package com.ryuqq.setof.domain.discount.id;

/** 쿠폰 ID Value Object. */
public record CouponId(Long value) {

    public static CouponId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("CouponId 값은 null일 수 없습니다");
        }
        return new CouponId(value);
    }

    public static CouponId forNew() {
        return new CouponId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

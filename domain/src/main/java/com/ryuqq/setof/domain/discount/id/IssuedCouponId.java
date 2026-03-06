package com.ryuqq.setof.domain.discount.id;

/** 발급된 쿠폰 ID Value Object. */
public record IssuedCouponId(Long value) {

    public static IssuedCouponId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("IssuedCouponId 값은 null일 수 없습니다");
        }
        return new IssuedCouponId(value);
    }

    public static IssuedCouponId forNew() {
        return new IssuedCouponId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

package com.ryuqq.setof.domain.discount.vo;

/**
 * 쿠폰 표시명 Value Object.
 *
 * <p>유저에게 보이는 쿠폰 이름입니다. 1~50자 범위.
 */
public record CouponName(String value) {

    private static final int MAX_LENGTH = 50;

    public CouponName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("쿠폰명은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("쿠폰명은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static CouponName of(String value) {
        return new CouponName(value);
    }
}

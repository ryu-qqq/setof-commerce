package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인율 Value Object.
 *
 * <p>0~100 범위의 할인율(%)을 표현합니다.
 */
public record DiscountRate(double value) {

    private static final double MIN_VALUE = 0.0;
    private static final double MAX_VALUE = 100.0;

    public DiscountRate {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                    String.format("할인율은 %.1f~%.1f 사이여야 합니다: %.2f", MIN_VALUE, MAX_VALUE, value));
        }
    }

    public static DiscountRate of(double value) {
        return new DiscountRate(value);
    }

    /** 할인율을 소수점 비율로 반환 (예: 10% → 0.1) */
    public double toFraction() {
        return value / 100.0;
    }
}

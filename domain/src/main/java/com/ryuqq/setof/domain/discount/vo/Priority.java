package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 우선순위 Value Object.
 *
 * <p>0~100 범위. 값이 높을수록 우선 적용됩니다.
 */
public record Priority(int value) {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 100;

    public Priority {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                    String.format("우선순위는 %d~%d 사이여야 합니다: %d", MIN_VALUE, MAX_VALUE, value));
        }
    }

    public static Priority of(int value) {
        return new Priority(value);
    }

    public static Priority defaultPriority() {
        return new Priority(0);
    }

    /** 더 높은 우선순위인지 비교 */
    public boolean isHigherThan(Priority other) {
        return this.value > other.value;
    }
}

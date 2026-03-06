package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 정책명 Value Object.
 *
 * <p>1~50자 범위의 정책명을 표현합니다.
 */
public record DiscountPolicyName(String value) {

    private static final int MAX_LENGTH = 50;

    public DiscountPolicyName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("할인 정책명은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("할인 정책명은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static DiscountPolicyName of(String value) {
        return new DiscountPolicyName(value);
    }
}

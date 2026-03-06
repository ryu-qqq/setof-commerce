package com.ryuqq.setof.domain.discount.id;

/** 할인 정책 ID Value Object. */
public record DiscountPolicyId(Long value) {

    public static DiscountPolicyId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("DiscountPolicyId 값은 null일 수 없습니다");
        }
        return new DiscountPolicyId(value);
    }

    public static DiscountPolicyId forNew() {
        return new DiscountPolicyId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

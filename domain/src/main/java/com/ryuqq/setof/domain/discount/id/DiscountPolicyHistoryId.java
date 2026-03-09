package com.ryuqq.setof.domain.discount.id;

/** 할인 정책 변경 이력 ID Value Object. */
public record DiscountPolicyHistoryId(Long value) {

    public static DiscountPolicyHistoryId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("DiscountPolicyHistoryId 값은 null일 수 없습니다");
        }
        return new DiscountPolicyHistoryId(value);
    }

    public static DiscountPolicyHistoryId forNew() {
        return new DiscountPolicyHistoryId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

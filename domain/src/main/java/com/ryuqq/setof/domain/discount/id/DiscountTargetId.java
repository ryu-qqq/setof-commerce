package com.ryuqq.setof.domain.discount.id;

/** 할인 적용 대상 ID Value Object. */
public record DiscountTargetId(Long value) {

    public static DiscountTargetId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("DiscountTargetId 값은 null일 수 없습니다");
        }
        return new DiscountTargetId(value);
    }

    public static DiscountTargetId forNew() {
        return new DiscountTargetId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

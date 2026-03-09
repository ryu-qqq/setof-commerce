package com.ryuqq.setof.domain.discount.id;

/** 할인 사용 이력 ID Value Object. */
public record DiscountUsageId(Long value) {

    public static DiscountUsageId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("DiscountUsageId 값은 null일 수 없습니다");
        }
        return new DiscountUsageId(value);
    }

    public static DiscountUsageId forNew() {
        return new DiscountUsageId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

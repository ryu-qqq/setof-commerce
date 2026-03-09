package com.ryuqq.setof.domain.discount.id;

/** 할인 아웃박스 ID Value Object. */
public record DiscountOutboxId(Long value) {

    public static DiscountOutboxId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("DiscountOutboxId 값은 null일 수 없습니다");
        }
        return new DiscountOutboxId(value);
    }

    public static DiscountOutboxId forNew() {
        return new DiscountOutboxId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

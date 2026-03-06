package com.ryuqq.setof.domain.refund.id;

/** 반품 아이템 ID Value Object. */
public record RefundItemId(Long value) {

    public static RefundItemId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("RefundItemId 값은 null일 수 없습니다");
        }
        return new RefundItemId(value);
    }

    public static RefundItemId forNew() {
        return new RefundItemId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

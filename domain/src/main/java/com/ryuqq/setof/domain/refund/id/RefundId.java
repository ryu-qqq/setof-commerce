package com.ryuqq.setof.domain.refund.id;

/** 반품 ID Value Object. */
public record RefundId(Long value) {

    public static RefundId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("RefundId 값은 null일 수 없습니다");
        }
        return new RefundId(value);
    }

    public static RefundId forNew() {
        return new RefundId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

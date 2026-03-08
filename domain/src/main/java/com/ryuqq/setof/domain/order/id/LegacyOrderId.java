package com.ryuqq.setof.domain.order.id;

/** 주문 ID Value Object (레거시 스키마). */
public record LegacyOrderId(Long value) {

    public static LegacyOrderId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("LegacyOrderId 값은 null일 수 없습니다");
        }
        return new LegacyOrderId(value);
    }

    public static LegacyOrderId forNew() {
        return new LegacyOrderId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

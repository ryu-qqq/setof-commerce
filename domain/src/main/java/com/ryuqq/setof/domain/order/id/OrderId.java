package com.ryuqq.setof.domain.order.id;

/** 주문 ID Value Object (새 스키마). */
public record OrderId(String value) {

    public static OrderId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OrderId 값은 null이거나 빈 문자열일 수 없습니다");
        }
        return new OrderId(value);
    }

    public static OrderId forNew() {
        return new OrderId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

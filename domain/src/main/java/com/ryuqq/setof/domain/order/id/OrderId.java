package com.ryuqq.setof.domain.order.id;

/** 주문 ID Value Object. */
public record OrderId(Long value) {

    public static OrderId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("OrderId 값은 null일 수 없습니다");
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

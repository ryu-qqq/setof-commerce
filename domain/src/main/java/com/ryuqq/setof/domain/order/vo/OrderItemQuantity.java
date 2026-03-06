package com.ryuqq.setof.domain.order.vo;

/** 주문 수량 Value Object. */
public record OrderItemQuantity(int value) {

    public OrderItemQuantity {
        if (value < 1) {
            throw new IllegalArgumentException("주문 수량은 1 이상이어야 합니다: " + value);
        }
    }

    public static OrderItemQuantity of(int value) {
        return new OrderItemQuantity(value);
    }
}

package com.ryuqq.setof.domain.cart.id;

/**
 * 장바구니 아이템 ID Value Object.
 *
 * <p>장바구니 아이템을 식별하는 Long 기반 ID입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartItemId(Long value) {

    public static CartItemId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("CartItemId 값은 null일 수 없습니다");
        }
        return new CartItemId(value);
    }

    public static CartItemId forNew() {
        return new CartItemId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

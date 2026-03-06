package com.ryuqq.setof.domain.cart.vo;

/**
 * 장바구니 수량 Value Object.
 *
 * <p>장바구니 아이템의 수량을 나타냅니다. 최소 1, 최대 999개까지 허용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartQuantity(int value) {

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 999;

    public CartQuantity {
        if (value < MIN_QUANTITY) {
            throw new IllegalArgumentException(
                    String.format("장바구니 수량은 %d 이상이어야 합니다: %d", MIN_QUANTITY, value));
        }
        if (value > MAX_QUANTITY) {
            throw new IllegalArgumentException(
                    String.format("장바구니 수량은 %d 이하여야 합니다: %d", MAX_QUANTITY, value));
        }
    }

    public static CartQuantity of(int value) {
        return new CartQuantity(value);
    }

    public CartQuantity increase(int amount) {
        return new CartQuantity(this.value + amount);
    }

    public CartQuantity decrease(int amount) {
        return new CartQuantity(this.value - amount);
    }
}

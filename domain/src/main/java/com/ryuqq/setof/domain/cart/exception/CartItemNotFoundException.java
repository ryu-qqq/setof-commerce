package com.ryuqq.setof.domain.cart.exception;

/**
 * 장바구니 아이템 미발견 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class CartItemNotFoundException extends CartException {

    private static final CartErrorCode ERROR_CODE = CartErrorCode.CART_ITEM_NOT_FOUND;

    public CartItemNotFoundException() {
        super(ERROR_CODE);
    }

    public CartItemNotFoundException(Long cartItemId) {
        super(ERROR_CODE, String.format("ID가 %d인 장바구니 아이템을 찾을 수 없습니다", cartItemId));
    }
}

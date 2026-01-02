package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.cart.vo.CartItemId;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * CartItemNotFoundException - 장바구니 아이템 미존재 예외
 *
 * <p>요청한 장바구니 아이템이 존재하지 않는 경우 발생합니다.
 */
public class CartItemNotFoundException extends DomainException {

    public CartItemNotFoundException(CartItemId cartItemId) {
        super(
                CartErrorCode.CART_ITEM_NOT_FOUND,
                String.format("장바구니 아이템을 찾을 수 없습니다: %d", cartItemId.value()),
                Map.of("cartItemId", cartItemId.value()));
    }

    public CartItemNotFoundException(Long productStockId) {
        super(
                CartErrorCode.CART_ITEM_NOT_FOUND,
                String.format("해당 상품이 장바구니에 없습니다: productStockId=%d", productStockId),
                Map.of("productStockId", productStockId));
    }
}

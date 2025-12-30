package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * InvalidCartItemIdException - 유효하지 않은 장바구니 아이템 ID 예외
 *
 * <p>장바구니 아이템 ID가 유효하지 않은 경우 발생합니다.
 */
public class InvalidCartItemIdException extends DomainException {

    public InvalidCartItemIdException(String message) {
        super(CartErrorCode.INVALID_CART_ITEM_ID, message);
    }
}

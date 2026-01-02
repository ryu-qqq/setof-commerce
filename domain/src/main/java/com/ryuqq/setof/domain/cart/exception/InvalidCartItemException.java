package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * InvalidCartItemException - 유효하지 않은 장바구니 아이템 예외
 *
 * <p>장바구니 아이템 데이터가 유효하지 않은 경우 발생합니다.
 */
public class InvalidCartItemException extends DomainException {

    public InvalidCartItemException(String message) {
        super(CartErrorCode.INVALID_CART_ITEM, message);
    }
}

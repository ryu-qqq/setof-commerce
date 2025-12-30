package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * InvalidCartIdException - 유효하지 않은 장바구니 ID 예외
 *
 * <p>장바구니 ID가 유효하지 않은 경우 발생합니다.
 */
public class InvalidCartIdException extends DomainException {

    public InvalidCartIdException(String message) {
        super(CartErrorCode.INVALID_CART_ID, message);
    }
}

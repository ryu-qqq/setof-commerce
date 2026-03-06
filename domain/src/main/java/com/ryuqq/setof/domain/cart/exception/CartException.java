package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 장바구니 도메인 예외 기본 클래스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class CartException extends DomainException {

    public CartException(CartErrorCode errorCode) {
        super(errorCode);
    }

    public CartException(CartErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public CartException(CartErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}

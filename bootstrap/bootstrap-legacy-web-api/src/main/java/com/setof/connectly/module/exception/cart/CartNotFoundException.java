package com.setof.connectly.module.exception.cart;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends CartException {

    public static final String CODE = "CART-404";
    public static final String MESSAGE = "카트의 정보가 존재하지 않습니다";

    public CartNotFoundException(long cartId) {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE + cartId);
    }
}

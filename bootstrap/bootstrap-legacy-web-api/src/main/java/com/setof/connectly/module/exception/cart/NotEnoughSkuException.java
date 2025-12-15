package com.setof.connectly.module.exception.cart;

import org.springframework.http.HttpStatus;

public class NotEnoughSkuException extends CartException {

    public static final String CODE = "CART-400";
    public static final String MESSAGE = "해당 제품의 재고 수를 초과했습니다. 잔여 수량 ";
    public static final String TITLE = "앗! 재고가 없어요";

    public NotEnoughSkuException(int stock) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE + stock, TITLE);
    }
}

package com.setof.connectly.module.exception.discount;

import org.springframework.http.HttpStatus;

public class DiscountNotFoundException extends DiscountException {

    public static final String CODE = "DISCOUNT-404";
    public static final String MESSAGE = "해당 할인 정책이 존재하지 않습니다";

    public DiscountNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}

package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class ShippingAddressNotFoundException extends UserException {

    public static final String CODE = "USER_SHIPPING-404";
    public static final String MESSAGE = "해당 배송 정보를 찾을 수 없습니다.";

    public ShippingAddressNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

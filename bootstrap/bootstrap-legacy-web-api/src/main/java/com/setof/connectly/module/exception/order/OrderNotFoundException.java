package com.setof.connectly.module.exception.order;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends OrderException {

    public static final String CODE = "ORDER-404";
    public static final String MESSAGE = "주문 건을 찾을 수 없습니다.";

    public OrderNotFoundException(long orderId) {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE + orderId);
    }
}

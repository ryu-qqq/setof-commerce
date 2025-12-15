package com.setof.connectly.module.exception.product;

public class ProductNotFoundException extends ProductException {

    public static final String CODE = "PRODUCT-404";
    public static final String MESSAGE = "해당 제품이 존재하지 않습니다";
    public static final String MESSAGES = "해당 제품군이 존재하지 않습니다";

    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.NOT_FOUND;

    public ProductNotFoundException(long productId) {
        super(CODE, HttpStatus, MESSAGE + productId);
    }

    public ProductNotFoundException() {
        super(CODE, HttpStatus, MESSAGES);
    }
}

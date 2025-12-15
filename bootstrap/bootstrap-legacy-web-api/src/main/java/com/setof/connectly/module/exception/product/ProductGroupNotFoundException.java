package com.setof.connectly.module.exception.product;

public class ProductGroupNotFoundException extends ProductException {

    public static final String CODE = "PRODUCT_GROUP-404";
    public static final String MESSAGE = "해당 제품 그룹이 존재하지 않습니다";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.NOT_FOUND;

    public ProductGroupNotFoundException(long productGroupId) {
        super(CODE, HttpStatus, MESSAGE + " " + productGroupId);
    }
}

package com.setof.connectly.module.exception.product;

public class ProductDescriptionNotFoundException extends ProductException {

    public static final String CODE = "PRODUCT_GROUP-404";
    public static final String MESSAGE = "해당 제품 그룹 상세 페이지가 존재하지 않습니다";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.NOT_FOUND;

    public ProductDescriptionNotFoundException(long productGroupId) {
        super(CODE, HttpStatus, MESSAGE + productGroupId);
    }
}

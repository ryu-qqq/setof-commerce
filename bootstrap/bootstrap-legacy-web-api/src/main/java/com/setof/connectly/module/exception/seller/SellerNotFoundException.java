package com.setof.connectly.module.exception.seller;

public class SellerNotFoundException extends SellerException {

    public static final String CODE = "SELLER-404";
    public static final String MESSAGE = "해당 셀러가 존재하지 않습니다  ";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.NOT_FOUND;

    public SellerNotFoundException(long sellerId) {
        super(CODE, HttpStatus, MESSAGE + sellerId);
    }
}

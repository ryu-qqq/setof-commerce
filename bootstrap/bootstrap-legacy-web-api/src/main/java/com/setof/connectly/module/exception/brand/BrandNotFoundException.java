package com.setof.connectly.module.exception.brand;

public class BrandNotFoundException extends BrandException {

    public static final String CODE = "BRAND-404";
    public static final String MESSAGE = "해당 브랜드가 존재하지 않습니다";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.NOT_FOUND;

    public BrandNotFoundException(long brandId) {
        super(CODE, HttpStatus, MESSAGE + brandId);
    }
}

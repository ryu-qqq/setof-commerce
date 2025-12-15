package com.setof.connectly.module.exception.user;

public class ExcessShippingAddressException extends UserException {

    public static final String CODE = "USER-400";
    public static final String MESSAGE = "배송 정보는 최대 5개까지 등록 가능 합니다.";
    public static final org.springframework.http.HttpStatus HttpStatus =
            org.springframework.http.HttpStatus.BAD_REQUEST;

    public ExcessShippingAddressException() {
        super(CODE, HttpStatus, MESSAGE);
    }
}

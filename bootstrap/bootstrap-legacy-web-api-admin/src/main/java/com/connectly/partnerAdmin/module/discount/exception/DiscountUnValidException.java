package com.connectly.partnerAdmin.module.discount.exception;

import org.springframework.http.HttpStatus;

public class DiscountUnValidException extends DiscountException{

    public static final String CODE = "DISCOUNT-400";
    public static final String MESSAGE = "할인 정책의 종료기간은 현재 시간 보다 미래여야 합니다.";

    public DiscountUnValidException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }

}

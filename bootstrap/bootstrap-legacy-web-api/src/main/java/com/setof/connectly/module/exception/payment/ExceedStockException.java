package com.setof.connectly.module.exception.payment;

import org.springframework.http.HttpStatus;

public class ExceedStockException extends PaymentException {

    public static final String CODE = "PAYMENT-400";
    public static final String TITLE = "앗! 재고가 없어요";

    public ExceedStockException(String productGroupName, long stock) {
        super(CODE, HttpStatus.BAD_REQUEST, buildMessage(productGroupName, stock), TITLE);
    }

    private static String buildMessage(String productGroupName, long stock) {
        return String.format("%s의 재고가 %d개 남았습니다. 서비스 이용에 불편을 드려 죄송합니다.", productGroupName, stock);
    }
}

package com.setof.connectly.module.exception.order;

import org.springframework.http.HttpStatus;

public class ExpireReturnRequestException extends OrderException {

    public static final String CODE = "ORDER-400";
    private static final String BASE_MESSAGE = "해당 주문의 반품 요청 기간이 지났습니다.";

    public ExpireReturnRequestException() {
        super(CODE, HttpStatus.BAD_REQUEST, buildMessage());
    }

    private static String buildMessage() {
        return BASE_MESSAGE + String.format("배송 완료 기준 7일 이내에 반품 또는 교환을 요청할 수 있습니다");
    }
}

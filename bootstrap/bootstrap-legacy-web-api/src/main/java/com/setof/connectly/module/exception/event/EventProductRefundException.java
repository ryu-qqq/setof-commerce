package com.setof.connectly.module.exception.event;

import org.springframework.http.HttpStatus;

public class EventProductRefundException extends EventException {

    public static final String CODE = "EVENT-400";
    public static final String TITLE = "앗! 잠깐만요";
    public static final String MESSAGE = "해당 상품은 환불이 불가능해요";

    public EventProductRefundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE, TITLE);
    }
}

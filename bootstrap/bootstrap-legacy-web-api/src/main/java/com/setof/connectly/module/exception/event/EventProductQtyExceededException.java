package com.setof.connectly.module.exception.event;

import org.springframework.http.HttpStatus;

public class EventProductQtyExceededException extends EventException {

    public static final String CODE = "EVENT-400";
    public static final String TITLE = "앗! 잠깐만요";
    public static final String MESSAGE = "해당 상품의 구매 가능 수를 초과했어요.";

    public EventProductQtyExceededException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE, TITLE);
    }
}

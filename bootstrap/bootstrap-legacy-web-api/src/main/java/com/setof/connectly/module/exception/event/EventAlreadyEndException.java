package com.setof.connectly.module.exception.event;

import org.springframework.http.HttpStatus;

public class EventAlreadyEndException extends EventException {

    public static final String CODE = "EVENT-400";
    public static final String TITLE = "앗! 잠깐만요";
    public static final String MESSAGE = "이벤트가 종료된 상품입니다.";

    public EventAlreadyEndException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE, TITLE);
    }
}

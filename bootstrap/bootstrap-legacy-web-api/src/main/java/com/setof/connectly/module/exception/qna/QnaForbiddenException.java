package com.setof.connectly.module.exception.qna;

public class QnaForbiddenException extends QnaException{

    public static final String CODE = "QNA_ORDER-400";
    public static final String MESSAGE = "주문 QNA는 본인만 볼 수 있습니다.";
    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.BAD_REQUEST;

    public QnaForbiddenException() {
        super(CODE, HttpStatus, MESSAGE);
    }
}

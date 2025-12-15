package com.setof.connectly.module.exception.qna;


public class QnaNotFoundException extends QnaException{

    public static final String CODE = "QNA-404";
    public static final String MESSAGE = "해당 QNA가 존재하지 않습니다 ";
    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.NOT_FOUND;


    public QnaNotFoundException(long qnaId) {
        super(CODE, HttpStatus, MESSAGE + qnaId);
    }
}

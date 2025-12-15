package com.setof.connectly.module.exception.qna;


public class QnaAnswerNotFoundException extends QnaException{

    public static final String CODE = "QNA-ANSWER-404";
    public static final String MESSAGE = "해당 QNA 답변이  존재하지 않습니다";
    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.NOT_FOUND;


    public QnaAnswerNotFoundException(long qnaId) {
        super(CODE, HttpStatus, MESSAGE + qnaId);
    }
}

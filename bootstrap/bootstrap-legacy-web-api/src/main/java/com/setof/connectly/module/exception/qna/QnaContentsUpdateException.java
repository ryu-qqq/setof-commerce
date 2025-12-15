package com.setof.connectly.module.exception.qna;

public class QnaContentsUpdateException extends QnaException{

    public static final String CODE = "QNA-ANSWER-400";
    public static final String MESSAGE = "해당 QNA 답변이 닫혀 수정을 할 수 없습니다.";
    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.NOT_FOUND;

    public QnaContentsUpdateException(long qnaId) {
        super(CODE, HttpStatus, MESSAGE + qnaId);
    }
}

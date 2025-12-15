package com.setof.connectly.module.exception.qna;

import org.springframework.http.HttpStatus;

public class QnaReplyException extends QnaException{

    public static final String CODE = "QNA-ANSWER-400";
    public static final String MESSAGE = "해당 QNA에 추가적으로 답변을 할 수 없습니다. 답변이 완료될 때까지 기다려 주세요. ";
    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.BAD_REQUEST;

    public QnaReplyException(long qnaId) {
        super(CODE, HttpStatus, MESSAGE + qnaId);
    }
}

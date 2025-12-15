package com.setof.connectly.module.exception.qna;

public class QnaImagesExceedException extends QnaException{

    public static final String CODE = "QNA-IMAGES-400";
    public static final String MESSAGE = "QNA 사진 이미지는 최대 3장만 등록 가능합니다. ";
    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.BAD_REQUEST;

    public QnaImagesExceedException() {
        super(CODE, HttpStatus, MESSAGE);
    }
}

package com.setof.connectly.module.exception.content;

import org.springframework.http.HttpStatus;

public class ContentNotFoundException extends ContentException {

    public static final String CODE = "CONTENT-404";
    public static final String MESSAGE = "해당 컨텐츠가 존재하지 않습니다";

    public ContentNotFoundException(long contentId) {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE + contentId);
    }
}

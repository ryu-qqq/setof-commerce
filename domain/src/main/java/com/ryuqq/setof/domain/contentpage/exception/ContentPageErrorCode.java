package com.ryuqq.setof.domain.contentpage.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 콘텐츠 페이지 도메인 에러 코드. */
public enum ContentPageErrorCode implements ErrorCode {
    CONTENT_PAGE_NOT_FOUND("CTP-001", 404, "콘텐츠 페이지를 찾을 수 없습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ContentPageErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

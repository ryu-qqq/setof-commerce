package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 상품고시 도메인 에러 코드. */
public enum ProductNoticeErrorCode implements ErrorCode {
    NOTICE_NOT_FOUND("NOTICE-001", 404, "상품고시를 찾을 수 없습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductNoticeErrorCode(String code, int httpStatus, String message) {
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

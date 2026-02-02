package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 카테고리 도메인 에러 코드. */
public enum CategoryErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND("CTG-001", 404, "카테고리를 찾을 수 없습니다"),
    CATEGORY_DUPLICATE("CTG-002", 409, "동일한 카테고리가 이미 존재합니다"),
    CATEGORY_INACTIVE("CTG-003", 400, "비활성화된 카테고리입니다"),
    PARENT_CATEGORY_NOT_FOUND("CTG-004", 404, "부모 카테고리를 찾을 수 없습니다"),
    INVALID_CATEGORY_DEPTH("CTG-005", 400, "유효하지 않은 카테고리 깊이입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    CategoryErrorCode(String code, int httpStatus, String message) {
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

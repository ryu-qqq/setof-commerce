package com.ryuqq.setof.domain.brand.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 브랜드 도메인 에러 코드. */
public enum BrandErrorCode implements ErrorCode {
    BRAND_NOT_FOUND("BRD-001", 404, "브랜드를 찾을 수 없습니다"),
    BRAND_DUPLICATE("BRD-002", 409, "동일한 브랜드가 이미 존재합니다"),
    BRAND_INACTIVE("BRD-003", 400, "비활성화된 브랜드입니다"),
    INVALID_DISPLAY_ORDER("BRD-004", 400, "표시 순서는 0 이상이어야 합니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    BrandErrorCode(String code, int httpStatus, String message) {
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

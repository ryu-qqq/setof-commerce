package com.ryuqq.setof.domain.productgroupimage.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 상품그룹 이미지 도메인 에러 코드. */
public enum ProductGroupImageErrorCode implements ErrorCode {
    NO_THUMBNAIL("PGI-001", 400, "대표 이미지(THUMBNAIL)가 최소 1개 필요합니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductGroupImageErrorCode(String code, int httpStatus, String message) {
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

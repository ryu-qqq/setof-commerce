package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 상품그룹 상세설명 도메인 에러 코드. */
public enum ProductDescriptionErrorCode implements ErrorCode {
    DESCRIPTION_NOT_FOUND("DESC-001", 404, "상세설명을 찾을 수 없습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductDescriptionErrorCode(String code, int httpStatus, String message) {
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

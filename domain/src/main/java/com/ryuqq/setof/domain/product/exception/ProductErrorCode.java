package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 상품(SKU) 도메인 에러 코드. */
public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND("PRD-001", 404, "상품을 찾을 수 없습니다"),
    PRODUCT_ALREADY_DELETED("PRD-002", 400, "이미 삭제된 상품입니다"),
    INVALID_STATUS_TRANSITION("PRD-003", 400, "유효하지 않은 상태 전이입니다"),
    PRODUCT_INVALID_PRICE("PRD-004", 400, "가격 체계가 유효하지 않습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductErrorCode(String code, int httpStatus, String message) {
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

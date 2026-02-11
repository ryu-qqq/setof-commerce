package com.ryuqq.setof.domain.productgroup.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 상품그룹 도메인 에러 코드. */
public enum ProductGroupErrorCode implements ErrorCode {
    PRODUCT_GROUP_NOT_FOUND("PG-001", 404, "상품그룹을 찾을 수 없습니다"),
    PRODUCT_GROUP_ALREADY_DELETED("PG-002", 400, "이미 삭제된 상품그룹입니다"),
    INVALID_STATUS_TRANSITION("PG-003", 400, "유효하지 않은 상태 전이입니다"),
    INVALID_PRICE("PG-004", 400, "유효하지 않은 가격 정보입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductGroupErrorCode(String code, int httpStatus, String message) {
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

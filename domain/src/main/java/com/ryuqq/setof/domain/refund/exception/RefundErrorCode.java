package com.ryuqq.setof.domain.refund.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 반품 도메인 에러 코드. */
public enum RefundErrorCode implements ErrorCode {
    REFUND_NOT_FOUND("RFD-001", 404, "반품을 찾을 수 없습니다"),
    INVALID_REFUND_STATUS_TRANSITION("RFD-002", 400, "유효하지 않은 반품 상태 전이입니다"),
    REFUND_ALREADY_EXISTS("RFD-003", 409, "해당 주문 아이템에 대한 반품이 이미 존재합니다"),
    REFUND_ON_HOLD("RFD-004", 400, "보류 중인 반품입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    RefundErrorCode(String code, int httpStatus, String message) {
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

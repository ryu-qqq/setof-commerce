package com.ryuqq.setof.domain.cancel.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 취소 도메인 에러 코드. */
public enum CancelErrorCode implements ErrorCode {
    CANCEL_NOT_FOUND("CNL-001", 404, "취소를 찾을 수 없습니다"),
    INVALID_CANCEL_STATUS_TRANSITION("CNL-002", 400, "유효하지 않은 취소 상태 전이입니다"),
    CANCEL_ALREADY_EXISTS("CNL-003", 409, "해당 주문 아이템에 대한 취소가 이미 존재합니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    CancelErrorCode(String code, int httpStatus, String message) {
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

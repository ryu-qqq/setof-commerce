package com.ryuqq.setof.domain.settlement.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 정산 도메인 에러 코드. */
public enum SettlementErrorCode implements ErrorCode {
    SETTLEMENT_NOT_FOUND("STL-001", 404, "정산 정보를 찾을 수 없습니다"),
    INVALID_SETTLEMENT_STATUS_TRANSITION("STL-002", 400, "유효하지 않은 정산 상태 전이입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    SettlementErrorCode(String code, int httpStatus, String message) {
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

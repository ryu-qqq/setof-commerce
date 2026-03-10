package com.ryuqq.setof.domain.mileage.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 마일리지 도메인 에러 코드. */
public enum MileageErrorCode implements ErrorCode {
    MILEAGE_NOT_FOUND("MLG-001", 404, "마일리지 정보를 찾을 수 없습니다"),
    INSUFFICIENT_BALANCE("MLG-002", 400, "마일리지 잔액이 부족합니다"),
    BELOW_MIN_USAGE("MLG-003", 400, "최소 사용 금액 미만입니다"),
    EXCEEDS_MAX_USAGE_RATIO("MLG-004", 400, "최대 사용 비율을 초과했습니다"),
    INVALID_MILEAGE_AMOUNT("MLG-005", 400, "유효하지 않은 마일리지 금액입니다"),
    INVALID_ENTRY_STATUS("MLG-006", 400, "유효하지 않은 마일리지 상태입니다"),
    LEDGER_ALREADY_EXISTS("MLG-007", 409, "마일리지 원장이 이미 존재합니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    MileageErrorCode(String code, int httpStatus, String message) {
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

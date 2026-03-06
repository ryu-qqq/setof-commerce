package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 배송 도메인 에러 코드. */
public enum ShipmentErrorCode implements ErrorCode {
    SHIPMENT_NOT_FOUND("SHP-001", 404, "배송 정보를 찾을 수 없습니다"),
    INVALID_DELIVERY_STATUS_TRANSITION("SHP-002", 400, "유효하지 않은 배송 상태 전이입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ShipmentErrorCode(String code, int httpStatus, String message) {
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

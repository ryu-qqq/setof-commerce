package com.ryuqq.setof.domain.claim.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 클레임 배송 도메인 에러 코드. */
public enum ClaimShipmentErrorCode implements ErrorCode {
    CLAIM_SHIPMENT_NOT_FOUND("CLS-001", 404, "클레임 배송을 찾을 수 없습니다"),
    INVALID_CLAIM_SHIPMENT_STATUS_TRANSITION("CLS-002", 400, "유효하지 않은 클레임 배송 상태 전이입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ClaimShipmentErrorCode(String code, int httpStatus, String message) {
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

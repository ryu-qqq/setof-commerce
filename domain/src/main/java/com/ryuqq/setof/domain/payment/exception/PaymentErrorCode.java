package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 결제 도메인 에러 코드. */
public enum PaymentErrorCode implements ErrorCode {
    PAYMENT_NOT_FOUND("PMT-001", 404, "결제 정보를 찾을 수 없습니다"),
    INVALID_PAYMENT_STATUS("PMT-002", 400, "유효하지 않은 결제 상태 전이입니다"),
    PAYMENT_AMOUNT_MISMATCH("PMT-003", 400, "결제 금액이 일치하지 않습니다"),
    REFUND_AMOUNT_EXCEEDS_PAYMENT("PMT-004", 400, "환불 금액이 결제 금액을 초과합니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    PaymentErrorCode(String code, int httpStatus, String message) {
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

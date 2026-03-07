package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 환불 계좌 도메인 에러 코드. */
public enum RefundAccountErrorCode implements ErrorCode {
    REFUND_ACCOUNT_NOT_FOUND("RFA-001", 404, "환불 계좌를 찾을 수 없습니다"),
    ACCOUNT_VERIFICATION_FAILED("RFA-002", 400, "계좌 실명 검증에 실패했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    RefundAccountErrorCode(String code, int httpStatus, String message) {
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

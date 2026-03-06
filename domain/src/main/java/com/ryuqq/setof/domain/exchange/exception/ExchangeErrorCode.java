package com.ryuqq.setof.domain.exchange.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 교환 도메인 에러 코드. */
public enum ExchangeErrorCode implements ErrorCode {
    EXCHANGE_NOT_FOUND("EXC-001", 404, "교환을 찾을 수 없습니다"),
    INVALID_EXCHANGE_STATUS_TRANSITION("EXC-002", 400, "유효하지 않은 교환 상태 전이입니다"),
    EXCHANGE_ALREADY_EXISTS("EXC-003", 409, "해당 주문 아이템에 대한 교환이 이미 존재합니다"),
    EXCHANGE_TARGET_NOT_SET("EXC-004", 400, "교환 대상 상품이 설정되지 않았습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ExchangeErrorCode(String code, int httpStatus, String message) {
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

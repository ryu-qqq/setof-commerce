package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 주문 도메인 에러 코드. */
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND("ORD-001", 404, "주문을 찾을 수 없습니다"),
    ORDER_ITEM_NOT_FOUND("ORD-002", 404, "주문 아이템을 찾을 수 없습니다"),
    INVALID_ORDER_STATUS_TRANSITION("ORD-003", 400, "유효하지 않은 주문 상태 전이입니다"),
    INVALID_ORDER_ITEM_STATUS_TRANSITION("ORD-004", 400, "유효하지 않은 주문 아이템 상태 전이입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    OrderErrorCode(String code, int httpStatus, String message) {
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

package com.ryuqq.setof.domain.stock.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * 재고 도메인 에러 코드.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum StockErrorCode implements ErrorCode {
    INSUFFICIENT_STOCK("STOCK-001", 409, "재고가 부족합니다"),
    STOCK_NOT_FOUND("STOCK-002", 404, "재고 정보를 찾을 수 없습니다"),
    STOCK_LOCK_FAILED("STOCK-003", 409, "재고 락 획득에 실패했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    StockErrorCode(String code, int httpStatus, String message) {
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

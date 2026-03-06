package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 할인 도메인 에러 코드. */
public enum DiscountErrorCode implements ErrorCode {
    DISCOUNT_POLICY_NOT_FOUND("DISC-001", 404, "할인 정책을 찾을 수 없습니다"),
    INVALID_DISCOUNT_CONFIG("DISC-002", 400, "잘못된 할인 설정입니다"),
    INSUFFICIENT_BUDGET("DISC-003", 400, "할인 예산이 부족합니다"),
    COUPON_NOT_FOUND("DISC-004", 404, "쿠폰을 찾을 수 없습니다"),
    COUPON_ALREADY_ISSUED("DISC-005", 409, "이미 발급된 쿠폰입니다"),
    COUPON_EXPIRED("DISC-006", 400, "만료된 쿠폰입니다"),
    COUPON_ISSUANCE_LIMIT_EXCEEDED("DISC-007", 400, "쿠폰 발급 한도를 초과했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    DiscountErrorCode(String code, int httpStatus, String message) {
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

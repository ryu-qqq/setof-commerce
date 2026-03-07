package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 배송지 도메인 에러 코드. */
public enum ShippingAddressErrorCode implements ErrorCode {
    SHIPPING_ADDRESS_NOT_FOUND("SHP-001", 404, "배송지를 찾을 수 없습니다"),
    CANNOT_UNMARK_DEFAULT_SHIPPING_ADDRESS("SHP-002", 400, "기본 배송지 설정은 해제할 수 없습니다"),
    SHIPPING_ADDRESS_LIMIT_EXCEEDED("SHP-003", 400, "배송지는 최대 5개까지 등록할 수 있습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ShippingAddressErrorCode(String code, int httpStatus, String message) {
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

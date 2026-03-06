package com.ryuqq.setof.domain.banner.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 배너 도메인 에러 코드. */
public enum BannerErrorCode implements ErrorCode {
    BANNER_GROUP_NOT_FOUND("BNR-001", 404, "배너 그룹을 찾을 수 없습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    BannerErrorCode(String code, int httpStatus, String message) {
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

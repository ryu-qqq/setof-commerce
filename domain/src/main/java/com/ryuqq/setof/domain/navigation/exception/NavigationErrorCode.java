package com.ryuqq.setof.domain.navigation.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 네비게이션 도메인 에러 코드. */
public enum NavigationErrorCode implements ErrorCode {
    NAVIGATION_MENU_NOT_FOUND("NAV-001", 404, "네비게이션 메뉴를 찾을 수 없습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    NavigationErrorCode(String code, int httpStatus, String message) {
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

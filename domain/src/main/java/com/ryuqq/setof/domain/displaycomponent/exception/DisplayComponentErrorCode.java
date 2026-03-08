package com.ryuqq.setof.domain.displaycomponent.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * DisplayComponentErrorCode - 전시 컴포넌트 에러 코드.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum DisplayComponentErrorCode implements ErrorCode {
    DISPLAY_COMPONENT_NOT_FOUND("DSP-001", 404, "전시 컴포넌트를 찾을 수 없습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    DisplayComponentErrorCode(String code, int httpStatus, String message) {
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

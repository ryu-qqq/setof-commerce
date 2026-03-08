package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * 리뷰 도메인 에러 코드.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_NOT_FOUND("RVW-001", 404, "리뷰를 찾을 수 없습니다"),
    REVIEW_ALREADY_WRITTEN("RVW-003", 409, "이미 리뷰를 작성한 주문입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ReviewErrorCode(String code, int httpStatus, String message) {
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

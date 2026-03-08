package com.ryuqq.setof.domain.reviewimage.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * 리뷰 이미지 도메인 에러 코드.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum ReviewImageErrorCode implements ErrorCode {
    REVIEW_IMAGE_LIMIT_EXCEEDED("RVI-001", 400, "리뷰 이미지는 최대 3장까지 등록할 수 있습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ReviewImageErrorCode(String code, int httpStatus, String message) {
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

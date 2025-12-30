package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

public enum ReviewErrorCode implements ErrorCode {
    REVIEW_NOT_FOUND("REVIEW-001", 404, "리뷰를 찾을 수 없습니다."),
    DUPLICATE_REVIEW("REVIEW-002", 409, "이미 작성한 리뷰가 있습니다."),
    REVIEW_NOT_OWNED("REVIEW-003", 403, "본인이 작성한 리뷰만 수정/삭제할 수 있습니다."),
    INVALID_RATING("REVIEW-004", 400, "평점은 1~5 사이의 값이어야 합니다."),
    INVALID_REVIEW_CONTENT("REVIEW-005", 400, "유효하지 않은 리뷰 내용입니다."),
    REVIEW_IMAGE_LIMIT_EXCEEDED("REVIEW-006", 400, "리뷰 이미지는 최대 3개까지 첨부할 수 있습니다."),
    REVIEW_ALREADY_DELETED("REVIEW-007", 400, "이미 삭제된 리뷰입니다.");

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

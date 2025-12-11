package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Category Bounded Context 에러 코드
 *
 * <p>카테고리 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: CAT-XXX
 *
 * <ul>
 *   <li>CAT-001 ~ CAT-099: 카테고리 조회 관련
 *   <li>CAT-100 ~ CAT-199: 카테고리 정보 유효성 관련
 * </ul>
 */
public enum CategoryErrorCode implements ErrorCode {

    // === 카테고리 조회 관련 (CAT-001 ~ CAT-099) ===
    CATEGORY_NOT_FOUND("CAT-001", 404, "카테고리를 찾을 수 없습니다."),

    // === 카테고리 정보 유효성 관련 (CAT-100 ~ CAT-199) ===
    INVALID_CATEGORY_ID("CAT-100", 400, "카테고리 ID는 null이 아닌 양수여야 합니다."),
    INVALID_CATEGORY_CODE("CAT-101", 400, "카테고리 코드가 올바르지 않습니다."),
    INVALID_CATEGORY_NAME("CAT-102", 400, "카테고리명이 올바르지 않습니다."),
    INVALID_CATEGORY_PATH("CAT-103", 400, "카테고리 경로가 올바르지 않습니다."),
    INVALID_CATEGORY_DEPTH("CAT-104", 400, "카테고리 깊이가 올바르지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    CategoryErrorCode(String code, int httpStatus, String message) {
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

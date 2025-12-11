package com.ryuqq.setof.domain.brand.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Brand Bounded Context 에러 코드
 *
 * <p>브랜드 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: BRD-XXX
 *
 * <ul>
 *   <li>BRD-001 ~ BRD-099: 브랜드 조회 관련
 *   <li>BRD-100 ~ BRD-199: 브랜드 정보 유효성 관련
 * </ul>
 */
public enum BrandErrorCode implements ErrorCode {

    // === 브랜드 조회 관련 (BRD-001 ~ BRD-099) ===
    BRAND_NOT_FOUND("BRD-001", 404, "브랜드를 찾을 수 없습니다."),

    // === 브랜드 정보 유효성 관련 (BRD-100 ~ BRD-199) ===
    INVALID_BRAND_ID("BRD-100", 400, "브랜드 ID는 null이 아닌 양수여야 합니다."),
    INVALID_BRAND_CODE("BRD-101", 400, "브랜드 코드가 올바르지 않습니다."),
    INVALID_BRAND_NAME_KO("BRD-102", 400, "한글 브랜드명이 올바르지 않습니다."),
    INVALID_BRAND_NAME_EN("BRD-103", 400, "영문 브랜드명이 올바르지 않습니다."),
    INVALID_BRAND_LOGO_URL("BRD-104", 400, "브랜드 로고 URL이 올바르지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    BrandErrorCode(String code, int httpStatus, String message) {
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

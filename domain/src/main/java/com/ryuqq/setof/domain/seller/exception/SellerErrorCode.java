package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Seller Bounded Context 에러 코드
 *
 * <p>셀러 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: SLR-XXX
 *
 * <ul>
 *   <li>SLR-001 ~ SLR-099: 셀러 조회 관련
 *   <li>SLR-100 ~ SLR-199: 셀러 정보 유효성 관련
 *   <li>SLR-200 ~ SLR-299: 사업자 정보 관련
 *   <li>SLR-300 ~ SLR-399: CS 정보 관련
 * </ul>
 */
public enum SellerErrorCode implements ErrorCode {

    // === 셀러 조회 관련 (SLR-001 ~ SLR-099) ===
    SELLER_NOT_FOUND("SLR-001", 404, "셀러를 찾을 수 없습니다."),

    // === 셀러 정보 유효성 관련 (SLR-100 ~ SLR-199) ===
    INVALID_SELLER_ID("SLR-100", 400, "셀러 ID는 null이 아닌 양수여야 합니다."),
    INVALID_SELLER_NAME("SLR-101", 400, "셀러 이름이 올바르지 않습니다."),
    INVALID_LOGO_URL("SLR-102", 400, "로고 URL이 올바르지 않습니다."),
    INVALID_DESCRIPTION("SLR-103", 400, "셀러 설명이 올바르지 않습니다."),

    // === 사업자 정보 관련 (SLR-200 ~ SLR-299) ===
    INVALID_REGISTRATION_NUMBER("SLR-200", 400, "사업자등록번호가 올바르지 않습니다."),
    INVALID_SALE_REPORT_NUMBER("SLR-201", 400, "통신판매업 신고번호가 올바르지 않습니다."),
    INVALID_REPRESENTATIVE("SLR-202", 400, "대표자명이 올바르지 않습니다."),
    INVALID_BUSINESS_ADDRESS("SLR-203", 400, "사업장 주소가 올바르지 않습니다."),

    // === CS 정보 관련 (SLR-300 ~ SLR-399) ===
    INVALID_CS_EMAIL("SLR-300", 400, "CS 이메일이 올바르지 않습니다."),
    INVALID_CS_MOBILE_PHONE("SLR-301", 400, "CS 휴대폰 번호가 올바르지 않습니다."),
    INVALID_CS_LANDLINE_PHONE("SLR-302", 400, "CS 유선전화 번호가 올바르지 않습니다."),
    INVALID_CUSTOMER_SERVICE_INFO("SLR-303", 400, "이메일, 휴대폰, 유선전화 중 최소 1개는 필수입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    SellerErrorCode(String code, int httpStatus, String message) {
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

package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ProductDescription 에러 코드 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>ErrorCode 인터페이스 구현 필수
 *   <li>Spring HttpStatus 사용 금지 (int 타입 사용)
 *   <li>Lombok 금지
 * </ul>
 */
public enum ProductDescriptionErrorCode implements ErrorCode {
    PRODUCT_DESCRIPTION_NOT_FOUND("PRODUCT_DESC-001", 404, "상품설명을 찾을 수 없습니다"),
    INVALID_PRODUCT_DESCRIPTION_ID("PRODUCT_DESC-002", 400, "유효하지 않은 상품설명 ID입니다"),
    INVALID_IMAGE_URL("PRODUCT_DESC-003", 400, "유효하지 않은 이미지 URL입니다"),
    INVALID_HTML_CONTENT("PRODUCT_DESC-004", 400, "유효하지 않은 HTML 컨텐츠입니다"),
    DUPLICATE_IMAGE_ORDER("PRODUCT_DESC-005", 400, "중복된 이미지 순서가 있습니다"),
    MAX_IMAGE_COUNT_EXCEEDED("PRODUCT_DESC-006", 400, "최대 이미지 개수를 초과했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductDescriptionErrorCode(String code, int httpStatus, String message) {
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

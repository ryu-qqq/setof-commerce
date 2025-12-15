package com.ryuqq.setof.domain.productimage.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ProductImage 에러 코드 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>ErrorCode 인터페이스 구현 필수
 *   <li>Spring HttpStatus 사용 금지 (int 타입 사용)
 *   <li>Lombok 금지
 * </ul>
 */
public enum ProductImageErrorCode implements ErrorCode {
    PRODUCT_IMAGE_NOT_FOUND("PRODUCT_IMG-001", 404, "상품이미지를 찾을 수 없습니다"),
    INVALID_PRODUCT_IMAGE_ID("PRODUCT_IMG-002", 400, "유효하지 않은 상품이미지 ID입니다"),
    INVALID_IMAGE_URL("PRODUCT_IMG-003", 400, "유효하지 않은 이미지 URL입니다"),
    INVALID_IMAGE_TYPE("PRODUCT_IMG-004", 400, "유효하지 않은 이미지 타입입니다"),
    DUPLICATE_MAIN_IMAGE("PRODUCT_IMG-005", 400, "메인 이미지는 하나만 등록할 수 있습니다"),
    MAIN_IMAGE_REQUIRED("PRODUCT_IMG-006", 400, "메인 이미지는 필수입니다"),
    MAX_IMAGE_COUNT_EXCEEDED("PRODUCT_IMG-007", 400, "최대 이미지 개수를 초과했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductImageErrorCode(String code, int httpStatus, String message) {
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

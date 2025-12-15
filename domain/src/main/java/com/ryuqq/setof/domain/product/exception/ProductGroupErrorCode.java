package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ProductGroup 에러 코드 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>ErrorCode 인터페이스 구현 필수
 *   <li>Spring HttpStatus 사용 금지 (int 타입 사용)
 *   <li>Lombok 금지
 * </ul>
 */
public enum ProductGroupErrorCode implements ErrorCode {
    PRODUCT_GROUP_NOT_FOUND("PRODUCT-001", 404, "상품그룹을 찾을 수 없습니다"),
    PRODUCT_NOT_FOUND("PRODUCT-002", 404, "상품을 찾을 수 없습니다"),
    INVALID_PRODUCT_GROUP_ID("PRODUCT-003", 400, "유효하지 않은 상품그룹 ID입니다"),
    INVALID_PRODUCT_ID("PRODUCT-004", 400, "유효하지 않은 상품 ID입니다"),
    INVALID_PRODUCT_GROUP_NAME("PRODUCT-005", 400, "유효하지 않은 상품그룹명입니다"),
    INVALID_PRICE("PRODUCT-006", 400, "유효하지 않은 가격입니다"),
    INVALID_MONEY("PRODUCT-007", 400, "유효하지 않은 금액입니다"),
    PRODUCT_GROUP_ALREADY_DELETED("PRODUCT-008", 400, "이미 삭제된 상품그룹입니다"),
    PRODUCT_GROUP_NOT_EDITABLE("PRODUCT-009", 400, "수정할 수 없는 상품그룹입니다"),
    INVALID_OPTION_CONFIGURATION("PRODUCT-010", 400, "유효하지 않은 옵션 구성입니다"),
    PRODUCT_NOT_BELONG_TO_GROUP("PRODUCT-011", 400, "해당 상품그룹에 속하지 않는 상품입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductGroupErrorCode(String code, int httpStatus, String message) {
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

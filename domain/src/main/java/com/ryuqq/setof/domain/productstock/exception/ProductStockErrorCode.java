package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ProductStock 에러 코드 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>ErrorCode 인터페이스 구현 필수
 *   <li>Spring HttpStatus 사용 금지 (int 타입 사용)
 *   <li>Lombok 금지
 * </ul>
 */
public enum ProductStockErrorCode implements ErrorCode {
    PRODUCT_STOCK_NOT_FOUND("STOCK-001", 404, "재고 정보를 찾을 수 없습니다"),
    INVALID_PRODUCT_STOCK_ID("STOCK-002", 400, "유효하지 않은 재고 ID입니다"),
    NOT_ENOUGH_STOCK("STOCK-003", 400, "재고가 부족합니다"),
    STOCK_OVERFLOW("STOCK-004", 400, "재고 수량이 최대값을 초과합니다"),
    INVALID_STOCK_QUANTITY("STOCK-005", 400, "유효하지 않은 재고 수량입니다"),
    STOCK_CONCURRENT_MODIFICATION("STOCK-006", 409, "재고 동시 수정 충돌이 발생했습니다. 잠시 후 다시 시도해주세요"),
    STOCK_OPTIMISTIC_LOCK("STOCK-007", 409, "재고 업데이트 충돌이 발생했습니다"),
    STOCK_LOCK_ACQUISITION_FAILED("STOCK-008", 423, "재고 락 획득에 실패했습니다. 잠시 후 다시 시도해주세요");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductStockErrorCode(String code, int httpStatus, String message) {
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

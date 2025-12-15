package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ProductNotice 에러 코드 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>ErrorCode 인터페이스 구현 필수
 *   <li>Spring HttpStatus 사용 금지 (int 타입 사용)
 *   <li>Lombok 금지
 * </ul>
 */
public enum ProductNoticeErrorCode implements ErrorCode {
    NOTICE_TEMPLATE_NOT_FOUND("NOTICE-001", 404, "상품고시 템플릿을 찾을 수 없습니다"),
    PRODUCT_NOTICE_NOT_FOUND("NOTICE-002", 404, "상품고시를 찾을 수 없습니다"),
    INVALID_NOTICE_TEMPLATE_ID("NOTICE-003", 400, "유효하지 않은 템플릿 ID입니다"),
    INVALID_PRODUCT_NOTICE_ID("NOTICE-004", 400, "유효하지 않은 상품고시 ID입니다"),
    REQUIRED_FIELD_MISSING("NOTICE-005", 400, "필수 고시 항목이 누락되었습니다"),
    DUPLICATE_FIELD_KEY("NOTICE-006", 400, "중복된 필드 키가 있습니다"),
    INVALID_FIELD_VALUE("NOTICE-007", 400, "유효하지 않은 필드 값입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ProductNoticeErrorCode(String code, int httpStatus, String message) {
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

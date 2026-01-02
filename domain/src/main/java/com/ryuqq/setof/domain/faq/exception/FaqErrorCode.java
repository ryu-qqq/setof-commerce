package com.ryuqq.setof.domain.faq.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Faq 에러 코드 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>ErrorCode 인터페이스 구현 필수
 *   <li>Spring HttpStatus 사용 금지 (int 타입 사용)
 *   <li>Lombok 금지
 * </ul>
 */
public enum FaqErrorCode implements ErrorCode {
    FAQ_NOT_FOUND("FAQ-001", 404, "FAQ를 찾을 수 없습니다"),
    INVALID_FAQ_ID("FAQ-002", 400, "유효하지 않은 FAQ ID입니다"),
    INVALID_FAQ_STATUS("FAQ-003", 400, "유효하지 않은 FAQ 상태입니다"),
    INVALID_FAQ_CONTENT("FAQ-004", 400, "유효하지 않은 FAQ 내용입니다"),
    INVALID_FAQ_CATEGORY("FAQ-005", 400, "유효하지 않은 FAQ 카테고리입니다"),
    FAQ_ALREADY_PUBLISHED("FAQ-006", 409, "이미 게시된 FAQ입니다"),
    FAQ_ALREADY_HIDDEN("FAQ-007", 409, "이미 숨김 처리된 FAQ입니다"),
    FAQ_CANNOT_PUBLISH("FAQ-008", 400, "게시할 수 없는 상태입니다"),
    FAQ_CANNOT_HIDE("FAQ-009", 400, "숨김 처리할 수 없는 상태입니다"),
    FAQ_ALREADY_DELETED("FAQ-010", 410, "이미 삭제된 FAQ입니다"),
    FAQ_CATEGORY_NOT_FOUND("FAQ-011", 404, "FAQ 카테고리를 찾을 수 없습니다"),
    DUPLICATE_FAQ_CATEGORY_CODE("FAQ-012", 409, "중복된 FAQ 카테고리 코드입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    FaqErrorCode(String code, int httpStatus, String message) {
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

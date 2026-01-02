package com.ryuqq.setof.domain.cms.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * CMS Bounded Context 에러 코드
 *
 * <p>에러 코드 규칙: CMS-{3자리 숫자}
 *
 * <ul>
 *   <li>0XX: 404 Not Found
 *   <li>01X: 400 Bad Request (입력 검증)
 *   <li>02X: 409 Conflict
 *   <li>03X: 400 Bad Request (비즈니스 룰)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public enum CmsErrorCode implements ErrorCode {

    // === 404 Not Found ===
    CONTENT_NOT_FOUND("CMS-001", 404, "Content not found"),
    COMPONENT_NOT_FOUND("CMS-002", 404, "Component not found"),
    BANNER_NOT_FOUND("CMS-003", 404, "Banner not found"),
    BANNER_ITEM_NOT_FOUND("CMS-004", 404, "Banner item not found"),
    GNB_NOT_FOUND("CMS-005", 404, "GNB not found"),

    // === 400 Bad Request (Validation) ===
    INVALID_CONTENT_TITLE("CMS-010", 400, "Invalid content title"),
    INVALID_DISPLAY_PERIOD("CMS-011", 400, "Invalid display period"),
    INVALID_COMPONENT_TYPE("CMS-012", 400, "Invalid component type"),

    // === 409 Conflict ===
    CONTENT_ALREADY_EXISTS("CMS-020", 409, "Content already exists"),

    // === 400 Bad Request (Business Rule) ===
    INVALID_CONTENT_STATE("CMS-030", 400, "Invalid content state for this operation"),
    INVALID_COMPONENT_STATE("CMS-031", 400, "Invalid component state for this operation"),
    INVALID_BANNER_STATE("CMS-032", 400, "Invalid banner state for this operation"),
    CONTENT_NOT_DISPLAYABLE("CMS-033", 400, "Content is not displayable at this time");

    private final String code;
    private final int httpStatus;
    private final String message;

    CmsErrorCode(String code, int httpStatus, String message) {
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

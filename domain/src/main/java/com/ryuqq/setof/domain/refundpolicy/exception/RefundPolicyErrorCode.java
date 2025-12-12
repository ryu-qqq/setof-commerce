package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * RefundPolicy Bounded Context 에러 코드
 *
 * <p>반품 정책 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: RFD-XXX
 *
 * <ul>
 *   <li>RFD-001 ~ RFD-099: 반품 정책 조회 관련
 *   <li>RFD-100 ~ RFD-199: 반품 정책 정보 유효성 관련
 *   <li>RFD-200 ~ RFD-299: 반품 정책 비즈니스 규칙 관련
 * </ul>
 */
public enum RefundPolicyErrorCode implements ErrorCode {

    // === 반품 정책 조회 관련 (RFD-001 ~ RFD-099) ===
    REFUND_POLICY_NOT_FOUND("RFD-001", 404, "반품 정책을 찾을 수 없습니다."),

    // === 반품 정책 정보 유효성 관련 (RFD-100 ~ RFD-199) ===
    INVALID_REFUND_POLICY_ID("RFD-100", 400, "반품 정책 ID는 null이 아닌 양수여야 합니다."),
    INVALID_RETURN_ADDRESS("RFD-101", 400, "반품 주소가 올바르지 않습니다."),
    INVALID_REFUND_PERIOD_DAYS("RFD-102", 400, "반품 기간이 올바르지 않습니다."),
    INVALID_REFUND_DELIVERY_COST("RFD-103", 400, "반품 배송비가 올바르지 않습니다."),
    INVALID_REFUND_GUIDE("RFD-104", 400, "반품 안내가 올바르지 않습니다."),
    INVALID_POLICY_NAME("RFD-105", 400, "정책명이 올바르지 않습니다."),

    // === 반품 정책 비즈니스 규칙 관련 (RFD-200 ~ RFD-299) ===
    DEFAULT_REFUND_POLICY_REQUIRED("RFD-200", 400, "기본 반품 정책이 반드시 1개 존재해야 합니다."),
    LAST_REFUND_POLICY_CANNOT_BE_DELETED("RFD-201", 400, "마지막 반품 정책은 삭제할 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    RefundPolicyErrorCode(String code, int httpStatus, String message) {
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

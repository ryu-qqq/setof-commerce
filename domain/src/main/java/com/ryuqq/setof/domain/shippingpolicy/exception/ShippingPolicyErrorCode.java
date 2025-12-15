package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ShippingPolicy Bounded Context 에러 코드
 *
 * <p>배송 정책 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: SHP-XXX
 *
 * <ul>
 *   <li>SHP-001 ~ SHP-099: 배송 정책 조회 관련
 *   <li>SHP-100 ~ SHP-199: 배송 정책 정보 유효성 관련
 *   <li>SHP-200 ~ SHP-299: 배송 정책 비즈니스 규칙 관련
 * </ul>
 */
public enum ShippingPolicyErrorCode implements ErrorCode {

    // === 배송 정책 조회 관련 (SHP-001 ~ SHP-099) ===
    SHIPPING_POLICY_NOT_FOUND("SHP-001", 404, "배송 정책을 찾을 수 없습니다."),

    // === 배송 정책 정보 유효성 관련 (SHP-100 ~ SHP-199) ===
    INVALID_SHIPPING_POLICY_ID("SHP-100", 400, "배송 정책 ID는 null이 아닌 양수여야 합니다."),
    INVALID_POLICY_NAME("SHP-101", 400, "배송 정책명이 올바르지 않습니다."),
    INVALID_DELIVERY_COST("SHP-102", 400, "배송비가 올바르지 않습니다."),
    INVALID_FREE_SHIPPING_THRESHOLD("SHP-103", 400, "무료 배송 기준 금액이 올바르지 않습니다."),
    INVALID_DELIVERY_GUIDE("SHP-104", 400, "배송 안내가 올바르지 않습니다."),
    INVALID_DISPLAY_ORDER("SHP-105", 400, "표시 순서가 올바르지 않습니다."),

    // === 배송 정책 비즈니스 규칙 관련 (SHP-200 ~ SHP-299) ===
    DEFAULT_SHIPPING_POLICY_REQUIRED("SHP-200", 400, "기본 배송 정책이 반드시 1개 존재해야 합니다."),
    LAST_SHIPPING_POLICY_CANNOT_BE_DELETED("SHP-201", 400, "마지막 배송 정책은 삭제할 수 없습니다."),
    NOT_OWNER("SHP-202", 403, "해당 배송 정책에 대한 권한이 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    ShippingPolicyErrorCode(String code, int httpStatus, String message) {
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

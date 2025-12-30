package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * DiscountPolicy Bounded Context 에러 코드
 *
 * <p>할인 정책 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: DSC-XXX
 *
 * <ul>
 *   <li>DSC-001 ~ DSC-099: 할인 정책 조회 관련
 *   <li>DSC-100 ~ DSC-199: 할인 정책 정보 유효성 관련
 *   <li>DSC-200 ~ DSC-299: 할인 정책 비즈니스 규칙 관련
 *   <li>DSC-300 ~ DSC-399: 할인 적용/계산 관련
 * </ul>
 */
public enum DiscountPolicyErrorCode implements ErrorCode {

    // === 할인 정책 조회 관련 (DSC-001 ~ DSC-099) ===
    DISCOUNT_POLICY_NOT_FOUND("DSC-001", 404, "할인 정책을 찾을 수 없습니다."),

    // === 할인 정책 정보 유효성 관련 (DSC-100 ~ DSC-199) ===
    INVALID_DISCOUNT_POLICY_ID("DSC-100", 400, "할인 정책 ID는 null이 아닌 양수여야 합니다."),
    INVALID_POLICY_NAME("DSC-101", 400, "정책명이 올바르지 않습니다."),
    INVALID_DISCOUNT_RATE("DSC-102", 400, "할인율이 올바르지 않습니다."),
    INVALID_DISCOUNT_AMOUNT("DSC-103", 400, "할인 금액이 올바르지 않습니다."),
    INVALID_VALID_PERIOD("DSC-104", 400, "유효 기간이 올바르지 않습니다."),
    INVALID_USAGE_LIMIT("DSC-105", 400, "사용 제한이 올바르지 않습니다."),
    INVALID_COST_SHARE("DSC-106", 400, "비용 분담 비율이 올바르지 않습니다."),
    INVALID_PRIORITY("DSC-107", 400, "우선순위가 올바르지 않습니다."),
    INVALID_MINIMUM_ORDER_AMOUNT("DSC-108", 400, "최소 주문 금액이 올바르지 않습니다."),

    // === 할인 정책 비즈니스 규칙 관련 (DSC-200 ~ DSC-299) ===
    NOT_OWNER("DSC-200", 403, "해당 할인 정책에 대한 권한이 없습니다."),
    DISCOUNT_POLICY_ALREADY_ACTIVE("DSC-201", 409, "이미 활성화된 할인 정책입니다."),
    DISCOUNT_POLICY_ALREADY_INACTIVE("DSC-202", 409, "이미 비활성화된 할인 정책입니다."),
    DISCOUNT_PERIOD_OVERLAP("DSC-203", 409, "동일 그룹 내 할인 기간이 중복됩니다."),

    // === 할인 적용/계산 관련 (DSC-300 ~ DSC-399) ===
    DISCOUNT_NOT_APPLICABLE("DSC-300", 400, "할인을 적용할 수 없습니다."),
    USAGE_LIMIT_EXCEEDED("DSC-301", 400, "사용 횟수 제한을 초과했습니다."),
    DISCOUNT_PERIOD_EXPIRED("DSC-302", 400, "할인 기간이 만료되었습니다."),
    MINIMUM_ORDER_AMOUNT_NOT_MET("DSC-303", 400, "최소 주문 금액을 충족하지 않습니다."),

    // === 할인 사용 히스토리 관련 (DSC-400 ~ DSC-499) ===
    INVALID_DISCOUNT_USAGE_HISTORY_ID("DSC-400", 400, "할인 사용 히스토리 ID가 올바르지 않습니다."),
    DISCOUNT_USAGE_HISTORY_NOT_FOUND("DSC-401", 404, "할인 사용 히스토리를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    DiscountPolicyErrorCode(String code, int httpStatus, String message) {
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

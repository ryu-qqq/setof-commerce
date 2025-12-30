package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Shipment Bounded Context 에러 코드
 *
 * <p>운송장 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: SHP-XXX
 *
 * <ul>
 *   <li>SHP-001 ~ SHP-099: 운송장 조회 관련
 *   <li>SHP-100 ~ SHP-199: 운송장 정보 유효성 관련
 *   <li>SHP-200 ~ SHP-299: 배송 상태 관련
 *   <li>SHP-300 ~ SHP-399: 배송 추적 관련
 * </ul>
 */
public enum ShipmentErrorCode implements ErrorCode {

    // === 운송장 조회 관련 (SHP-001 ~ SHP-099) ===
    SHIPMENT_NOT_FOUND("SHP-001", 404, "운송장을 찾을 수 없습니다."),

    // === 운송장 정보 유효성 관련 (SHP-100 ~ SHP-199) ===
    INVALID_SHIPMENT_ID("SHP-100", 400, "운송장 ID는 null이 아닌 양수여야 합니다."),
    INVALID_INVOICE_NUMBER("SHP-101", 400, "운송장 번호가 올바르지 않습니다."),
    INVALID_SENDER_INFO("SHP-102", 400, "발송인 정보가 올바르지 않습니다."),

    // === 배송 상태 관련 (SHP-200 ~ SHP-299) ===
    INVALID_STATUS_TRANSITION("SHP-200", 400, "유효하지 않은 배송 상태 변경입니다."),
    ALREADY_DELIVERED("SHP-201", 400, "이미 배송 완료된 운송장입니다."),

    // === 배송 추적 관련 (SHP-300 ~ SHP-399) ===
    TRACKING_UPDATE_FAILED("SHP-300", 500, "배송 추적 정보 업데이트에 실패했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    ShipmentErrorCode(String code, int httpStatus, String message) {
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

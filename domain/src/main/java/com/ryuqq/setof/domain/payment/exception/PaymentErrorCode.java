package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * PaymentErrorCode - 결제 도메인 에러 코드
 *
 * <p>Payment Bounded Context의 모든 에러 코드를 정의합니다.
 *
 * <p>에러 코드 형식: PAYMENT-{NUMBER}
 */
public enum PaymentErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_PAYMENT_ID("PAYMENT-001", 400, "유효하지 않은 결제 ID입니다"),
    INVALID_PAYMENT_MONEY("PAYMENT-002", 400, "유효하지 않은 결제 금액입니다"),
    INVALID_REFUND_AMOUNT("PAYMENT-003", 400, "유효하지 않은 환불 금액입니다"),
    REFUND_EXCEEDS_AVAILABLE("PAYMENT-004", 400, "환불 금액이 환불 가능 금액을 초과합니다"),

    // 404 Not Found
    PAYMENT_NOT_FOUND("PAYMENT-010", 404, "결제 정보를 찾을 수 없습니다"),

    // 409 Conflict
    PAYMENT_ALREADY_APPROVED("PAYMENT-020", 409, "이미 승인된 결제입니다"),
    PAYMENT_ALREADY_CANCELLED("PAYMENT-021", 409, "이미 취소된 결제입니다"),
    PAYMENT_ALREADY_REFUNDED("PAYMENT-022", 409, "이미 전액 환불된 결제입니다"),
    PAYMENT_NOT_APPROVABLE("PAYMENT-023", 409, "승인할 수 없는 결제 상태입니다"),
    PAYMENT_NOT_REFUNDABLE("PAYMENT-024", 409, "환불할 수 없는 결제 상태입니다"),
    PAYMENT_NOT_CANCELLABLE("PAYMENT-025", 409, "취소할 수 없는 결제 상태입니다"),
    PAYMENT_COMPLETION_IN_PROGRESS("PAYMENT-026", 409, "이미 결제 완료 처리 중입니다"),

    // 500 Internal Server Error
    PG_COMMUNICATION_ERROR("PAYMENT-050", 500, "PG사 통신 오류가 발생했습니다"),
    PAYMENT_PROCESSING_ERROR("PAYMENT-051", 500, "결제 처리 중 오류가 발생했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    PaymentErrorCode(String code, int httpStatus, String message) {
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

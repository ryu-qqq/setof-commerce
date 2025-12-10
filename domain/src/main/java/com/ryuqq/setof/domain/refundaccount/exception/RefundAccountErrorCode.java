package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * RefundAccount Bounded Context 에러 코드
 *
 * <p>환불계좌 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: RFA-XXX
 *
 * <ul>
 *   <li>RFA-001 ~ RFA-099: 환불계좌 조회 관련
 *   <li>RFA-100 ~ RFA-199: 환불계좌 정보 유효성 관련
 *   <li>RFA-200 ~ RFA-299: 환불계좌 비즈니스 규칙 관련
 *   <li>RFA-300 ~ RFA-399: 계좌 검증 관련
 * </ul>
 */
public enum RefundAccountErrorCode implements ErrorCode {

    // === 환불계좌 조회 관련 (RFA-001 ~ RFA-099) ===
    REFUND_ACCOUNT_NOT_FOUND("RFA-001", 404, "환불계좌를 찾을 수 없습니다."),

    // === 환불계좌 정보 유효성 관련 (RFA-100 ~ RFA-199) ===
    INVALID_REFUND_ACCOUNT_ID("RFA-100", 400, "환불계좌 ID는 null이 아닌 양수여야 합니다."),
    INVALID_ACCOUNT_NUMBER("RFA-101", 400, "계좌번호가 올바르지 않습니다. (8~30자리 숫자)"),
    INVALID_ACCOUNT_HOLDER_NAME("RFA-102", 400, "예금주명이 올바르지 않습니다. (1~20자)"),

    // === 환불계좌 비즈니스 규칙 관련 (RFA-200 ~ RFA-299) ===
    REFUND_ACCOUNT_ALREADY_EXISTS("RFA-200", 409, "이미 등록된 환불계좌가 있습니다. 수정 또는 삭제 후 다시 등록해주세요."),
    NOT_OWNER("RFA-201", 403, "해당 환불계좌에 대한 권한이 없습니다."),

    // === 계좌 검증 관련 (RFA-300 ~ RFA-399) ===
    ACCOUNT_VERIFICATION_FAILED("RFA-300", 400, "계좌 검증에 실패했습니다. 계좌 정보를 확인해주세요."),
    ACCOUNT_VERIFICATION_SERVICE_ERROR("RFA-301", 503, "계좌 검증 서비스에 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    ACCOUNT_NOT_VERIFIED("RFA-302", 400, "검증되지 않은 계좌입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    RefundAccountErrorCode(String code, int httpStatus, String message) {
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

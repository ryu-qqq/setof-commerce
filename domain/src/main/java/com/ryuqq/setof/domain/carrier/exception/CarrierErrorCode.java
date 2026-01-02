package com.ryuqq.setof.domain.carrier.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Carrier Bounded Context 에러 코드
 *
 * <p>택배사 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: CRR-XXX
 *
 * <ul>
 *   <li>CRR-001 ~ CRR-099: 택배사 조회 관련
 *   <li>CRR-100 ~ CRR-199: 택배사 정보 유효성 관련
 * </ul>
 */
public enum CarrierErrorCode implements ErrorCode {

    // === 택배사 조회 관련 (CRR-001 ~ CRR-099) ===
    CARRIER_NOT_FOUND("CRR-001", 404, "택배사를 찾을 수 없습니다."),

    // === 택배사 정보 유효성 관련 (CRR-100 ~ CRR-199) ===
    INVALID_CARRIER_ID("CRR-100", 400, "택배사 ID는 null이 아닌 양수여야 합니다."),
    INVALID_CARRIER_CODE("CRR-101", 400, "택배사 코드가 올바르지 않습니다."),
    INVALID_CARRIER_NAME("CRR-102", 400, "택배사명이 올바르지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    CarrierErrorCode(String code, int httpStatus, String message) {
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

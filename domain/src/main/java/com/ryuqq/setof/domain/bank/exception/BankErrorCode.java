package com.ryuqq.setof.domain.bank.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Bank Bounded Context 에러 코드
 *
 * <p>은행 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: BNK-XXX
 *
 * <ul>
 *   <li>BNK-001 ~ BNK-099: 은행 조회 관련
 *   <li>BNK-100 ~ BNK-199: 은행 정보 유효성 관련
 * </ul>
 */
public enum BankErrorCode implements ErrorCode {

    // === 은행 조회 관련 (BNK-001 ~ BNK-099) ===
    BANK_NOT_FOUND("BNK-001", 404, "은행을 찾을 수 없습니다."),

    // === 은행 정보 유효성 관련 (BNK-100 ~ BNK-199) ===
    INVALID_BANK_ID("BNK-100", 400, "은행 ID는 null이 아닌 양수여야 합니다."),
    INVALID_BANK_CODE("BNK-101", 400, "은행 코드는 3자리 숫자여야 합니다."),
    INVALID_BANK_NAME("BNK-102", 400, "은행 이름이 올바르지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    BankErrorCode(String code, int httpStatus, String message) {
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

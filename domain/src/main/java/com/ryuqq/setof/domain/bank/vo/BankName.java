package com.ryuqq.setof.domain.bank.vo;

import com.ryuqq.setof.domain.bank.exception.InvalidBankNameException;

/**
 * 은행 이름 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>은행 이름 길이 검증 (1~30자)
 * </ul>
 *
 * @param value 은행 이름 값
 */
public record BankName(String value) {

    private static final int MAX_LENGTH = 30;

    /** Compact Constructor - 검증 로직 */
    public BankName {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 은행 이름 문자열
     * @return BankName 인스턴스
     * @throws InvalidBankNameException value가 유효하지 않은 경우
     */
    public static BankName of(String value) {
        return new BankName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidBankNameException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidBankNameException(value);
        }
    }
}

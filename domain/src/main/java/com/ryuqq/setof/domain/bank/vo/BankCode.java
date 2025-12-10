package com.ryuqq.setof.domain.bank.vo;

import com.ryuqq.setof.domain.bank.exception.InvalidBankCodeException;

/**
 * 은행 코드 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>은행 코드 형식 검증 (3자리 숫자)
 * </ul>
 *
 * <p>은행 코드 예시:
 *
 * <ul>
 *   <li>004: KB국민은행
 *   <li>088: 신한은행
 *   <li>020: 우리은행
 * </ul>
 *
 * @param value 은행 코드 값 (3자리 숫자 문자열)
 */
public record BankCode(String value) {

    private static final int CODE_LENGTH = 3;

    /** Compact Constructor - 검증 로직 */
    public BankCode {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 은행 코드 문자열
     * @return BankCode 인스턴스
     * @throws InvalidBankCodeException value가 유효하지 않은 경우
     */
    public static BankCode of(String value) {
        return new BankCode(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidBankCodeException(value);
        }
        if (value.length() != CODE_LENGTH) {
            throw new InvalidBankCodeException(value);
        }
        if (!value.chars().allMatch(Character::isDigit)) {
            throw new InvalidBankCodeException(value);
        }
    }
}

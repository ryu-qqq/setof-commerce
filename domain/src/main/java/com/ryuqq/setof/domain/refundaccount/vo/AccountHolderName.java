package com.ryuqq.setof.domain.refundaccount.vo;

import com.ryuqq.setof.domain.refundaccount.exception.InvalidAccountHolderNameException;

/**
 * 예금주명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>예금주명 길이 검증 (1~20자)
 * </ul>
 *
 * @param value 예금주명 값
 */
public record AccountHolderName(String value) {

    private static final int MAX_LENGTH = 20;

    /** Compact Constructor - 검증 로직 */
    public AccountHolderName {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 예금주명 문자열
     * @return AccountHolderName 인스턴스
     * @throws InvalidAccountHolderNameException value가 유효하지 않은 경우
     */
    public static AccountHolderName of(String value) {
        return new AccountHolderName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAccountHolderNameException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidAccountHolderNameException(value);
        }
    }
}

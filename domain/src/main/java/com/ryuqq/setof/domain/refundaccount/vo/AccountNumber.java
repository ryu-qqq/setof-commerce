package com.ryuqq.setof.domain.refundaccount.vo;

import com.ryuqq.setof.domain.refundaccount.exception.InvalidAccountNumberException;

/**
 * 계좌번호 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>계좌번호 형식 검증 (숫자와 하이픈만, 최대 30자)
 * </ul>
 *
 * @param value 계좌번호 값
 */
public record AccountNumber(String value) {

    private static final int MAX_LENGTH = 30;
    private static final int MIN_LENGTH = 8;

    /** Compact Constructor - 검증 로직 */
    public AccountNumber {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 계좌번호 문자열
     * @return AccountNumber 인스턴스
     * @throws InvalidAccountNumberException value가 유효하지 않은 경우
     */
    public static AccountNumber of(String value) {
        return new AccountNumber(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAccountNumberException(value);
        }
        String normalized = normalizeAccountNumber(value);
        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH) {
            throw new InvalidAccountNumberException(value);
        }
        if (!normalized.chars().allMatch(Character::isDigit)) {
            throw new InvalidAccountNumberException(value);
        }
    }

    /**
     * 정규화된 계좌번호 반환 (숫자만)
     *
     * @return 숫자만 포함된 계좌번호
     */
    public String normalized() {
        return normalizeAccountNumber(value);
    }

    /**
     * 마스킹된 계좌번호 반환
     *
     * <p>앞 4자리와 뒤 4자리만 표시하고 나머지는 *로 마스킹
     *
     * @return 마스킹된 계좌번호
     */
    public String masked() {
        String normalized = normalized();
        if (normalized.length() <= 8) {
            return normalized.substring(0, 2) + "****" + normalized.substring(normalized.length() - 2);
        }
        return normalized.substring(0, 4) + "****" + normalized.substring(normalized.length() - 4);
    }

    private static String normalizeAccountNumber(String value) {
        return value.replaceAll("[^0-9]", "");
    }
}

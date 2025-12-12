package com.ryuqq.setof.domain.refundpolicy.vo;

import com.ryuqq.setof.domain.refundpolicy.exception.InvalidPolicyNameException;

/**
 * 반품 정책명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>최대 50자 제한
 * </ul>
 *
 * @param value 정책명 값
 */
public record PolicyName(String value) {

    private static final int MAX_LENGTH = 50;

    /** Compact Constructor - 검증 로직 */
    public PolicyName {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 정책명 값
     * @return PolicyName 인스턴스
     * @throws InvalidPolicyNameException value가 null이거나 빈 문자열인 경우
     */
    public static PolicyName of(String value) {
        return new PolicyName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPolicyNameException(value, "정책명은 필수입니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidPolicyNameException(value, "정책명은 50자를 초과할 수 없습니다.");
        }
    }
}

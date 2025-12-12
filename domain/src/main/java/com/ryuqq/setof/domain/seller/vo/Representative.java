package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidRepresentativeException;

/**
 * 대표자명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param value 대표자명 값
 */
public record Representative(String value) {

    private static final int MAX_LENGTH = 50;

    /** Compact Constructor - 검증 로직 */
    public Representative {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 대표자명 값
     * @return Representative 인스턴스
     * @throws InvalidRepresentativeException value가 null이거나 빈 문자열인 경우
     */
    public static Representative of(String value) {
        return new Representative(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidRepresentativeException(value, "대표자명은 필수입니다.");
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidRepresentativeException(value, "대표자명은 50자를 초과할 수 없습니다.");
        }
    }
}

package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidSellerNameException;

/**
 * 셀러 이름 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>최대 100자 제한
 * </ul>
 *
 * @param value 셀러 이름 값
 */
public record SellerName(String value) {

    private static final int MAX_LENGTH = 100;

    /** Compact Constructor - 검증 로직 */
    public SellerName {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 셀러 이름 값
     * @return SellerName 인스턴스
     * @throws InvalidSellerNameException value가 null이거나 빈 문자열인 경우
     */
    public static SellerName of(String value) {
        return new SellerName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidSellerNameException(value, "셀러 이름은 필수입니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidSellerNameException(value, "셀러 이름은 100자를 초과할 수 없습니다.");
        }
    }
}

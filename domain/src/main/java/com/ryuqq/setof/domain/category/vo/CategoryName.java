package com.ryuqq.setof.domain.category.vo;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryNameException;

/**
 * 카테고리명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>카테고리명 형식 검증 (필수, 최대 255자)
 * </ul>
 *
 * @param value 카테고리명 값 (필수, 최대 255자)
 */
public record CategoryName(String value) {

    private static final int MAX_LENGTH = 255;

    /** Compact Constructor - 검증 로직 */
    public CategoryName {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 카테고리명 문자열
     * @return CategoryName 인스턴스
     * @throws InvalidCategoryNameException value가 유효하지 않은 경우
     */
    public static CategoryName of(String value) {
        return new CategoryName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCategoryNameException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidCategoryNameException(value);
        }
    }
}

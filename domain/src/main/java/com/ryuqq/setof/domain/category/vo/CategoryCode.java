package com.ryuqq.setof.domain.category.vo;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryCodeException;

/**
 * 카테고리 코드 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>카테고리 코드 형식 검증 (최대 100자)
 * </ul>
 *
 * @param value 카테고리 코드 값 (최대 100자)
 */
public record CategoryCode(String value) {

    private static final int MAX_LENGTH = 100;

    /** Compact Constructor - 검증 로직 */
    public CategoryCode {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 카테고리 코드 문자열
     * @return CategoryCode 인스턴스
     * @throws InvalidCategoryCodeException value가 유효하지 않은 경우
     */
    public static CategoryCode of(String value) {
        return new CategoryCode(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCategoryCodeException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidCategoryCodeException(value);
        }
    }
}

package com.ryuqq.setof.domain.category.vo;

/**
 * 카테고리명 Value Object.
 *
 * <p>카테고리의 기본 이름을 표현합니다.
 */
public record CategoryName(String value) {

    private static final int MAX_LENGTH = 50;

    public CategoryName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("카테고리명은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("카테고리명은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static CategoryName of(String value) {
        return new CategoryName(value);
    }
}

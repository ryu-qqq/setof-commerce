package com.ryuqq.setof.domain.category.vo;

/**
 * 카테고리 경로 Value Object.
 *
 * <p>카테고리 트리에서의 경로를 표현합니다. (예: /1/2/3)
 */
public record CategoryPath(String value) {

    private static final int MAX_LENGTH = 500;

    public CategoryPath {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("카테고리 경로는 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("카테고리 경로는 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static CategoryPath of(String value) {
        return new CategoryPath(value);
    }
}

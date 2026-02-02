package com.ryuqq.setof.domain.category.vo;

/**
 * 카테고리 표시명 Value Object.
 *
 * <p>사용자에게 보여지는 카테고리의 이름입니다.
 */
public record CategoryDisplayName(String value) {

    private static final int MAX_LENGTH = 50;

    public CategoryDisplayName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("카테고리 표시명은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("카테고리 표시명은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static CategoryDisplayName of(String value) {
        return new CategoryDisplayName(value);
    }
}

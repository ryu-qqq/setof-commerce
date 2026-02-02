package com.ryuqq.setof.domain.category.vo;

/**
 * 카테고리 깊이 Value Object.
 *
 * <p>카테고리 트리에서의 계층 깊이를 표현합니다. (1: 루트, 2: 1단계 하위, ...)
 */
public record CategoryDepth(int value) {

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;

    public CategoryDepth {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                    String.format("카테고리 깊이는 %d~%d 사이여야 합니다", MIN_VALUE, MAX_VALUE));
        }
    }

    public static CategoryDepth of(int value) {
        return new CategoryDepth(value);
    }

    public static CategoryDepth root() {
        return new CategoryDepth(1);
    }
}

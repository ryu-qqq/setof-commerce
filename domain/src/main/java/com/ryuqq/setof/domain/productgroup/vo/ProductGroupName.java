package com.ryuqq.setof.domain.productgroup.vo;

/**
 * 상품그룹명 Value Object.
 *
 * <p>상품그룹의 이름을 표현합니다.
 */
public record ProductGroupName(String value) {

    private static final int MAX_LENGTH = 200;

    public ProductGroupName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("상품그룹명은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("상품그룹명은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static ProductGroupName of(String value) {
        return new ProductGroupName(value);
    }
}

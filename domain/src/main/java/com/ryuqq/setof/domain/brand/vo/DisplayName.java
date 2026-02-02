package com.ryuqq.setof.domain.brand.vo;

/**
 * 브랜드 표시명 Value Object.
 *
 * <p>고객에게 노출되는 브랜드명입니다.
 */
public record DisplayName(String value) {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    public DisplayName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("브랜드 표시명은 필수입니다");
        }
        value = value.trim();
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("브랜드 표시명은 %d~%d자 이내여야 합니다", MIN_LENGTH, MAX_LENGTH));
        }
    }

    public static DisplayName of(String value) {
        return new DisplayName(value);
    }
}

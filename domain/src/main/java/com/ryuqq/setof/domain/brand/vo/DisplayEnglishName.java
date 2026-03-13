package com.ryuqq.setof.domain.brand.vo;

/**
 * 브랜드 영문 표시명 Value Object.
 *
 * <p>고객에게 노출되는 브랜드 영문명입니다.
 */
public record DisplayEnglishName(String value) {

    private static final int MAX_LENGTH = 100;

    public DisplayEnglishName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("브랜드 영문 표시명은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("브랜드 영문 표시명은 %d자 이내여야 합니다", MAX_LENGTH));
        }
    }

    public static DisplayEnglishName of(String value) {
        return new DisplayEnglishName(value);
    }
}

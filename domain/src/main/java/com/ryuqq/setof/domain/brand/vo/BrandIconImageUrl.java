package com.ryuqq.setof.domain.brand.vo;

/**
 * 브랜드 아이콘 이미지 URL Value Object.
 *
 * <p>브랜드 아이콘 이미지의 URL을 표현합니다.
 */
public record BrandIconImageUrl(String value) {

    private static final int MAX_LENGTH = 255;

    public BrandIconImageUrl {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("브랜드 아이콘 이미지 URL은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("브랜드 아이콘 이미지 URL은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static BrandIconImageUrl of(String value) {
        return new BrandIconImageUrl(value);
    }
}

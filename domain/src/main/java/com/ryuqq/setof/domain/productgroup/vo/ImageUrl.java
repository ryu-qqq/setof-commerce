package com.ryuqq.setof.domain.productgroup.vo;

/**
 * 이미지 URL Value Object.
 *
 * <p>상품그룹 이미지의 URL을 표현합니다.
 */
public record ImageUrl(String value) {

    private static final int MAX_LENGTH = 500;

    public ImageUrl {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("이미지 URL은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    public static ImageUrl of(String value) {
        return new ImageUrl(value);
    }
}

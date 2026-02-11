package com.ryuqq.setof.domain.productdescription.vo;

/**
 * 상세설명 HTML 컨텐츠 Value Object.
 *
 * <p>상품 상세설명의 HTML 내용을 표현합니다.
 */
public record DescriptionHtml(String value) {

    private static final int MAX_LENGTH = 100_000;

    public DescriptionHtml {
        if (value != null) {
            value = value.trim();
            if (value.length() > MAX_LENGTH) {
                throw new IllegalArgumentException("상세설명은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
            }
            if (value.isEmpty()) {
                value = null;
            }
        }
    }

    public static DescriptionHtml of(String value) {
        return new DescriptionHtml(value);
    }

    public static DescriptionHtml empty() {
        return new DescriptionHtml(null);
    }

    public boolean isEmpty() {
        return value == null || value.isBlank();
    }
}

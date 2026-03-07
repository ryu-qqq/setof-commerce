package com.ryuqq.setof.domain.faq.vo;

/**
 * FaqTitle - FAQ 제목 Value Object.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param value FAQ 제목
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FaqTitle(String value) {

    private static final int MAX_LENGTH = 50;

    public FaqTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("FAQ 제목은 필수입니다");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("FAQ 제목은 " + MAX_LENGTH + "자 이하여야 합니다");
        }
    }

    public static FaqTitle of(String value) {
        return new FaqTitle(value);
    }
}

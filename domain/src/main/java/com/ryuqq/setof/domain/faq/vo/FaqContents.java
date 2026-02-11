package com.ryuqq.setof.domain.faq.vo;

/**
 * FaqContents - FAQ 내용 Value Object.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param value FAQ 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FaqContents(String value) {

    public FaqContents {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("FAQ 내용은 필수입니다");
        }
    }

    public static FaqContents of(String value) {
        return new FaqContents(value);
    }
}

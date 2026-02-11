package com.ryuqq.setof.domain.faq.vo;

/**
 * FaqDisplayOrder - FAQ 표시 순서 Value Object.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param value 표시 순서 (0 이상)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FaqDisplayOrder(int value) {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 9999;

    public FaqDisplayOrder {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                    String.format("FAQ 표시 순서는 %d~%d 사이여야 합니다", MIN_VALUE, MAX_VALUE));
        }
    }

    public static FaqDisplayOrder of(int value) {
        return new FaqDisplayOrder(value);
    }

    public static FaqDisplayOrder defaultOrder() {
        return new FaqDisplayOrder(0);
    }
}

package com.ryuqq.setof.domain.displaycomponent.id;

/**
 * ProductCurationId - 상품 큐레이션 식별자.
 *
 * <p>DOM-ID-001, DOM-ID-002 참조.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductCurationId(Long value) {

    public static ProductCurationId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ProductCurationId must be positive");
        }
        return new ProductCurationId(value);
    }

    public static ProductCurationId forNew() {
        return new ProductCurationId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

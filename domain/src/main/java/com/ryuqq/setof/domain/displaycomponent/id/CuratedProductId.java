package com.ryuqq.setof.domain.displaycomponent.id;

/**
 * CuratedProductId - 큐레이팅된 상품 식별자.
 *
 * <p>DOM-ID-001, DOM-ID-002 참조.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CuratedProductId(Long value) {

    public static CuratedProductId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("CuratedProductId must be positive");
        }
        return new CuratedProductId(value);
    }

    public static CuratedProductId forNew() {
        return new CuratedProductId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

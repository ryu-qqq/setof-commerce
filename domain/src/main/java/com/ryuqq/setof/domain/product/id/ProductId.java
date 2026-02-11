package com.ryuqq.setof.domain.product.id;

/** 상품(SKU) ID Value Object. */
public record ProductId(Long value) {

    public static ProductId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductId 값은 null일 수 없습니다");
        }
        return new ProductId(value);
    }

    public static ProductId forNew() {
        return new ProductId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

package com.ryuqq.setof.domain.product.id;

/** 상품-옵션값 매핑 ID Value Object. */
public record ProductOptionMappingId(Long value) {

    public static ProductOptionMappingId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductOptionMappingId 값은 null일 수 없습니다");
        }
        return new ProductOptionMappingId(value);
    }

    public static ProductOptionMappingId forNew() {
        return new ProductOptionMappingId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

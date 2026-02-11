package com.ryuqq.setof.domain.productdescription.id;

/** 상품그룹 상세설명 ID Value Object. */
public record ProductGroupDescriptionId(Long value) {

    public static ProductGroupDescriptionId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductGroupDescriptionId 값은 null일 수 없습니다");
        }
        return new ProductGroupDescriptionId(value);
    }

    public static ProductGroupDescriptionId forNew() {
        return new ProductGroupDescriptionId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}

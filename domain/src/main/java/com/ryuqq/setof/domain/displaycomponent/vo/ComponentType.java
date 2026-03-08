package com.ryuqq.setof.domain.displaycomponent.vo;

/**
 * ComponentType - 컴포넌트 타입 열거형.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum ComponentType {
    TEXT,
    TITLE,
    IMAGE,
    BLANK,
    TAB,
    BRAND,
    CATEGORY,
    PRODUCT;

    public boolean isProductRelated() {
        return this == TAB || this == BRAND || this == CATEGORY || this == PRODUCT;
    }

    public boolean isNonProductRelated() {
        return !isProductRelated();
    }
}

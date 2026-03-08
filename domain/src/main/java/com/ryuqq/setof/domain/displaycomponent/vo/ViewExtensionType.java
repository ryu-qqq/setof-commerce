package com.ryuqq.setof.domain.displaycomponent.vo;

/**
 * ViewExtensionType - 뷰 확장 타입 열거형.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum ViewExtensionType {
    PAGE,
    LINKING,
    PRODUCT,
    SCROLL,
    NONE;

    public boolean isProductExtension() {
        return this == PRODUCT;
    }
}

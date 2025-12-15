package com.setof.connectly.module.display.enums.component;

public enum ComponentType {
    TEXT,
    TITLE,
    IMAGE,
    BLANK,

    TAB,
    BRAND,
    CATEGORY,
    PRODUCT;

    public boolean isNonProductRelatedContents() {
        return this.equals(TEXT) || this.equals(TITLE) || this.equals(IMAGE) || this.equals(BLANK);
    }

    public boolean isProductRelatedContents() {
        return this.equals(TAB)
                || this.equals(BRAND)
                || this.equals(CATEGORY)
                || this.equals(PRODUCT);
    }
}

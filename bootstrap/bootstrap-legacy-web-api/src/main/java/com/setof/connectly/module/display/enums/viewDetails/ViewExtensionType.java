package com.setof.connectly.module.display.enums.viewDetails;

public enum ViewExtensionType {
    PAGE,
    LINKING,
    PRODUCT,
    SCROLL,
    NONE;

    public boolean isProductExtension() {
        return this.equals(PRODUCT);
    }
}

package com.connectly.partnerAdmin.module.display.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum ComponentType implements EnumType {
    TEXT,
    TITLE,
    IMAGE,
    BLANK,

    TAB,
    BRAND,
    CATEGORY,
    PRODUCT;




    public boolean isNonProductRelatedContents(){
        return this.equals(TEXT) || this.equals(TITLE) || this.equals(IMAGE) || this.equals(BLANK);
    }

    public boolean isProductRelatedContents(){
        return this.equals(TAB) || this.equals(BRAND) || this.equals(CATEGORY) || this.equals(PRODUCT);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

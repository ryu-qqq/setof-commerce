package com.connectly.partnerAdmin.module.display.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum ViewExtensionType implements EnumType {

    PAGE,
    LINKING,
    PRODUCT,
    SCROLL,
    NONE;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

package com.connectly.partnerAdmin.module.display.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum ImageType implements EnumType {

    SINGLE,
    MULTI;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

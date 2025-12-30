package com.connectly.partnerAdmin.module.display.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum ListType implements EnumType {
    ONE_STEP,
    TWO_STEP,
    MULTI,
    COLUMN,
    ROW,
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

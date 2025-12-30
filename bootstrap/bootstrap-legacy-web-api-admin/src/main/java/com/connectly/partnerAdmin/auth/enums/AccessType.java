package com.connectly.partnerAdmin.auth.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum AccessType implements EnumType {
    READ,
    ALL;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

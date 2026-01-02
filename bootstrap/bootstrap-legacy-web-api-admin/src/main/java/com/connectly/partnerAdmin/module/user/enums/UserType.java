package com.connectly.partnerAdmin.module.user.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum UserType implements EnumType {
    MEMBERS,
    GUEST;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

package com.connectly.partnerAdmin.module.category.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum TargetGroup implements EnumType {
    MALE,
    FEMALE,
    KIDS,
    LIFE;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

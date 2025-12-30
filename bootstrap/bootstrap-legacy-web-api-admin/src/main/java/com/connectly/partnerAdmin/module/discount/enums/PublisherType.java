package com.connectly.partnerAdmin.module.discount.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum PublisherType implements EnumType {

    ADMIN,
    SELLER;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

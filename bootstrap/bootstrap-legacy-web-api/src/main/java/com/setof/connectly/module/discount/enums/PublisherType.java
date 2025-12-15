package com.setof.connectly.module.discount.enums;

import com.setof.connectly.module.common.enums.EnumType;

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

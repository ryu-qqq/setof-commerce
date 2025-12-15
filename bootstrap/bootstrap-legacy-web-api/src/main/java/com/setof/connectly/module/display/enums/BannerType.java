package com.setof.connectly.module.display.enums;

import com.setof.connectly.module.common.enums.EnumType;

public enum BannerType implements EnumType {
    CATEGORY,
    MY_PAGE,
    CART,
    PRODUCT_DETAIL_DESCRIPTION,
    RECOMMEND,
    LOGIN;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

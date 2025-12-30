package com.connectly.partnerAdmin.module.display.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum BannerType implements EnumType {
    CATEGORY,
    MY_PAGE,
    CART,
    PRODUCT_DETAIL_DESCRIPTION,
    RECOMMEND,
    LOGIN
    ;


    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

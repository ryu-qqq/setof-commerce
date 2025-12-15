package com.setof.connectly.module.discount.enums;

import com.setof.connectly.module.common.enums.EnumType;

public enum DiscountType implements EnumType {
    RATE,
    PRICE;

    public boolean isPriceType() {
        return this.equals(PRICE);
    }

    public boolean isRateType() {
        return this.equals(RATE);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

package com.connectly.partnerAdmin.module.discount.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import com.fasterxml.jackson.annotation.JsonIgnore;

public enum DiscountType implements EnumType {

    RATE,
    PRICE;

    public boolean isPriceType(){
        return this.equals(PRICE);
    }

    public boolean isRateType(){
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

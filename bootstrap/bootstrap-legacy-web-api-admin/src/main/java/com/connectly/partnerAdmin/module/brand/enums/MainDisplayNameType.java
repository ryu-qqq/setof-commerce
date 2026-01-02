package com.connectly.partnerAdmin.module.brand.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum MainDisplayNameType implements EnumType {

    US,
    KR;


    public boolean isKorBrandName(){
        return this.equals(KR);
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

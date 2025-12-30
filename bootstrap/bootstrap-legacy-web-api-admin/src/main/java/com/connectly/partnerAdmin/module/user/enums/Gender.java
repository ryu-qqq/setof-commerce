package com.connectly.partnerAdmin.module.user.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum Gender implements EnumType {
    M,
    W,
    N;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return  this.name().equals("M") ? "남성" : "여성" ;
    }
}


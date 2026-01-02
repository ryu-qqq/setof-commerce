package com.connectly.partnerAdmin.module.common.enums;


public enum Yn implements EnumType {
    Y,
    N;

    public boolean isYes(){
        return equals(Y);
    }

    public boolean isNo(){
        return equals(N);
    }


    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }
}


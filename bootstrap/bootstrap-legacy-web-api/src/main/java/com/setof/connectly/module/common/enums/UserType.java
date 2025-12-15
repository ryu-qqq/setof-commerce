package com.setof.connectly.module.common.enums;

public enum UserType implements EnumType {
    MEMBERS,
    GUEST;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

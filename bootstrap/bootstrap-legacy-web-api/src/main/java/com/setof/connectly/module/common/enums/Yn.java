package com.setof.connectly.module.common.enums;

public enum Yn implements EnumType {
    Y,
    N;

    public boolean isYes() {
        return equals(Y);
    }

    public boolean isNo() {
        return equals(N);
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

package com.setof.connectly.module.event.enums;

public enum EventPayType {
    MILEAGE_ONLY,
    CASH_ONLY,
    MIX,
    ;

    public boolean isMileageOnly() {
        return this.equals(MILEAGE_ONLY);
    }

    public boolean isCashOnly() {
        return this.equals(CASH_ONLY);
    }

    public boolean isMix() {
        return this.equals(MIX);
    }
}

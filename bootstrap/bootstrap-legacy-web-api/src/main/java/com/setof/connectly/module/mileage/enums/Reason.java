package com.setof.connectly.module.mileage.enums;

public enum Reason {
    SAVE,
    USE,
    REFUND,
    EXPIRED;

    public boolean isPlus() {
        return this.equals(SAVE) || this.equals(REFUND);
    }

    public boolean isUse() {
        return this.equals(USE);
    }

    public boolean isExpired() {
        return this.equals(EXPIRED);
    }

    public boolean isRefund() {
        return this.equals(REFUND);
    }
}

package com.setof.connectly.module.mileage.enums;

public enum MileageStatus {
    PENDING,
    APPROVED,
    CANCELLED;

    public boolean isPending() {
        return this.equals(PENDING);
    }
}

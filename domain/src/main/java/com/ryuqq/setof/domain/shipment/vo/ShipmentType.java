package com.ryuqq.setof.domain.shipment.vo;

/** 배송 유형. */
public enum ShipmentType {
    DIRECT_PICKUP("직접수령"),
    QUICK_SERVICE("퀵서비스"),
    PARCEL_SERVICE("택배");

    private final String description;

    ShipmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

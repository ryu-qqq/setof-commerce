package com.setof.connectly.module.shipment.enums;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShipmentType implements EnumType {
    DIRECT_PICKUP,
    QUICK_SERVICE,
    PARCEL_SERVICE;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}

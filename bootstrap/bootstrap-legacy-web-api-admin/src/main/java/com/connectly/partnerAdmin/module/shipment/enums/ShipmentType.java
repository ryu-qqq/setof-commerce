package com.connectly.partnerAdmin.module.shipment.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
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

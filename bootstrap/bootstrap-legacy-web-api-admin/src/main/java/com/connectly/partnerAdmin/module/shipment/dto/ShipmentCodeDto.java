package com.connectly.partnerAdmin.module.shipment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShipmentCodeDto {

    private String shipmentCompanyName;
    private String shipmentCompanyCode;

    @QueryProjection
    public ShipmentCodeDto(String shipmentCompanyName, String shipmentCompanyCode) {
        this.shipmentCompanyName = shipmentCompanyName;
        this.shipmentCompanyCode = shipmentCompanyCode;
    }
}

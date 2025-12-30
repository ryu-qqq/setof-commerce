package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("claimRejectedAndShipmentOrder")
public class ClaimRejectedAndShipmentOrder extends ClaimOrder{

    @Valid
    private ShipmentInfo shipmentInfo;

}

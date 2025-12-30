package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("shipOrder")
public class ShipOrder extends NormalOrder {

    @Valid
    private ShipmentInfo shipmentInfo;

}

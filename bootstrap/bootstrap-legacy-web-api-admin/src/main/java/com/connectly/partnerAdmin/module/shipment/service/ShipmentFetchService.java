package com.connectly.partnerAdmin.module.shipment.service;

import com.connectly.partnerAdmin.module.shipment.dto.ShipmentCodeDto;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;

import java.util.List;

public interface ShipmentFetchService {

    Shipment fetchShipmentEntity(long orderId);

    List<ShipmentCodeDto> fetchShipmentCode();
}

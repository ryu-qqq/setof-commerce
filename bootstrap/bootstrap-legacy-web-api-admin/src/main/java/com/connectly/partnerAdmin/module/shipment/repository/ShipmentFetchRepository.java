package com.connectly.partnerAdmin.module.shipment.repository;

import com.connectly.partnerAdmin.module.shipment.dto.ShipmentCodeDto;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentFetchRepository {

    Optional<Shipment> fetchShipmentEntity(long orderId);
    List<ShipmentCodeDto> fetchShipmentCode();
}

package com.setof.connectly.module.shipment.repository;

import com.setof.connectly.module.shipment.entity.Shipment;
import java.util.List;

public interface ShipmentJdbcRepository {

    void saveAll(List<Shipment> shipments);
}

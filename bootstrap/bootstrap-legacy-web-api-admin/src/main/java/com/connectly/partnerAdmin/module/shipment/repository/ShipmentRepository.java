package com.connectly.partnerAdmin.module.shipment.repository;

import com.connectly.partnerAdmin.module.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}

package com.ryuqq.setof.application.shipment.manager.command;

import com.ryuqq.setof.application.shipment.port.out.command.ShipmentPersistencePort;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Shipment Persistence Manager
 *
 * <p>Shipment Aggregate 영속화 담당 Manager
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Transaction 경계 관리
 *   <li>PersistencePort 래핑
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShipmentPersistenceManager {

    private final ShipmentPersistencePort shipmentPersistencePort;

    public ShipmentPersistenceManager(ShipmentPersistencePort shipmentPersistencePort) {
        this.shipmentPersistencePort = shipmentPersistencePort;
    }

    /**
     * Shipment 저장
     *
     * @param shipment 저장할 Shipment
     * @return 저장된 Shipment ID Long 값
     */
    @Transactional
    public Long persist(Shipment shipment) {
        return shipmentPersistencePort.persist(shipment).value();
    }
}

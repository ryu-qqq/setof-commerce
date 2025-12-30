package com.ryuqq.setof.application.shipment.service.command;

import com.ryuqq.setof.application.shipment.dto.command.RegisterShipmentCommand;
import com.ryuqq.setof.application.shipment.factory.command.ShipmentCommandFactory;
import com.ryuqq.setof.application.shipment.manager.command.ShipmentPersistenceManager;
import com.ryuqq.setof.application.shipment.port.in.command.RegisterShipmentUseCase;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import org.springframework.stereotype.Service;

/**
 * 운송장 등록 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ShipmentCommandFactory로 Shipment 생성
 *   <li>ShipmentPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterShipmentService implements RegisterShipmentUseCase {

    private final ShipmentCommandFactory shipmentCommandFactory;
    private final ShipmentPersistenceManager shipmentPersistenceManager;

    public RegisterShipmentService(
            ShipmentCommandFactory shipmentCommandFactory,
            ShipmentPersistenceManager shipmentPersistenceManager) {
        this.shipmentCommandFactory = shipmentCommandFactory;
        this.shipmentPersistenceManager = shipmentPersistenceManager;
    }

    @Override
    public Long execute(RegisterShipmentCommand command) {
        Shipment shipment = shipmentCommandFactory.create(command);
        return shipmentPersistenceManager.persist(shipment);
    }
}

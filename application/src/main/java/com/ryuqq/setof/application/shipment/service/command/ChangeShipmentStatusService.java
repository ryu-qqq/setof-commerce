package com.ryuqq.setof.application.shipment.service.command;

import com.ryuqq.setof.application.shipment.dto.command.ChangeShipmentStatusCommand;
import com.ryuqq.setof.application.shipment.factory.command.ShipmentCommandFactory;
import com.ryuqq.setof.application.shipment.manager.command.ShipmentPersistenceManager;
import com.ryuqq.setof.application.shipment.manager.query.ShipmentReadManager;
import com.ryuqq.setof.application.shipment.port.in.command.ChangeShipmentStatusUseCase;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import org.springframework.stereotype.Service;

/**
 * 운송장 상태 변경 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>기존 Shipment 조회
 *   <li>ShipmentCommandFactory로 상태 변경 적용
 *   <li>ShipmentPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ChangeShipmentStatusService implements ChangeShipmentStatusUseCase {

    private final ShipmentCommandFactory shipmentCommandFactory;
    private final ShipmentPersistenceManager shipmentPersistenceManager;
    private final ShipmentReadManager shipmentReadManager;

    public ChangeShipmentStatusService(
            ShipmentCommandFactory shipmentCommandFactory,
            ShipmentPersistenceManager shipmentPersistenceManager,
            ShipmentReadManager shipmentReadManager) {
        this.shipmentCommandFactory = shipmentCommandFactory;
        this.shipmentPersistenceManager = shipmentPersistenceManager;
        this.shipmentReadManager = shipmentReadManager;
    }

    @Override
    public void execute(Long shipmentId, ChangeShipmentStatusCommand command) {
        Shipment shipment = shipmentReadManager.findById(shipmentId);
        Shipment updated = shipmentCommandFactory.applyStatusChange(shipment, command);
        shipmentPersistenceManager.persist(updated);
    }
}

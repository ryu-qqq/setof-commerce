package com.ryuqq.setof.application.shipment.service.command;

import com.ryuqq.setof.application.shipment.factory.command.ShipmentCommandFactory;
import com.ryuqq.setof.application.shipment.manager.command.ShipmentPersistenceManager;
import com.ryuqq.setof.application.shipment.manager.query.ShipmentReadManager;
import com.ryuqq.setof.application.shipment.port.in.command.MarkDeliveredUseCase;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import org.springframework.stereotype.Service;

/**
 * 배송 완료 처리 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>기존 Shipment 조회
 *   <li>ShipmentCommandFactory로 배송 완료 적용
 *   <li>ShipmentPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class MarkDeliveredService implements MarkDeliveredUseCase {

    private final ShipmentCommandFactory shipmentCommandFactory;
    private final ShipmentPersistenceManager shipmentPersistenceManager;
    private final ShipmentReadManager shipmentReadManager;

    public MarkDeliveredService(
            ShipmentCommandFactory shipmentCommandFactory,
            ShipmentPersistenceManager shipmentPersistenceManager,
            ShipmentReadManager shipmentReadManager) {
        this.shipmentCommandFactory = shipmentCommandFactory;
        this.shipmentPersistenceManager = shipmentPersistenceManager;
        this.shipmentReadManager = shipmentReadManager;
    }

    @Override
    public void execute(Long shipmentId) {
        Shipment shipment = shipmentReadManager.findById(shipmentId);
        Shipment delivered = shipmentCommandFactory.applyMarkDelivered(shipment);
        shipmentPersistenceManager.persist(delivered);
    }
}

package com.ryuqq.setof.application.shipment.service.command;

import com.ryuqq.setof.application.shipment.dto.command.UpdateTrackingCommand;
import com.ryuqq.setof.application.shipment.factory.command.ShipmentCommandFactory;
import com.ryuqq.setof.application.shipment.manager.command.ShipmentPersistenceManager;
import com.ryuqq.setof.application.shipment.manager.query.ShipmentReadManager;
import com.ryuqq.setof.application.shipment.port.in.command.UpdateTrackingUseCase;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import org.springframework.stereotype.Service;

/**
 * 추적 정보 업데이트 서비스
 *
 * <p>스마트택배 API로부터 받은 추적 정보를 Shipment에 업데이트합니다.
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>기존 Shipment 조회
 *   <li>ShipmentCommandFactory로 추적 정보 업데이트 적용
 *   <li>ShipmentPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateTrackingService implements UpdateTrackingUseCase {

    private final ShipmentCommandFactory shipmentCommandFactory;
    private final ShipmentPersistenceManager shipmentPersistenceManager;
    private final ShipmentReadManager shipmentReadManager;

    public UpdateTrackingService(
            ShipmentCommandFactory shipmentCommandFactory,
            ShipmentPersistenceManager shipmentPersistenceManager,
            ShipmentReadManager shipmentReadManager) {
        this.shipmentCommandFactory = shipmentCommandFactory;
        this.shipmentPersistenceManager = shipmentPersistenceManager;
        this.shipmentReadManager = shipmentReadManager;
    }

    @Override
    public void execute(Long shipmentId, UpdateTrackingCommand command) {
        Shipment shipment = shipmentReadManager.findById(shipmentId);
        Shipment updated = shipmentCommandFactory.applyTrackingUpdate(shipment, command);
        shipmentPersistenceManager.persist(updated);
    }
}

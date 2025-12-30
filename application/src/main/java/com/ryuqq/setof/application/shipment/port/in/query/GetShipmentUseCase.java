package com.ryuqq.setof.application.shipment.port.in.query;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;

/**
 * Get Shipment UseCase (Query)
 *
 * <p>운송장 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShipmentUseCase {

    /**
     * 운송장 ID로 단건 조회
     *
     * @param shipmentId 운송장 ID
     * @return 조회된 Shipment
     * @throws com.ryuqq.setof.domain.shipment.exception.ShipmentNotFoundException 운송장이 없는 경우
     */
    Shipment execute(Long shipmentId);
}

package com.ryuqq.setof.application.shipment.port.out.command;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;

/**
 * Shipment Persistence Port (Command)
 *
 * <p>Shipment Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShipmentPersistencePort {

    /**
     * Shipment 저장 (신규 생성 또는 수정)
     *
     * @param shipment 저장할 Shipment (Domain Aggregate)
     * @return 저장된 Shipment의 ID
     */
    ShipmentId persist(Shipment shipment);
}

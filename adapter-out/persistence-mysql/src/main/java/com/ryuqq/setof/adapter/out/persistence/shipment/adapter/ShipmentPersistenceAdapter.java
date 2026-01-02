package com.ryuqq.setof.adapter.out.persistence.shipment.adapter;

import com.ryuqq.setof.adapter.out.persistence.shipment.entity.ShipmentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shipment.mapper.ShipmentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shipment.repository.ShipmentJpaRepository;
import com.ryuqq.setof.application.shipment.port.out.command.ShipmentPersistencePort;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;
import org.springframework.stereotype.Component;

/**
 * ShipmentPersistenceAdapter - Shipment Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Shipment 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Shipment 저장 (persist)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShipmentPersistenceAdapter implements ShipmentPersistencePort {

    private final ShipmentJpaRepository shipmentJpaRepository;
    private final ShipmentJpaEntityMapper shipmentJpaEntityMapper;

    public ShipmentPersistenceAdapter(
            ShipmentJpaRepository shipmentJpaRepository,
            ShipmentJpaEntityMapper shipmentJpaEntityMapper) {
        this.shipmentJpaRepository = shipmentJpaRepository;
        this.shipmentJpaEntityMapper = shipmentJpaEntityMapper;
    }

    /**
     * Shipment 저장 (생성/수정)
     *
     * @param shipment Shipment 도메인
     * @return 저장된 ShipmentId
     */
    @Override
    public ShipmentId persist(Shipment shipment) {
        ShipmentJpaEntity entity = shipmentJpaEntityMapper.toEntity(shipment);
        ShipmentJpaEntity savedEntity = shipmentJpaRepository.save(entity);
        return ShipmentId.of(savedEntity.getId());
    }
}

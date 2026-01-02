package com.ryuqq.setof.adapter.out.persistence.carrier.adapter;

import com.ryuqq.setof.adapter.out.persistence.carrier.entity.CarrierJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.carrier.mapper.CarrierJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.carrier.repository.CarrierJpaRepository;
import com.ryuqq.setof.application.carrier.port.out.command.CarrierPersistencePort;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import org.springframework.stereotype.Component;

/**
 * CarrierPersistenceAdapter - Carrier Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Carrier 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Carrier 저장 (persist)
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
public class CarrierPersistenceAdapter implements CarrierPersistencePort {

    private final CarrierJpaRepository carrierJpaRepository;
    private final CarrierJpaEntityMapper carrierJpaEntityMapper;

    public CarrierPersistenceAdapter(
            CarrierJpaRepository carrierJpaRepository,
            CarrierJpaEntityMapper carrierJpaEntityMapper) {
        this.carrierJpaRepository = carrierJpaRepository;
        this.carrierJpaEntityMapper = carrierJpaEntityMapper;
    }

    /**
     * Carrier 저장 (생성/수정)
     *
     * @param carrier Carrier 도메인
     * @return 저장된 CarrierId
     */
    @Override
    public CarrierId persist(Carrier carrier) {
        CarrierJpaEntity entity = carrierJpaEntityMapper.toEntity(carrier);
        CarrierJpaEntity savedEntity = carrierJpaRepository.save(entity);
        return CarrierId.of(savedEntity.getId());
    }
}

package com.ryuqq.setof.adapter.out.persistence.carrier.mapper;

import com.ryuqq.setof.adapter.out.persistence.carrier.entity.CarrierJpaEntity;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import com.ryuqq.setof.domain.carrier.vo.CarrierName;
import com.ryuqq.setof.domain.carrier.vo.CarrierStatus;
import org.springframework.stereotype.Component;

/**
 * CarrierJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Carrier 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Carrier -> CarrierJpaEntity (저장용)
 *   <li>CarrierJpaEntity -> Carrier (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CarrierJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * @param domain Carrier 도메인
     * @return CarrierJpaEntity
     */
    public CarrierJpaEntity toEntity(Carrier domain) {
        return CarrierJpaEntity.of(
                domain.getIdValue(),
                domain.getCodeValue(),
                domain.getNameValue(),
                domain.getStatusValue(),
                domain.getTrackingUrlTemplate(),
                domain.getDisplayOrder(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity CarrierJpaEntity
     * @return Carrier 도메인
     */
    public Carrier toDomain(CarrierJpaEntity entity) {
        return Carrier.reconstitute(
                CarrierId.of(entity.getId()),
                CarrierCode.of(entity.getCode()),
                CarrierName.of(entity.getName()),
                CarrierStatus.valueOf(entity.getStatus()),
                entity.getTrackingUrlTemplate(),
                entity.getDisplayOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

package com.ryuqq.setof.adapter.out.persistence.carrier.adapter;

import com.ryuqq.setof.adapter.out.persistence.carrier.mapper.CarrierJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.carrier.repository.CarrierQueryDslRepository;
import com.ryuqq.setof.application.carrier.port.out.query.CarrierQueryPort;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * CarrierQueryAdapter - Carrier Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Carrier 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>택배사 코드로 조회 (findByCode)
 *   <li>활성 택배사 목록 조회 (findAllActive)
 *   <li>전체 택배사 목록 조회 (findAll)
 *   <li>존재 여부 확인 (existsById, existsByCode)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CarrierQueryAdapter implements CarrierQueryPort {

    private final CarrierQueryDslRepository queryDslRepository;
    private final CarrierJpaEntityMapper carrierJpaEntityMapper;

    public CarrierQueryAdapter(
            CarrierQueryDslRepository queryDslRepository,
            CarrierJpaEntityMapper carrierJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.carrierJpaEntityMapper = carrierJpaEntityMapper;
    }

    /**
     * ID로 Carrier 단건 조회
     *
     * @param id Carrier ID (Value Object)
     * @return Carrier Domain (Optional)
     */
    @Override
    public Optional<Carrier> findById(CarrierId id) {
        return queryDslRepository.findById(id.value()).map(carrierJpaEntityMapper::toDomain);
    }

    /**
     * 택배사 코드로 Carrier 조회
     *
     * @param code 택배사 코드 (Value Object)
     * @return Carrier Domain (Optional)
     */
    @Override
    public Optional<Carrier> findByCode(CarrierCode code) {
        return queryDslRepository.findByCode(code.value()).map(carrierJpaEntityMapper::toDomain);
    }

    /**
     * 활성 상태 Carrier 전체 조회
     *
     * @return Carrier 목록 (displayOrder 순)
     */
    @Override
    public List<Carrier> findAllActive() {
        return queryDslRepository.findAllActive().stream()
                .map(carrierJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 전체 Carrier 조회
     *
     * @return Carrier 목록 (displayOrder 순)
     */
    @Override
    public List<Carrier> findAll() {
        return queryDslRepository.findAll().stream().map(carrierJpaEntityMapper::toDomain).toList();
    }

    /**
     * ID로 Carrier 존재 여부 확인
     *
     * @param id Carrier ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(CarrierId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 택배사 코드로 Carrier 존재 여부 확인
     *
     * @param code 택배사 코드 (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsByCode(CarrierCode code) {
        return queryDslRepository.existsByCode(code.value());
    }
}

package com.ryuqq.setof.application.carrier.manager.query;

import com.ryuqq.setof.application.carrier.port.out.query.CarrierQueryPort;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.exception.CarrierNotFoundException;
import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Carrier Read Manager
 *
 * <p>단일 Query Port 조회를 담당하는 Read Manager
 *
 * <p>읽기 전용 트랜잭션 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CarrierReadManager {

    private final CarrierQueryPort carrierQueryPort;

    public CarrierReadManager(CarrierQueryPort carrierQueryPort) {
        this.carrierQueryPort = carrierQueryPort;
    }

    /**
     * Carrier ID로 조회 (필수)
     *
     * @param carrierId Carrier ID (Long)
     * @return 조회된 Carrier
     * @throws CarrierNotFoundException Carrier를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Carrier findById(Long carrierId) {
        CarrierId id = CarrierId.of(carrierId);
        return carrierQueryPort
                .findById(id)
                .orElseThrow(() -> new CarrierNotFoundException(carrierId));
    }

    /**
     * Carrier ID로 조회 (선택)
     *
     * @param carrierId Carrier ID (Long)
     * @return 조회된 Carrier (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Carrier> findByIdOptional(Long carrierId) {
        CarrierId id = CarrierId.of(carrierId);
        return carrierQueryPort.findById(id);
    }

    /**
     * 택배사 코드로 조회 (필수)
     *
     * @param carrierCode 택배사 코드
     * @return 조회된 Carrier
     * @throws CarrierNotFoundException Carrier를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Carrier findByCode(String carrierCode) {
        CarrierCode code = CarrierCode.of(carrierCode);
        return carrierQueryPort
                .findByCode(code)
                .orElseThrow(() -> new CarrierNotFoundException(carrierCode));
    }

    /**
     * 택배사 코드로 조회 (선택)
     *
     * @param carrierCode 택배사 코드
     * @return 조회된 Carrier (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Carrier> findByCodeOptional(String carrierCode) {
        CarrierCode code = CarrierCode.of(carrierCode);
        return carrierQueryPort.findByCode(code);
    }

    /**
     * 활성 상태 택배사 전체 조회
     *
     * @return 활성 Carrier 목록
     */
    @Transactional(readOnly = true)
    public List<Carrier> findAllActive() {
        return carrierQueryPort.findAllActive();
    }

    /**
     * 전체 택배사 조회 (활성/비활성 포함)
     *
     * @return 전체 Carrier 목록
     */
    @Transactional(readOnly = true)
    public List<Carrier> findAll() {
        return carrierQueryPort.findAll();
    }

    /**
     * Carrier ID 존재 여부 확인
     *
     * @param carrierId Carrier ID (Long)
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long carrierId) {
        CarrierId id = CarrierId.of(carrierId);
        return carrierQueryPort.existsById(id);
    }

    /**
     * 택배사 코드 존재 여부 확인
     *
     * @param carrierCode 택배사 코드
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsByCode(String carrierCode) {
        CarrierCode code = CarrierCode.of(carrierCode);
        return carrierQueryPort.existsByCode(code);
    }
}

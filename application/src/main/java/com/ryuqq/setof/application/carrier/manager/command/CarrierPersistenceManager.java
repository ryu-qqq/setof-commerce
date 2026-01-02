package com.ryuqq.setof.application.carrier.manager.command;

import com.ryuqq.setof.application.carrier.port.out.command.CarrierPersistencePort;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Carrier Persistence Manager
 *
 * <p>Carrier 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CarrierPersistenceManager {

    private final CarrierPersistencePort carrierPersistencePort;

    public CarrierPersistenceManager(CarrierPersistencePort carrierPersistencePort) {
        this.carrierPersistencePort = carrierPersistencePort;
    }

    /**
     * Carrier 저장
     *
     * @param carrier 저장할 Carrier
     * @return 저장된 Carrier의 ID
     */
    @Transactional
    public CarrierId persist(Carrier carrier) {
        return carrierPersistencePort.persist(carrier);
    }
}

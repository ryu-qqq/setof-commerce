package com.ryuqq.setof.application.carrier.port.out.command;

import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;

/**
 * Carrier Persistence Port (Command)
 *
 * <p>Carrier Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CarrierPersistencePort {

    /**
     * Carrier 저장 (신규 생성 또는 수정)
     *
     * @param carrier 저장할 Carrier (Domain Aggregate)
     * @return 저장된 Carrier의 ID
     */
    CarrierId persist(Carrier carrier);
}

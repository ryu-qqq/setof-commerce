package com.ryuqq.setof.application.carrier.service.command;

import com.ryuqq.setof.application.carrier.manager.command.CarrierPersistenceManager;
import com.ryuqq.setof.application.carrier.manager.query.CarrierReadManager;
import com.ryuqq.setof.application.carrier.port.in.command.DeactivateCarrierUseCase;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 택배사 비활성화 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>기존 Carrier 조회
 *   <li>비활성화 상태로 변경
 *   <li>CarrierPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeactivateCarrierService implements DeactivateCarrierUseCase {

    private final CarrierPersistenceManager carrierPersistenceManager;
    private final CarrierReadManager carrierReadManager;
    private final ClockHolder clockHolder;

    public DeactivateCarrierService(
            CarrierPersistenceManager carrierPersistenceManager,
            CarrierReadManager carrierReadManager,
            ClockHolder clockHolder) {
        this.carrierPersistenceManager = carrierPersistenceManager;
        this.carrierReadManager = carrierReadManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(Long carrierId) {
        Carrier carrier = carrierReadManager.findById(carrierId);
        Instant now = Instant.now(clockHolder.getClock());
        Carrier deactivatedCarrier = carrier.deactivate(now);
        carrierPersistenceManager.persist(deactivatedCarrier);
    }
}

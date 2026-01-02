package com.ryuqq.setof.application.carrier.service.command;

import com.ryuqq.setof.application.carrier.dto.command.UpdateCarrierCommand;
import com.ryuqq.setof.application.carrier.factory.command.CarrierCommandFactory;
import com.ryuqq.setof.application.carrier.manager.command.CarrierPersistenceManager;
import com.ryuqq.setof.application.carrier.manager.query.CarrierReadManager;
import com.ryuqq.setof.application.carrier.port.in.command.UpdateCarrierUseCase;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import org.springframework.stereotype.Service;

/**
 * 택배사 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>기존 Carrier 조회
 *   <li>CarrierCommandFactory로 수정 적용
 *   <li>CarrierPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateCarrierService implements UpdateCarrierUseCase {

    private final CarrierCommandFactory carrierCommandFactory;
    private final CarrierPersistenceManager carrierPersistenceManager;
    private final CarrierReadManager carrierReadManager;

    public UpdateCarrierService(
            CarrierCommandFactory carrierCommandFactory,
            CarrierPersistenceManager carrierPersistenceManager,
            CarrierReadManager carrierReadManager) {
        this.carrierCommandFactory = carrierCommandFactory;
        this.carrierPersistenceManager = carrierPersistenceManager;
        this.carrierReadManager = carrierReadManager;
    }

    @Override
    public void execute(Long carrierId, UpdateCarrierCommand command) {
        Carrier carrier = carrierReadManager.findById(carrierId);
        Carrier updatedCarrier = carrierCommandFactory.applyUpdate(carrier, command);
        carrierPersistenceManager.persist(updatedCarrier);
    }
}

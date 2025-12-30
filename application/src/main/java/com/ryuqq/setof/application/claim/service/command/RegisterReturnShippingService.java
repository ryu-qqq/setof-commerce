package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.RegisterReturnShippingCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.RegisterReturnShippingUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RegisterReturnShippingService - 반품 송장 등록 Service
 *
 * <p>반품 배송을 위한 송장 정보를 등록합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class RegisterReturnShippingService implements RegisterReturnShippingUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public RegisterReturnShippingService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void registerShipping(RegisterReturnShippingCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.registerReturnShipping(
                command.shippingMethod(),
                command.trackingNumber(),
                command.carrier(),
                clockHolder.getClock().instant());

        claimPersistencePort.persist(claim);
    }
}

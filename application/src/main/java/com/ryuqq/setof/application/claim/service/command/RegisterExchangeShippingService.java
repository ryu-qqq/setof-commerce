package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.RegisterExchangeShippingCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.RegisterExchangeShippingUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RegisterExchangeShippingService - 교환품 발송 등록 Service
 *
 * <p>교환 클레임에서 새 상품(교환품)을 발송할 때 사용합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class RegisterExchangeShippingService implements RegisterExchangeShippingUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public RegisterExchangeShippingService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void registerShipping(RegisterExchangeShippingCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.registerExchangeShipping(
                command.trackingNumber(), command.carrier(), clockHolder.getClock().instant());

        claimPersistencePort.persist(claim);
    }
}

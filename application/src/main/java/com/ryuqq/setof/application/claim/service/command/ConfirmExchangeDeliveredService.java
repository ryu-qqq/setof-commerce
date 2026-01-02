package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.ConfirmExchangeDeliveredCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.ConfirmExchangeDeliveredUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ConfirmExchangeDeliveredService - 교환품 배송 완료 확인 Service
 *
 * <p>고객이 교환품을 수령했음을 확인합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class ConfirmExchangeDeliveredService implements ConfirmExchangeDeliveredUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public ConfirmExchangeDeliveredService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void confirmDelivered(ConfirmExchangeDeliveredCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.confirmExchangeDelivered(clockHolder.getClock().instant());

        claimPersistencePort.persist(claim);
    }
}

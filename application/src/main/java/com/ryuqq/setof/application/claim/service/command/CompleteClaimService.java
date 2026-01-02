package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.CompleteClaimCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.CompleteClaimUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CompleteClaimService - 클레임 완료 처리 Service
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class CompleteClaimService implements CompleteClaimUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public CompleteClaimService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void complete(CompleteClaimCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.complete(clockHolder.getClock().instant());
        claimPersistencePort.persist(claim);
    }
}

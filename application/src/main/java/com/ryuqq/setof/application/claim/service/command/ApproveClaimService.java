package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.ApproveClaimCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.ApproveClaimUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ApproveClaimService - 클레임 승인 Service
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class ApproveClaimService implements ApproveClaimUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public ApproveClaimService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void approve(ApproveClaimCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.approve(command.adminId(), clockHolder.getClock().instant());
        claimPersistencePort.persist(claim);
    }
}

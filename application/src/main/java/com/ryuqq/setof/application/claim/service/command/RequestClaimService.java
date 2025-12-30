package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.RequestClaimCommand;
import com.ryuqq.setof.application.claim.factory.command.ClaimCommandFactory;
import com.ryuqq.setof.application.claim.port.in.command.RequestClaimUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RequestClaimService - 클레임 요청 Service
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class RequestClaimService implements RequestClaimUseCase {

    private final ClaimCommandFactory claimCommandFactory;
    private final ClaimPersistencePort claimPersistencePort;

    public RequestClaimService(
            ClaimCommandFactory claimCommandFactory, ClaimPersistencePort claimPersistencePort) {
        this.claimCommandFactory = claimCommandFactory;
        this.claimPersistencePort = claimPersistencePort;
    }

    @Override
    @Transactional
    public String request(RequestClaimCommand command) {
        Claim claim = claimCommandFactory.create(command);
        return claimPersistencePort.persist(claim).value();
    }
}

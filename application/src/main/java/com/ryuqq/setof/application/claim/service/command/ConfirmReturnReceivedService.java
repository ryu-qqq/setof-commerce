package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.ConfirmReturnReceivedCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.ConfirmReturnReceivedUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ConfirmReturnReceivedService - 반품 수령 확인 Service
 *
 * <p>판매자가 반품 상품을 수령하고 검수 결과를 등록합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class ConfirmReturnReceivedService implements ConfirmReturnReceivedUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public ConfirmReturnReceivedService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void confirmReceived(ConfirmReturnReceivedCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.confirmReturnReceived(
                command.inspectionResult(),
                command.inspectionNote(),
                clockHolder.getClock().instant());

        claimPersistencePort.persist(claim);
    }
}

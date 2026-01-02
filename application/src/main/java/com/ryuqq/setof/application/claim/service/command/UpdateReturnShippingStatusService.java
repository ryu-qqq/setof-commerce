package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.UpdateReturnShippingStatusCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.UpdateReturnShippingStatusUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateReturnShippingStatusService - 반품 배송 상태 업데이트 Service
 *
 * <p>택배사 Webhook이나 관리자에 의해 반품 배송 상태를 업데이트합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class UpdateReturnShippingStatusService implements UpdateReturnShippingStatusUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public UpdateReturnShippingStatusService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void updateStatus(UpdateReturnShippingStatusCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.updateReturnShippingStatus(
                command.newStatus(), command.trackingNumber(), clockHolder.getClock().instant());

        claimPersistencePort.persist(claim);
    }
}

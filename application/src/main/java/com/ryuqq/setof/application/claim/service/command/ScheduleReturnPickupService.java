package com.ryuqq.setof.application.claim.service.command;

import com.ryuqq.setof.application.claim.dto.command.ScheduleReturnPickupCommand;
import com.ryuqq.setof.application.claim.manager.query.ClaimReadManager;
import com.ryuqq.setof.application.claim.port.in.command.ScheduleReturnPickupUseCase;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ScheduleReturnPickupService - 반품 수거 예약 Service
 *
 * <p>셀러가 고객의 반품 상품을 수거하기 위한 방문 일정을 예약합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class ScheduleReturnPickupService implements ScheduleReturnPickupUseCase {

    private final ClaimReadManager claimReadManager;
    private final ClaimPersistencePort claimPersistencePort;
    private final ClockHolder clockHolder;

    public ScheduleReturnPickupService(
            ClaimReadManager claimReadManager,
            ClaimPersistencePort claimPersistencePort,
            ClockHolder clockHolder) {
        this.claimReadManager = claimReadManager;
        this.claimPersistencePort = claimPersistencePort;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void schedulePickup(ScheduleReturnPickupCommand command) {
        Claim claim = claimReadManager.findById(command.claimId());

        claim.scheduleReturnPickup(
                command.scheduledAt(),
                command.pickupAddress(),
                command.customerPhone(),
                clockHolder.getClock().instant());

        claimPersistencePort.persist(claim);
    }
}

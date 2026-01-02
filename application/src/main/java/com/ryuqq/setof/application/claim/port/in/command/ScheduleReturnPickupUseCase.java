package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.ScheduleReturnPickupCommand;

/**
 * ScheduleReturnPickupUseCase - 반품 수거 예약 UseCase
 *
 * <p>셀러가 고객의 반품 상품을 수거하기 위한 방문 일정을 예약합니다. 주로 방문수거(SELLER_PICKUP) 방식에서 사용됩니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ScheduleReturnPickupUseCase {

    /**
     * 반품 수거 예약
     *
     * @param command 반품 수거 예약 Command
     */
    void schedulePickup(ScheduleReturnPickupCommand command);
}

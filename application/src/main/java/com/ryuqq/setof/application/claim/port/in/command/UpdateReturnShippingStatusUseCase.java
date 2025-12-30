package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.UpdateReturnShippingStatusCommand;

/**
 * UpdateReturnShippingStatusUseCase - 반품 배송 상태 업데이트 UseCase
 *
 * <p>택배사 Webhook이나 관리자에 의해 반품 배송 상태를 업데이트합니다. PENDING → PICKUP_SCHEDULED → PICKED_UP → IN_TRANSIT →
 * RECEIVED
 *
 * @author development-team
 * @since 2.0.0
 */
public interface UpdateReturnShippingStatusUseCase {

    /**
     * 반품 배송 상태 업데이트
     *
     * @param command 반품 배송 상태 업데이트 Command
     */
    void updateStatus(UpdateReturnShippingStatusCommand command);
}

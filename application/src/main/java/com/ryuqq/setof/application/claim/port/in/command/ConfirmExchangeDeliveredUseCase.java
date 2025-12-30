package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.ConfirmExchangeDeliveredCommand;

/**
 * ConfirmExchangeDeliveredUseCase - 교환품 배송 완료 확인 UseCase
 *
 * <p>고객이 교환품을 수령했음을 확인합니다. 교환 클레임의 최종 완료 단계입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ConfirmExchangeDeliveredUseCase {

    /**
     * 교환품 배송 완료 확인
     *
     * @param command 교환품 배송 완료 확인 Command
     */
    void confirmDelivered(ConfirmExchangeDeliveredCommand command);
}

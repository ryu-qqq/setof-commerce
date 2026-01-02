package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.RegisterReturnShippingCommand;

/**
 * RegisterReturnShippingUseCase - 반품 송장 등록 UseCase
 *
 * <p>반품 배송을 위한 송장 정보를 등록합니다. 고객 직접 발송, 착불 송장 발급 등의 경우에 사용됩니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface RegisterReturnShippingUseCase {

    /**
     * 반품 송장 등록
     *
     * @param command 반품 송장 등록 Command
     */
    void registerShipping(RegisterReturnShippingCommand command);
}

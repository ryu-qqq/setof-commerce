package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.RegisterExchangeShippingCommand;

/**
 * RegisterExchangeShippingUseCase - 교환품 발송 등록 UseCase
 *
 * <p>교환 클레임에서 새 상품(교환품)을 발송할 때 사용합니다. 반품 검수 통과 후 교환품 발송 단계에서 호출됩니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface RegisterExchangeShippingUseCase {

    /**
     * 교환품 발송 등록
     *
     * @param command 교환품 발송 등록 Command
     */
    void registerShipping(RegisterExchangeShippingCommand command);
}

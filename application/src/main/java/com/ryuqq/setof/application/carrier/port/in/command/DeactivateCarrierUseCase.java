package com.ryuqq.setof.application.carrier.port.in.command;

/**
 * Deactivate Carrier UseCase (Command)
 *
 * <p>택배사 비활성화를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeactivateCarrierUseCase {

    /**
     * 택배사 비활성화 실행
     *
     * @param carrierId 택배사 ID
     */
    void execute(Long carrierId);
}
